package skyblockclient.features.dungeons

import net.minecraft.block.BlockStainedGlass
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity
import net.minecraft.item.EnumDyeColor
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.RenderLivingEntityEvent
import skyblockclient.utils.OutlineUtils
import skyblockclient.utils.RenderUtils
import skyblockclient.utils.ScoreboardUtils

class LividESP {
    @SubscribeEvent
    fun onRenderEntity(event: RenderLivingEntityEvent) {
        if (!inDungeons || !config.lividFinder || !foundLivid || config.espType != 0) return
        if (event.entity == livid) {
            OutlineUtils.outlineESP(event, config.espColorLivid)
        }
    }

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        if (!inDungeons || !config.lividFinder || !foundLivid || config.espType == 0) return
        RenderUtils.drawEntityBox(livid, config.espColorStarMobs, config.espType == 2, event.partialTicks)
    }

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!inSkyblock || !config.lividFinder || inBoss) return
        val message = StringUtils.stripControlCodes(event.message.unformattedText)
        if (message == "[BOSS] Livid: I respect you for making it to here, but I'll be your undoing.") {
            inBoss = true
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!inDungeons || !config.lividFinder || event.phase != TickEvent.Phase.START) return
        if (isF5() && !foundLivid && inBoss) {
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
                                    mc.thePlayer.addChatMessage(ChatComponentText("Error encountered during Livid Check with color:" + color.name))
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

    private fun isF5(): Boolean {
        for (s in ScoreboardUtils.sidebarLines) {
            val line = ScoreboardUtils.cleanSB(s)
            if (line.contains("The Catacombs (")) {
                val dungeonFloor = line.substringAfter("(").substringBefore(")")
                return dungeonFloor == "M5" || dungeonFloor == "F5"
            }
        }
        return config.forceSkyblock
    }

    private fun closestLivid(chatFormatting: Char): Entity? {
        var dist = 100.0
        var entity: Entity? = null
        for (livid in mc.theWorld.loadedEntityList.filter {
            it is EntityOtherPlayerMP && it.name.equals(lividNames[chatFormatting])
        }) {
            if (lividTag!!.getDistanceSqToEntity(livid) < dist) {
                dist = lividTag!!.getDistanceSqToEntity(livid)
                entity = livid
            }
        }
        return entity
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