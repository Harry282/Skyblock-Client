package skyblockclient.features.dungeons

import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.boss.EntityWither
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.monster.EntityEnderman
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.RenderLivingEntityEvent
import skyblockclient.utils.OutlineUtils
import skyblockclient.utils.RenderUtils

class StarMobESP {
    @SubscribeEvent
    fun onRenderEntity(event: RenderLivingEntityEvent) {
        if (!inDungeons || !config.espStarMobs || config.espType != 0) return
        if (event.entity !is EntityArmorStand && starMobs.contains(event.entity)) {
            OutlineUtils.outlineESP(event, config.espColorStarMobs)
        }
    }

    @SubscribeEvent
    fun onPreRenderEntity(event: RenderLivingEvent.Pre<EntityLivingBase?>) {
        if (!inDungeons || !config.espStarMobs || event.entity !is EntityArmorStand || !event.entity.hasCustomName() ||
            !event.entity.customNameTag.contains("✯") || checked.contains(event.entity)
        ) return
        val possibleEntities = event.entity.entityWorld.getEntitiesInAABBexcluding(
            event.entity, event.entity.entityBoundingBox.expand(0.0, 3.0, 0.0)
        ) { entity: Entity? -> entity !is EntityArmorStand }
        if (possibleEntities.isEmpty()) return
        for (e in possibleEntities) {
            if (e is EntityWither || e == mc.thePlayer) continue
            if (e is EntityOtherPlayerMP) {
                if (e.isInvisible() || e.getUniqueID().version() != 2) continue
                if (config.espMiniboss) {
                    if (event.entity.name.contains(" Adventurer") && e.name == "Lost Adventurer") {
                        if (config.removeStarMobsNametag) mc.theWorld.removeEntity(event.entity) else checked.add(event.entity)
                        return
                    }
                    if (event.entity.name.contains("Angry Archeologist") && e.name == "Diamond Guy") {
                        if (config.removeStarMobsNametag) mc.theWorld.removeEntity(event.entity) else checked.add(event.entity)
                        return
                    }
                }
            } else if (e is EntityEnderman && config.espFels && event.entity.name.contains("Fels")) {
                if (config.removeStarMobsNametag) mc.theWorld.removeEntity(event.entity) else checked.add(event.entity)
            }
            if (starMobs.add(e)) {
                if (config.removeStarMobsNametag) mc.theWorld.removeEntity(event.entity) else checked.add(event.entity)
            }
        }
    }

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        if (!inDungeons || !config.espStarMobs || config.espType == 0) return
        for (entity in mc.theWorld.loadedEntityList) {
            if (entity !is EntityArmorStand && starMobs.contains(entity)) {
                RenderUtils.drawEntityBox(entity, config.espColorStarMobs, config.espType == 2, event.partialTicks)
            }
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load?) {
        checked.clear()
        starMobs.clear()
    }

    companion object {
        private val checked = HashSet<Entity>()
        private val starMobs = HashSet<Entity>()
    }
}
