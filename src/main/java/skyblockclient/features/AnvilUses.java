package skyblockclient.features;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.utils.SkyblockCheck;

public class AnvilUses {

    @SubscribeEvent
    public void tooltip(ItemTooltipEvent event) {
        if (!SkyblockCheck.inSkyblock || !SkyblockClient.config.showAnvilUses) return;
        if (event.itemStack.hasTagCompound() && event.itemStack.getTagCompound().hasKey("ExtraAttributes", 10)) {
            NBTTagCompound NBTData = event.itemStack.getTagCompound().getCompoundTag("ExtraAttributes");
            if (SkyblockClient.config.showAnvilUses && NBTData.hasKey("anvil_uses", 3)) {
                String text = "§7Anvil uses: " + NBTData.getInteger("anvil_uses");
                event.toolTip.add((event.showAdvancedItemTooltips) ? event.toolTip.size() - 2 : event.toolTip.size(), text);
            }
        }
    }

}
