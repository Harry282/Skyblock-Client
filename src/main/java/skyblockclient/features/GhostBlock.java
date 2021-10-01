package skyblockclient.features;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.utils.SkyblockCheck;

import java.util.ArrayList;
import java.util.Arrays;

public class GhostBlock {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final ArrayList<Block> interactables = new ArrayList<>(Arrays.asList(
            Blocks.acacia_door,
            Blocks.anvil,
            Blocks.beacon,
            Blocks.bed,
            Blocks.birch_door,
            Blocks.brewing_stand,
            Blocks.command_block,
            Blocks.crafting_table,
            Blocks.chest,
            Blocks.dark_oak_door,
            Blocks.daylight_detector,
            Blocks.daylight_detector_inverted,
            Blocks.dispenser,
            Blocks.dropper,
            Blocks.enchanting_table,
            Blocks.ender_chest,
            Blocks.furnace,
            Blocks.hopper,
            Blocks.jungle_door,
            Blocks.lever,
            Blocks.noteblock,
            Blocks.powered_comparator,
            Blocks.unpowered_comparator,
            Blocks.powered_repeater,
            Blocks.unpowered_repeater,
            Blocks.standing_sign,
            Blocks.wall_sign,
            Blocks.trapdoor,
            Blocks.trapped_chest,
            Blocks.wooden_button,
            Blocks.stone_button,
            Blocks.oak_door,
            Blocks.skull
    ));

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (!SkyblockCheck.inSkyblock || event.phase != TickEvent.Phase.START) return;
        if (SkyblockClient.keyBinds[2].isKeyDown()) {
            toAir(mc.objectMouseOver.getBlockPos());
        }
    }

    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent event) {
        if (!SkyblockCheck.inSkyblock || !SkyblockClient.config.stonkGhostBlock) return;
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = mc.thePlayer.getHeldItem();
            if (item == null) return;
            if (item.getDisplayName().contains("Stonk")) {
                toAir(mc.objectMouseOver.getBlockPos());
                event.setCanceled(true);
            }
        }
    }

    private void toAir(BlockPos blockPos) {
        if (blockPos == null) return;
        Block block = mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
        if (!interactables.contains(block)) {
            mc.theWorld.setBlockToAir(mc.objectMouseOver.getBlockPos());
        }
    }

}
