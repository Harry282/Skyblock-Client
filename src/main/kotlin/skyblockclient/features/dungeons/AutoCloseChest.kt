package skyblockclient.features.dungeons

import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.server.S2DPacketOpenWindow
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.ReceivePacketEvent
import skyblockclient.utils.LocationUtils.inDungeons

object AutoCloseChest {
    @SubscribeEvent
    fun onPacket(event: ReceivePacketEvent) {
        if (event.packet !is S2DPacketOpenWindow || !inDungeons) return
        if (config.autoCloseSecretChests || config.secretAura) {
            if (event.packet.windowTitle.formattedText == "Chest") {
                event.isCanceled = true
                mc.netHandler.networkManager.sendPacket(C0DPacketCloseWindow((event.packet.windowId)))
            }
        }
    }
}
