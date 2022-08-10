package skyblockclient.features

import net.minecraft.block.BlockStainedGlass
import net.minecraft.block.BlockStainedGlassPane
import net.minecraft.init.Blocks
import net.minecraft.item.EnumDyeColor
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3i
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.LocationUtils.inSkyblock
import skyblockclient.utils.RenderUtils.drawBlockBox
import skyblockclient.utils.ScoreboardUtils.cleanSB
import skyblockclient.utils.ScoreboardUtils.sidebarLines
import java.awt.Color

object GemstoneESP {

    private val gemstoneList = HashMap<BlockPos, Color>()
    private var lastUpdate: Long = 0
    private val locations = listOf(
        "Jungle",
        "Jungle Temple",
        "Mithril Deposits",
        "Mines of Divan",
        "Goblin Holdout",
        "Goblin Queen's Den",
        "Precursor Remnants",
        "Lost Precursor City",
        "Crystal Nucleus",
        "Magma Fields",
        "Khazad-dûm",
        "Fairy Grotto",
        "Dragon's Lair"
    )
    private var thread: Thread? = null

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || !config.gemstoneESP || !isCrystalHollow()) return
        if (thread?.isAlive == true || lastUpdate + config.gemstoneESPTime > System.currentTimeMillis()) return
        thread = Thread({
            val blockList = HashMap<BlockPos, Color>()
            val player = mc.thePlayer.position
            val radius = config.gemstoneESPRadius
            val vec3i = Vec3i(radius, radius, radius)
            BlockPos.getAllInBox(player.add(vec3i), player.subtract(vec3i)).forEach {
                val blockState = mc.theWorld.getBlockState(it)
                val dyeColor = when (blockState.block) {
                    Blocks.stained_glass -> blockState.getValue(BlockStainedGlass.COLOR)
                    Blocks.stained_glass_pane -> blockState.getValue(BlockStainedGlassPane.COLOR)
                    else -> return@forEach
                }
                val color = getColor(dyeColor) ?: return@forEach
                blockList[it] = color
            }
            synchronized(gemstoneList) {
                gemstoneList.clear()
                gemstoneList.putAll(blockList)
            }
            lastUpdate = System.currentTimeMillis()
        }, "Gemstone ESP")
        thread!!.start()
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (!config.gemstoneESP || !isCrystalHollow()) return
        synchronized(gemstoneList) {
            gemstoneList.forEach { (blockPos, color) ->
                drawBlockBox(blockPos, color, outline = true, fill = false, event.partialTicks)
            }
        }
    }

    private fun getColor(dyeColor: EnumDyeColor): Color? {
        return when (dyeColor) {
            EnumDyeColor.ORANGE -> if (config.gemstoneAmber) Color(237, 139, 35) else null
            EnumDyeColor.PURPLE -> if (config.gemstoneAmethyst) Color(137, 0, 201) else null
            EnumDyeColor.LIME -> if (config.gemstoneJade) Color(157, 249, 32) else null
            EnumDyeColor.MAGENTA -> if (config.gemstoneJasper) Color(214, 15, 150) else null
            EnumDyeColor.RED -> if (config.gemstoneRuby) Color(188, 3, 29) else null
            EnumDyeColor.LIGHT_BLUE -> if (config.gemstoneSapphire) Color(60, 121, 224) else null
            EnumDyeColor.YELLOW -> if (config.gemstoneTopaz) Color(249, 215, 36) else null
            else -> null
        }
    }

    private fun isCrystalHollow(): Boolean {
        return inSkyblock && sidebarLines.any { s -> locations.any { cleanSB(s).contains(it) } } || config.forceSkyblock
    }
}
