package skyblockclient.utils

import gg.essential.universal.UChat
import kotlinx.coroutines.delay
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc

object GuiMacroUtils {
    var currentChestName = ""

    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        currentChestName = getChestName(event.gui)
    }

    suspend fun clickSlot(slot: Int, shift: Boolean = false, delay: Long = 0) {
        if (mc.thePlayer.openContainer.inventorySlots.size < slot || currentChestName == "") {
            throw IndexOutOfBoundsException()
        }
        mc.playerController.windowClick(
            mc.thePlayer.openContainer.windowId,
            slot,
            0,
            if (shift) 1 else 0,
            mc.thePlayer
        )
        delay(delay + config.guiMacroDelay)
        return
    }

    private fun getChestName(gui: GuiScreen?): String {
        return if (gui is GuiChest) {
            (gui.inventorySlots as? ContainerChest)?.lowerChestInventory?.displayName?.unformattedText ?: ""
        } else ""
    }

    fun macroMessage(message: String) {
        if (config.guiMacroMessage) UChat.chat("${SkyblockClient.CHAT_PREFIX} $message")
    }
}
