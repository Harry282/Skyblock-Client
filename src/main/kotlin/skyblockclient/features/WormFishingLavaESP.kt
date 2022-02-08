package skyblockclient.features

import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3i
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient
import skyblockclient.utils.RenderUtils
import skyblockclient.utils.ScoreboardUtils
import java.awt.Color

class WormFishingLavaESP {
    private val lavaBlocksList: MutableList<BlockPos> = mutableListOf()
    private var lastUpdate: Long = 0
    private val locations = listOf(
        "Precursor Remnants",
        "Lost Precursor City",
        "Magma Fields",
        "Khazad-dûm",
    )
    private var thread: Thread? = null
    private var disabledInSession: Boolean = false

    //#region ESP
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || !SkyblockClient.config.wormFishingLavaESP || !isCrystalHollow()) return
        if (thread?.isAlive == true || lastUpdate + SkyblockClient.config.wormFishingLavaESPTime > System.currentTimeMillis()) return
        thread = Thread({
            val blockList: MutableList<BlockPos> = mutableListOf()
            val player = SkyblockClient.mc.thePlayer.position
            val radius = SkyblockClient.config.wormFishingLavaESPRadius
            val vec3i = Vec3i(radius, radius, radius)

            BlockPos.getAllInBox(player.add(vec3i), player.subtract(vec3i)).forEach {
                val blockState = SkyblockClient.mc.theWorld.getBlockState(it)

                if ((blockState.block == Blocks.lava || blockState.block == Blocks.flowing_lava) && it.y > 64)
                    blockList.add(it)
            }
            synchronized(lavaBlocksList) {
                lavaBlocksList.clear()
                lavaBlocksList.addAll(blockList)
            }
            lastUpdate = System.currentTimeMillis()
        }, "Worm fishing ESP")
        thread!!.start()
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (!SkyblockClient.config.wormFishingLavaESP || !isCrystalHollow()) return

        val player = SkyblockClient.mc.thePlayer.position
        synchronized(lavaBlocksList) {
            lavaBlocksList.forEach { blockPos ->
                if (!disabledInSession)
                    if (player.distanceSq(blockPos) > 40 && SkyblockClient.config.wormFishingLavaHideNear)
                        RenderUtils.drawBlockBox(blockPos, Color.ORANGE, outline = true, fill = true, event.partialTicks)
                    else if (!SkyblockClient.config.wormFishingLavaHideNear)
                        RenderUtils.drawBlockBox(blockPos, Color.ORANGE, outline = true, fill = true, event.partialTicks)
            }
        }
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if (!SkyblockClient.config.wormFishingHideFished) return

        if (event.message.unformattedText == "A flaming worm surfaces from the depths!")
            disabledInSession = true
    }

    @SubscribeEvent
    fun onChangeWorld(event: WorldEvent.Load) {
        if (!SkyblockClient.config.wormFishingHideFished || !disabledInSession) return

        disabledInSession = false
    }
    //#endregion

    private fun isCrystalHollow(): Boolean {
        return SkyblockClient.inSkyblock && ScoreboardUtils.sidebarLines.any { s -> locations.any { ScoreboardUtils.cleanSB(s).contains(it) } } || SkyblockClient.config.forceSkyblock
    }
}