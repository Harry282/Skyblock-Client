package skyblockclient.features.dungeons

import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.init.Blocks
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.Slot
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.Item
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.LocationUtils
import skyblockclient.utils.LocationUtils.dungeonFloor
import skyblockclient.utils.Utils.renderText
import kotlin.math.abs

object AutoTerminals {

    var clickQueue = arrayListOf<Slot>()
    var currentTerminal = TerminalType.NONE

    var closestColorIndex = -1
    val colorOrder = listOf(14, 1, 4, 13, 11)
    private var lastRowClicked = 0

    private var lastClickTime: Long = 0
    private var startWindowID = 0
    private var windowClicks = 0
    private var totalClicks = 0

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || mc.currentScreen is GuiChest) return
        if (currentTerminal != TerminalType.NONE) {
            reset()
        }
    }

    private fun reset() {
        currentTerminal = TerminalType.NONE
        closestColorIndex = -1
        lastRowClicked = 0
        resetClicks()
    }

    private fun resetClicks() {
        clickQueue.clear()
        startWindowID = 0
        windowClicks = 0
        totalClicks = 0
    }

    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        if (event.gui !is GuiChest || currentTerminal != TerminalType.NONE || dungeonFloor != 7) return
        val container = (event.gui as GuiChest).inventorySlots
        if (container is ContainerChest) {
            val chestName = container.lowerChestInventory.displayName.unformattedText
            currentTerminal = when {
                chestName == "Click in order!" -> TerminalType.NUMBERS
                chestName == "Correct all the panes!" -> TerminalType.CORRECT_ALL
                chestName.startsWith("What starts with: '") -> TerminalType.LETTER
                chestName.startsWith("Select all the") -> TerminalType.COLOR
                chestName == "Click the button on time!" -> TerminalType.TIMING
                chestName == "Change all to same color!" -> TerminalType.SAME_COLOR
                else -> TerminalType.NONE
            }
        }
    }

    @SubscribeEvent
    fun onGuiDraw(event: GuiScreenEvent.BackgroundDrawnEvent) {
        if (currentTerminal == TerminalType.NONE || event.gui !is GuiChest) return
        val container = (event.gui as GuiChest).inventorySlots
        if (container is ContainerChest) {
            if (clickQueue.isEmpty() || totalClicks - clickQueue.size > container.windowId - startWindowID + 2) {
                resetClicks()
                clickQueue.addAll(getClicks(container).also { totalClicks = it.size })
                startWindowID = container.windowId
            } else if (!config.terminalPingless) {
                updateClickQueue(container)
            }
            if (clickQueue.isNotEmpty() || currentTerminal == TerminalType.TIMING) {
                if (config.terminalAuto) {
                    handleClick(container)
                }
            }
            if (config.showTerminalInfo) {
                renderText("Terminal: $currentTerminal", 20, 20)
                renderText("Clicks: ${clickQueue.size}/$totalClicks", 20, 40)
                renderText(
                    "Start Window ID: $startWindowID, Current Window ID: ${container.windowId},Pingless Clicks: $windowClicks",
                    20,
                    60
                )
                renderText("Closest Color: $closestColorIndex", 20, 80)
            }
        }
    }

    private fun getClicks(container: ContainerChest): MutableList<Slot> {
        val chestSlots = container.inventorySlots.filterNot { it.inventory == mc.thePlayer.inventory }
        val clicks = mutableListOf<Slot>()
        when (currentTerminal) {
            TerminalType.NUMBERS -> {
                val panes = chestSlots.filter { it.stack?.item == Item.getItemFromBlock(Blocks.stained_glass_pane) }
                val min = panes.filter { it.stack.itemDamage == 5 }.maxOfOrNull { it.stack.stackSize } ?: 0
                clicks.addAll(panes.filter { it.stack.itemDamage == 14 && it.stack.stackSize < 15 }
                    .sortedBy { it.stack.stackSize })
                if (clicks.size + min != 14) clicks.clear()
            }
            TerminalType.CORRECT_ALL -> {
                clicks.addAll(chestSlots.filter { it.stack?.item == Item.getItemFromBlock(Blocks.stained_glass_pane) && it.stack?.itemDamage == 14 })
            }
            TerminalType.LETTER -> {
                val chestName = container.lowerChestInventory.displayName.unformattedText
                if (chestName.length > chestName.indexOf("'") + 1) {
                    val letterNeeded = chestName[chestName.indexOf("'") + 1]
                    clicks.addAll(chestSlots.filter {
                        it.stack?.isItemEnchanted == false && stripControlCodes(it.stack?.displayName)[0] == letterNeeded && with(
                            it.slotNumber
                        ) {
                            this in 10..43 && this % 9 in 1..7
                        }
                    })
                }
            }
            TerminalType.COLOR -> {
                val colorNeeded = EnumDyeColor.values().find {
                    container.lowerChestInventory.displayName.unformattedText.contains(
                        it.getName().replace("_", " ").uppercase()
                    )
                }?.unlocalizedName ?: return clicks
                clicks.addAll(chestSlots.filter {
                    it.stack?.isItemEnchanted == false && it.stack.unlocalizedName.contains(colorNeeded) && with(it.slotNumber) {
                        this in 10..43 && this % 9 in 1..7
                    }
                })
            }
            TerminalType.SAME_COLOR -> {
                val panes = chestSlots.filter {
                    it.stack?.item == Item.getItemFromBlock(Blocks.stained_glass_pane) && colorOrder.contains(it.stack?.itemDamage)
                }
                closestColorIndex = colorOrder.indexOf(getClosestColor(panes))
                if (closestColorIndex == -1) return clicks
                val extraClick = mutableListOf<Slot>()
                panes.forEach {
                    val colorIndex = colorOrder.indexOf(it.stack.itemDamage)
                    if (closestColorIndex != colorIndex) {
                        clicks.add(it)
                        if (abs(closestColorIndex - colorIndex % 5) > 1) {
                            extraClick.add(it)
                        }
                    }
                }
                clicks.addAll(extraClick)
            }
            else -> {}
        }
        return clicks
    }

    private fun getClosestColor(panes: List<Slot>): Int? {
        var minClicks = IndexedValue(-1, 9999)
        for (color in colorOrder.withIndex()) {
            var clicks = 0
            panes.forEach {
                clicks += abs(color.index - colorOrder.indexOf(it.stack.itemDamage) % 5)
            }
            if (minClicks.value > clicks) minClicks = IndexedValue(color.index, clicks)
        }
        return if (minClicks.value == 0) null else colorOrder.getOrNull(minClicks.index)
    }

    private fun updateClickQueue(container: ContainerChest) {
        val invSlots = container.inventorySlots
        when (currentTerminal) {
            TerminalType.NUMBERS, TerminalType.CORRECT_ALL -> {
                clickQueue.removeIf { invSlots[it.slotNumber].stack?.itemDamage == 5 }
            }
            TerminalType.LETTER, TerminalType.COLOR -> {
                clickQueue.removeIf { invSlots[it.slotNumber].stack?.isItemEnchanted == true }
            }
            TerminalType.SAME_COLOR -> clickQueue.removeIf {
                invSlots[it.slotNumber].stack?.itemDamage == colorOrder.getOrElse(closestColorIndex) { 0 }
            }
            else -> {}
        }
    }

    private fun handleClick(container: ContainerChest) {
        if (System.currentTimeMillis() - lastClickTime > config.terminalClickDelay) {
            when (currentTerminal) {
                TerminalType.NUMBERS, TerminalType.CORRECT_ALL, TerminalType.LETTER, TerminalType.COLOR -> {
                    clickSlot(clickQueue[0].slotNumber, if (config.terminalMiddleClick) 2 else 0)
                }
                TerminalType.TIMING -> {
                    val panes = container.inventorySlots.filter { it.inventory != mc.thePlayer.inventory }
                    val column =
                        panes.indexOfFirst { it.stack?.item == Item.getItemFromBlock(Blocks.stained_glass_pane) && it.stack.itemDamage == 10 }
                    val greenPane =
                        panes.indexOfFirst { it.stack?.item == Item.getItemFromBlock(Blocks.stained_glass_pane) && it.stack.itemDamage == 5 }
                    if (column == -1 || greenPane == -1) return
                    if (column % 9 == greenPane % 9) {
                        val row = greenPane / 9
                        if (lastRowClicked == row && System.currentTimeMillis() - lastClickTime < 500) return
                        mc.playerController.windowClick(
                            mc.thePlayer.openContainer.windowId,
                            row * 9 + 7,
                            if (config.terminalMiddleClick) 2 else 0,
                            0,
                            mc.thePlayer
                        )
                        lastClickTime = System.currentTimeMillis()
                        lastRowClicked = row
                    }
                }
                TerminalType.SAME_COLOR -> {
                    val slot = container.inventorySlots[clickQueue[0].slotNumber]
                    if (slot.stack?.item != Item.getItemFromBlock(Blocks.stained_glass_pane)) return
                    val paneIndex = colorOrder.indexOf(slot.stack.itemDamage)
                    if (paneIndex == -1) return
                    val button = if ((closestColorIndex - paneIndex + 5) % 5 < 3) {
                        if (config.terminalMiddleClick) 2 else 0
                    } else 1
                    clickSlot(clickQueue[0].slotNumber, button)
                }
                else -> {}
            }
        }
    }

    fun clickSlot(slotNumber: Int, clickType: Int) {
        var windowID = if (windowClicks == 0) mc.thePlayer.openContainer.windowId
        else startWindowID + windowClicks
        if (windowID > 100) windowID -= 100
        lastClickTime = System.currentTimeMillis()
        mc.playerController.windowClick(
            windowID, slotNumber, clickType, 0, mc.thePlayer
        )
        if (config.terminalPingless) {
            windowClicks++
            clickQueue.removeAt(0)
        }
    }

    enum class TerminalType {
        NUMBERS, CORRECT_ALL, LETTER, COLOR, TIMING, SAME_COLOR, NONE
    }
}
