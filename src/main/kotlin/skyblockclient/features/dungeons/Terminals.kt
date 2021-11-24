package skyblockclient.features.dungeons

import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Blocks
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.Slot
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.Item
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.GuiContainerEvent.DrawSlotEvent
import skyblockclient.events.GuiContainerEvent.SlotClickEvent
import skyblockclient.utils.RenderUtilsKT.renderText

class Terminals {
    @SubscribeEvent
    fun onGuiDraw(event: BackgroundDrawnEvent) {
        if (!inDungeons || event.gui !is GuiChest) return
        val container = (event.gui as GuiChest).inventorySlots
        if (container is ContainerChest) {
            if (currentTerminal == TerminalType.NONE) {
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
            if (currentTerminal != TerminalType.NONE) {
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
            }
            if (config.showTerminalInfo) {
                renderText(mc, "Terminal: " + currentTerminal.name, 20, 20, 1.0)
                renderText(mc, "Clicks left: " + clickQueue.size, 20, 40, 1.0)
                renderText(mc, "Window ID: $windowId, Pingless Clicks: $windowClicks", 20, 60, 1.0)
            }
        }
    }

    @SubscribeEvent
    fun onDrawSlot(event: DrawSlotEvent.Pre) {
        if (!inDungeons || event.gui !is GuiChest || event.slot.stack == null || !config.experimentHighlight) return
        val x = event.slot.xDisplayPosition
        val y = event.slot.yDisplayPosition
        when (currentTerminal) {
            TerminalType.NUMBERS -> {
                for (i in 0..2) {
                    if (clickQueue.size > i && event.slot.slotNumber == clickQueue[i].slotNumber) {
                        Gui.drawRect(x, y, x + 16, y + 16, getColor(i))
                        break
                    }
                }
                if (event.slot.inventory != mc.thePlayer.inventory) {
                    val item = event.slot.stack
                    if (item.item == Item.getItemFromBlock(Blocks.stained_glass_pane) && item.itemDamage == 14) {
                        GlStateManager.pushMatrix()
                        GlStateManager.disableLighting()
                        GlStateManager.disableDepth()
                        GlStateManager.disableBlend()
                        mc.fontRendererObj.drawStringWithShadow(
                            item.stackSize.toString(),
                            (x + 9 - mc.fontRendererObj.getStringWidth(item.stackSize.toString()) / 2).toFloat(),
                            (y + 4).toFloat(), 16777215
                        )
                        GlStateManager.popMatrix()
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
        if (!inDungeons || clickQueue.isEmpty() || event.slot == null || !event.slot!!.hasStack ||
            (!config.terminalPingless && !config.terminalBlockClicks && !config.terminalMiddleClick)
        ) return
        event.isCanceled = true
        if (config.terminalBlockClicks) {
            when (currentTerminal) {
                TerminalType.MAZE, TerminalType.NUMBERS -> if (clickQueue[0].slotNumber != event.slotId) return
                TerminalType.CORRECTALL -> if (event.slot!!.stack.itemDamage == 5) return
                TerminalType.COLOR, TerminalType.LETTER -> if (clickQueue.none { it.slotNumber == event.slotId }) return
                else -> {
                }
            }
        }
        clickSlot(event.slot!!)
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!inDungeons || event.phase != TickEvent.Phase.START || mc.currentScreen is GuiChest) return
        currentTerminal = TerminalType.NONE
        clickQueue.clear()
        shouldClick = false
    }

    @SubscribeEvent
    fun onTooltip(event: ItemTooltipEvent) {
        if (!inDungeons || !config.terminalHideTooltip || event.toolTip == null || currentTerminal == TerminalType.NONE) return
        event.toolTip.clear()
    }

    private fun getColor(index: Int): Int {
        return when (index) {
            0 -> config.terminalColorNumberFirst.rgb
            1 -> config.terminalColorNumberSecond.rgb
            2 -> config.terminalColorNumberThird.rgb
            else -> 0xffffff
        }
    }

    private fun getClicks(container: ContainerChest): Boolean {
        val invSlots = container.inventorySlots
        clickQueue.clear()
        windowClicks = 0
        when (currentTerminal) {
            TerminalType.MAZE -> {
                val mazeSlotDirection = intArrayOf(-9, -1, 1, 9)
                val isStartSlot = BooleanArray(54)
                var endSlot = -1
                for (slot in invSlots) {
                    if (slot.inventory == mc.thePlayer.inventory) continue
                    val itemStack = slot.stack ?: continue
                    if (itemStack.item == Item.getItemFromBlock(Blocks.stained_glass_pane)) {
                        when (itemStack.itemDamage) {
                            5 -> isStartSlot[slot.slotNumber] = true
                            14 -> endSlot = slot.slotNumber
                        }
                    }
                }
                for (slot in isStartSlot.indices) {
                    if (!isStartSlot[slot]) continue
                    val mazeVisited = BooleanArray(54)
                    var startSlot = slot
                    var newSlotChosen: Boolean
                    while (startSlot != endSlot) {
                        newSlotChosen = false
                        for (i in mazeSlotDirection) {
                            val nextSlot = startSlot + i
                            if (nextSlot < 0 || nextSlot > 53 || i == -1 && startSlot % 9 == 0 || i == 1 && startSlot % 9 == 8) continue
                            if (mazeVisited[nextSlot]) continue
                            if (nextSlot == endSlot) return false
                            val itemStack = invSlots[nextSlot].stack ?: continue
                            if (itemStack.item == Item.getItemFromBlock(Blocks.stained_glass_pane) && itemStack.itemDamage == 0) {
                                clickQueue.add(invSlots[nextSlot])
                                startSlot = nextSlot
                                mazeVisited[nextSlot] = true
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
                    if (slot.inventory == mc.thePlayer.inventory) continue
                    if (slot.slotNumber < 9 || slot.slotNumber > 35 || slot.slotNumber % 9 <= 1 || slot.slotNumber % 9 >= 7) continue
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
                        if (slot.inventory == mc.thePlayer.inventory) continue
                        if (slot.slotNumber < 9 || slot.slotNumber > 44 || slot.slotNumber % 9 == 0 || slot.slotNumber % 9 == 8) continue
                        val item = slot.stack ?: return true
                        if (item.isItemEnchanted) continue
                        if (StringUtils.stripControlCodes(item.displayName)[0] == letterNeeded) {
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
                    if (slot.inventory == mc.thePlayer.inventory) continue
                    if (slot.slotNumber < 9 || slot.slotNumber > 44 || slot.slotNumber % 9 == 0 || slot.slotNumber % 9 == 8) continue
                    val item = slot.stack ?: return true
                    if (item.isItemEnchanted) continue
                    if (item.unlocalizedName.contains(colorNeeded)) {
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