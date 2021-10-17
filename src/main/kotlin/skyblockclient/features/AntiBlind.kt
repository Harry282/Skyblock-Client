package skyblockclient.features

import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock

class AntiBlind {
    @SubscribeEvent
    fun onRenderFog(event: FogDensity) {
        if (!inSkyblock || !config.antiBlind) return
        event.density = 0f
        event.isCanceled = true
    }

    @SubscribeEvent
    fun onOverlay(event: RenderGameOverlayEvent) {
        if (!inSkyblock || !config.antiPortal) return
        if (event.type == RenderGameOverlayEvent.ElementType.PORTAL) {
            event.isCanceled = true
        }
    }
}