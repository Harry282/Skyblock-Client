package skyblockclient.features.dungeons

import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.init.Blocks
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.Slot
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.Item
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.GuiContainerEvent.DrawSlotEvent
import skyblockclient.events.GuiContainerEvent.SlotClickEvent
import skyblockclient.utils.Utils.equalsOneOf
import skyblockclient.utils.Utils.isFloor
import skyblockclient.utils.Utils.renderText

class Terminals {

    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        currentTerminal = TerminalType.NONE
        clickQueue.clear()
        if (!isFloor(7) || event.gui !is GuiChest) return
        val container = (event.gui as GuiChest).inventorySlots
        if (container is ContainerChest) {
            val chestName = container.lowerChestInventory.displayName.unformattedText
            if (chestName == "Navigate the maze!") {
                currentTerminal = TerminalType.MAZE
            } else if (chestName == "Click in order!") {
                currentTerminal = TerminalType.NUMBERS
            } else if (chestName == "Correct all the panes!") {
                currentTerminal = TerminalType.CORRECTALL
            } else if (chestName.startsWith("What starts with: '")) {
                currentTerminal = TerminalType.LETTER
            } else if (chestName.startsWith("Select all the")) {
                currentTerminal = TerminalType.COLOR
            }
            shouldClick = true
        }
    }

    @SubscribeEvent
    fun onGuiDraw(event: BackgroundDrawnEvent) {
        if (!isFloor(7) || event.gui !is GuiChest || currentTerminal == TerminalType.NONE) return
        val container = (event.gui as GuiChest).inventorySlots
        if (container is ContainerChest) {
            if (clickQueue.isEmpty() || recalculate) {
                recalculate = getClicks(container)
            } else {
                val invSlots = container.inventorySlots
                shouldClick = when (currentTerminal) {
                    TerminalType.MAZE, TerminalType.NUMBERS, TerminalType.CORRECTALL -> clickQueue.removeIf {
                        invSlots[it.slotNumber].hasStack && invSlots[it.slotNumber].stack.itemDamage == 5
                    }
                    TerminalType.LETTER, TerminalType.COLOR -> clickQueue.removeIf {
                        invSlots[it.slotNumber].hasStack && invSlots[it.slotNumber].stack.isItemEnchanted
                    }
                    TerminalType.NONE -> false
                } || shouldClick
                if (clickQueue.isNotEmpty() && config.terminalAuto && (shouldClick || config.terminalPingless) &&
                    System.currentTimeMillis() - lastClickTime > config.terminalClickDelay
                ) {
                    clickSlot(clickQueue[0])
                    shouldClick = false
                }
            }
            if (config.showTerminalInfo) {
                renderText(mc, "Terminal: " + currentTerminal.name, 20, 20, 1.0)
                renderText(mc, "Clicks left: " + clickQueue.size, 20, 40, 1.0)
                renderText(mc, "Window ID: $windowId, Pingless Clicks: $windowClicks", 20, 60, 1.0)
            }
        }
    }

    @SubscribeEvent
    fun onDrawSlot(event: DrawSlotEvent) {
        if (!config.terminalHighlight || !isFloor(7) || event.gui !is GuiChest || event.slot.stack == null) return
        val x = event.slot.xDisplayPosition
        val y = event.slot.yDisplayPosition
        if (event.slot.inventory != mc.thePlayer.inventory) when (currentTerminal) {
            TerminalType.NUMBERS -> {
                for (i in 0..2) if (clickQueue.size > i && event.slot.slotNumber == clickQueue[i].slotNumber) {
                    Gui.drawRect(x, y, x + 16, y + 16, getColor(i))
                    break
                }
                if (event.slot.inventory != mc.thePlayer.inventory) {
                    val item = event.slot.stack
                    if (item.item == Item.getItemFromBlock(Blocks.stained_glass_pane) && item.itemDamage == 14) {
                        renderText(
                            text = item.stackSize.toString(),
                            x = x + 9 - mc.fontRendererObj.getStringWidth(item.stackSize.toString()) / 2,
                            y = y + 4
                        )
                        event.isCanceled = true
                    }
                }
            }
            TerminalType.LETTER, TerminalType.COLOR -> if (clickQueue.any { it.slotNumber == event.slot.slotNumber }) {
                Gui.drawRect(x, y, x + 16, y + 16, config.terminalColorHighlight.rgb)
            }
            else -> {
            }
        }
    }

    @SubscribeEvent
    fun onSlotClick(event: SlotClickEvent) {
        if (!config.terminalPingless && !config.terminalBlockClicks && !config.terminalMiddleClick ||
            !isFloor(7) || clickQueue.isEmpty() || event.slot == null
        ) return
        event.isCanceled = true
        if (config.terminalBlockClicks) {
            when (currentTerminal) {
                TerminalType.MAZE, TerminalType.NUMBERS -> if (clickQueue[0].slotNumber != event.slotId) return
                TerminalType.CORRECTALL -> if (event.slot?.stack?.itemDamage == 5) return
                TerminalType.COLOR, TerminalType.LETTER -> if (clickQueue.none { it.slotNumber == event.slotId }) return
                else -> {
                }
            }
        }
        clickSlot(event.slot!!)
    }

    @SubscribeEvent
    fun onTooltip(event: ItemTooltipEvent) {
        if (!config.terminalHideTooltip || isFloor(7) || currentTerminal == TerminalType.NONE || event.toolTip == null) return
        event.toolTip.clear()
    }

    private fun getColor(index: Int): Int {
        return when (index) {
            0 -> config.terminalColorNumberFirst.rgb
            1 -> config.terminalColorNumberSecond.rgb
            2 -> config.terminalColorNumberThird.rgb
            else -> 0xFFFFFF
        }
    }

    private fun getClicks(container: ContainerChest): Boolean {
        val invSlots = container.inventorySlots
        clickQueue.clear()
        windowClicks = 0
        when (currentTerminal) {
            TerminalType.MAZE -> {
                val startSlots = ArrayList<Slot>()
                var endSlot = -1
                invSlots.filter {
                    it.inventory != mc.thePlayer.inventory && it.stack?.item == Item.getItemFromBlock(Blocks.stained_glass_pane)
                }.forEach {
                    when (it.stack.itemDamage) {
                        5 -> startSlots.add(it)
                        14 -> endSlot = it.slotNumber
                    }
                }
                for (slot in startSlots) {
                    val mazeVisited = BooleanArray(54)
                    var start = slot.slotNumber
                    while (start != endSlot) {
                        var newSlotChosen = false
                        for (i in intArrayOf(-9, -1, 1, 9)) {
                            val next = start + i
                            if (next < 0 || next > 53 || i == -1 && start % 9 == 0 || i == 1 && start % 9 == 8) continue
                            if (mazeVisited[next]) continue
                            if (next == endSlot) return false
                            val itemStack = invSlots[next].stack ?: continue
                            if (itemStack.item == Item.getItemFromBlock(Blocks.stained_glass_pane) && itemStack.itemDamage == 0) {
                                clickQueue.add(invSlots[next])
                                start = next
                                mazeVisited[next] = true
                                newSlotChosen = true
                                break
                            }
                        }
                        if (!newSlotChosen) break
                    }
                }
                return true
            }
            TerminalType.NUMBERS -> {
                var min = 0
                val temp = arrayOfNulls<Slot>(14)
                for (i in 10..25) {
                    if (i == 17 || i == 18) continue
                    val item = invSlots[i].stack ?: return true
                    if (item.item == Item.getItemFromBlock(Blocks.stained_glass_pane) && item.stackSize < 15) {
                        when (item.itemDamage) {
                            5 -> min = 0.coerceAtLeast(item.stackSize)
                            14 -> temp[item.stackSize - 1] = invSlots[i]
                        }
                    }
                }
                clickQueue.addAll(temp.filterNotNull())
                if (clickQueue.size != 14 - min) return true
            }
            TerminalType.CORRECTALL -> {
                for (slot in invSlots) {
                    if (slot.slotNumber !in 10..34 || slot.slotNumber % 9 <= 1 || slot.slotNumber % 9 >= 7) continue
                    val item = slot.stack ?: return true
                    if (item.item == Item.getItemFromBlock(Blocks.stained_glass_pane) && item.itemDamage == 14) {
                        clickQueue.add(slot)
                    }
                }
            }
            TerminalType.LETTER -> {
                val chestName = container.lowerChestInventory.displayName.unformattedText
                if (chestName.length > chestName.indexOf("'") + 1) {
                    val letterNeeded = chestName[chestName.indexOf("'") + 1]
                    for (slot in invSlots) {
                        if (slot.slotNumber !in 10..43 || (slot.slotNumber % 9).equalsOneOf(0, 8)) continue
                        val item = slot.stack ?: return true
                        if (!item.isItemEnchanted && stripControlCodes(item.displayName)[0] == letterNeeded) {
                            clickQueue.add(slot)
                        }
                    }
                }
            }
            TerminalType.COLOR -> {
                val colorNeeded = EnumDyeColor.values().find {
                    container.lowerChestInventory.displayName.unformattedText.contains(
                        it.getName().replace("_", " ").uppercase()
                    )
                }?.unlocalizedName ?: return false
                for (slot in invSlots) {
                    if (slot.slotNumber !in 10..43 || (slot.slotNumber % 9).equalsOneOf(0, 8)) continue
                    val item = slot.stack ?: return true
                    if (!item.isItemEnchanted && item.unlocalizedName.contains(colorNeeded)) {
                        clickQueue.add(slot)
                    }
                }
            }
            TerminalType.NONE -> {
            }
        }
        return false
    }

    private fun clickSlot(slot: Slot) {
        if (windowClicks == 0) windowId = mc.thePlayer.openContainer.windowId
        mc.playerController.windowClick(
            windowId + windowClicks,
            slot.slotNumber,
            if (config.terminalMiddleClick) 2 else 0,
            0,
            mc.thePlayer
        )
        lastClickTime = System.currentTimeMillis()
        if (config.terminalPingless) {
            windowClicks++
            clickQueue.remove(slot)
        }
    }

    private enum class TerminalType {
        MAZE, NUMBERS, CORRECTALL, LETTER, COLOR, NONE
    }

    companion object {
        private val clickQueue = ArrayList<Slot>(28)
        private var currentTerminal = TerminalType.NONE
        private var lastClickTime: Long = 0
        private var recalculate = false
        private var shouldClick = false
        private var windowId = 0
        private var windowClicks = 0
    }
}