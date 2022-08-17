package skyblockclient.features.macros

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.minecraft.client.gui.GuiButton
import net.minecraft.init.Items
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.SkyblockClient.Companion.scope
import skyblockclient.utils.GuiMacroUtils
import skyblockclient.utils.LocationUtils.inSkyblock

object AutoBookCombine : GuiMacro("Book Combining") {
    init {
        resetJob()
    }

    override lateinit var job: Job
    override val button = GuiButton(69, 0, 0, 100, 20, "Auto Combine")
    override val inGui
        get() = inSkyblock && config.bookCombine && GuiMacroUtils.currentChestName == "Anvil"
    private val bookPairs = HashMap<String, MutableList<Int>>()

    private fun getPairs() {
        bookPairs.clear()
        mc.thePlayer.openContainer.inventorySlots.drop(54).forEach {
            val attributes = it.stack?.getSubCompound("ExtraAttributes", false) ?: return@forEach
            if (it.stack.item == Items.enchanted_book && attributes.hasKey("enchantments")) {
                val tag = attributes.getCompoundTag("enchantments")
                if (tag.keySet.size == 1) {
                    val enchant = tag.keySet.first() + tag.getInteger(tag.keySet.first()).toString()
                    bookPairs.getOrPut(enchant) { mutableListOf() }.add(it.slotNumber)
                }
            }
        }
    }

    override fun resetJob() {
        job = scope.launch(start = CoroutineStart.LAZY) {
            getPairs()
            while (bookPairs.isNotEmpty() && bookPairs.values.maxOf { it.size } > 1) {
                bookPairs.forEach { (enchant, books) ->
                    if (books.size > 1 && !enchant.endsWith("5") && !enchant.endsWith("10")) {
                        try {
                            GuiMacroUtils.clickSlot(books[0], true, 200)
                            GuiMacroUtils.clickSlot(books[1], true, 200)
                            GuiMacroUtils.clickSlot(22, false, 500)
                            GuiMacroUtils.clickSlot(22, false, 500)
                        } catch (e: IndexOutOfBoundsException) {
                            GuiMacroUtils.macroMessage("§aStopped Book Combining!")
                            return@launch
                        }
                        books.drop(2)
                    }
                }
                getPairs()
            }
            GuiMacroUtils.macroMessage("§aFinished Book Combining!")
        }
    }
}
