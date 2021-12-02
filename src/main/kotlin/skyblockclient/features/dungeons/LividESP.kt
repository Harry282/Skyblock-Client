package skyblockclient.features.dungeons

import net.minecraft.block.BlockStainedGlass
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity
import net.minecraft.item.EnumDyeColor
import net.minecraft.util.BlockPos
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.RenderLivingEntityEvent
import skyblockclient.utils.OutlineUtils.outlineESP
import skyblockclient.utils.RenderUtils.drawEntityBox
import skyblockclient.utils.Utils.isFloor
import skyblockclient.utils.Utils.modMessage

class LividESP {
    @SubscribeEvent
    fun onRenderEntity(event: RenderLivingEntityEvent) {
        if (!config.lividFinder || !isFloor(5) || !foundLivid || config.espType != 0) return
        if (event.entity == livid) outlineESP(event, config.espColorLivid)
    }

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        if (!config.lividFinder || !isFloor(5) || !foundLivid || config.espType == 0) return
        drawEntityBox(livid, config.espColorStarMobs, config.espBoxOutlineOpacity == 0F, event.partialTicks)
    }

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!config.lividFinder || !isFloor(5) || inBoss) return
        val message = stripControlCodes(event.message.unformattedText)
        if (message == "[BOSS] Livid: I respect you for making it to here, but I'll be your undoing.") {
            inBoss = true
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || !config.lividFinder || !isFloor(5)) return
        if (!foundLivid && inBoss) {
            val loadedLivids = mc.theWorld.loadedEntityList.filter {
                it.name.contains("Livid") && it.name.length > 5 && it.name[1] == it.name[5]
            }
            if (loadedLivids.size > 8) {
                lividTag = loadedLivids[0]
                livid = closestLivid(lividTag!!.name[5])
                if (livid != null) foundLivid = true
                if (thread == null || !thread!!.isAlive) {
                    thread = Thread({
                        Thread.sleep(1500)
                        val state = mc.theWorld.getBlockState(BlockPos(205, 109, 242))
                        val color = state.getValue(BlockStainedGlass.COLOR)
                        livid = closestLivid(
                            when (color) {
                                EnumDyeColor.GREEN -> '2'
                                EnumDyeColor.PURPLE -> '5'
                                EnumDyeColor.GRAY -> '7'
                                EnumDyeColor.BLUE -> '9'
                                EnumDyeColor.LIME -> 'a'
                                EnumDyeColor.MAGENTA -> 'd'
                                EnumDyeColor.YELLOW -> 'e'
                                EnumDyeColor.RED -> 'c'
                                EnumDyeColor.WHITE -> 'f'
                                else -> {
                                    modMessage("Error encountered during Livid Check with color:" + color.name)
                                    return@Thread
                                }
                            }
                        ) ?: return@Thread
                    }, "Livid Check")
                    thread!!.start()
                }
            }
        }
    }

    @SubscribeEvent
    fun onWorldChange(event: WorldEvent.Load?) {
        foundLivid = false
        livid = null
        inBoss = false
    }

    private fun closestLivid(chatFormatting: Char): Entity? {
        return mc.theWorld.loadedEntityList.filterIsInstance<EntityOtherPlayerMP>()
            .filter { it.name.equals(lividNames[chatFormatting]) }
            .sortedWith(Comparator.comparingDouble { lividTag!!.getDistanceSqToEntity(it) }).firstOrNull()
    }

    companion object {
        private val lividNames = mapOf(
            '2' to "Frog Livid",
            '5' to "Purple Livid",
            '7' to "Doctor Livid",
            '9' to "Scream Livid",
            'a' to "Smile Livid",
            'c' to "Hockey Livid",
            'd' to "Crossed Livid",
            'e' to "Arcade Livid",
            'f' to "Vendetta Livid"
        )
        private var foundLivid = false
        private var livid: Entity? = null
        private var lividTag: Entity? = null
        private var inBoss = false
        private var thread: Thread? = null
    }
}