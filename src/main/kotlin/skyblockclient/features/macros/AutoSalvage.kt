package skyblockclient.features.macros

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.client.gui.GuiButton
import net.minecraft.item.ItemStack
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.SkyblockClient.Companion.scope
import skyblockclient.utils.GuiMacroUtils
import skyblockclient.utils.LocationUtils.inSkyblock
import skyblockclient.utils.Utils.itemID

object AutoSalvage : GuiMacro("Salvaging") {
    init {
        resetJob()
    }

    override lateinit var job: Job
    override val button = GuiButton(69, 0, 0, 100, 20, "Auto Salvage")
    override val inGui
        get() = inSkyblock && config.autoSalvage && GuiMacroUtils.currentChestName == "Salvage Item"
    private val netherSalvageID = listOf(
        "BLADE_OF_THE_VOLCANO",
        "STAFF_OF_THE_VOLCANO",
        "TAURUS_HELMET",
        "FLAMING_CHESTPLATE",
        "MOOGMA_LEGGINGS",
        "SLUG_BOOTS"
    )

    private fun isSalvageable(item: ItemStack): Boolean {
        val id = item.itemID
        if (id == "ICE_SPRAY_WAND") return false
        val attributes = item.getSubCompound("ExtraAttributes", false) ?: return false
        return when {
            attributes.hasKey("baseStatBoostPercentage") && !attributes.hasKey("dungeon_item_level") -> config.dungeonSalvage
            id in netherSalvageID -> config.netherSalvage
            else -> false
        }
    }

    override fun resetJob() {
        job = scope.launch(start = CoroutineStart.LAZY) {
            val invSlots = mc.thePlayer.openContainer.inventorySlots
            while (true) {
                val item = invSlots.firstOrNull {
                    it.inventory == mc.thePlayer.inventory && it.hasStack && isSalvageable(it.stack)
                } ?: break
                try {
                    GuiMacroUtils.clickSlot(item.slotNumber, true, 500)
                    GuiMacroUtils.clickSlot(31, false, 500)
                    if (invSlots[22].hasStack) {
                        delay(500)
                        GuiMacroUtils.clickSlot(31, false, 500)
                    }
                } catch (e: IndexOutOfBoundsException) {
                    GuiMacroUtils.macroMessage("§aStopped Auto Salvage!")
                    return@launch
                }
            }
            GuiMacroUtils.macroMessage("§aFinished Salvaging!")
        }
    }
}
