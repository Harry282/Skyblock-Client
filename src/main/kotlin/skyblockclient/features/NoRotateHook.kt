package skyblockclient.features

import net.minecraft.network.play.server.S08PacketPlayerPosLook
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc

object NoRotateHook {

    private var prevPitch = 0f
    private var prevYaw = 0f

    fun handlePlayerPosLookPre() {
        if (mc.thePlayer == null || mc.thePlayer.rotationPitch % 1 == 0f && mc.thePlayer.rotationYaw % 1 == 0f) return
        prevPitch = mc.thePlayer.rotationPitch % 360
        prevYaw = mc.thePlayer.rotationYaw % 360
    }

    fun handlePlayerPosLook(packet: S08PacketPlayerPosLook) {
        if (!config.noRotate || !inSkyblock || packet.pitch % 1 == 0f && packet.yaw % 1 == 0f) return
        mc.thePlayer.run {
            rotationPitch = prevPitch.also { prevRotationPitch = it }
            rotationYaw = prevYaw.also { prevRotationYaw = it }
        }
    }
}
