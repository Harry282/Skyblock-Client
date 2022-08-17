package skyblockclient.features.dungeons

import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.LocationUtils.inDungeons

object CrystalPlacer {
    private var lastPlace = 0L

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || !inDungeons || !config.crystalPlacer || System.currentTimeMillis() - lastPlace < 500) return
        if (mc.thePlayer?.inventory?.getStackInSlot(8)?.displayName?.contains("Energy Crystal") == true) {
            mc.theWorld.loadedEntityList.filter { entity ->
                entity is EntityArmorStand && entity.displayName.unformattedText.contains("CLICK HERE") && mc.theWorld.getEntitiesWithinAABBExcludingEntity(
                    entity, entity.entityBoundingBox
                ).any {
                    it is EntityArmorStand && it.displayName.unformattedText.contains("Energy Crystal Missing")
                }
            }.minByOrNull { it.getDistanceSqToEntity(mc.thePlayer) }?.let {
                mc.playerController.interactWithEntitySendPacket(mc.thePlayer, it)
                lastPlace = System.currentTimeMillis()
            }
        }
    }
}
