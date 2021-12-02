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
        if (!config.mimicKillMessage || !inDungeons || event.entity !is EntityZombie || mimicKilled) return
        val entity = event.entity as EntityZombie
        if (entity.isChild) {
            for (i in 0..3) if (entity.getCurrentArmor(i) != null) return
            mimicKilled = true
            mc.thePlayer.sendChatMessage("/pc ${config.mimicMessage}")
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