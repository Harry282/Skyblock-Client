package skyblockclient.features

import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock

class AnvilUses {
    @SubscribeEvent
    fun onTooltip(event: ItemTooltipEvent) {
        if (!inSkyblock || !config.showAnvilUses || !event.itemStack.hasTagCompound() ||
            !event.itemStack.tagCompound.hasKey("ExtraAttributes", 10)
        ) return
        val nbtTag = event.itemStack.tagCompound.getCompoundTag("ExtraAttributes")
        if (config.showAnvilUses && nbtTag.hasKey("anvil_uses", 3)) {
            val text = "§7Anvil uses: " + nbtTag.getInteger("anvil_uses")
            event.toolTip.add(if (event.showAdvancedItemTooltips) event.toolTip.size - 2 else event.toolTip.size, text)
        }
    }
}