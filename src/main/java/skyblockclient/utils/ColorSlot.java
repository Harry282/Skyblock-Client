package skyblockclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;

public class ColorSlot {

    public ColorSlot(Slot slot, int size, int color) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int x = (sr.getScaledWidth() - 176) / 2 + slot.xDisplayPosition;
        int y = (sr.getScaledHeight() - 222) / 2 + slot.yDisplayPosition;
        if (size != 90) y += (6 - (size - 36) / 9) * 9;
        GL11.glTranslated(0, 0, 1);
        Gui.drawRect(x, y, x + 16, y + 16, color);
        GL11.glTranslated(0, 0, -1);
    }

}
