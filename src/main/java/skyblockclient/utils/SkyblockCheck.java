package skyblockclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;

public class SkyblockCheck {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean inSkyblock = false;
    public static boolean inDungeons = false;

    public static boolean isOnHypixel() {
        try {
            if (mc.theWorld != null && !mc.isSingleplayer()) {
                if (mc.thePlayer != null && mc.thePlayer.getClientBrand() != null) {
                    return mc.thePlayer.getClientBrand().toLowerCase().contains("hypixel");
                }
                if (mc.getCurrentServerData() != null) {
                    return mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isInSkyblock() {
        if (isOnHypixel()) {
            ScoreObjective scoreboardObj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreboardObj != null) {
                return ScoreboardUtils.cleanSB(scoreboardObj.getDisplayName()).contains("SKYBLOCK");
            }
        }
        return false;
    }

    public static boolean isInDungeon() {
        if (inSkyblock) {
            return ScoreboardUtils.getSidebarLines().stream().anyMatch(s -> ScoreboardUtils.cleanSB(s).contains("The Catacombs") &&
                    !ScoreboardUtils.cleanSB(s).contains("Queue") || ScoreboardUtils.cleanSB(s).contains("Dungeon Cleared:"));
        }
        return false;
    }

}
