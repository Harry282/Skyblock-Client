package club.sk1er.mods.scrollabletooltips;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;

public class GuiUtilsOverride {
    public static int scrollY = 0;
    public static boolean allowScrolling;
    public static int scrollX = 0;

    /**
     * Draws a tooltip box on the screen with text in it.
     * Automatically positions the box relative to the mouse to match Mojang's implementation.
     * Automatically wraps text when there is not enough space on the screen to display the text without wrapping.
     * Can have a maximum width set to avoid creating very wide tooltips.
     *
     * @param textLines    the lines of text to be drawn in a hovering tooltip box.
     * @param screenHeight the available  screen height for the tooltip to drawn in
     */
    public static void drawHoveringText(List<String> textLines, int screenHeight, int tooltipY, int tooltipHeight) {
        if (!allowScrolling) {
            scrollX = 0;
            scrollY = 0;
        }

        allowScrolling = tooltipY < 0;
        GlStateManager.pushMatrix();
        if (allowScrolling) {
            int eventDWheel = Mouse.getDWheel();
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (eventDWheel < 0) {
                    scrollX += 10;
                } else if (eventDWheel > 0) {
                    //Scrolling to access higher stuff
                    scrollX -= 10;
                }
            } else {
                if (eventDWheel < 0) {
                    scrollY -= 10;
                } else if (eventDWheel > 0) {
                    //Scrolling to access higher stuff
                    scrollY += 10;
                }
            }

            if (scrollY + tooltipY > 6) {
                scrollY = -tooltipY + 6;
            } else if (scrollY + tooltipY + tooltipHeight + 6 < screenHeight) {
                scrollY = screenHeight - 6 - tooltipY - tooltipHeight;
            }
        }

        GlStateManager.translate(scrollX, scrollY, 0);
    }
}
