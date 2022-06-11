package skyblockclient.ui

import com.google.gson.JsonArray
import com.google.gson.JsonObject
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
import gg.essential.universal.UKeyboard
import gg.essential.vigilance.gui.settings.DropDown
import gg.essential.vigilance.utils.onLeftClick
import net.minecraft.client.settings.GameSettings
import skyblockclient.SkyblockClient
import skyblockclient.SkyblockClient.Companion.configData
import skyblockclient.config.ConfigManager.parseData
import skyblockclient.config.ConfigManager.writeConfig
import skyblockclient.features.ItemMacro.macros
import java.awt.Color

class ItemMacros : WindowScreen(newGuiScale = GuiScale.scaleForScreenSize().ordinal) {

    private var clickedButton: Macro? = null
    private val scrollComponent: ScrollComponent
    private val components = HashMap<UIContainer, Macro>()

    init {
        UIText("Item Macros").constrain {
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

        macros.forEach {
            addEntry(it.item, it.keycode, it.onlyWhileHolding, it.mouseButton)
        }
    }

    private fun addEntry(
        item: String = "",
        keycode: Int = 0,
        onlyWhileHolding: List<String> = emptyList(),
        mouseButton: Int = 0
    ) {
        val container = UIContainer().constrain {
            x = CenterConstraint()
            y = SiblingConstraint(5f)
            width = 80.percent()
            height = 18.percent()
        } effect (OutlineEffect(
            Color(85, 255, 255, 192),
            2f,
            sides = setOf(OutlineEffect.Side.Bottom)
        )) childOf scrollComponent

        (UITextInput("Item Name / Item ID").constrain {
            x = 5.pixels()
            y = 15.percent()
            width = 50.percent()
        } childOf container).apply {
            onLeftClick {
                if (clickedButton == null) grabWindowFocus()
            }
            setText(item)
        }

        (UITextInput("Only While Holding Item (ignored if empty)").constrain {
            x = 5.pixels()
            y = 65.percent()
            width = 50.percent()
        } childOf container).apply {
            onLeftClick {
                if (clickedButton == null) grabWindowFocus()
            }
            setText(onlyWhileHolding.joinToString())
        }

        val keybindButton = UIButton("Keybind").constrain {
            x = 85.percent()
            y = 15.percent()
        } childOf container

        val macro = Macro(item, keycode, onlyWhileHolding, mouseButton, container, keybindButton)

        keybindButton.onLeftClick {
            clickedButton = macro
        }

        UIButton("Remove").constrain {
            x = 85.percent()
            y = 60.percent()
        }.onLeftClick {
            scrollComponent.removeChild(container)
            components.remove(container)
        } childOf container

        (DropDown(0, listOf("Right Click", "Left Click"), OutlineEffect(Color(0xffffff), 1f)).constrain {
            x = 60.percent()
            y = 15.percent()
            width = 15.percent()
        } childOf container).onValueChange {
            macro.mouseButton = it
        }

        components[container] = macro
    }

    override fun onKeyPressed(keyCode: Int, typedChar: Char, modifiers: UKeyboard.Modifiers?) {
        if (clickedButton != null) {
            when {
                keyCode == 1 -> {
                    clickedButton!!.keycode = 0
                }
                keyCode != 0 -> {
                    clickedButton!!.keycode = keyCode
                }
                typedChar.code > 0 -> {
                    clickedButton!!.keycode = typedChar.code + 256
                }
            }
            clickedButton = null
        } else super.onKeyPressed(keyCode, typedChar, modifiers)
    }

    override fun onMouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        if (clickedButton != null) {
            clickedButton!!.keycode = -100 + mouseButton
            clickedButton = null
        } else super.onMouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun onTick() {
        super.onTick()
        components.forEach { (_, macro) ->
            macro.button.text.setText(GameSettings.getKeyDisplayString(macro.keycode))
            if (clickedButton === macro) {
                macro.button.text.setText("§f> §e${macro.button.text.getText()}§f <")
            }
        }
    }

    override fun onScreenClose() {
        super.onScreenClose()
        macros.clear()
        val jsonArray = JsonArray()
        components.forEach { (container, macro) ->
            val jsonObject = JsonObject()

            macro.item = container.childrenOfType<UITextInput>().firstOrNull {
                it.placeholder == "Item Name / Item ID"
            }?.getText() ?: return@forEach
            if (macro.item == "") return@forEach
            jsonObject.addProperty("item", macro.item)

            jsonObject.addProperty("keycode", macro.keycode)

            macro.onlyWhileHolding = container.childrenOfType<UITextInput>().firstOrNull {
                it.placeholder == "Only While Holding Item (ignored if empty)"
            }?.getText()?.split(", ") ?: emptyList()

            jsonObject.add("onlyWhileHolding", macro.onlyWhileHolding.run {
                val arr = JsonArray()
                forEach { arr.add(JsonPrimitive(it)) }
                return@run arr
            })

            jsonObject.addProperty("mouseButton", macro.mouseButton)

            jsonArray.add(jsonObject)

            macros.add(macro)
        }
        configData["Item Macros"] = jsonArray
        parseData()
        SkyblockClient.configFile?.let { writeConfig(it) }
    }

    data class Macro(
        var item: String,
        var keycode: Int,
        var onlyWhileHolding: List<String>,
        var mouseButton: Int = 0,
        val container: UIContainer = UIContainer(),
        val button: UIButton = UIButton("")
    )
}
