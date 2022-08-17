package skyblockclient.features.macros

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.keyBinds
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.SkyblockClient.Companion.scope
import skyblockclient.utils.LocationUtils.inSkyblock
import skyblockclient.utils.Utils

object BoneMacro {
    @SubscribeEvent
    fun onKey(event: InputEvent.KeyInputEvent?) {
        if (!inSkyblock || !keyBinds[1].isPressed) return
        scope.launch {
            for (i in 0..8) {
                val item = mc.thePlayer.inventory.getStackInSlot(i) ?: continue
                if (item.displayName.contains("Bonemerang")) {
                    mc.thePlayer.inventory.currentItem = i
                    Utils.rightClick()
                    delay(config.boneThrowDelay.toLong())
                }
            }
        }
    }
}
