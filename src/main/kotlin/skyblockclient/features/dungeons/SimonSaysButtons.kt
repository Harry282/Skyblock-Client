package skyblockclient.features.dungeons

import net.minecraft.init.Blocks
import net.minecraft.util.Vec3
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc


object SimonSaysButtons {
    @SubscribeEvent
    fun onInteract(event: PlayerInteractEvent) {
        if (!config.simonSaysButtons || !inSkyblock || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return
        val block = mc.objectMouseOver?.blockPos ?: return
        if (block.x == 111 && block.y in 120..123 && block.z in 92..95) {
            if (mc.theWorld.getBlockState(block.west()).block == Blocks.stone_button) {
                event.isCanceled = true
                if (mc.playerController.onPlayerRightClick(
                        mc.thePlayer,
                        mc.theWorld,
                        mc.thePlayer.inventory.getCurrentItem(),
                        block.west(),
                        mc.objectMouseOver?.sideHit,
                        block.run { Vec3(110.0625, y + 0.5, z + 0.5) }
                    )
                ) {
                    mc.thePlayer.swingItem()
                }
            }
        }
    }
}
