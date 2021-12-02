package skyblockclient.features.dungeons

import net.minecraft.inventory.ContainerChest
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inDungeons
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.ClickEvent
import skyblockclient.utils.Utils.itemID
import skyblockclient.utils.Utils.modMessage
import skyblockclient.utils.Utils.rightClick

class FastLeap {

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!config.fastLeap || !inDungeons || event.type.toInt() == 2) return
        val message = stripControlCodes(event.message.unformattedText)
        if (message.endsWith(" opened a WITHER door!")) {
            val opener = message.split(" ")[0]
            if (opener != mc.thePlayer.name) {
                lastOpener = opener
            }
        } else if (message == "The BLOOD DOOR has been opened!") {
            bloodOpened = true
        }
    }

    @SubscribeEvent
    fun onClick(event: ClickEvent.LeftClickEvent) {
        if (!config.fastLeap || !inDungeons) return
        if (config.fastLeapBloodDisable && bloodOpened) return
        if (mc.thePlayer.heldItem?.itemID == "SPIRIT_LEAP" && lastOpener != "") {
            event.isCanceled = true
            rightClick()
            if (thread == null || !thread!!.isAlive) {
                thread = Thread({
                    for (i in 0..100) if (mc.thePlayer.openContainer !is ContainerChest) {
                        Thread.sleep(20)
                    } else {
                        val invSlots = mc.thePlayer.openContainer.inventorySlots
                        if (config.fastLeapTarget != "") {
                            for (slot in 10..18) if (invSlots[slot].stack?.displayName?.contains(config.fastLeapTarget) == true) {
                                mc.playerController.windowClick(
                                    mc.thePlayer.openContainer.windowId,
                                    slot,
                                    2,
                                    0,
                                    mc.thePlayer
                                )
                                return@Thread
                            }
                        }
                        for (slot in 10..18) if (invSlots[slot].stack?.displayName?.contains(lastOpener) == true) {
                            mc.playerController.windowClick(
                                mc.thePlayer.openContainer.windowId,
                                slot,
                                2,
                                0,
                                mc.thePlayer
                            )
                            return@Thread
                        }
                    }
                    modMessage("§aFast Spirit Leap failed for player $lastOpener")
                }, "Auto Spirit Leap")
                thread!!.start()
            }
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load?) {
        lastOpener = ""
        bloodOpened = false
    }

    companion object {
        private var lastOpener = ""
        private var bloodOpened = false
        private var thread: Thread? = null
    }
}