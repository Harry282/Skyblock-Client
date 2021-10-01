package skyblockclient.features;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.utils.SkyblockCheck;

public class BoneMacro {

    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (!SkyblockCheck.inSkyblock) return;
        if (SkyblockClient.keyBinds[1].isPressed()) {
            new Thread(() -> {
                for (int i = 0; i < 8; i++) {
                    ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
                    if (item == null) continue;
                    if (item.getDisplayName().contains("Bonemerang")) {
                        mc.thePlayer.inventory.currentItem = i;
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
                                mc.thePlayer.inventory.getCurrentItem());
                        try {
                            Thread.sleep(SkyblockClient.config.boneThrowDelay);
                            // TODO add randomized delay
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }).start();
        }
    }

}
