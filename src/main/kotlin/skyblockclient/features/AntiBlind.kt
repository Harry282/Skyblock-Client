package skyblockclient.features

import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock

class AntiBlind {
    @SubscribeEvent
    fun onRenderFog(event: FogDensity) {
        if (!config.antiBlind || !inSkyblock) return
        event.density = 0f
        event.isCanceled = true
    }

    @SubscribeEvent
    fun onOverlay(event: RenderGameOverlayEvent.Pre) {
        if (!config.antiPortal || !inSkyblock) return
        if (event.type == RenderGameOverlayEvent.ElementType.PORTAL) {
            event.isCanceled = true
        }
    }
}