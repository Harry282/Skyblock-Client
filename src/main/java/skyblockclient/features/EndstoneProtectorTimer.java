package skyblockclient.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.utils.SkyblockCheck;
import skyblockclient.utils.TextRenderer;

public class EndstoneProtectorTimer {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static long timer;

    @SubscribeEvent
    public void chat(ClientChatReceivedEvent event) {
        if (!SkyblockCheck.inSkyblock || !SkyblockClient.config.endstoneProtectorTimer) return;
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (message.equals("The ground begins to shake as an Endstone Protector rises from below!")) {
            mc.thePlayer.playSound("random.orb", 1, (float) 0.5);
            timer = System.currentTimeMillis() + 20000;
        }
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Post event) {
        if (!SkyblockCheck.inSkyblock || mc.ingameGUI == null || event.type != RenderGameOverlayEvent.ElementType.HOTBAR)
            return;
        if (timer - System.currentTimeMillis() > 0) {
            float time = timer - System.currentTimeMillis();
            time /= 1000;
            ScaledResolution sr = new ScaledResolution(mc);
            int width = sr.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth("00.00") / 2;
            if (time < 10) {
                width += mc.fontRendererObj.getStringWidth("0");
            }
            int height = sr.getScaledHeight() / 2 + 10;
            new TextRenderer(mc, String.format("%.2f", time), width, height, 1);
        }
    }

}
