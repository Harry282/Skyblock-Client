package skyblockclient.features;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.events.GuiContainerEvent;
import skyblockclient.utils.SkyblockCheck;

public class ItemOverlay {

    @SubscribeEvent
    public void drawSlot(GuiContainerEvent.DrawSlotEvent.Pre event) {
        if (!SkyblockCheck.inSkyblock || event.slot.getStack() == null) return;
        if (SkyblockClient.config.overlaySalvageable) {
            if (event.gui instanceof GuiChest) {
                ItemStack item = event.slot.getStack();
                NBTTagCompound attributes = item.getSubCompound("ExtraAttributes", false);
                if (attributes != null && attributes.hasKey("baseStatBoostPercentage") && !attributes.hasKey("dungeon_item_level")) {
                    int x = event.slot.xDisplayPosition;
                    int y = event.slot.yDisplayPosition;
                    if (attributes.getInteger("baseStatBoostPercentage") == 50) {
                        Gui.drawRect(x, y, x + 16, y + 16, SkyblockClient.config.overlayColorTopSalvageable.getRGB());
                    } else {
                        Gui.drawRect(x, y, x + 16, y + 16, SkyblockClient.config.overlayColorSalvageable.getRGB());
                    }
                }
            }
        }
    }

}
