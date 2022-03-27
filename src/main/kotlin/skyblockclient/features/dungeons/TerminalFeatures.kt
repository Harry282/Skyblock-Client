package skyblockclient.features.dungeons

import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.GuiContainerEvent
import skyblockclient.features.dungeons.AutoTerminals.TerminalType
import skyblockclient.features.dungeons.AutoTerminals.clickQueue
import skyblockclient.features.dungeons.AutoTerminals.clickSlot
import skyblockclient.features.dungeons.AutoTerminals.closestColorIndex
import skyblockclient.features.dungeons.AutoTerminals.colorOrder
import skyblockclient.features.dungeons.AutoTerminals.currentTerminal
import skyblockclient.utils.Utils.renderText

object TerminalFeatures {

    private val useCustomClicks
        get() = config.terminalPingless || config.terminalBlockClicks || config.terminalMiddleClick

    @SubscribeEvent
    fun onDrawSlot(event: GuiContainerEvent.DrawSlotEvent) {
        if (!config.terminalHighlight || event.gui !is GuiChest || event.slot.inventory == mc.thePlayer.inventory) return
        val x = event.slot.xDisplayPosition
        val y = event.slot.yDisplayPosition
        when (currentTerminal) {
            TerminalType.NUMBERS -> {
                for (i in 0 until clickQueue.size.coerceAtMost(3)) {
                    if (event.slot.slotNumber == clickQueue[i].slotNumber) {
                        Gui.drawRect(
                            x, y, x + 16, y + 16, when (i) {
                                0 -> config.terminalColorNumberFirst.rgb
                                1 -> config.terminalColorNumberSecond.rgb
                                else -> config.terminalColorNumberThird.rgb
                            }
                        )
                        break
                    }
                }
                val stack = event.slot.stack ?: return
                if (stack.item == Item.getItemFromBlock(Blocks.stained_glass_pane) && stack.itemDamage == 14) {
                    renderText(
                        text = stack.stackSize.toString(),
                        x = x + 9 - mc.fontRendererObj.getStringWidth(stack.stackSize.toString()) / 2,
                        y = y + 4
                    )
                    event.isCanceled = true
                }
            }
            TerminalType.LETTER, TerminalType.COLOR -> {
                if (clickQueue.any { it.slotNumber == event.slot.slotNumber }) {
                    Gui.drawRect(x, y, x + 16, y + 16, config.terminalColorHighlight.rgb)
                }
            }
            else -> {}
        }
    }

    @SubscribeEvent
    fun onTooltip(event: ItemTooltipEvent) {
        if (!config.terminalHideTooltip || currentTerminal == TerminalType.NONE || event.toolTip == null) return
        event.toolTip.clear()
    }

    @SubscribeEvent
    fun onSlotClick(event: GuiContainerEvent.SlotClickEvent) {
        if (!useCustomClicks || currentTerminal == TerminalType.NONE || clickQueue.isEmpty() || event.slot == null) return
        event.isCanceled = true
        if (config.terminalBlockClicks) {
            when (currentTerminal) {
                TerminalType.NUMBERS -> if (clickQueue[0].slotNumber != event.slotId) return
                TerminalType.CORRECT_ALL -> if (event.slot?.stack?.itemDamage == 5) return
                TerminalType.COLOR, TerminalType.LETTER -> if (clickQueue.none { it.slotNumber == event.slotId }) return
                TerminalType.SAME_COLOR -> if (event.slot?.stack?.itemDamage == colorOrder.getOrElse(
                        closestColorIndex
                    ) { 0 }
                ) return
                else -> {}
            }
        }
        clickSlot(event.slot!!.slotNumber, if (config.terminalMiddleClick) 2 else 0)
    }


}
