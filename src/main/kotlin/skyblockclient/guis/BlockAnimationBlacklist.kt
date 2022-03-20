package skyblockclient.guis

import com.google.gson.JsonArray
import com.google.gson.JsonPrimitive
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.GuiScale
import gg.essential.vigilance.utils.onLeftClick
import skyblockclient.SkyblockClient
import skyblockclient.SkyblockClient.Companion.configData
import skyblockclient.config.ConfigManager.parseData
import skyblockclient.config.ConfigManager.writeConfig
import skyblockclient.features.NoBlockAnimation.blacklist
import java.awt.Color

class BlockAnimationBlacklist : WindowScreen(newGuiScale = GuiScale.scaleForScreenSize().ordinal) {

    private val scrollComponent: ScrollComponent

    init {
        UIText("Block Animation Blacklist").constrain {
            x = CenterConstraint()
            y = RelativeConstraint(0.075f)
            height = 20.pixels()
        } childOf window

        UIButton("Add New").constrain {
            x = CenterConstraint() - 15.percent()
            y = 90.percent()
        }.onLeftClick {
            addEntry()
        } childOf window

        UIButton("Save and Exit").constrain {
            x = CenterConstraint() + 15.percent()
            y = 90.percent()
        }.onLeftClick {
            mc.displayGuiScreen(null)
        } childOf window

        scrollComponent = ScrollComponent(innerPadding = 4f, scrollIconColor = Color(128, 128, 128, 128)).constrain {
            x = CenterConstraint()
            y = 15.percent()
            width = 70.percent()
            height = 75.percent()
        } childOf window

        blacklist.forEach {
            addEntry(it)
        }
    }

    private fun addEntry(itemName: String = "") {
        val container = UIContainer().constrain {
            x = CenterConstraint()
            y = SiblingConstraint(5f)
            width = 80.percent()
            height = 9.5.percent()
        } effect (OutlineEffect(
            Color(85, 255, 255, 192),
            2f,
            sides = setOf(OutlineEffect.Side.Bottom)
        )) childOf scrollComponent

        (UITextInput("Item Name / Item ID").constrain {
            x = 5.pixels()
            y = CenterConstraint()
            width = 70.percent()
        } childOf container).apply {
            onLeftClick {
                grabWindowFocus()
            }
            setText(itemName)
        }

        UIButton("Remove").constrain {
            x = 85.percent()
            y = CenterConstraint()
        }.onLeftClick {
            scrollComponent.removeChild(container)
        } childOf container
    }

    override fun onScreenClose() {
        super.onScreenClose()
        val jsonArray = JsonArray()
        scrollComponent.allChildren.forEach { container ->
            val textInput = container.childrenOfType<UITextInput>().find {
                it.placeholder == "Item Name / Item ID"
            } ?: return@forEach
            if (textInput.getText() != "") {
                jsonArray.add(JsonPrimitive(textInput.getText()))
            }
        }
        configData["Block Animation Blacklist"] = jsonArray
        parseData()
        SkyblockClient.configFile?.let { writeConfig(it) }
    }
}
