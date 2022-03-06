package skyblockclient.features.dungeons

import net.minecraft.entity.item.EntityItemFrame
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.ClickEvent
import skyblockclient.utils.Utils.isFloor
import skyblockclient.utils.Utils.rightClick
import java.util.*

class ArrowAlign {

    private val area = BlockPos.getAllInBox(BlockPos(-2, 125, 79), BlockPos(-2, 121, 75))
        .toList().sortedWith { a, b ->
            if (a.y == b.y) return@sortedWith b.z - a.z
            if (a.y < b.y) return@sortedWith 1
            if (a.y > b.y) return@sortedWith -1
            return@sortedWith 0
        }
    private val neededRotations = HashMap<Pair<Int, Int>, Int>()
    var ticks = 0

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || config.arrowAlignSolver == 0 || !isFloor(7)) return
        ticks++
        if (mc.thePlayer.getDistanceSq(BlockPos(-2, 122, 76)) <= 15 * 15) {
            if (ticks % 20 == 0) {
                calculate()
                ticks = 0
            }
            if (mc.objectMouseOver?.entityHit is EntityItemFrame) {
                if (mc.thePlayer.isSneaking && config.arrowAlignSneakOverride) return
                val frame = mc.objectMouseOver.entityHit as EntityItemFrame
                val x = 79 - frame.hangingPosition.z
                val y = 124 - frame.hangingPosition.y
                if (x in 0..4 && y in 0..4) {
                    val clicks = neededRotations[Pair(x, y)] ?: return
                    if (config.arrowAlignSolver == 3 && clicks > 0) {
                        rightClick()
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun onRightClick(event: ClickEvent.RightClickEvent) {
        if (config.arrowAlignSolver == 0 || !isFloor(7) || mc.objectMouseOver == null) return
        if (mc.objectMouseOver?.entityHit is EntityItemFrame) {
            if (mc.thePlayer.isSneaking && config.arrowAlignSneakOverride) return
            val frame = mc.objectMouseOver.entityHit as EntityItemFrame
            val x = 79 - frame.hangingPosition.z
            val y = 124 - frame.hangingPosition.y
            if (x in 0..4 && y in 0..4) {
                val clicks = neededRotations[Pair(x, y)] ?: return
                if (clicks == 0) {
                    event.isCanceled = true
                    return
                }
                neededRotations[Pair(x, y)] = clicks - 1
                if (config.arrowAlignSolver > 1 && clicks > 1) {
                    rightClick()
                }
            }
        }
    }

    private fun calculate() {
        val frames = mc.theWorld.getEntities(EntityItemFrame::class.java) {
            it != null && area.contains(it.position) && it.displayedItem != null
        }
        if (frames.isNotEmpty()) {
            val solutions = HashMap<Pair<Int, Int>, Int>()
            val maze = Array(5) { IntArray(5) }
            val queue = LinkedList<Pair<Int, Int>>()
            val visited = Array(5) { BooleanArray(5) }
            neededRotations.clear()
            area.withIndex().forEach { (i, pos) ->
                val x = i % 5
                val y = i / 5
                val frame = frames.find { it.position == pos } ?: return@forEach
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
                    1 -> neededRotations[Pair(x, y)] = frame.rotation
                    3 -> queue.add(Pair(x, y))
                }
            }
            while (queue.size != 0) {
                val s = queue.poll()
                val directions = arrayOf(intArrayOf(1, 0), intArrayOf(0, 1), intArrayOf(-1, 0), intArrayOf(0, -1))
                for (i in 3 downTo 0) {
                    val x = (s.first + directions[i][0])
                    val y = (s.second + directions[i][1])
                    if (x in 0..4 && y in 0..4) {
                        val rotations = i * 2 + 1
                        if (solutions[Pair(x, y)] == null && maze[x][y] in 1..2) {
                            queue.add(Pair(x, y))
                            solutions[s] = rotations
                            if (!visited[s.first][s.second]) {
                                var neededRotation = neededRotations[s] ?: continue
                                neededRotation = rotations - neededRotation
                                if (neededRotation < 0) neededRotation += 8
                                neededRotations[s] = neededRotation
                                visited[s.first][s.second] = true
                            }
                        }
                    }
                }
            }
        }
    }
}
