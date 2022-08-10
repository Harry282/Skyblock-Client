package skyblockclient.features

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.LocationUtils.inSkyblock
import skyblockclient.utils.ScoreboardUtils

object HideServerID {
    private val dateRegex = Regex("§7(\\d{2}/\\d{2}/\\d{2}) §8.+")

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!config.hideServerID || !inSkyblock || event.phase != TickEvent.Phase.START) return
        mc.theWorld.scoreboard.teams.find { team ->
            team.teamName == "team_${ScoreboardUtils.sidebarLines.indexOfFirst { dateRegex.matches(it) } + 1}"
        }?.run {
            setNamePrefix(colorPrefix.substringBefore(" §8"))
            setNameSuffix("")
        }
    }
}
