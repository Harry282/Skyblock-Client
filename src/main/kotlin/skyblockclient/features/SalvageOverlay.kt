package skyblockclient.features

import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.events.GuiContainerEvent.DrawSlotEvent

class SalvageOverlay {
    @SubscribeEvent
    fun onDrawSlot(event: DrawSlotEvent) {
        if (!config.overlaySalvageable || !inSkyblock || event.gui !is GuiChest) return
        val item = event.slot.stack ?: return
        val attributes = item.getSubCompound("ExtraAttributes", false) ?: return
        if (attributes.hasKey("baseStatBoostPercentage") && !attributes.hasKey("dungeon_item_level")) {
            val x = event.slot.xDisplayPosition
            val y = event.slot.yDisplayPosition
            Gui.drawRect(
                x, y, x + 16, y + 16,
                if (attributes.getInteger("baseStatBoostPercentage") == 50) {
                    config.overlayColorTopSalvageable.rgb
                } else config.overlayColorSalvageable.rgb
            )
        }
    }
}