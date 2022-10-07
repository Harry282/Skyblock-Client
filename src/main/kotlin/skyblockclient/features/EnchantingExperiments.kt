package skyblockclient.features

import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.Item
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.GuiContainerEvent
import skyblockclient.events.GuiContainerEvent.SlotClickEvent
import skyblockclient.utils.LocationUtils.inSkyblock
import skyblockclient.utils.Utils.renderText

object EnchantingExperiments {

    private var currentExperiment = ExperimentType.NONE
    private var hasAdded = false
    private var clicks = 0
    private var lastClickTime: Long = 0
    private val chronomatronOrder = ArrayList<Pair<Int, String>>(28)
    private var lastAdded = 0
    private var ultrasequencerOrder = HashMap<Int, Int>()

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
                        invSlots.filter { it.slotNumber in 10..43 }.find {
                            it.stack?.isItemEnchanted == true
                        }?.let {
                            chronomatronOrder.add(Pair(it.slotNumber, it.stack.displayName))
                            lastAdded = it.slotNumber
                            hasAdded = true
                            clicks = 0
                        }
                    }
                    if (hasAdded && invSlots[49].stack?.item == Items.clock && chronomatronOrder.size > clicks &&
                        config.experimentAuto && System.currentTimeMillis() - lastClickTime > config.experimentClickDelay
                    ) {
                        mc.playerController.windowClick(
                            mc.thePlayer.openContainer.windowId,
                            chronomatronOrder[clicks].first,
                            2,
                            3,
                            mc.thePlayer
                        )
                        lastClickTime = System.currentTimeMillis()
                        clicks++
                    }
                    if (config.experimentHighlight) {
                        chronomatronOrder.withIndex().forEach { (i, slot) ->
                            renderText(
                                if (i == clicks) "${slot.second} §l<" else slot.second,
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
                        invSlots.filter { it.slotNumber in 9..44 }.forEach {
                            if (it.stack?.item == Items.dye) {
                                ultrasequencerOrder[it.stack.stackSize - 1] = it.slotNumber
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
                                it, 2, 3, mc.thePlayer
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
            ExperimentType.CHRONOMATRON -> chronomatronOrder.size > clicks && event.slot?.stack?.displayName == chronomatronOrder[clicks].second
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
                    3,
                    mc.thePlayer
                )
            }
        } else {
            event.isCanceled = config.experimentBlockClicks
        }
    }

    @SubscribeEvent
    fun onDrawSlot(event: GuiContainerEvent.DrawSlotEvent) {
        if (!config.experimentHighlight || !inSkyblock || event.gui !is GuiChest || event.slot.stack == null) return
        val x = event.slot.xDisplayPosition
        val y = event.slot.yDisplayPosition
        when (currentExperiment) {
            ExperimentType.CHRONOMATRON -> {
                (0..2).find {
                    chronomatronOrder.size > clicks + it && event.slot.stack.displayName == chronomatronOrder[clicks + it].second
                }?.let { Gui.drawRect(x, y, x + 16, y + 16, getColor(it)) }
            }
            ExperimentType.ULTRASEQUENCER -> {
                if (event.container.inventorySlots[49].stack?.item == Items.clock && event.slot.slotNumber in 9..44) {
                    event.isCanceled = event.slot.stack.item == Item.getItemFromBlock(Blocks.stained_glass_pane)
                    (0..2).find {
                        event.slot.slotNumber == ultrasequencerOrder[clicks + it]
                    }?.let { Gui.drawRect(x, y, x + 16, y + 16, getColor(it)) }
                    ultrasequencerOrder.entries.find { event.slot.slotNumber == it.value }?.let {
                        renderText(
                            (it.key + 1).toString(),
                            x + 9 - mc.fontRendererObj.getStringWidth((it.key + 1).toString()) / 2,
                            y + 4
                        )
                    }
                }
            }
            else -> return
        }
    }

    @SubscribeEvent
    fun onTooltip(event: ItemTooltipEvent) {
        if (!config.experimentHideTooltips || !inSkyblock || event.toolTip == null) return
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
}
