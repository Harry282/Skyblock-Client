package skyblockclient.utils

import net.minecraftforge.common.MinecraftForge
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.MovementUpdateEvent

object ServerRotateUtils {
    private var queuedAction: () -> Unit = {}
    private var queuedRotation: Rotation? = null
    private var savedRotation: Rotation? = null

    fun queueRotation(rotation: Rotation, action: () -> Unit = {}) {
        if (queuedRotation == null) {
            queuedRotation = rotation
            queuedAction = action
        } else {
            println("Rotation already queued, skipping")
        }
    }

    fun handlePre() {
        MinecraftForge.EVENT_BUS.post(MovementUpdateEvent.Pre())
        queuedRotation?.run {
            savedRotation = Rotation(mc.thePlayer.rotationPitch, mc.thePlayer.rotationYaw)
            mc.thePlayer.rotationPitch = pitch
            mc.thePlayer.rotationYaw = yaw
            queuedRotation = null
        }
    }

    fun handlePost() {
        MinecraftForge.EVENT_BUS.post(MovementUpdateEvent.Post())
        savedRotation?.run {
            queuedAction()
            queuedAction = {}

            mc.thePlayer.rotationPitch = pitch
            mc.thePlayer.rotationYaw = yaw
            savedRotation = null
        }
    }

    data class Rotation(val pitch: Float, val yaw: Float)
}
