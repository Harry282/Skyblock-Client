package skyblockclient.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import skyblockclient.SkyblockClient.Companion.mc
import kotlin.math.round

object RenderUtilsKT {
    fun drawTitle(text: String, scale: Double) {
        var textScale = scale
        val sr = ScaledResolution(mc)
        val width = sr.scaledWidth
        val height = sr.scaledHeight
        var drawHeight = 0
        val splitText = text.split("\n".toRegex()).toTypedArray()
        for (title in splitText) {
            val textLength = mc.fontRendererObj.getStringWidth(title)
            if (textLength * textScale > width * 0.9f) {
                textScale = (width * 0.9f / textLength.toFloat()).toDouble()
            }
            val titleX = (width / 2 - textLength * textScale / 2).toInt()
            val titleY = (height * 0.45 / textScale).toInt() + (drawHeight * textScale).toInt()
            renderText(mc, title, titleX, titleY, textScale)
            drawHeight += mc.fontRendererObj.FONT_HEIGHT
        }
    }

    fun renderText(mc: Minecraft, text: String, x: Int, y: Int, scale: Double) {
        var yOffset = y
        GlStateManager.pushMatrix()
        GlStateManager.scale(scale, scale, scale)
        yOffset -= mc.fontRendererObj.FONT_HEIGHT
        for (line in text.split("\n".toRegex()).toTypedArray()) {
            yOffset += (mc.fontRendererObj.FONT_HEIGHT * scale).toInt()
            mc.fontRendererObj.drawString(
                line,
                round(x / scale).toFloat(),
                round(y / scale).toFloat(),
                0xFFFFFF,
                true
            )
        }
        GlStateManager.popMatrix()
    }
}