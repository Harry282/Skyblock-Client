package skyblockclient.features

import net.minecraft.block.BlockStainedGlass
import net.minecraft.block.BlockStainedGlassPane
import net.minecraft.init.Blocks
import net.minecraft.item.EnumDyeColor
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3i
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.RenderUtils
import java.awt.Color

class GemstoneESP {
    @SubscribeEvent
    fun onUpdate(event: LivingUpdateEvent) {
        if (!inSkyblock || !config.gemstoneESP) return
        if (event.entity === mc.thePlayer && lastUpdate + config.gemstoneESPTime < System.currentTimeMillis()) {
            if (thread == null || !thread!!.isAlive) {
                thread = Thread({
                    val blockList =
                        HashMap<BlockPos, Color>()
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
                            else ->
                                continue
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
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (!inSkyblock || !config.gemstoneESP) return
        synchronized(gemstoneList) {
            gemstoneList.forEach { (blockPos: BlockPos, color: Color?) ->
                RenderUtils.drawBlockBox(
                    blockPos, color, true, event.partialTicks
                )
            }
        }
    }

    private fun getColor(dyeColor: EnumDyeColor): Color? {
        return when (dyeColor) {
            EnumDyeColor.RED ->                 // RUBY
                Color(188, 3, 29)
            EnumDyeColor.PURPLE ->              // AMETHYST
                Color(137, 0, 201)
            EnumDyeColor.LIME ->                // JADE
                Color(157, 249, 32)
            EnumDyeColor.LIGHT_BLUE ->          // SAPPHIRE
                Color(60, 121, 224)
            EnumDyeColor.ORANGE ->              // AMBER
                Color(237, 139, 35)
            EnumDyeColor.YELLOW ->              // TOPAZ
                Color(249, 215, 36)
            EnumDyeColor.MAGENTA ->             // JASPER
                Color(214, 15, 150)
            else -> null
        }
    }

    companion object {
        private val gemstoneList = HashMap<BlockPos, Color>()
        private var lastUpdate: Long = 0
        private var thread: Thread? = null
    }
}