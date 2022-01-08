package skyblockclient

import com.google.gson.JsonElement
import gg.essential.api.EssentialAPI
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
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent
import org.lwjgl.input.Keyboard
import skyblockclient.command.SkyblockClientCommands
import skyblockclient.config.Config
import skyblockclient.config.ConfigManager.loadConfig
import skyblockclient.config.ConfigManager.parseData
import skyblockclient.config.ConfigManager.writeConfig
import skyblockclient.features.*
import skyblockclient.features.dungeons.*
import skyblockclient.utils.ScoreboardUtils
import skyblockclient.utils.UpdateChecker
import java.awt.Desktop
import java.io.File
import java.net.URI

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
        if (!directory.exists()) {
            directory.mkdirs()
        }
        configFile = File(directory, "config.json")
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        config.init()

        ClientCommandHandler.instance.registerCommand(SkyblockClientCommands())

        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(AntiBlind())
        MinecraftForge.EVENT_BUS.register(AnvilUses())
        MinecraftForge.EVENT_BUS.register(ArrowAlign())
        MinecraftForge.EVENT_BUS.register(BloodReady())
        MinecraftForge.EVENT_BUS.register(BoneMacro())
        MinecraftForge.EVENT_BUS.register(BookAnvilMacro())
        MinecraftForge.EVENT_BUS.register(EnchantingExperiments())
        MinecraftForge.EVENT_BUS.register(EndstoneProtectorTimer())
        MinecraftForge.EVENT_BUS.register(F7P3Ghost())
        MinecraftForge.EVENT_BUS.register(FastLeap())
        MinecraftForge.EVENT_BUS.register(GemstoneESP())
        MinecraftForge.EVENT_BUS.register(GhostBlock())
        MinecraftForge.EVENT_BUS.register(HiddenMobs())
        MinecraftForge.EVENT_BUS.register(ImpactParticles())
        MinecraftForge.EVENT_BUS.register(ItemMacro())
        MinecraftForge.EVENT_BUS.register(LividESP())
        MinecraftForge.EVENT_BUS.register(MimicMessage())
        MinecraftForge.EVENT_BUS.register(MobESP())
        MinecraftForge.EVENT_BUS.register(NoBlockAnimation())
        MinecraftForge.EVENT_BUS.register(NoWaterFOV())
        MinecraftForge.EVENT_BUS.register(SalvageOverlay())
        MinecraftForge.EVENT_BUS.register(Terminals())
        MinecraftForge.EVENT_BUS.register(ThornStun())

        for (keyBind in keyBinds) {
            ClientRegistry.registerKeyBinding(keyBind)
        }
    }

    @Mod.EventHandler
    fun postInit(event: FMLLoadCompleteEvent) {
        configFile?.let { loadConfig(it) }
        parseData()
        configFile?.let { writeConfig(it) }
        if (UpdateChecker.hasUpdate() > 0) {
            try {
                EssentialAPI.getNotifications().push(
                    "Skyblock Client",
                    "New release available on Github. Click to open download link.",
                    10f
                ) {
                    Desktop.getDesktop().browse(URI("https://github.com/Harry282/Skyblock-Client/releases"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        tickCount++
        if (display != null) {
            mc.displayGuiScreen(display)
            display = null
        }
        if (tickCount % 20 == 0) {
            if (mc.thePlayer != null) {
                val onHypixel = EssentialAPI.getMinecraftUtil().isHypixel()

                inSkyblock = config.forceSkyblock || onHypixel && mc.theWorld.scoreboard.getObjectiveInDisplaySlot(1)
                    ?.let { ScoreboardUtils.cleanSB(it.displayName).contains("SKYBLOCK") } ?: false

                inDungeons = config.forceSkyblock || inSkyblock && ScoreboardUtils.sidebarLines.any {
                    ScoreboardUtils.cleanSB(it).run {
                        (contains("The Catacombs") && !contains("Queue")) || contains("Dungeon Cleared:")
                    }
                }
            }
            tickCount = 0
        }
    }

    @SubscribeEvent
    fun onDisconnect(event: ClientDisconnectionFromServerEvent?) {
        inSkyblock = false
        inDungeons = false
    }

    @SubscribeEvent
    fun onKey(event: KeyInputEvent?) {
        if (keyBinds[0].isPressed) display = config.gui()
    }

    companion object {
        const val MOD_ID = "sbclient"
        const val MOD_NAME = "Skyblock Client"
        const val MOD_VERSION = "0.1.2"
        const val CHAT_PREFIX = "§b§l<§fSkyblockClient§b§l>§r"
        val mc: Minecraft = Minecraft.getMinecraft()
        var config = Config
        val configData = HashMap<String, JsonElement>()
        var configFile: File? = null
        var display: GuiScreen? = null
        var inSkyblock = false
        var inDungeons = false
        val keyBinds = arrayOf(
            KeyBinding("Open Settings", Keyboard.KEY_RSHIFT, "Skyblock Client"),
            KeyBinding("Bone Macro", Keyboard.KEY_B, "Skyblock Client"),
            KeyBinding("Ghost Block", Keyboard.KEY_G, "Skyblock Client")
        )
        var tickCount = 0
    }
}