package skyblockclient.features.dungeons

import net.minecraft.inventory.ContainerChest
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.ClickEvent
import skyblockclient.utils.LocationUtils.inDungeons
import skyblockclient.utils.Utils.equalsOneOf
import skyblockclient.utils.Utils.itemID
import skyblockclient.utils.Utils.modMessage
import skyblockclient.utils.Utils.rightClick

object FastLeap {

    private var lastOpener = ""
    private var bloodOpened = false
    private var thread: Thread? = null

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!config.fastLeap || !inDungeons || event.type.toInt() == 2) return
        val message = stripControlCodes(event.message.unformattedText)
        if (message.endsWith(" opened a WITHER door!")) {
            val opener = message.substringBefore(" ")
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
        if (mc.thePlayer.heldItem?.itemID.equalsOneOf("SPIRIT_LEAP", "INFINITE_SPIRIT_LEAP") && lastOpener != "") {
            event.isCanceled = true
            rightClick()
            if (thread == null || !thread!!.isAlive) {
                thread = Thread({
                    for (i in 0..100) {
                        if (mc.thePlayer.openContainer is ContainerChest) {
                            val invSlots = mc.thePlayer.openContainer.inventorySlots
                            val name = if (config.fastLeapTarget != "") config.fastLeapTarget else lastOpener
                            invSlots.subList(10, 19).find {
                                it.stack?.displayName?.contains(name) == true
                            }?.let {
                                mc.playerController.windowClick(
                                    mc.thePlayer.openContainer.windowId,
                                    it.slotNumber,
                                    2,
                                    0,
                                    mc.thePlayer
                                )
                                return@Thread
                            }
                        }
                        Thread.sleep(20)
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
}
