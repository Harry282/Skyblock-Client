package skyblockclient

import com.google.gson.JsonElement
import gg.essential.api.EssentialAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import org.lwjgl.input.Keyboard
import skyblockclient.command.SkyblockClientCommands
import skyblockclient.config.Config
import skyblockclient.config.ConfigManager.loadConfig
import skyblockclient.config.ConfigManager.parseData
import skyblockclient.config.ConfigManager.writeConfig
import skyblockclient.features.*
import skyblockclient.features.dungeons.*
import skyblockclient.features.nether.AutoAttune
import skyblockclient.features.nether.FirePillar
import skyblockclient.features.nether.dojo.Discipline
import skyblockclient.features.nether.dojo.Force
import skyblockclient.utils.LocationUtils
import skyblockclient.utils.UpdateChecker
import skyblockclient.utils.Utils
import java.awt.Desktop
import java.io.File
import java.net.URI
import kotlin.coroutines.EmptyCoroutineContext

@Mod(
    modid = SkyblockClient.MOD_ID,
    name = SkyblockClient.MOD_NAME,
    version = SkyblockClient.MOD_VERSION,
    clientSideOnly = true
)
class SkyblockClient {
    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        val directory = File(event.modConfigurationDirectory, "sbclient")
        directory.mkdirs()
        configFile = File(directory, "config.json")
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        config.init()

        ClientCommandHandler.instance.registerCommand(SkyblockClientCommands())

        listOf(
            this,
            AntiBlind,
            AnvilUses,
            AutoAttune,
            AutoClicker,
            AutoCloseChest,
            AutoTerminals,
            ArrowAlign,
            BloodReady,
            BoneMacro,
            BookAnvilMacro,
            Discipline,
            EnchantingExperiments,
            EndstoneProtectorTimer,
            F7PreGhostBlocks,
            FastLeap,
            FirePillar,
            Force,
            GemstoneESP,
            GhostBlock,
            HiddenMobs,
            HideServerID,
            ImpactParticles,
            ItemMacro,
            LividESP,
            LocationUtils,
            MimicMessage,
            MobESP,
            NoBlockAnimation,
            NoWaterFOV,
            RuneCombineMacro,
            SalvageOverlay,
            SimonSaysButtons,
            SummonsFeatures,
            TerminalFeatures,
            ThornStun,
            WormFishingLavaESP
        ).forEach(MinecraftForge.EVENT_BUS::register)

        keyBinds.forEach(ClientRegistry::registerKeyBinding)
    }

    @Mod.EventHandler
    fun postInit(event: FMLLoadCompleteEvent) = scope.launch(Dispatchers.IO) {
        configFile?.let {
            loadConfig(it)
            parseData()
            writeConfig(it)
        }
        if (UpdateChecker.hasUpdate() > 0) {
            EssentialAPI.getNotifications()
                .push(MOD_NAME, "New release available on Github. Click to open download link.", 10f, action = {
                    try {
                        Desktop.getDesktop().browse(URI("https://github.com/Harry282/Skyblock-Client/releases"))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || display == null) return
        mc.displayGuiScreen(display)
        display = null
    }

    @SubscribeEvent
    fun onKey(event: KeyInputEvent) {
        if (keyBinds[0].isPressed) display = config.gui()
        if (keyBinds[3].isPressed) {
            config.noRotate = !config.noRotate
            Utils.modMessage("§aNo Rotate ${if (config.noRotate) "on" else "off"}")
        }
        if (keyBinds[4].isPressed) {
            config.antiKBSkyblock = !config.antiKBSkyblock
            Utils.modMessage("§aAnti KB ${if (config.antiKBSkyblock) "on" else "off"}")
        }
    }

    companion object {
        const val MOD_ID = "sbclient"
        const val MOD_NAME = "Skyblock Client"
        const val MOD_VERSION = "0.2.0-pre1"
        const val CHAT_PREFIX = "§b§l<§fSkyblockClient§b§l>§r"

        val mc: Minecraft = Minecraft.getMinecraft()
        var config = Config
        val configData = HashMap<String, JsonElement>()
        var configFile: File? = null
        var display: GuiScreen? = null
        val scope = CoroutineScope(EmptyCoroutineContext)

        val keyBinds = arrayOf(
            KeyBinding("Open Settings", Keyboard.KEY_RSHIFT, "Skyblock Client"),
            KeyBinding("Bone Macro", Keyboard.KEY_B, "Skyblock Client"),
            KeyBinding("Ghost Block", Keyboard.KEY_G, "Skyblock Client"),
            // Toggle keybinds until I make better way of doing this lol
            KeyBinding("Toggle NoRotate", Keyboard.KEY_NONE, "Skyblock Client"),
            KeyBinding("Toggle AntiKB", Keyboard.KEY_NONE, "Skyblock Client"),
            KeyBinding("Toggle Autoclicker", Keyboard.KEY_X, "Skyblock Client")
        )
        var capesLoaded = false
    }
}
