package skyblockclient.features.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.events.RenderLivingEntityEvent;
import skyblockclient.utils.OutlineUtils;
import skyblockclient.utils.RenderUtils;
import skyblockclient.utils.SkyblockCheck;

import java.awt.*;
import java.util.HashSet;

public class StarMobESP {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final HashSet<Entity> checkedArmorStands = new HashSet<>();
    private final HashSet<Entity> starArmorStands = new HashSet<>();
    private final HashSet<Entity> starMobs = new HashSet<>();
    private int tickCount = 0;

    @SubscribeEvent
    public void renderLiving(RenderLivingEntityEvent event) {
        if (!SkyblockCheck.inDungeons || !SkyblockClient.config.espStarMobs) return;
        processEntity(event.entity);
        if (starMobs.contains(event.entity)) {
            OutlineUtils.outlineESP(event, new Color(255, 255, 0));
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || mc.thePlayer == null || mc.theWorld == null) return;
        tickCount++;
        if (tickCount % 100 == 0) {
            starArmorStands.removeIf(entity -> entity.isDead);
            tickCount = 0;
        }
        if (tickCount % 5 == 0 && SkyblockClient.config.espType != 0) {
            mc.theWorld.loadedEntityList.forEach(this::processEntity);
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!SkyblockCheck.inDungeons || !SkyblockClient.config.espStarMobs || SkyblockClient.config.espType == 0)
            return;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer || entity instanceof EntitySkeleton || entity instanceof EntityZombie || entity instanceof EntityEnderman) {
                if (starMobs.contains(entity)) {
                    RenderUtils.drawEntityBox(entity, new Color(255, 255, 0), SkyblockClient.config.espType == 2, event.partialTicks);
                }
            }
        }
    }

    @SubscribeEvent
    public void worldChange(WorldEvent.Load event) {
        checkedArmorStands.clear();
        starArmorStands.clear();
        starMobs.clear();
    }

    private void processEntity(Entity entity) {
        if (entity instanceof EntityArmorStand) {
            if (entity.getName().contains("✯") && checkedArmorStands.contains(entity)) {
                starArmorStands.add(entity);
                return;
            }
        } else if (entity instanceof EntityPlayer) {
            if (entity == mc.thePlayer || entity.getName().equals("Lost Adventurer") ||
                    entity.getName().equals("Diamond Guy")) return;
            // Skips other players
            if (entity instanceof EntityPlayerMP) {
                ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam) ((EntityPlayer) entity).getTeam();
                if (scoreplayerteam != null && scoreplayerteam.getNameTagVisibility() != Team.EnumVisible.NEVER) {
                    if (FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix()).length() >= 2) {
                        return;
                    }
                }
            }
        } else if (entity instanceof EntityEnderman && SkyblockClient.config.espFels) {
            return;
        } else if (entity instanceof EntityZombie || entity instanceof EntitySkeleton || entity instanceof EntityEnderman) {
            if (starMobs.contains(entity)) return;
        }
        for (Entity e : starArmorStands) {
            if (new Vec3(e.posX, e.posY - 2, e.posZ).squareDistanceTo(entity.getPositionVector()) <= 3) {
                starMobs.add(entity);
                checkedArmorStands.add(e);
                starArmorStands.remove(e);
                return;
            }
        }
    }

}
