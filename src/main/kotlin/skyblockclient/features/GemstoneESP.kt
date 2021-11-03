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
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.RenderUtils
import skyblockclient.utils.ScoreboardUtils
import java.awt.Color

class GemstoneESP {
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!inSkyblock || !config.gemstoneESP || event.phase != TickEvent.Phase.START || !isCrystalHollow() ||
            lastUpdate + config.gemstoneESPTime > System.currentTimeMillis()
        ) return
        if (thread == null || !thread!!.isAlive) {
            thread = Thread({
                val blockList = HashMap<BlockPos, Color>()
                val player = mc.thePlayer.position
                val radius = config.gemstoneESPRadius
                val vec3i = Vec3i(radius, radius, radius)
                for (blockPos in BlockPos.getAllInBox(player.add(vec3i), player.subtract(vec3i))) {
                    val blockState = mc.theWorld.getBlockState(blockPos)
                    val dyeColor = when (blockState.block) {
                        Blocks.stained_glass ->
                            blockState.getValue(BlockStainedGlass.COLOR)
                        Blocks.stained_glass_pane ->
                            blockState.getValue(BlockStainedGlassPane.COLOR)
                        else -> continue
                    }
                    val color = getColor(dyeColor) ?: continue
                    blockList[blockPos] = color
                }
                synchronized(gemstoneList) {
                    gemstoneList.clear()
                    gemstoneList.putAll(blockList)
                }
                lastUpdate = System.currentTimeMillis()
            }, "Gemstone ESP")
            thread!!.start()
        }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (!inSkyblock || !config.gemstoneESP || !isCrystalHollow()) return
        synchronized(gemstoneList) {
            gemstoneList.forEach { (blockPos: BlockPos, color: Color) ->
                RenderUtils.drawBlockBox(blockPos, color, true, event.partialTicks)
            }
        }
    }

    private fun getColor(dyeColor: EnumDyeColor): Color? {
        return when (dyeColor) {
            EnumDyeColor.ORANGE ->              // AMBER
                if (config.gemstoneAmber) Color(237, 139, 35) else null
            EnumDyeColor.PURPLE ->              // AMETHYST
                if (config.gemstoneAmethyst) Color(137, 0, 201) else null
            EnumDyeColor.LIME ->                // JADE
                if (config.gemstoneJade) Color(157, 249, 32) else null
            EnumDyeColor.MAGENTA ->             // JASPER
                if (config.gemstoneJasper) Color(214, 15, 150) else null
            EnumDyeColor.RED ->                 // RUBY
                if (config.gemstoneRuby) Color(188, 3, 29) else null
            EnumDyeColor.LIGHT_BLUE ->          // SAPPHIRE
                if (config.gemstoneSapphire) Color(60, 121, 224) else null
            EnumDyeColor.YELLOW ->              // TOPAZ
                if (config.gemstoneTopaz) Color(249, 215, 36) else null
            else -> null
        }
    }

    private fun isCrystalHollow(): Boolean {
        for (s in ScoreboardUtils.sidebarLines) {
            val line = ScoreboardUtils.cleanSB(s)
            if (Locations.any { line.contains(it) }) return true
        }
        return config.forceSkyblock
    }

    companion object {
        private val gemstoneList = HashMap<BlockPos, Color>()
        private var lastUpdate: Long = 0
        private val Locations = listOf(
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
    }
}