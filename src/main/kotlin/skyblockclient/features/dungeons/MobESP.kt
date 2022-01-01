package skyblockclient.features.dungeons

import net.minecraft.entity.Entity
import net.minecraft.entity.boss.EntityWither
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.passive.EntityBat
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.RenderLivingEntityEvent
import skyblockclient.utils.OutlineUtils.outlineESP
import skyblockclient.utils.RenderUtilsKT.drawEntityBox
import java.awt.Color

class MobESP {

    @SubscribeEvent
    fun onRenderEntity(event: RenderLivingEntityEvent) {
        if (!inDungeons) return
        if (event.entity is EntityArmorStand) {
            if (config.espStarMobs && event.entity.hasCustomName() &&
                event.entity.customNameTag.contains("✯") && !checked.contains(event.entity)
            ) {
                val possibleEntities = event.entity.entityWorld.getEntitiesInAABBexcluding(
                    event.entity, event.entity.entityBoundingBox.offset(0.0, -1.0, 0.0)
                ) { it !is EntityArmorStand }
                for (entity in possibleEntities) {
                    val shouldAdd = when (entity) {
                        is EntityPlayer -> !entity.isInvisible() && entity.getUniqueID()
                            .version() == 2 && entity != mc.thePlayer
                        is EntityWither -> false
                        else -> true
                    }
                    if (shouldAdd && !starMobs.contains(entity)) {
                        if (getColor(entity) == null) starMobs.add(entity)
                        if (config.removeStarMobsNametag) {
                            mc.theWorld.removeEntity(event.entity)
                        } else checked.add(event.entity)
                        break
                    }
                }
            }
            return
        }
        if (config.espType == 0) {
            if (starMobs.contains(event.entity)) {
                outlineESP(event, config.espColorStarMobs)
            } else getColor(event.entity)?.let {
                outlineESP(event, it)
            }
        }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (!inDungeons || config.espType == 0) return
        for (entity in mc.theWorld.loadedEntityList) {
            if (starMobs.contains(entity)) {
                drawEntityBox(
                    entity,
                    config.espColorStarMobs,
                    config.espBoxOutlineOpacity != 0f,
                    config.espBoxOpacity != 0f,
                    event.partialTicks
                )
            } else getColor(entity)?.let {
                drawEntityBox(
                    entity,
                    it,
                    config.espBoxOutlineOpacity != 0F,
                    config.espBoxOpacity != 0f,
                    event.partialTicks
                )
            }
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        starMobs.clear()
        checked.clear()
    }

    companion object {
        val starMobs = HashSet<Entity>()
        private val checked = HashSet<Entity>()

        fun getColor(entity: Entity): Color? {
            return when (entity) {
                is EntityBat -> if (config.espBats && !entity.isInvisible) config.espColorBats else null
                is EntityEnderman -> if (config.espFels && entity.name == "Dinnerbone") config.espColorFels else null
                is EntityPlayer -> if ((config.espShadowAssassin || config.espMiniboss) && entity.name.contains("Shadow Assassin")) {
                    config.espColorShadowAssassin
                } else if (config.espMiniboss && entity.getCurrentArmor(0) != null) {
                    when (entity.name) {
                        "Lost Adventurer" -> if (config.espSeperateMinibossColor) {
                            when (entity.getCurrentArmor(0).displayName) {
                                "§6Unstable Dragon Boots" -> config.espColorUnstable
                                "§6Young Dragon Boots" -> config.espColorYoung
                                "§6Superior Dragon Boots" -> config.espColorSuperior
                                "§6Holy Dragon Boots" -> config.espColorHoly
                                "§6Frozen Blaze Boots" -> config.espColorFrozen
                                else -> null
                            }
                        } else config.espColorMiniboss
                        "Diamond Guy" -> if (config.espSeperateMinibossColor &&
                            entity.getCurrentArmor(0).displayName.startsWith("§6Perfect Boots - Tier")
                        ) {
                            config.espColorAngryArchaeologist
                        } else config.espColorMiniboss
                        else -> null
                    }
                } else null
                else -> null
            }
        }
    }
}