package skyblockclient.features

import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.ReceivePacketEvent

class NoRotate {
    @SubscribeEvent
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun onPacket(event: ReceivePacketEvent) {
        if (!inSkyblock || event.packet !is S08PacketPlayerPosLook) return
        val player = mc.thePlayer
        val item = player.heldItem ?: return
        if (config.noRotate && swordNames.any { s -> item.displayName.contains(s) }) {
            val packet = event.packet as S08PacketPlayerPosLook
            if (config.noRotateAutoDisable) {
                if (packet.pitch % 1 == 0f && packet.yaw % 1 == 0f) return
            }
            val yaw = packet.javaClass.getDeclaredField("field_148936_d")
            val pitch = packet.javaClass.getDeclaredField("field_148937_e")
            yaw.isAccessible = true
            pitch.isAccessible = true
            yaw.setFloat(packet, player.rotationYaw)
            pitch.setFloat(packet, player.rotationPitch)
        }
    }

    companion object {
        private val swordNames =
            listOf("Hyperion", "Astraea", "Scylla", "Valkyrie", "Aspect of the End", "Aspect of the Void")
    }
}