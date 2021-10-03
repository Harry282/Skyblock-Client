package skyblockclient.features.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.events.RenderLivingEntityEvent;
import skyblockclient.utils.OutlineUtils;
import skyblockclient.utils.ScoreboardUtils;
import skyblockclient.utils.SkyblockCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LividESP {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Map<Character, String> lividNames = Stream.of(new Object[][]{
            {'f', "Vendetta Livid"},
            {'d', "Crossed Livid"},
            {'c', "Hockey Livid"},
            {'7', "Doctor Livid"},
            {'2', "Frog Livid"},
            {'a', "Smile Livid"},
            {'9', "Scream Livid"},
            {'5', "Purple Livid"},
            {'e', "Arcade Livid"},
    }).collect(Collectors.toMap(data -> (Character) data[0], data -> (String) data[1]));
    private static boolean foundLivid = false;
    private static Entity livid = null;

    @SubscribeEvent
    public void renderLiving(RenderLivingEntityEvent event) {
        if (!SkyblockClient.config.lividFinder || !SkyblockCheck.inDungeons || !foundLivid) return;
        if (event.entity instanceof EntityPlayer) {
            char c = livid.getName().charAt(5);
            if (lividNames.containsKey(c)) {
                if (event.entity.getName().equals(lividNames.get(c))) {
                    OutlineUtils.outlineESP(event, SkyblockClient.config.espColorLivid);
                }
            }
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (SkyblockCheck.inDungeons || !SkyblockClient.config.lividFinder || event.phase != TickEvent.Phase.START)
            return;
        if (isF5() && !foundLivid) {
            List<Entity> livids = new ArrayList<>();
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                String name = entity.getName();
                if (name.contains("Livid") && name.length() > 5 && name.charAt(1) == name.charAt(5) && !livids.contains(entity)) {
                    livids.add(entity);
                }
            }
            if (livids.size() > 8) {
                foundLivid = true;
                livid = livids.get(0);
                System.out.println("Livid is: " + livid.getName());
            }
        }
    }

    @SubscribeEvent
    public void worldChange(WorldEvent.Load event) {
        livid = null;
        foundLivid = false;
    }

    private boolean isF5() {
        for (String s : ScoreboardUtils.getSidebarLines()) {
            String line = ScoreboardUtils.cleanSB(s);
            if (line.contains("The Catacombs (")) {
                String dungeonFloor = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                if (dungeonFloor.equals("M5") || dungeonFloor.equals("F5")) {
                    return true;
                }
            }
        }
        return false;
    }

}
