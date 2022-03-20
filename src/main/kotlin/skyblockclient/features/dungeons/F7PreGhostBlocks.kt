package skyblockclient.features.dungeons

import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.util.BlockPos
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.ReceivePacketEvent
import skyblockclient.utils.Utils.isFloor

object F7PreGhostBlocks {

    private val P3_BLOCK_POS_LIST = listOf(
        BlockPos(87, 167, 40),
        BlockPos(87, 167, 41),
        BlockPos(88, 167, 41),
        BlockPos(89, 167, 41),
        BlockPos(90, 167, 41),
        BlockPos(91, 166, 40),
        BlockPos(91, 166, 41),
        BlockPos(91, 167, 40),
        BlockPos(91, 167, 41),
        BlockPos(92, 166, 40),
        BlockPos(92, 166, 41),
        BlockPos(92, 167, 40),
        BlockPos(92, 167, 41),
        BlockPos(93, 166, 40),
        BlockPos(93, 166, 41),
        BlockPos(93, 167, 40),
        BlockPos(93, 167, 41),
        BlockPos(94, 166, 40),
        BlockPos(94, 166, 41),
        BlockPos(94, 167, 41),
        BlockPos(94, 167, 40),
        BlockPos(95, 166, 40),
        BlockPos(95, 166, 41),
        BlockPos(95, 167, 41),
        BlockPos(95, 167, 40),
    )

    private val P5_BLOCK_POS_LIST = listOf(
        BlockPos(54, 64, 80),
        BlockPos(54, 64, 79),
        BlockPos(54, 63, 79)
    )

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onChatPacket(event: ReceivePacketEvent) {
        if (event.packet !is S02PacketChat || event.packet.type.toInt() == 2 || !isFloor(7)) return
        val message = stripControlCodes(event.packet.chatComponent.unformattedText)
        if (Regex("\\[BOSS] (Maxor|Storm|Goldor|Necron): .+").matches(message)) {
            if (config.f7p3Ghost) {
                P3_BLOCK_POS_LIST.forEach { mc.theWorld.setBlockToAir(it) }
            }
            if (config.m7p5Ghost) {
                P5_BLOCK_POS_LIST.forEach { mc.theWorld.setBlockToAir(it) }
            }
        }
    }
}
