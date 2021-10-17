package skyblockclient.events

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

open class GuiContainerEvent(val container: Container, val gui: GuiContainer) : Event() {
    open class DrawSlotEvent(container: Container, gui: GuiContainer, var slot: Slot) :
        GuiContainerEvent(container, gui) {
        @Cancelable
        class Pre(container: Container, gui: GuiContainer, slot: Slot) :
            DrawSlotEvent(container, gui, slot)
    }

    @Cancelable
    class SlotClickEvent(
        container: Container,
        gui: GuiContainer,
        var slot: Slot?,
        var slotId: Int,
        var clickedButton: Int,
        var clickType: Int
    ) : GuiContainerEvent(container, gui)
}