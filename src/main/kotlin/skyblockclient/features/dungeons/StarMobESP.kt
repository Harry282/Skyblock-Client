package skyblockclient.features.dungeons

import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
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
        if (!inDungeons || !config.espStarMobs) return
        if (event.entity is EntityArmorStand) {
            if (event.entity.hasCustomName() && event.entity.customNameTag.contains("✯")) {
                if (checkedArmorStands.contains(event.entity)) return
                val possibleEntities = event.entity.entityWorld.getEntitiesInAABBexcluding(
                    event.entity, event.entity.entityBoundingBox.expand(0.0, 3.0, 0.0)
                ) { entity: Entity? -> entity !is EntityArmorStand }
                if (possibleEntities.isNotEmpty()) {
                    for (e in possibleEntities) {
                        if (e == mc.thePlayer) {
                            return
                        } else if (e is EntityOtherPlayerMP) {
                            if (e.isInvisible() || e.getUniqueID().version() != 2) return
                            if (config.espMiniboss) {
                                if (event.entity.name.contains(" Adventurer") && e.getName() == "Lost Adventurer") {
                                    checkedArmorStands.add(event.entity)
                                    return
                                }
                                if (event.entity.name.contains("Angry Archeologist") && e.getName() == "Diamond Guy") {
                                    checkedArmorStands.add(event.entity)
                                    return
                                }
                            }
                        } else if (e is EntityEnderman && config.espFels) {
                            return
                        }
                        if (starMobs.add(e)) checkedArmorStands.add(event.entity)
                    }
                }
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
        checkedArmorStands.clear()
        starMobs.clear()
    }

    companion object {
        private val checkedArmorStands = HashSet<Entity>()
        private val starMobs = HashSet<Entity>()
    }
}
