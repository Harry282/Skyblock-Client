package skyblockclient.features.macros

import kotlinx.coroutines.Job
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.GuiMacroUtils

abstract class GuiMacro(val name: String) {
    abstract var job: Job
    abstract val button: GuiButton
    abstract val inGui: Boolean

    @SubscribeEvent
    fun onGuiBackground(event: GuiScreenEvent.BackgroundDrawnEvent) {
        if (!inGui) return
        button.xPosition = ScaledResolution(mc).scaledWidth / 2 + 100
        button.yPosition = ScaledResolution(mc).scaledHeight / 2 - 50
        button.drawButton(mc, event.mouseX, event.mouseY)
    }

    @SubscribeEvent
    fun onMouseInput(event: GuiScreenEvent.MouseInputEvent.Pre) {
        if (!inGui || !Mouse.getEventButtonState() || Mouse.getEventButton() != 0) return
        if (button.isMouseOver) {
            button.playPressSound(mc.soundHandler)
            toggle()
        }
    }

    private fun toggle() {
        if (job.isActive) {
            job.cancel()
            GuiMacroUtils.macroMessage("§aStopped $name!")
        } else {
            if (job.isCompleted) resetJob()
            job.start()
            GuiMacroUtils.macroMessage("§aStarting $name!")
        }
    }

    abstract fun resetJob()
}
