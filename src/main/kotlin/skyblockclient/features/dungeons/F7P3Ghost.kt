package skyblockclient.features.dungeons

import net.minecraft.util.BlockPos
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.Utils.isFloor

class F7P3Ghost {

    private val BLOCK_POS_LIST = listOf(
        BlockPos(87, 167, 40),
        BlockPos(88, 167, 40),
        BlockPos(89, 167, 40),
        BlockPos(90, 166, 39),
        BlockPos(90, 166, 40),
        BlockPos(90, 167, 39),
        BlockPos(90, 167, 40),
        BlockPos(91, 166, 39),
        BlockPos(91, 166, 40),
        BlockPos(91, 167, 39),
        BlockPos(91, 167, 40),
        BlockPos(92, 166, 39),
        BlockPos(92, 166, 40),
        BlockPos(92, 167, 39),
        BlockPos(92, 167, 40),
        BlockPos(93, 166, 39),
        BlockPos(93, 166, 40),
        BlockPos(93, 167, 39),
        BlockPos(93, 167, 40),
        BlockPos(94, 166, 39),
        BlockPos(94, 166, 40),
        BlockPos(94, 167, 40),
        BlockPos(94, 167, 39),
    )

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!config.f7p3Ghost || !isFloor(7)) return
        val message = stripControlCodes(event.message.unformattedText)
        if (message == "[BOSS] Necron: You caused me many troubles, your journey ends here adventurers!") {
            BLOCK_POS_LIST.forEach { mc.theWorld.setBlockToAir(it) }
        }
    }
}
