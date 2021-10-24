package skyblockclient.features.dungeons

import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.passive.EntityBat
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.RenderLivingEntityEvent
import skyblockclient.utils.OutlineUtils
import skyblockclient.utils.RenderUtils

class HiddenMobs {
    @SubscribeEvent
    fun onRenderEntity(event: RenderLivingEntityEvent) {
        if (!inDungeons) return
        if (event.entity is EntityEnderman && event.entity.name == "Dinnerbone") {
            if (config.showFels) event.entity.isInvisible = false
            if (config.espFels && config.espType == 0) {
                OutlineUtils.outlineESP(event, config.espColorFels)
            }
        } else if (event.entity is EntityPlayer) {
            if (event.entity.name.contains("Shadow Assassin")) {
                if (config.showShadowAssassin) event.entity.isInvisible = false
                if (config.espShadowAssassin && config.espType == 0) {
                    OutlineUtils.outlineESP(event, config.espColorShadowAssassin)
                }
            } else if (config.showStealthy) {
                if (watcherMobs.any { s: String ->
                        event.entity.name.trim { it <= ' ' } == s
                    }) {
                    event.entity.isInvisible = false
                }
            }
        } else if (config.espBats && event.entity is EntityBat &&
            !event.entity.isInvisible && config.espType == 0
        ) {
            OutlineUtils.outlineESP(event, config.espColorBats)
        }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (config.espType == 0) return
        for (entity in mc.theWorld.loadedEntityList) {
            if (entity is EntityEnderman && entity.getName() == "Dinnerbone" && config.espFels) {
                RenderUtils.drawEntityBox(
                    entity,
                    config.espColorFels,
                    config.espType == 2,
                    event.partialTicks
                )
            } else if (entity is EntityPlayer && config.showShadowAssassin &&
                entity.getName().contains("Shadow Assassin") && config.espShadowAssassin
            ) {
                RenderUtils.drawEntityBox(
                    entity,
                    config.espColorShadowAssassin,
                    config.espType == 2,
                    event.partialTicks
                )
            }
            if (entity is EntityBat && !entity.isInvisible() && config.espBats) {
                RenderUtils.drawEntityBox(
                    entity,
                    config.espColorBats,
                    config.espType == 2,
                    event.partialTicks
                )
            }
        }
    }

    companion object {
        private val watcherMobs = listOf(
            "Revoker",
            "Psycho",
            "Reaper",
            "Cannibal",
            "Mute",
            "Ooze",
            "Putrid",
            "Freak",
            "Leech",
            "Tear",
            "Parasite",
            "Flamer",
            "Skull",
            "Mr. Dead",
            "Vader",
            "Frost",
            "Walker",
            "Wandering Soul",
            "Bonzo",
            "Scarf",
            "Livid"
        )
    }
}