package skyblockclient.features.nether

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.Utils

object FirePillar {
    @SubscribeEvent
    fun onOverlay(event: RenderGameOverlayEvent.Post) {
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR || !config.showFirePillar || !inSkyblock || mc.ingameGUI == null) return
        mc.theWorld.loadedEntityList.filterIsInstance<EntityArmorStand>()
            .filter { StringUtils.stripControlCodes(it.name).endsWith("s 8 hits") }.withIndex().forEach {
                val sr = ScaledResolution(mc)
                Utils.renderText(
                    text = it.value.name,
                    x = sr.scaledWidth / 2 - mc.fontRendererObj.getStringWidth(it.value.name),
                    y = sr.scaledHeight / 2 - 30 * it.index,
                    scale = 2.0
                )
            }
    }
}
