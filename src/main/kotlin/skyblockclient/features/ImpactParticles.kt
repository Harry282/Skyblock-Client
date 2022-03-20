package skyblockclient.features

import net.minecraft.network.play.server.S2APacketParticles
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.Vec3
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.ReceivePacketEvent
import skyblockclient.utils.Utils.equalsOneOf

object ImpactParticles {
    @SubscribeEvent
    fun onPacket(event: ReceivePacketEvent) {
        if (!config.noShieldParticles || !inSkyblock || event.packet !is S2APacketParticles) return
        if (event.packet.particleType.equalsOneOf(EnumParticleTypes.SPELL_WITCH, EnumParticleTypes.HEART)) {
            val particlePos = event.packet.run { Vec3(xCoordinate, yCoordinate, zCoordinate) }
            if (particlePos.squareDistanceTo(mc.thePlayer.positionVector) <= 169) {
                event.isCanceled = true
            }
        }
    }
}
