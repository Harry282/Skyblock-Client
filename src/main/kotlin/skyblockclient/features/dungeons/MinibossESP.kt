package skyblockclient.features.dungeons

import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.RenderLivingEntityEvent
import skyblockclient.utils.OutlineUtils
import skyblockclient.utils.RenderUtils

class MinibossESP {
    @SubscribeEvent
    fun onRenderEntity(event: RenderLivingEntityEvent) {
        if (!inDungeons || !config.espMiniboss || config.espType != 0) return
        if (event.entity is EntityPlayer) {
            if (event.entity.name == "Lost Adventurer") {
                if (event.entity.getCurrentArmor(0) != null) {
                    when (event.entity.getCurrentArmor(0).displayName) {
                        "§6Unstable Dragon Boots" ->
                            OutlineUtils.outlineESP(event, config.espColorUnstable)
                        "§6Young Dragon Boots" ->
                            OutlineUtils.outlineESP(event, config.espColorYoung)
                        "§6Superior Dragon Boots" ->
                            OutlineUtils.outlineESP(event, config.espColorSuperior)
                        "§6Holy Dragon Boots" ->
                            OutlineUtils.outlineESP(event, config.espColorHoly)
                        "§6Frozen Blaze Boots" ->
                            OutlineUtils.outlineESP(event, config.espColorFrozen)
                    }
                }
            } else if (event.entity.name == "Diamond Guy" && event.entity.getCurrentArmor(0) != null &&
                event.entity.getCurrentArmor(0).displayName.startsWith("§6Perfect Boots - Tier")
            ) {
                OutlineUtils.outlineESP(event, config.espColorAngryArchaeologist)
            }
        }
    }

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        if (!inDungeons || !config.espMiniboss || config.espType == 0) return
        for (e in mc.theWorld.loadedEntityList) {
            if (e is EntityPlayer) {
                if (e.name == "Lost Adventurer") {
                    if (e.getCurrentArmor(0) != null) {
                        when (e.getCurrentArmor(0).displayName) {
                            "§6Unstable Dragon Boots" ->
                                RenderUtils.drawEntityBox(
                                    e,
                                    config.espColorUnstable,
                                    config.espType == 2,
                                    event.partialTicks
                                )
                            "§6Young Dragon Boots" ->
                                RenderUtils.drawEntityBox(
                                    e,
                                    config.espColorYoung,
                                    config.espType == 2,
                                    event.partialTicks
                                )
                            "§6Superior Dragon Boots" ->
                                RenderUtils.drawEntityBox(
                                    e,
                                    config.espColorSuperior,
                                    config.espType == 2,
                                    event.partialTicks
                                )
                            "§6Holy Dragon Boots" ->
                                RenderUtils.drawEntityBox(
                                    e,
                                    config.espColorHoly,
                                    config.espType == 2,
                                    event.partialTicks
                                )
                            "§6Frozen Blaze Boots" -> RenderUtils.drawEntityBox(
                                e,
                                config.espColorFrozen,
                                config.espType == 2,
                                event.partialTicks
                            )
                        }
                    }
                } else if (e.name == "Diamond Guy" && e.getCurrentArmor(0) != null &&
                    e.getCurrentArmor(0).displayName.startsWith("§6Perfect Boots - Tier")
                ) {
                    RenderUtils.drawEntityBox(
                        e,
                        config.espColorAngryArchaeologist,
                        config.espType == 2,
                        event.partialTicks
                    )
                }
            }
        }
    }
}