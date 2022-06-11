package skyblockclient.features

import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.keyBinds
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.Utils
import java.awt.Color


object AutoClicker {

    private var toggled = false

    @SubscribeEvent
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        if (keyBinds[5].isPressed) {
            toggled = !toggled
            if (toggled) {
                Thread {
                    while (toggled) {
                        Utils.rightClick()
                        Thread.sleep(1000L / config.autoClickerCPS)
                    }
                }.start()
            }
        }
    }

    @SubscribeEvent
    fun onOpenGui(event: GuiOpenEvent) {
        toggled = false
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent.Post) {
        if (toggled && event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            val x = ScaledResolution(mc).scaledWidth / 2 + 10
            val y = ScaledResolution(mc).scaledHeight / 2 - 2
            Gui.drawRect(x, y, x + 5, y + 5, Color.red.rgb)
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Unload) {
        toggled = false
    }
}