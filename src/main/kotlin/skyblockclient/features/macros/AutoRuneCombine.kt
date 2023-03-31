package skyblockclient.features.macros

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.client.gui.GuiButton
import net.minecraft.util.StringUtils
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.SkyblockClient.Companion.scope
import skyblockclient.utils.GuiMacroUtils
import skyblockclient.utils.LocationUtils.inSkyblock

object AutoRuneCombine : GuiMacro("Rune Combining") {
    init {
        resetJob()
    }

    override lateinit var job: Job
    override val button = GuiButton(69, 0, 0, 100, 20, "Auto Combine")
    override val inGui
        get() = inSkyblock && config.runeCombine && GuiMacroUtils.currentChestName == "Runic Pedestal"

    override fun resetJob() {
        job = scope.launch(start = CoroutineStart.LAZY) {
            val invSlots = mc.thePlayer.openContainer.inventorySlots
            while (true) {
                val runeSlot = invSlots.firstOrNull {
                    it.inventory == mc.thePlayer.inventory && it.stack?.run {
                        stackSize > 1 && StringUtils.stripControlCodes(displayName).run {
                            contains("Rune") && !contains("III")
                        }
                    } == true
                } ?: break
                try {
                    GuiMacroUtils.clickSlot(runeSlot.slotNumber, true, 200)
                    GuiMacroUtils.clickSlot(runeSlot.slotNumber, true, 200)
                    GuiMacroUtils.clickSlot(13, false, 200)
                    for (i in 0..30) {
                        if (invSlots[19].hasStack) delay(200) else break
                    }
                    GuiMacroUtils.clickSlot(31, true, 500)
                } catch (e: IndexOutOfBoundsException) {
                    GuiMacroUtils.macroMessage("§aStopped Rune Combining!")
                    return@launch
                }
            }
            GuiMacroUtils.macroMessage("§aFinished Rune Combining!")
        }
    }
}
