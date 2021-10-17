package skyblockclient.features

import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import skyblockclient.SkyblockClient
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.RightClickEvent

class GhostBlock {
    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!inSkyblock || event.phase != TickEvent.Phase.START) return
        if (SkyblockClient.keyBinds[2]!!.isKeyDown) {
            toAir(mc.objectMouseOver.blockPos)
        }
    }

    @SubscribeEvent
    fun onRightClick(event: RightClickEvent) {
        if (!inSkyblock || !config.stonkGhostBlock || mc.objectMouseOver == null) return
        if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            val item = mc.thePlayer.heldItem ?: return
            if (item.displayName.contains("Stonk")) {
                event.isCanceled = toAir(mc.objectMouseOver.blockPos)
            }
        }
    }

    private fun toAir(blockPos: BlockPos?): Boolean {
        if (blockPos != null) {
            val block = mc.theWorld.getBlockState(mc.objectMouseOver.blockPos).block
            if (!blacklist.contains(block)) {
                mc.theWorld.setBlockToAir(mc.objectMouseOver.blockPos)
                return true
            }
        }
        return false
    }

    companion object {
        private val blacklist = ArrayList(
            listOf(
                Blocks.acacia_door,
                Blocks.anvil,
                Blocks.beacon,
                Blocks.bed,
                Blocks.birch_door,
                Blocks.brewing_stand,
                Blocks.command_block,
                Blocks.crafting_table,
                Blocks.chest,
                Blocks.dark_oak_door,
                Blocks.daylight_detector,
                Blocks.daylight_detector_inverted,
                Blocks.dispenser,
                Blocks.dropper,
                Blocks.enchanting_table,
                Blocks.ender_chest,
                Blocks.furnace,
                Blocks.hopper,
                Blocks.jungle_door,
                Blocks.lever,
                Blocks.noteblock,
                Blocks.powered_comparator,
                Blocks.unpowered_comparator,
                Blocks.powered_repeater,
                Blocks.unpowered_repeater,
                Blocks.standing_sign,
                Blocks.wall_sign,
                Blocks.trapdoor,
                Blocks.trapped_chest,
                Blocks.wooden_button,
                Blocks.stone_button,
                Blocks.oak_door,
                Blocks.skull
            )
        )
    }
}