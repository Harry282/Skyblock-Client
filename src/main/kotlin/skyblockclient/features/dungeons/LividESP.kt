package skyblockclient.features.dungeons

import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
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
    fun onTick(event: ClientTickEvent) {
        if (!inDungeons || !config.lividFinder || event.phase != TickEvent.Phase.START) return
        if (isF5 && !foundLivid) {
            val loadedLivids = mc.theWorld.loadedEntityList.filter {
                it.name.contains("Livid") && it.name.length > 5 && it.name[1] == it.name[5]
            }
            if (loadedLivids.size > 8) {
                lividTag = loadedLivids[0]
                livid = closestLivid
                if (livid != null) foundLivid = true
            }
        }
    }

    @SubscribeEvent
    fun onWorldChange(event: WorldEvent.Load?) {
        livid = null
        foundLivid = false
    }

    private val isF5: Boolean
        get() {
            for (s in ScoreboardUtils.sidebarLines) {
                val line = ScoreboardUtils.cleanSB(s)
                if (line.contains("The Catacombs (")) {
                    val dungeonFloor = line.substringAfter("(").substringBefore(")")
                    return dungeonFloor == "M5" || dungeonFloor == "F5"
                }
            }
            return config.forceSkyblock
        }

    private val closestLivid: Entity?
        get() {
            var dist = 100.0
            var temp: Entity? = null
            for (livid in mc.theWorld.loadedEntityList.filter {
                it is EntityOtherPlayerMP && it.name.equals(lividNames[lividTag!!.name[5]])
            }) {
                if (lividTag!!.getDistanceSqToEntity(livid) < dist) {
                    dist = lividTag!!.getDistanceSqToEntity(livid)
                    temp = livid
                }
            }
            return temp
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
    }
}