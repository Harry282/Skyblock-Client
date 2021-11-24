package skyblockclient.features

import javafx.scene.control.Tooltip
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.Slot
import net.minecraft.item.Item
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.GuiContainerEvent
import skyblockclient.events.GuiContainerEvent.SlotClickEvent
import skyblockclient.features.dungeons.Terminals
import skyblockclient.utils.RenderUtilsKT.renderText

class EnchantingExperiments {

    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        currentExperiment = ExperimentType.NONE
        hasAdded = false
        chronomatronOrder.clear()
        lastAdded = 0
        ultrasequencerOrder.clear()
        if (!inSkyblock || event.gui !is GuiChest) return
        val container = (event.gui as GuiChest).inventorySlots
        if (container is ContainerChest) {
            val chestName = container.lowerChestInventory.displayName.unformattedText
            if (chestName.startsWith("Chronomatron (")) {
                currentExperiment = ExperimentType.CHRONOMATRON
            } else if (chestName.startsWith("Ultrasequencer (")) {
                currentExperiment = ExperimentType.ULTRASEQUENCER
            } else if (chestName.startsWith("Superpairs(")) {
                currentExperiment = ExperimentType.SUPERPAIRS
            }
        }
    }

    @SubscribeEvent
    fun onGuiDraw(event: GuiScreenEvent.BackgroundDrawnEvent) {
        if (!inSkyblock || event.gui !is GuiChest) return
        val container = (event.gui as GuiChest).inventorySlots
        if (container is ContainerChest) {
            val invSlots = container.inventorySlots
            when (currentExperiment) {
                ExperimentType.CHRONOMATRON -> {
                    if (invSlots[49].stack?.item == Item.getItemFromBlock(Blocks.glowstone) &&
                        invSlots[lastAdded].stack?.isItemEnchanted == false
                    ) {
                        hasAdded = false
                        if (chronomatronOrder.size > 11 && config.experimentAutoExit) {
                            mc.thePlayer.closeScreen()
                        }
                    }
                    if (!hasAdded && invSlots[49].stack?.item == Items.clock) {
                        for (slot in invSlots) {
                            if (slot.slotNumber !in 10..43) continue
                            if (slot.stack?.isItemEnchanted == true) {
                                chronomatronOrder.add(slot)
                                lastAdded = slot.slotNumber
                                hasAdded = true
                                clicks = 0
                                break
                            }
                        }
                    }
                    if (hasAdded && invSlots[49].stack?.item == Items.clock && chronomatronOrder.size > clicks &&
                        config.experimentAuto && System.currentTimeMillis() - lastClickTime > config.experimentClickDelay
                    ) {
                        mc.playerController.windowClick(
                            mc.thePlayer.openContainer.windowId,
                            chronomatronOrder[clicks].slotNumber,
                            2,
                            0,
                            mc.thePlayer
                        )
                        lastClickTime = System.currentTimeMillis()
                        clicks++
                    }
                    if (config.experimentHighlight) {
                        for ((i, slot) in chronomatronOrder.withIndex()) {
                            renderText(
                                mc,
                                if (i == clicks) "${slot.stack.displayName} §l<" else slot.stack.displayName,
                                ScaledResolution(mc).scaledWidth / 2 - 150,
                                ScaledResolution(mc).scaledHeight / 2 - 150 + i * 15,
                                1.0
                            )
                        }
                    }
                }
                ExperimentType.ULTRASEQUENCER -> {
                    if (invSlots[49].stack?.item == Items.clock) {
                        hasAdded = false
                    }
                    if (!hasAdded && invSlots[49].stack?.item == Item.getItemFromBlock(Blocks.glowstone)) {
                        if (!invSlots[44].hasStack) return
                        ultrasequencerOrder.clear()
                        for (slot in invSlots) {
                            if (slot.slotNumber !in 9..44) continue
                            if (slot.stack?.item == Items.dye) {
                                ultrasequencerOrder[slot.stack.stackSize - 1] = slot.slotNumber
                            }
                        }
                        hasAdded = true
                        clicks = 0
                        if (ultrasequencerOrder.size > 9 && config.experimentAutoExit) {
                            mc.thePlayer.closeScreen()
                        }
                    }
                    if (invSlots[49].stack?.item == Items.clock && ultrasequencerOrder.contains(clicks) &&
                        config.experimentAuto && System.currentTimeMillis() - lastClickTime > config.experimentClickDelay
                    ) {
                        ultrasequencerOrder[clicks]?.let {
                            mc.playerController.windowClick(
                                mc.thePlayer.openContainer.windowId,
                                it, 2, 0, mc.thePlayer
                            )
                        }
                        lastClickTime = System.currentTimeMillis()
                        clicks++
                    }
                }
                else -> return
            }
        }
    }

    @SubscribeEvent
    fun onSlotClick(event: SlotClickEvent) {
        if (!inSkyblock || event.gui !is GuiChest || event.slot == null) return
        val isCorrect = when (currentExperiment) {
            ExperimentType.CHRONOMATRON -> chronomatronOrder.size > clicks && event.slot?.stack?.displayName == chronomatronOrder[clicks].stack.displayName
            ExperimentType.ULTRASEQUENCER -> event.slot?.slotNumber == (ultrasequencerOrder[clicks] ?: return)
            else -> return
        }
        if (isCorrect) {
            clicks++
            if (config.experimentMiddleClick) {
                event.isCanceled = true
                mc.playerController.windowClick(
                    mc.thePlayer.openContainer.windowId,
                    event.slot!!.slotNumber,
                    2,
                    0,
                    mc.thePlayer
                )
            }
        } else {
            event.isCanceled = config.experimentBlockClicks
        }
    }

    @SubscribeEvent
    fun onDrawSlot(event: GuiContainerEvent.DrawSlotEvent.Pre) {
        if (!inSkyblock || event.gui !is GuiChest || event.slot.stack == null || !config.experimentHighlight) return
        val x = event.slot.xDisplayPosition
        val y = event.slot.yDisplayPosition
        when (currentExperiment) {
            ExperimentType.CHRONOMATRON -> {
                for (i in 0..2) {
                    if (chronomatronOrder.size > clicks + i && event.slot.stack.displayName == chronomatronOrder[clicks + i].stack.displayName) {
                        Gui.drawRect(x, y, x + 16, y + 16, getColor(i))
                        return
                    }
                }
            }
            ExperimentType.ULTRASEQUENCER -> {
                if (event.container.inventorySlots[49].stack?.item == Items.clock && event.slot.slotNumber in 9..44) {
                    event.isCanceled = event.slot.stack.item == Item.getItemFromBlock(Blocks.stained_glass_pane)
                    for (i in 0..2) {
                        if (event.slot.slotNumber == ultrasequencerOrder[clicks + i]) {
                            Gui.drawRect(x, y, x + 16, y + 16, getColor(i))
                            break
                        }
                    }
                    for ((int, slot) in ultrasequencerOrder) {
                        if (event.slot.slotNumber == slot) {
                            GlStateManager.pushMatrix()
                            GlStateManager.disableLighting()
                            GlStateManager.disableDepth()
                            GlStateManager.disableBlend()
                            mc.fontRendererObj.drawStringWithShadow(
                                (int + 1).toString(),
                                (x + 9 - mc.fontRendererObj.getStringWidth((int + 1).toString()) / 2).toFloat(),
                                (y + 4).toFloat(), 0xffffff
                            )
                            GlStateManager.popMatrix()
                            return
                        }
                    }
                }
            }
            else -> return
        }
    }

    @SubscribeEvent
    fun onTooltip(event: ItemTooltipEvent) {
        if (!inSkyblock || !config.experimentHideTooltips || event.toolTip == null) return
        if (currentExperiment == ExperimentType.CHRONOMATRON || currentExperiment == ExperimentType.ULTRASEQUENCER) {
            event.toolTip.clear()
        }
    }

    private fun getColor(index: Int): Int {
        return when (index) {
            0 -> config.experimentColorNumberFirst.rgb
            1 -> config.experimentColorNumberSecond.rgb
            2 -> config.experimentColorNumberThird.rgb
            else -> 0xffffff
        }
    }

    private enum class ExperimentType {
        CHRONOMATRON,
        ULTRASEQUENCER,
        SUPERPAIRS,
        NONE
    }

    companion object {
        private var currentExperiment = ExperimentType.NONE
        private var hasAdded = false
        private var clicks = 0
        private var lastClickTime: Long = 0
        private val chronomatronOrder = ArrayList<Slot>(28)
        private var lastAdded = 0
        private var ultrasequencerOrder = HashMap<Int, Int>()
    }
}