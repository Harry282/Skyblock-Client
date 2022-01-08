package skyblockclient.config

import com.google.gson.*
import skyblockclient.SkyblockClient.Companion.configData
import skyblockclient.features.ItemMacro
import skyblockclient.features.NoBlockAnimation
import skyblockclient.guis.ItemMacros
import java.io.File
import java.nio.charset.StandardCharsets

object ConfigManager {

    private val categories = mapOf<String, JsonElement>(
        Pair("Block Animation Blacklist", JsonArray()),
        Pair("Hidden Mod IDs", JsonArray()),
        Pair("Item Macros", JsonArray())
    )

    fun loadConfig(config: File) {
        try {
            if (!config.exists()) config.createNewFile()
            val gson = Gson().fromJson(config.readText(), JsonElement::class.java)?.asJsonObject ?: JsonObject()
            configData.clear()
            gson.entrySet().forEach { configData[it.key] = it.value }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun writeConfig(config: File) {
        try {
            config.bufferedWriter(StandardCharsets.UTF_8).run {
                write(GsonBuilder().setPrettyPrinting().create().toJson(configData))
                close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun parseData() {
        for ((dataCategory, type) in categories) {
            if (!configData.contains(dataCategory)) {
                configData[dataCategory] = type
            }
        }

        try {
            NoBlockAnimation.blacklist.clear()
            configData["Block Animation Blacklist"]?.asJsonArray?.forEach {
                NoBlockAnimation.blacklist.add(it.asString)
            }

            ItemMacro.macros.clear()
            configData["Item Macros"]?.asJsonArray?.forEach { macro ->
                val json = macro.asJsonObject
                val item = json["item"]?.asString
                val keycode = json["keycode"]?.asInt
                val onlyWhileHolding = mutableListOf<String>()
                json["onlyWhileHolding"]?.asJsonArray?.forEach {
                    onlyWhileHolding.add(it.asString)
                }
                val mouseButton = json["mouseButton"]?.asInt
                ItemMacro.macros.add(
                    ItemMacros.Macro(
                        item ?: "",
                        keycode ?: 0,
                        onlyWhileHolding,
                        mouseButton ?: 0
                    )
                )
            }
        } catch (e: Exception) {
            println("Error Reading Config")
            e.printStackTrace()
        }
    }
}