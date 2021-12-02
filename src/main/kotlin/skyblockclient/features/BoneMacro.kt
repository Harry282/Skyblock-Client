package skyblockclient.features

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.keyBinds
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.Utils.rightClick

class BoneMacro {
    @SubscribeEvent
    fun onKey(event: KeyInputEvent?) {
        if (!inSkyblock || !keyBinds[1].isPressed) return
        Thread {
            for (i in 0..7) {
                val item = mc.thePlayer.inventory.getStackInSlot(i) ?: continue
                if (item.displayName.contains("Bonemerang")) {
                    mc.thePlayer.inventory.currentItem = i
                    rightClick()
                    Thread.sleep(config.boneThrowDelay.toLong())
                }
            }
        }.start()
    }
}