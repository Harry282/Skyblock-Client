package skyblockclient.features.dungeons

import net.minecraft.entity.monster.EntityZombie
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.mc

class MimicMessage {
    @SubscribeEvent
    fun onEntityDeath(event: LivingDeathEvent) {
        if (!inDungeons || !config.mimicKillMessage || event.entity !is EntityZombie) return
        val entity = event.entity as EntityZombie
        if (!mimicKilled && entity.isChild && entity.getCurrentArmor(0) == null && entity.getCurrentArmor(1) == null &&
            entity.getCurrentArmor(2) == null && entity.getCurrentArmor(3) == null
        ) {
            mimicKilled = true
            mc.thePlayer.sendChatMessage("/pc " + config.mimicMessage)
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load?) {
        mimicKilled = false
    }

    companion object {
        private var mimicKilled = false
    }
}