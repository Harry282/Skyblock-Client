package skyblockclient.features.dungeons

import net.minecraft.event.ClickEvent
import net.minecraft.network.play.server.S02PacketChat
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.ReceivePacketEvent
import skyblockclient.utils.LocationUtils.inDungeons

object ShowExtraStats {
    @SubscribeEvent
    fun onChatPacket(event: ReceivePacketEvent) {
        if (event.packet !is S02PacketChat || event.packet.type.toInt() == 2 || !inDungeons || !config.showExtraStats) return
        if (event.packet.chatComponent.siblings.any {
                it.chatStyle?.chatClickEvent?.run { action == ClickEvent.Action.RUN_COMMAND && value == "/showextrastats" } == true
            }) {
            event.isCanceled = true
            mc.thePlayer.sendChatMessage("/showextrastats")
        }
    }
}
