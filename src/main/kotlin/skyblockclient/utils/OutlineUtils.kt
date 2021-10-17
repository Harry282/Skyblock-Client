package skyblockclient.utils

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.shader.Framebuffer
import org.lwjgl.opengl.EXTFramebufferObject
import org.lwjgl.opengl.EXTPackedDepthStencil
import org.lwjgl.opengl.GL11
import skyblockclient.SkyblockClient
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.RenderLivingEntityEvent
import java.awt.Color

object OutlineUtils {
    fun outlineESP(event: RenderLivingEntityEvent, color: Color) {
        val fancyGraphics = mc.gameSettings.fancyGraphics
        val gamma = mc.gameSettings.gammaSetting
        mc.gameSettings.fancyGraphics = false
        mc.gameSettings.gammaSetting = 100000f
        GlStateManager.resetColor()
        setColor(color)
        renderOne(SkyblockClient.config.espOutlineWidth)
        event.modelBase.render(
            event.entity,
            event.p_77036_2_,
            event.p_77036_3_,
            event.p_77036_4_,
            event.p_77036_5_,
            event.p_77036_6_,
            event.scaleFactor
        )
        setColor(color)
        renderTwo()
        event.modelBase.render(
            event.entity,
            event.p_77036_2_,
            event.p_77036_3_,
            event.p_77036_4_,
            event.p_77036_5_,
            event.p_77036_6_,
            event.scaleFactor
        )
        setColor(color)
        renderThree()
        event.modelBase.render(
            event.entity,
            event.p_77036_2_,
            event.p_77036_3_,
            event.p_77036_4_,
            event.p_77036_5_,
            event.p_77036_6_,
            event.scaleFactor
        )
        setColor(color)
        renderFour(color)
        event.modelBase.render(
            event.entity,
            event.p_77036_2_,
            event.p_77036_3_,
            event.p_77036_4_,
            event.p_77036_5_,
            event.p_77036_6_,
            event.scaleFactor
        )
        setColor(color)
        renderFive()
        setColor(Color.WHITE)
        mc.gameSettings.fancyGraphics = fancyGraphics
        mc.gameSettings.gammaSetting = gamma
    }

    private fun renderOne(lineWidth: Float) {
        checkSetupFBO()
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS)
        GL11.glDisable(GL11.GL_ALPHA_TEST)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glLineWidth(lineWidth)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glEnable(GL11.GL_STENCIL_TEST)
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT)
        GL11.glClearStencil(0xF)
        GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xF)
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE)
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE)
    }

    private fun renderTwo() {
        GL11.glStencilFunc(GL11.GL_NEVER, 0, 0xF)
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE)
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL)
    }

    private fun renderThree() {
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xF)
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP)
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE)
    }

    private fun renderFour(color: Color) {
        setColor(color)
        GL11.glDepthMask(false)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE)
        GL11.glPolygonOffset(1.0f, -2000000f)
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f)
    }

    private fun renderFive() {
        GL11.glPolygonOffset(1.0f, 2000000f)
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDepthMask(true)
        GL11.glDisable(GL11.GL_STENCIL_TEST)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_LIGHTING)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_ALPHA_TEST)
        GL11.glPopAttrib()
    }

    private fun setColor(color: Color) {
        GL11.glColor4d(
            (color.red / 255f).toDouble(),
            (color.green / 255f).toDouble(),
            (color.blue / 255f).toDouble(),
            (color.alpha / 255f).toDouble()
        )
    }

    private fun checkSetupFBO() {
        val fbo = mc.framebuffer
        if (fbo != null) {
            if (fbo.depthBuffer > -1) {
                setupFBO(fbo)
                fbo.depthBuffer = -1
            }
        }
    }

    private fun setupFBO(fbo: Framebuffer) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer)
        val stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT()
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID)
        EXTFramebufferObject.glRenderbufferStorageEXT(
            EXTFramebufferObject.GL_RENDERBUFFER_EXT,
            EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT,
            mc.displayWidth,
            mc.displayHeight
        )
        EXTFramebufferObject.glFramebufferRenderbufferEXT(
            EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
            EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT,
            EXTFramebufferObject.GL_RENDERBUFFER_EXT,
            stencilDepthBufferID
        )
        EXTFramebufferObject.glFramebufferRenderbufferEXT(
            EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
            EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT,
            EXTFramebufferObject.GL_RENDERBUFFER_EXT,
            stencilDepthBufferID
        )
    }
}