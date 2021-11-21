package skyblockclient.features

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.keyBinds
import skyblockclient.SkyblockClient.Companion.mc

class BoneMacro {
    @SubscribeEvent
    fun onKey(event: KeyInputEvent?) {
        if (!inSkyblock || !keyBinds[1].isPressed) return
        Thread {
            for (i in 0..7) {
                val item = mc.thePlayer.inventory.getStackInSlot(i) ?: continue
                if (item.displayName.contains("Bonemerang")) {
                    mc.thePlayer.inventory.currentItem = i
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem())
                    Thread.sleep(config.boneThrowDelay.toLong())
                }
            }
        }.start()
    }
}