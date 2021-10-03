package skyblockclient.features;

import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.utils.RenderUtils;
import skyblockclient.utils.SkyblockCheck;

import java.awt.*;
import java.util.HashMap;

public class GemstoneESP {

    private final HashMap<BlockPos, Color> gemstoneList = new HashMap<>();
    private long lastUpdate = 0;
    private Thread thread;

    @SubscribeEvent
    public void update(LivingEvent.LivingUpdateEvent event) {
        if (!SkyblockCheck.inSkyblock || !SkyblockClient.config.gemstoneESP) return;
        if (event.entity == Minecraft.getMinecraft().thePlayer && lastUpdate + SkyblockClient.config.gemstoneESPTime < System.currentTimeMillis()) {
            if (thread == null || !thread.isAlive()) {
                thread = new Thread(() -> {
                    HashMap<BlockPos, Color> blockList = new HashMap<>();
                    BlockPos player = Minecraft.getMinecraft().thePlayer.getPosition();
                    int radius = SkyblockClient.config.gemstoneESPRadius;
                    Vec3i vec3i = new Vec3i(radius, radius, radius);
                    for (BlockPos blockPos : BlockPos.getAllInBox(player.add(vec3i), player.subtract(vec3i))) {
                        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                        EnumDyeColor dyeColor = null;
                        if (blockState.getBlock() == Blocks.stained_glass) {
                            dyeColor = blockState.getValue(BlockStainedGlass.COLOR);
                        } else if (blockState.getBlock() == Blocks.stained_glass_pane) {
                            dyeColor = blockState.getValue(BlockStainedGlassPane.COLOR);
                        }
                        if (dyeColor == null) return;
                        Color color = getColor(dyeColor);
                        if (color == null) return;
                        blockList.put(blockPos, color);
                    }
                    synchronized (gemstoneList) {
                        gemstoneList.clear();
                        gemstoneList.putAll(blockList);
                    }
                    lastUpdate = System.currentTimeMillis();
                }, "Gemstone ESP");
                thread.start();
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!SkyblockCheck.inSkyblock || !SkyblockClient.config.gemstoneESP) return;
        synchronized (gemstoneList) {
            gemstoneList.forEach((blockPos, color) ->
                    RenderUtils.drawBlockBox(blockPos, color, true, event.partialTicks));
        }
    }

    private Color getColor(EnumDyeColor dyeColor) {
        switch (dyeColor) {
            case RED:
                // RUBY
                return new Color(188, 3, 29);
            case PURPLE:
                // AMETHYST
                return new Color(137, 0, 201);
            case LIME:
                // JADE
                return new Color(157, 249, 32);
            case LIGHT_BLUE:
                // SAPPHIRE
                return new Color(60, 121, 224);
            case ORANGE:
                // AMBER
                return new Color(237, 139, 35);
            case YELLOW:
                // TOPAZ
                return new Color(249, 215, 36);
            case MAGENTA:
                // JASPER
                return new Color(214, 15, 150);
            default:
                return null;
        }
    }

}
