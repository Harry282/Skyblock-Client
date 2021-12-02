package skyblockclient.utils

import net.minecraft.scoreboard.Score
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.StringUtils.stripControlCodes
import skyblockclient.SkyblockClient.Companion.mc

object ScoreboardUtils {
    fun cleanSB(scoreboard: String?): String {
        return stripControlCodes(scoreboard).toCharArray().filter { it.code in 21..126 }.joinToString("")
    }

    val sidebarLines: List<String>
        get() {
            val scoreboard = mc.theWorld?.scoreboard ?: return emptyList()
            val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return emptyList()
            var scores = scoreboard.getSortedScores(objective)
            val list = scores.filter { input: Score? ->
                input != null && input.playerName != null && !input.playerName.startsWith("#")
            }
            scores = if (list.size > 15) list.drop(15) else list
            return scores.map {
                ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(it.playerName), it.playerName)
            }
        }
}