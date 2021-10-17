package skyblockclient.features

import net.minecraft.network.play.server.S2APacketParticles
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.Vec3
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.ReceivePacketEvent

class ImpactParticles {
    @SubscribeEvent
    fun onPacket(event: ReceivePacketEvent) {
        if (!inSkyblock || !config.noShieldParticles) return
        if (event.packet is S2APacketParticles) {
            val packet = event.packet as S2APacketParticles
            if (packet.particleType == EnumParticleTypes.SPELL_WITCH || packet.particleType == EnumParticleTypes.HEART) {
                val player = mc.thePlayer
                val particlePos = Vec3(packet.xCoordinate, packet.yCoordinate, packet.zCoordinate)
                if (particlePos.squareDistanceTo(player.positionVector) <= 11 * 11) {
                    event.isCanceled = true
                }
            }
        }
    }
}