package skyblockclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class TextRenderer extends Gui {

    public TextRenderer(Minecraft mc, String text, int x, int y, double scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        y -= mc.fontRendererObj.FONT_HEIGHT;
        for (String line : text.split("\n")) {
            y += mc.fontRendererObj.FONT_HEIGHT * scale;
            mc.fontRendererObj.drawString(line, (int) Math.round(x / scale), (int) Math.round(y / scale), 0xFFFFFF, true);
        }
        GlStateManager.popMatrix();
    }

}
