package skyblockclient.events;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GuiContainerEvent extends Event {

    public Container container;
    public GuiContainer gui;

    public GuiContainerEvent(Container container, GuiContainer gui) {
        this.container = container;
        this.gui = gui;
    }

    public static class DrawSlotEvent extends GuiContainerEvent {

        public Slot slot;

        public DrawSlotEvent(Container container, GuiContainer gui, Slot slot) {
            super(container, gui);
            this.slot = slot;
        }

        @Cancelable
        public static class Pre extends DrawSlotEvent {
            public Pre(Container container, GuiContainer gui, Slot slot) {
                super(container, gui, slot);
            }
        }
    }

    @Cancelable
    public static class SlotClickEvent extends GuiContainerEvent {

        public Slot slot;
        public int slotId, clickedButton, clickType;

        public SlotClickEvent(Container container, GuiContainer gui, Slot slot, int slotId, int clickedButton, int clickType) {
            super(container, gui);
            this.slot = slot;
            this.slotId = slotId;
            this.clickedButton = clickedButton;
            this.clickType = clickType;
        }
    }

}
