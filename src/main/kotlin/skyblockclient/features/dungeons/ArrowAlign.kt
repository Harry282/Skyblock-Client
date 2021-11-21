package skyblockclient.features.dungeons

import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityItemFrame
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.util.BlockPos
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.RightClickEvent
import skyblockclient.utils.ScoreboardUtils
import java.awt.Point
import java.lang.reflect.Method
import java.util.*

class ArrowAlign {
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!config.arrowAlign && !config.autoCompleteArrowAlign || !inDungeons || !isF7() || event.phase != TickEvent.Phase.START) return
        if (ticks % 20 == 0) {
            if (mc.thePlayer.getDistanceSq(BlockPos(197, 122, 276)) <= 20 * 20) calculate()
            ticks = 0
        }
        ticks++
    }

    @SubscribeEvent
    fun onRightClick(event: RightClickEvent) {
        if (!config.arrowAlign && !config.autoCompleteArrowAlign || !isF7() || mc.objectMouseOver == null) return
        if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            if (mc.objectMouseOver.entityHit is EntityItemFrame) {
                if (mc.thePlayer.isSneaking && config.arrowAlignSneakOverride) return
                val frame = mc.objectMouseOver.entityHit as EntityItemFrame
                val x = 278 - frame.hangingPosition.z
                val y = 124 - frame.hangingPosition.y
                if (x in 0..4 && y in 0..4) {
                    val clicks = neededRotations[Point(x, y)] ?: return
                    if (clicks == 0) {
                        event.isCanceled = true
                        return
                    }
                    neededRotations[Point(x, y)] = clicks - 1
                    if (config.autoCompleteArrowAlign && clicks > 1) {
                        rightClickMouse.isAccessible = true
                        rightClickMouse.invoke(mc)
                    }
                }
            }
        }
    }

    private fun isF7(): Boolean {
        for (s in ScoreboardUtils.sidebarLines) {
            val line = ScoreboardUtils.cleanSB(s)
            if (line.contains("The Catacombs (")) {
                val dungeonFloor = line.substring(line.indexOf("(") + 1, line.indexOf(")"))
                return dungeonFloor == "F7"
            }
        }
        return config.forceSkyblock
    }

    private fun calculate() {
        val frames = mc.theWorld.getEntities(EntityItemFrame::class.java) {
            it != null && area.contains(it.position) && it.displayedItem != null
        }
        if (frames.isNotEmpty()) {
            val solutions = HashMap<Point, Int>()
            val maze = Array(5) { IntArray(5) }
            val queue = LinkedList<Point>()
            val visited = Array(5) { BooleanArray(5) }
            neededRotations.clear()
            for ((i, pos) in area.withIndex()) {
                val x = i % 5
                val y = i / 5
                val frame = frames.find { it.position == pos } ?: continue
                // 0 = null, 1 = arrow, 2 = end, 3 = start
                maze[x][y] = when (frame.displayedItem.item) {
                    Items.arrow -> 1
                    Item.getItemFromBlock(Blocks.wool) -> {
                        when (frame.displayedItem.itemDamage) {
                            5 -> 3
                            14 -> 2
                            else -> 0
                        }
                    }
                    else -> 0
                }
                when (maze[x][y]) {
                    1 -> neededRotations[Point(x, y)] = frame.rotation
                    3 -> queue.add(Point(x, y))
                }
            }
            while (queue.size != 0) {
                val s = queue.poll()
                val directions = arrayOf(intArrayOf(1, 0), intArrayOf(0, 1), intArrayOf(-1, 0), intArrayOf(0, -1))
                for (i in 3 downTo 0) {
                    val x = (s.x + directions[i][0])
                    val y = (s.y + directions[i][1])
                    if (x in 0..4 && y in 0..4) {
                        val rotations = i * 2 + 1
                        if (solutions[Point(x, y)] == null && maze[x][y] in 1..2) {
                            queue.add(Point(x, y))
                            solutions[s] = rotations
                            if (!visited[s.x][s.y]) {
                                var neededRotation = neededRotations[s] ?: continue
                                neededRotation = rotations - neededRotation
                                if (neededRotation < 0) neededRotation += 8
                                neededRotations[s] = neededRotation
                                visited[s.x][s.y] = true
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val area =
            BlockPos.getAllInBox(BlockPos(197, 125, 278), BlockPos(197, 121, 274)).toList().sortedWith { a, b ->
                if (a.y == b.y) return@sortedWith b.z - a.z
                if (a.y < b.y) return@sortedWith 1
                if (a.y > b.y) return@sortedWith -1
                return@sortedWith 0
            }
        private var ticks = 0
        private val neededRotations = HashMap<Point, Int>()
        private val rightClickMouse: Method = try {
            Minecraft::class.java.getDeclaredMethod("func_147121_ag")
        } catch (e: NoSuchMethodException) {
            Minecraft::class.java.getDeclaredMethod("rightClickMouse")
        }
    }
}