package skyblockclient.features

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.RenderUtilsKT.renderText

class EndstoneProtectorTimer {
    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!inSkyblock || !config.endstoneProtectorTimer) return
        val message = StringUtils.stripControlCodes(event.message.unformattedText)
        if (message == "The ground begins to shake as an Endstone Protector rises from below!") {
            mc.thePlayer.playSound("random.orb", 1f, 0.5.toFloat())
            timer = System.currentTimeMillis() + 20000
        }
    }

    @SubscribeEvent
    fun onOverlay(event: RenderGameOverlayEvent.Post) {
        if (!inSkyblock || mc.ingameGUI == null || event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return
        if (timer - System.currentTimeMillis() > 0) {
            var time = (timer - System.currentTimeMillis())
            time /= 1000
            val sr = ScaledResolution(mc)
            var width = sr.scaledWidth / 2 - mc.fontRendererObj.getStringWidth("00.00") / 2
            if (time < 10) {
                width += mc.fontRendererObj.getStringWidth("0")
            }
            val height = sr.scaledHeight / 2 + 10
            renderText(mc, String.format("%.2f", time), width, height, 1.0)
        }
    }

    companion object {
        private var timer: Long = 0
    }
}