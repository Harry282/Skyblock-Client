package skyblockclient.features.dungeons

import net.minecraft.util.BlockPos
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.Utils.isFloor

class F7P3Ghost {
    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!config.f7p3Ghost || !isFloor(7)) return
        val message = stripControlCodes(event.message.unformattedText)
        if (message == "[BOSS] Necron: You caused me many troubles, your journey ends here adventurers!") {
            BLOCK_POS_LIST.forEach { mc.theWorld.setBlockToAir(it) }
        }
    }

    companion object {
        private val BLOCK_POS_LIST = listOf(
            BlockPos(287, 167, 240),

            BlockPos(288, 167, 240),

            BlockPos(289, 167, 240),

            BlockPos(290, 166, 239),
            BlockPos(290, 166, 240),
            BlockPos(290, 167, 239),
            BlockPos(290, 167, 240),

            BlockPos(291, 166, 239),
            BlockPos(291, 166, 240),
            BlockPos(291, 167, 239),
            BlockPos(291, 167, 240),

            BlockPos(292, 166, 239),
            BlockPos(292, 166, 240),
            BlockPos(292, 167, 239),
            BlockPos(292, 167, 240),

            BlockPos(293, 166, 239),
            BlockPos(293, 166, 240),
            BlockPos(293, 167, 239),
            BlockPos(293, 167, 240),

            BlockPos(294, 166, 239),
            BlockPos(294, 166, 240),
            BlockPos(294, 167, 240),
            BlockPos(294, 167, 239),
        )
    }
}