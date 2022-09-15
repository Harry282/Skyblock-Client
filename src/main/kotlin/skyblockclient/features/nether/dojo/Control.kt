package skyblockclient.features.nether.dojo

import net.minecraft.entity.monster.EntitySkeleton
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.LocationUtils.inSkyblock
import skyblockclient.utils.ScoreboardUtils
import skyblockclient.utils.VecUtils

object Control {
    private var target: EntitySkeleton? = null

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (target != null || event.phase != TickEvent.Phase.START || !inSkyblock || !config.dojoControl || !isControlDojo) return
        val entities = mc.theWorld.loadedEntityList.filterIsInstance<EntitySkeleton>().filter {
            it.skeletonType == 1 && it.getDistanceSqToEntity(mc.thePlayer) < 400
        }
        if (entities.isEmpty()) return
        target = entities.firstOrNull { it.getCurrentArmor(3)?.item != Item.getItemFromBlock(Blocks.redstone_block) }
    }

    @SubscribeEvent
    fun onFrame(event: RenderWorldLastEvent) {
        if (!inSkyblock || !config.dojoControl || target == null) return

        target?.run {
            if (mc.thePlayer.getPositionEyes(event.partialTicks).distanceTo(getPositionEyes(event.partialTicks)) > 30) {
                target = null
                return@run
            }
            val rotation = VecUtils.getRotation(
                mc.thePlayer.getPositionEyes(event.partialTicks), getPositionEyes(event.partialTicks + config.tickOffset)
            )
            mc.thePlayer.rotationYaw = rotation.yaw
            mc.thePlayer.rotationPitch = rotation.pitch
        }
    }

    private val isControlDojo
        get() = ScoreboardUtils.sidebarLines.any { s ->
            StringUtils.stripControlCodes(ScoreboardUtils.cleanSB(s)) == "Challenge: Control"
        } || config.forceSkyblock
}
