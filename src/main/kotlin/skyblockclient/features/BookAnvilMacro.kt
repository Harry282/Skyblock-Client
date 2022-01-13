package skyblockclient.features

import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.init.Items
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.Utils.modMessage

class BookAnvilMacro {

    private val bookPairs = HashMap<String, List<Int>>()
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
        if (isAnvil(event.gui)) {
            button.xPosition = ScaledResolution(mc).scaledWidth / 2 + 100
            button.yPosition = ScaledResolution(mc).scaledHeight / 2 - 50
            button.drawButton(mc, event.mouseX, event.mouseY)
        }
    }

    @SubscribeEvent
    fun onMouseInput(event: GuiScreenEvent.MouseInputEvent.Pre) {
        if (config.bookCombine && button.isMouseOver && Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
            if (isAnvil(event.gui)) {
                button.playPressSound(mc.soundHandler)
                combine()
            }
        }
    }

    private fun combine() {
        if (thread?.isAlive == true) {
            thread!!.interrupt()
            if (config.bookCombineMessage) {
                modMessage("§aStopped Book Combining!")
            }
        } else {
            thread = Thread({
                if (config.bookCombineMessage) {
                    modMessage("§aStarting Book Combining...")
                }
                getPairs()
                while (bookPairs.isNotEmpty() && bookPairs.values.maxOf { it.size } > 1) {
                    bookPairs.forEach { (enchant, books) ->
                        if (books.size > 1 && !enchant.endsWith("5") && !enchant.endsWith("10")) {
                            click(books[0], true, 0)
                            while (mc.thePlayer.openContainer.inventory[33] == null) Thread.sleep(50)
                            click(books[1], true, 0)
                            while (mc.thePlayer.openContainer.inventory[29] == null) Thread.sleep(50)
                            click(22, false, 250)
                            while (mc.thePlayer.openContainer.inventory[33] != null) Thread.sleep(50)
                            click(22, false, 200)
                            books.drop(2)
                        }
                    }
                    getPairs()
                }
                if (config.bookCombineMessage) {
                    modMessage("§aFinished Book Combining!")
                }
            }, "Book Combine")
            thread!!.start()
        }
    }

    private fun click(slot: Int, shift: Boolean, delay: Long) {
        mc.playerController.windowClick(
            mc.thePlayer.openContainer.windowId, slot, 0, if (shift) 1 else 0, mc.thePlayer
        )
        Thread.sleep(config.bookCombineSpeed.toLong() + delay)
    }

    private fun getPairs() {
        bookPairs.clear()
        mc.thePlayer.openContainer.inventorySlots.drop(54).forEach {
            val attributes = it.stack?.getSubCompound("ExtraAttributes", false) ?: return@forEach
            if (it.stack.item == Items.enchanted_book && attributes.hasKey("enchantments")) {
                val tag = attributes.getCompoundTag("enchantments")
                if (tag.keySet.size == 1) {
                    val enchant = tag.keySet.first() + tag.getInteger(tag.keySet.first()).toString()
                    bookPairs[enchant] = bookPairs.getOrDefault(enchant, emptyList()).plus(it.slotNumber)
                }
            }
        }
    }

    private fun isAnvil(guiScreen: GuiScreen): Boolean {
        if (inSkyblock && config.bookCombine && guiScreen is GuiChest) {
            val container = guiScreen.inventorySlots
            if (container is ContainerChest) {
                return container.lowerChestInventory.displayName.unformattedText == "Anvil"
            }
        }
        return false
    }
}
