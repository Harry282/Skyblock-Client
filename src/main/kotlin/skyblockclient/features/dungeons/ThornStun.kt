package skyblockclient.features.dungeons

import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.monster.EntityGhast
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.ScoreboardUtils

class ThornStun {
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!config.afkThornStun || !inDungeons || !isF4() || event.phase != TickEvent.Phase.START) return
        thorn = mc.theWorld.loadedEntityList.filterIsInstance<EntityGhast>().sortedWith(
            Comparator.comparingDouble { e: EntityGhast -> e.getDistanceSqToEntity(mc.thePlayer) }
        ).firstOrNull() ?: return
        if (thorn!!.getDistanceSqToCenter(BlockPos(224, 82, 215)) < 9 ||
                thorn!!.getDistanceSqToCenter(BlockPos(224, 82, 223)) < 9) {
            val item = mc.thePlayer.heldItem ?: return
            if (item.displayName.contains("Tribal Spear")) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, true)
            }
        }
    }

    private fun isF4(): Boolean {
        for (s in ScoreboardUtils.sidebarLines) {
            val line = ScoreboardUtils.cleanSB(s)
            if (line.contains("The Catacombs (")) {
                val dungeonFloor = line.substring(line.indexOf("(") + 1, line.indexOf(")"))
                return dungeonFloor == "M4" || dungeonFloor == "F4"
            }
        }
        return config.forceSkyblock
    }

    companion object {
        var thorn: EntityGhast? = null
    }
}