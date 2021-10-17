package skyblockclient.features.dungeons

import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.RenderUtilsKT.drawTitle

class BloodReady {
    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!inDungeons || !config.bloodReadyNotify) return
        val message = StringUtils.stripControlCodes(event.message.unformattedText)
        if (message == "[BOSS] The Watcher: That will be enough for now.") {
            mc.thePlayer.playSound("random.orb", 1f, 0.5.toFloat())
            timer = System.currentTimeMillis() + 1500
            bloodReady = true
        } else if (!bloodReady && message == "[BOSS] The Watcher: You have proven yourself. You may pass.") {
            mc.thePlayer.playSound("random.orb", 1f, 0.5.toFloat())
            timer = System.currentTimeMillis() + 1500
            bloodReady = true
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load?) {
        bloodReady = false
    }

    @SubscribeEvent
    fun onOverlay(event: RenderGameOverlayEvent.Post) {
        if (!inSkyblock || mc.ingameGUI == null || event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return
        if (timer > System.currentTimeMillis()) {
            drawTitle("§cBlood Spawned", 4.0)
        }
    }

    companion object {
        private var bloodReady = false
        private var timer: Long = 0
    }
}