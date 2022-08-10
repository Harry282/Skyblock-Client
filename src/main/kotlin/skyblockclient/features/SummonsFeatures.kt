package skyblockclient.features

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityZombie
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.utils.LocationUtils.inDungeons
import skyblockclient.utils.LocationUtils.inSkyblock
import skyblockclient.utils.Utils.itemID


object SummonsFeatures {

    @SubscribeEvent
    fun onPreRenderEntity(event: RenderLivingEvent.Pre<EntityLivingBase?>) {
        if (!config.ghostSummons || !inSkyblock || inDungeons) return
        if (isSummon(event.entity)) {
            event.entity.setDead()
            event.isCanceled = true
        }
    }

    private fun isSummon(entity: Entity): Boolean =
        entity is EntityZombie && entity.getEquipmentInSlot(1)?.itemID == "HEAVY_BOOTS"
}
