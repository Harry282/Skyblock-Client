package skyblockclient.features.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.events.RenderLivingEntityEvent;
import skyblockclient.utils.OutlineUtils;
import skyblockclient.utils.RenderUtils;
import skyblockclient.utils.SkyblockCheck;

import java.util.Arrays;
import java.util.List;

public class HiddenMobs {

    private static final List<String> watcherMobs = Arrays.asList("Revoker", "Psycho", "Reaper", "Cannibal", "Mute", "Ooze", "Putrid", "Freak", "Leech", "Tear", "Parasite", "Flamer", "Skull", "Mr. Dead", "Vader", "Frost", "Walker", "Wandering Soul", "Bonzo", "Scarf", "Livid");

    @SubscribeEvent
    public void renderLiving(RenderLivingEntityEvent event) {
        if (!SkyblockCheck.inDungeons) return;
        if (SkyblockClient.config.showFels && event.entity instanceof EntityEnderman && event.entity.getName().equals("Dinnerbone")) {
            event.entity.setInvisible(false);
            if (SkyblockClient.config.espFels && SkyblockClient.config.espType == 0) {
                OutlineUtils.outlineESP(event, SkyblockClient.config.espColorFels);
            }
        } else if (event.entity instanceof EntityPlayer) {
            if (SkyblockClient.config.showShadowAssassin && event.entity.getName().contains("Shadow Assassin")) {
                event.entity.setInvisible(false);
                if (SkyblockClient.config.espShadowAssassin && SkyblockClient.config.espType == 0) {
                    OutlineUtils.outlineESP(event, SkyblockClient.config.espColorShadowAssassin);
                }
            } else if (SkyblockClient.config.showStealthy) {
                if (watcherMobs.stream().anyMatch(s -> event.entity.getName().trim().equals(s))) {
                    event.entity.setInvisible(false);
                }
            }
        } else if (SkyblockClient.config.espBats && event.entity instanceof EntityBat &&
                !event.entity.isInvisible() && SkyblockClient.config.espType == 0) {
            OutlineUtils.outlineESP(event, SkyblockClient.config.espColorBats);
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (SkyblockClient.config.espType == 0) return;
        for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity instanceof EntityEnderman && entity.getName().equals("Dinnerbone") &&
                    SkyblockClient.config.espFels) {
                RenderUtils.drawEntityBox(entity, SkyblockClient.config.espColorFels, SkyblockClient.config.espType == 2, event.partialTicks);
            } else if (entity instanceof EntityPlayer && SkyblockClient.config.showShadowAssassin &&
                    entity.getName().contains("Shadow Assassin") && SkyblockClient.config.espShadowAssassin) {
                RenderUtils.drawEntityBox(entity, SkyblockClient.config.espColorShadowAssassin, SkyblockClient.config.espType == 2, event.partialTicks);
            }
            if (entity instanceof EntityBat && !entity.isInvisible() && SkyblockClient.config.espBats) {
                RenderUtils.drawEntityBox(entity, SkyblockClient.config.espColorBats, SkyblockClient.config.espType == 2, event.partialTicks);
            }
        }
    }

}
