package skyblockclient.features.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.utils.ScoreboardUtils;
import skyblockclient.utils.SkyblockCheck;

import java.util.Arrays;
import java.util.List;

public class F7P3Ghost {

    private static final List<BlockPos> BLOCK_POS_LIST = Arrays.asList(
            new BlockPos(275, 220, 231),
            new BlockPos(275, 220, 232),
            new BlockPos(287, 167, 240),
            new BlockPos(288, 167, 240),
            new BlockPos(289, 167, 240),
            new BlockPos(290, 167, 240),
            new BlockPos(290, 166, 240),
            new BlockPos(290, 167, 239),
            new BlockPos(291, 167, 239),
            new BlockPos(291, 166, 240),
            new BlockPos(291, 167, 240),
            new BlockPos(290, 166, 239),
            new BlockPos(291, 166, 239),
            new BlockPos(292, 166, 239),
            new BlockPos(292, 167, 240),
            new BlockPos(292, 166, 240),
            new BlockPos(292, 167, 239),
            new BlockPos(293, 167, 239),
            new BlockPos(293, 166, 240),
            new BlockPos(293, 167, 240),
            new BlockPos(293, 166, 239),
            new BlockPos(294, 166, 239),
            new BlockPos(294, 167, 240),
            new BlockPos(294, 167, 239),
            new BlockPos(294, 166, 240),
            new BlockPos(298, 168, 247),
            new BlockPos(298, 168, 246),
            new BlockPos(298, 168, 244),
            new BlockPos(298, 168, 243),
            new BlockPos(299, 168, 243),
            new BlockPos(299, 168, 244),
            new BlockPos(299, 168, 246),
            new BlockPos(299, 168, 247),
            new BlockPos(300, 168, 247),
            new BlockPos(300, 168, 246),
            new BlockPos(300, 168, 244),
            new BlockPos(300, 168, 243)
    );

    @SubscribeEvent
    public void chat(ClientChatReceivedEvent event) {
        if (!SkyblockClient.config.f7p3Ghost || !SkyblockCheck.inDungeons || !isF7()) return;
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (message.equals("[BOSS] Necron: You caused me many troubles, your journey ends here adventurers!")) {
            BLOCK_POS_LIST.forEach(blockPos -> {
                System.out.println("Replaced block " + Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock());
                Minecraft.getMinecraft().theWorld.setBlockToAir(blockPos);
            });
        }
    }

    private boolean isF7() {
        for (String s : ScoreboardUtils.getSidebarLines()) {
            String line = ScoreboardUtils.cleanSB(s);
            if (line.contains("The Catacombs (")) {
                String dungeonFloor = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                if (dungeonFloor.equals("F7")) {
                    return true;
                }
            }
        }
        return false;
    }

}
