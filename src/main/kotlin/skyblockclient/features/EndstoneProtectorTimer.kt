package skyblockclient.features

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.Utils.renderText

class EndstoneProtectorTimer {
    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!config.endstoneProtectorTimer || !inSkyblock) return
        val message = stripControlCodes(event.message.unformattedText)
        if (message == "The ground begins to shake as an Endstone Protector rises from below!") {
            mc.thePlayer.playSound("random.orb", 1f, 0.5.toFloat())
            timer = System.currentTimeMillis() + 20000
        }
    }

    @SubscribeEvent
    fun onOverlay(event: RenderGameOverlayEvent.Post) {
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR || !config.endstoneProtectorTimer || !inSkyblock || mc.ingameGUI == null) return
        if (timer - System.currentTimeMillis() > 0) {
            var time = (timer - System.currentTimeMillis()).toFloat()
            time /= 1000
            val sr = ScaledResolution(mc)
            val width = sr.scaledWidth / 2 - mc.fontRendererObj.getStringWidth("00.00") / 2
            renderText(
                text = String.format("%.2f", time),
                x = if (width < 10) width + mc.fontRendererObj.getStringWidth("0") else width,
                y = sr.scaledHeight / 2 + 10,
            )
        }
    }

    companion object {
        private var timer: Long = 0
    }
}
