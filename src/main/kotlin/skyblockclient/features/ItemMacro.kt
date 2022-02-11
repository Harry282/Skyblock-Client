package skyblockclient.features

import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.guis.ItemMacros
import skyblockclient.utils.Utils.itemID
import skyblockclient.utils.Utils.leftClick
import skyblockclient.utils.Utils.rightClick

class ItemMacro {

    @SubscribeEvent
    fun onInput(event: InputEvent) {
        if (!inSkyblock || macros.isEmpty()) return
        val pressedKey = when {
            event is InputEvent.KeyInputEvent && Keyboard.getEventKeyState() -> Keyboard.getEventKey()
            event is InputEvent.MouseInputEvent && Mouse.getEventButtonState() -> Mouse.getEventButton() - 100
            else -> return
        }
        macros.forEach { macro ->
            if (macro.keycode == pressedKey && (macro.onlyWhileHolding.toString() == "[]" || macro.onlyWhileHolding.any {
                    mc.thePlayer.currentEquippedItem?.run { displayName.contains(it) || itemID == it } == true
                })) {
                macroItem(macro.item, macro.mouseButton == 1)
            }
        }
    }

    private fun macroItem(name: String, leftMouse: Boolean) {
        for (i in 0..8) {
            val previous = mc.thePlayer.inventory.currentItem
            if (i == previous) continue
            if (mc.thePlayer.inventory.getStackInSlot(i)
                    ?.run { displayName.contains(name) || itemID == name } == true
            ) {
                mc.thePlayer.inventory.currentItem = i
                if (leftMouse) {
                    if (mc.objectMouseOver?.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
                        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(i))
                        leftClick()
                        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(previous))
                        mc.thePlayer.inventory.currentItem = previous
                        return
                    }
                    leftClick()
                } else rightClick()
                mc.thePlayer.inventory.currentItem = previous
                return
            }
        }
    }


    companion object {
        // Updated in gui ItemMacros.kt
        val macros = HashSet<ItemMacros.Macro>()
    }
}
