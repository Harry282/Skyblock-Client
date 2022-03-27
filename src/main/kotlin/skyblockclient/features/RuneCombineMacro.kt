package skyblockclient.features

import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.Utils.modMessage

object RuneCombineMacro {

    private var thread: Thread? = null
    private val button = GuiButton(
        69,
        0,
        0,
        100,
        20,
        "Auto Combine",
    )

    @SubscribeEvent
    fun onGuiBackground(event: GuiScreenEvent.BackgroundDrawnEvent) {
        if (isRunePedestal(event.gui)) {
            button.xPosition = ScaledResolution(mc).scaledWidth / 2 + 100
            button.yPosition = ScaledResolution(mc).scaledHeight / 2 - 50
            button.drawButton(mc, event.mouseX, event.mouseY)
        }
    }

    @SubscribeEvent
    fun onMouseInput(event: GuiScreenEvent.MouseInputEvent.Pre) {
        if (config.runeCombine && button.isMouseOver && Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
            if (isRunePedestal(event.gui)) {
                button.playPressSound(mc.soundHandler)
                combine()
            }
        }
    }

    private fun combine() {
        if (thread?.isAlive == true) {
            thread!!.interrupt()
            modMessage("§aStopped Rune Combining!")
        } else {
            thread = Thread({
                modMessage("§aStarting Rune Combining...")
                val invSlots = mc.thePlayer.openContainer.inventorySlots
                while (true) {
                    val runeSlot = invSlots.firstOrNull {
                        it.inventory == mc.thePlayer.inventory && it.hasStack && it.stack.run {
                            stackSize > 1 && StringUtils.stripControlCodes(displayName).run {
                                contains("Rune") && !contains("3")
                            }
                        }
                    } ?: break
                    click(runeSlot.slotNumber, true, 200)
                    click(runeSlot.slotNumber, true, 200)
                    click(13, false, 200)
                    while (invSlots[19].hasStack) {
                        Thread.sleep(200)
                    }
                    click(31, false, 500)
                }
                modMessage("§aFinished Rune Combining!")
            }, "Book Combine")
            thread!!.start()
        }
    }

    private fun click(slot: Int, shift: Boolean, delay: Long) {
        mc.playerController.windowClick(
            mc.thePlayer.openContainer.windowId, slot, 0, if (shift) 1 else 0, mc.thePlayer
        )
        Thread.sleep(delay)
    }

    private fun isRunePedestal(guiScreen: GuiScreen): Boolean {
        if (inSkyblock && config.runeCombine && guiScreen is GuiChest) {
            val container = guiScreen.inventorySlots
            if (container is ContainerChest) {
                return container.lowerChestInventory.displayName.unformattedText == "Runic Pedestal"
            }
        }
        return false
    }
}
