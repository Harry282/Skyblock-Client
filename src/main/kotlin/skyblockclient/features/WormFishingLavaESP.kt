package skyblockclient.features

import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3i
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.RenderUtils
import skyblockclient.utils.ScoreboardUtils
import java.awt.Color

object WormFishingLavaESP {

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

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || !config.wormFishingLavaESP || !isCrystalHollow()) return
        if (thread?.isAlive == true || lastUpdate + config.wormFishingLavaESPTime > System.currentTimeMillis()) return
        thread = Thread({
            val blockList: MutableList<BlockPos> = mutableListOf()
            val player = mc.thePlayer.position
            val radius = config.wormFishingLavaESPRadius
            val vec3i = Vec3i(radius, radius, radius)

            BlockPos.getAllInBox(player.add(vec3i), player.subtract(vec3i)).forEach {
                val blockState = mc.theWorld.getBlockState(it)

                if ((blockState.block == Blocks.lava || blockState.block == Blocks.flowing_lava) && it.y > 64) {
                    blockList.add(it)
                }
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
        if (!config.wormFishingLavaESP || !isCrystalHollow() || disabledInSession) return
        val player = mc.thePlayer.position
        synchronized(lavaBlocksList) {
            lavaBlocksList.forEach { blockPos ->
                if (!config.wormFishingLavaHideNear || player.distanceSq(blockPos) > 40 && config.wormFishingLavaHideNear) {
                    RenderUtils.drawBlockBox(blockPos, Color.ORANGE, outline = true, fill = true, event.partialTicks)
                }
            }
        }
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if (!config.wormFishingHideFished || event.type.toInt() == 2) return
        if (event.message.unformattedText == "A flaming worm surfaces from the depths!") {
            disabledInSession = true
        }
    }

    @SubscribeEvent
    fun onChangeWorld(event: WorldEvent.Load) {
        if (!config.wormFishingHideFished || !disabledInSession) return
        disabledInSession = false
    }

    private fun isCrystalHollow(): Boolean {
        return inSkyblock && ScoreboardUtils.sidebarLines.any { s ->
            locations.any { ScoreboardUtils.cleanSB(s).contains(it) }
        } || config.forceSkyblock
    }
}
