package skyblockclient.features.dungeons

import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.monster.EntityGhast
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.Utils.isFloor
import skyblockclient.utils.Utils.itemID

class ThornStun {
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || !config.afkThornStun || !isFloor(4)) return
        thorn = mc.theWorld.loadedEntityList.filterIsInstance<EntityGhast>().sortedWith(
            Comparator.comparingDouble { it.getDistanceSqToEntity(mc.thePlayer) }
        ).firstOrNull() ?: return
        if (thorn!!.getDistanceSqToCenter(BlockPos(224, 82, 215)) < 9 ||
            thorn!!.getDistanceSqToCenter(BlockPos(224, 82, 223)) < 9
        ) {
            if (mc.thePlayer.heldItem?.itemID == "TRIBAL_SPEAR") {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, true)
            }
        }
    }

    companion object {
        var thorn: EntityGhast? = null
    }
}