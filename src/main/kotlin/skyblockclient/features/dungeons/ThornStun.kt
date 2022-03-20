package skyblockclient.features.dungeons

import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.Utils.equalsOneOf
import skyblockclient.utils.Utils.isFloor
import skyblockclient.utils.Utils.itemID

object ThornStun {

    private var isClicking = false

    @SubscribeEvent
    fun onMouse(event: MouseEvent) {
        if (!config.afkThornStun || !isFloor(4) || event.button != 1) return
        event.isCanceled = isClicking
        if (mc.thePlayer.heldItem?.itemID?.equalsOneOf("TRIBAL_SPEAR", "BONE_BOOMERANG") == true || isClicking) {
            if (event.buttonstate) {
                isClicking = !isClicking
            }
        }
    }
}
