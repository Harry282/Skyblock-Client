package skyblockclient.features

import net.minecraft.network.play.server.S08PacketPlayerPosLook
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc

object NoRotateHook {

    private var prevPitch = 0f
    private var prevYaw = 0f

    fun handlePlayerPosLookPre() {
        prevPitch = mc.thePlayer.rotationPitch
        prevYaw = mc.thePlayer.rotationYaw
    }

    fun handlePlayerPosLook(packet: S08PacketPlayerPosLook) {
        if (!config.noRotate || !inSkyblock || packet.pitch % 1 == 0f) return
        mc.thePlayer.run {
            rotationPitch = prevPitch.also { prevRotationPitch = it }
            rotationYaw = prevYaw.also { prevRotationYaw = it }
        }
    }
}
