package skyblockclient.features.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.utils.SkyblockCheck;

public class MimicKilled {

    private static boolean mimicKilled;

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (!SkyblockCheck.inDungeons) return;
        if (event.entity instanceof EntityZombie) {
            EntityZombie entity = (EntityZombie) event.entity;
            if (entity.isChild() && entity.getCurrentArmor(0) == null && entity.getCurrentArmor(1) == null &&
                    entity.getCurrentArmor(2) == null && entity.getCurrentArmor(3) == null && !mimicKilled) {
                mimicKilled = true;
                if (SkyblockClient.config.mimicKillMessage) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc " + SkyblockClient.config.mimicMessage);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        mimicKilled = false;
    }
}
