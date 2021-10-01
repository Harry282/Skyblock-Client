package skyblockclient.features;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.utils.SkyblockCheck;

public class AntiBlind {

    @SubscribeEvent
    public void renderFog(EntityViewRenderEvent.FogDensity event) {
        if (!SkyblockCheck.inSkyblock || !SkyblockClient.config.antiBlind) return;
        event.density = 0f;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {
        if (!SkyblockCheck.inSkyblock || !SkyblockClient.config.antiPortal) return;
        if (event.type == RenderGameOverlayEvent.ElementType.PORTAL) {
            event.setCanceled(true);
        }
    }

}
