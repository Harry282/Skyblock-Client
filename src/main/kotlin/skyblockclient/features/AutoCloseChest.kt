package skyblockclient.features

import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.mc


object AutoCloseChest {
    @SubscribeEvent
    fun onGuiBackgroundRender(event: BackgroundDrawnEvent) {
        if (!inDungeons || !config.autoCloseSecretChests || event.gui !is GuiChest) return
        val container = (event.gui as GuiChest).inventorySlots
        if (container is ContainerChest) {
            if (container.lowerChestInventory.displayName.unformattedText == "Chest") {
                mc.thePlayer.closeScreen()
            }
        }
    }
}