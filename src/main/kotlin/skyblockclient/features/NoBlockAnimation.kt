package skyblockclient.features

import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.Utils.itemID
import skyblockclient.utils.Utils.lore

object NoBlockAnimation {

    private var isRightClickKeyDown = false
    val blacklist = HashSet<String>()

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || !config.noBlockAnimation || !inSkyblock) return
        isRightClickKeyDown = mc.gameSettings.keyBindUseItem.isKeyDown
    }

    @SubscribeEvent
    fun onInteract(event: PlayerInteractEvent) {
        if (!config.noBlockAnimation || !inSkyblock) return
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            val item = mc.thePlayer.heldItem ?: return
            if (item.item !is ItemSword || blacklist.any { item.itemID == it || item.displayName.contains(it) }) return
            if (mc.thePlayer.heldItem.lore.any { it.contains("§6Ability: ") && it.endsWith("§e§lRIGHT CLICK") }) {
                event.isCanceled = true
                if (!isRightClickKeyDown) {
                    mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.heldItem))
                }
            }
        }
    }
}
