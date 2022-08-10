package skyblockclient.features.dungeons

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.LocationUtils.inDungeons
import skyblockclient.utils.Utils.equalsOneOf
import skyblockclient.utils.Utils.renderText

object BloodReady {

    private var bloodReady = false
    private var timer: Long = 0

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!config.bloodReadyNotify || !inDungeons) return
        if (!bloodReady && stripControlCodes(event.message.unformattedText).equalsOneOf(
                "[BOSS] The Watcher: That will be enough for now.",
                "[BOSS] The Watcher: You have proven yourself. You may pass."
            )
        ) {
            mc.thePlayer.playSound("random.orb", 1f, 0.5.toFloat())
            timer = System.currentTimeMillis() + 1500
            bloodReady = true
        }
    }

    @SubscribeEvent
    fun onOverlay(event: RenderGameOverlayEvent.Post) {
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR || !config.bloodReadyNotify || !inDungeons || mc.ingameGUI == null) return
        if (timer > System.currentTimeMillis()) {
            val sr = ScaledResolution(mc)
            renderText(
                "§cBlood Spawned",
                sr.scaledWidth / 2 - mc.fontRendererObj.getStringWidth("Blood Spawned") * 2,
                sr.scaledHeight / 4,
                4.0
            )
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load?) {
        bloodReady = false
    }
}
