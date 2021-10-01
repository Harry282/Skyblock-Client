package skyblockclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class DrawTitle {

    public DrawTitle(String text, double scale) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        int drawHeight = 0;
        String[] splitText = text.split("\n");
        for (String title : splitText) {
            int textLength = mc.fontRendererObj.getStringWidth(title);
            if (textLength * scale > (width * 0.9F)) {
                scale = (width * 0.9F) / (float) textLength;
            }
            int titleX = (int) ((width / 2) - (textLength * scale / 2));
            int titleY = (int) ((height * 0.45) / scale) + (int) (drawHeight * scale);
            new TextRenderer(mc, title, titleX, titleY, scale);
            drawHeight += mc.fontRendererObj.FONT_HEIGHT;
        }
    }

}
