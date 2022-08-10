package skyblockclient.features

import net.minecraft.init.Blocks
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.util.BlockPos
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.LocationUtils.inDungeons
import skyblockclient.utils.LocationUtils.inSkyblock
import skyblockclient.utils.Utils.equalsOneOf
import skyblockclient.utils.Utils.itemID

object NoRotateHook {

    private var prevPitch = 0f
    private var prevYaw = 0f

    fun handlePlayerPosLookPre() {
        if (mc.thePlayer == null || mc.thePlayer.rotationPitch % 1 == 0f && mc.thePlayer.rotationYaw % 1 == 0f) return
        prevPitch = mc.thePlayer.rotationPitch % 360
        prevYaw = mc.thePlayer.rotationYaw % 360
    }

    fun handlePlayerPosLook(packet: S08PacketPlayerPosLook) {
        if (!config.noRotate || !inSkyblock || packet.pitch % 1 == 0f) return
        if (inDungeons && mc.thePlayer.run {
                BlockPos.getAllInBox(position.add(2, -1, 2), position.add(-2, -1, -2))
                    .any { mc.theWorld.getBlockState(it).block == Blocks.end_portal_frame } ||
                        heldItem?.itemID.equalsOneOf("SPIRIT_LEAP", "INFINITE_SPIRIT_LEAP")
            }) return
        mc.thePlayer.run {
            rotationPitch = prevPitch
            rotationYaw = prevYaw
        }
    }
}
