package skyblockclient.features.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.utils.DrawTitle;
import skyblockclient.utils.SkyblockCheck;

public class BloodReadyMessage {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static boolean bloodReady;
    private static long timer;
    private static int renderTimer;

    @SubscribeEvent
    public void chat(ClientChatReceivedEvent event) {
        if (!SkyblockCheck.inDungeons || !SkyblockClient.config.bloodReadyNotify) return;
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (message.equals("[BOSS] The Watcher: That will be enough for now.")) {
            mc.thePlayer.playSound("random.orb", 1, (float) 0.5);
            timer = System.currentTimeMillis() + 1500;
            bloodReady = true;
        } else if (!bloodReady && message.equals("[BOSS] The Watcher: You have proven yourself. You may pass.")) {
            mc.thePlayer.playSound("random.orb", 1, (float) 0.5);
            timer = System.currentTimeMillis() + 1500;
            bloodReady = true;
        }
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        bloodReady = false;
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Post event) {
        if (!SkyblockCheck.inSkyblock || mc.ingameGUI == null || event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return;
        if (timer > System.currentTimeMillis()) {
            new DrawTitle("§cBlood Spawned", 4);
        }
    }

}
