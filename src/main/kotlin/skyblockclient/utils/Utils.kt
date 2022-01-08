package skyblockclient.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraft.util.ChatComponentText
import skyblockclient.SkyblockClient.Companion.CHAT_PREFIX
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.ScoreboardUtils.sidebarLines
import kotlin.math.round


object Utils {
    fun Any?.equalsOneOf(vararg other: Any): Boolean {
        for (obj in other) {
            if (this == obj) return true
        }
        return false
    }

    fun isFloor(floor: Int): Boolean {
        for (s in sidebarLines) {
            val line = ScoreboardUtils.cleanSB(s)
            if (line.contains("The Catacombs (")) {
                if (line.substringAfter("(").substringBefore(")").equalsOneOf("F$floor", "M$floor")) {
                    return true
                }
            }
        }
        return config.forceSkyblock && mc.thePlayer != null && mc.theWorld != null
    }

    val ItemStack.itemID: String
        get() {
            if (this.hasTagCompound() && this.tagCompound.hasKey("ExtraAttributes")) {
                val attributes = this.getSubCompound("ExtraAttributes", false)
                if (attributes.hasKey("id", 8)) {
                    return attributes.getString("id")
                }
            }
            return ""
        }

    val ItemStack.lore: List<String>
        get() {
            if (this.hasTagCompound() && this.tagCompound.hasKey("display", 10)) {
                val display = this.tagCompound.getCompoundTag("display")
                if (display.hasKey("Lore", 9)) {
                    val nbt = display.getTagList("Lore", 8)
                    val lore = ArrayList<String>()
                    for (lineNumber in 0..nbt.tagCount()) {
                        lore.add(nbt.getStringTagAt(lineNumber))
                    }
                    return lore
                }
            }
            return emptyList()
        }

    fun modMessage(message: String) {
        mc.thePlayer.addChatMessage(ChatComponentText("$CHAT_PREFIX $message"))
    }

    fun renderText(
        mc: Minecraft = Minecraft.getMinecraft(),
        text: String,
        x: Int,
        y: Int,
        scale: Double = 1.0,
        color: Int = 0xFFFFFF
    ) {
        var yOffset = y

        GlStateManager.pushMatrix()
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()
        GlStateManager.disableBlend()
        GlStateManager.scale(scale, scale, scale)
        yOffset -= mc.fontRendererObj.FONT_HEIGHT
        for (line in text.split("\n")) {
            yOffset += (mc.fontRendererObj.FONT_HEIGHT * scale).toInt()
            mc.fontRendererObj.drawString(
                line,
                round(x / scale).toFloat(),
                round(yOffset / scale).toFloat(),
                color,
                true
            )
        }
        GlStateManager.popMatrix()
    }

    fun rightClick() {
        val method = try {
            Minecraft::class.java.getDeclaredMethod("func_147121_ag")
        } catch (e: NoSuchMethodException) {
            Minecraft::class.java.getDeclaredMethod("rightClickMouse")
        }
        method.isAccessible = true
        method.invoke(Minecraft.getMinecraft())
    }

    fun leftClick() {
        val method = try {
            Minecraft::class.java.getDeclaredMethod("func_147116_af")
        } catch (e: NoSuchMethodException) {
            Minecraft::class.java.getDeclaredMethod("clickMouse")
        }
        method.isAccessible = true
        method.invoke(Minecraft.getMinecraft())
    }
}