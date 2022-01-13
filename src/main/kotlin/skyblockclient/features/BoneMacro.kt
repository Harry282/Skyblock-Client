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
            (0..8).forEach {
                val item = mc.thePlayer.inventory.getStackInSlot(it) ?: return@forEach
                if (item.displayName.contains("Bonemerang")) {
                    mc.thePlayer.inventory.currentItem = it
                    rightClick()
                    Thread.sleep(config.boneThrowDelay.toLong())
                }
            }
        }.start()
    }
}
