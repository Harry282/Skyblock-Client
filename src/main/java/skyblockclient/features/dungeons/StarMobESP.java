package skyblockclient.features.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.events.RenderLivingEntityEvent;
import skyblockclient.utils.OutlineUtils;
import skyblockclient.utils.RenderUtils;
import skyblockclient.utils.SkyblockCheck;

import java.util.HashSet;
import java.util.List;

public class StarMobESP {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final HashSet<Entity> checkedArmorStands = new HashSet<>();
    private final HashSet<Entity> starMobs = new HashSet<>();

    @SubscribeEvent
    public void renderLiving(RenderLivingEntityEvent event) {
        if (!SkyblockCheck.inDungeons || !SkyblockClient.config.espStarMobs || SkyblockClient.config.espType != 0)
            return;
        if (!(event.entity instanceof EntityArmorStand) && starMobs.contains(event.entity)) {
            OutlineUtils.outlineESP(event, SkyblockClient.config.espColorStarMobs);
        }
    }

    @SubscribeEvent
    public void preRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (!SkyblockCheck.inDungeons || !SkyblockClient.config.espStarMobs) return;
        if (event.entity instanceof EntityArmorStand) {
            if (event.entity.hasCustomName() && event.entity.getCustomNameTag().contains("✯")) {
                if (checkedArmorStands.contains(event.entity)) return;
                List<Entity> possibleEntities = event.entity.getEntityWorld().getEntitiesInAABBexcluding(event.entity, event.entity.getEntityBoundingBox().expand(0, 3, 0), entity -> !(entity instanceof EntityArmorStand));
                if (!possibleEntities.isEmpty()) {
                    for (Entity e : possibleEntities) {
                        if (e instanceof EntityPlayer) {
                            if (e == mc.thePlayer || e.isInvisible() || e.getUniqueID().version() != 2) break;
                            if (SkyblockClient.config.espMiniboss) {
                                if (event.entity.getName().contains("Adventurer") && e.getName().equals("Lost Adventurer")) {
                                    checkedArmorStands.add(event.entity);
                                    return;
                                }
                                if (event.entity.getName().contains("Angry Archaeologist") && e.getName().equals("Diamond Guy")) {
                                    checkedArmorStands.add(event.entity);
                                    return;
                                }
                            }
                        } else if (e instanceof EntityEnderman && SkyblockClient.config.espFels) {
                            break;
                        } else if (starMobs.contains(e)) {
                            break;
                        }
                        starMobs.add(e);
                        checkedArmorStands.add(event.entity);
                        return;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!SkyblockCheck.inDungeons || !SkyblockClient.config.espStarMobs || SkyblockClient.config.espType == 0)
            return;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityArmorStand) && starMobs.contains(entity)) {
                RenderUtils.drawEntityBox(entity, SkyblockClient.config.espColorStarMobs, SkyblockClient.config.espType == 2, event.partialTicks);
            }
        }
    }

    @SubscribeEvent
    public void worldChange(WorldEvent.Load event) {
        checkedArmorStands.clear();
        starMobs.clear();
    }

}
