package skyblockclient

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
import skyblockclient.features.*
import skyblockclient.features.dungeons.*
import skyblockclient.utils.ScoreboardUtils
import skyblockclient.utils.UpdateChecker
import java.io.File

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
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent?) {
        config.init()

        ClientCommandHandler.instance.registerCommand(SkyblockClientCommands())

        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(F7P3Ghost())
        MinecraftForge.EVENT_BUS.register(ArrowAlign())
        MinecraftForge.EVENT_BUS.register(AntiBlind())
        MinecraftForge.EVENT_BUS.register(AnvilUses())
        MinecraftForge.EVENT_BUS.register(BloodReady())
        MinecraftForge.EVENT_BUS.register(BoneMacro())
        MinecraftForge.EVENT_BUS.register(EndstoneProtectorTimer())
        MinecraftForge.EVENT_BUS.register(GemstoneESP())
        MinecraftForge.EVENT_BUS.register(GhostBlock())
        MinecraftForge.EVENT_BUS.register(HiddenMobs())
        MinecraftForge.EVENT_BUS.register(SalvageOverlay())
        MinecraftForge.EVENT_BUS.register(ImpactParticles())
        MinecraftForge.EVENT_BUS.register(LividESP())
        MinecraftForge.EVENT_BUS.register(MimicMessage())
        MinecraftForge.EVENT_BUS.register(MinibossESP())
        MinecraftForge.EVENT_BUS.register(NoRotate())
        MinecraftForge.EVENT_BUS.register(StarMobESP())
        MinecraftForge.EVENT_BUS.register(Terminals())

        keyBinds[0] = KeyBinding("Open Settings", Keyboard.KEY_RSHIFT, "Skyblock Client")
        keyBinds[1] = KeyBinding("Bone Macro", Keyboard.KEY_B, "Skyblock Client")
        keyBinds[2] = KeyBinding("Ghost Block", Keyboard.KEY_G, "Skyblock Client")

        for (keyBind in keyBinds) {
            ClientRegistry.registerKeyBinding(keyBind)
        }
    }

    @Mod.EventHandler
    fun postInit(event: FMLLoadCompleteEvent) {
        val response = UpdateChecker.hasUpdate()
        if (response == 1 || response == -2) {
            EssentialAPI.getNotifications().push("Skyblock Client", "New release available on Github.")
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
                isOnHypixel = mc.runCatching {
                    theWorld != null && !isSingleplayer && (thePlayer?.clientBrand?.lowercase()?.contains("hypixel")
                        ?: currentServerData?.serverIP?.lowercase()?.contains("hypixel") ?: false)
                }.onFailure { it.printStackTrace() }.getOrDefault(false)

                inSkyblock = config.forceSkyblock || isOnHypixel && mc.theWorld.scoreboard.getObjectiveInDisplaySlot(1)
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
        if (keyBinds[0]!!.isPressed) {
            display = config.gui()
        }
    }

    companion object {
        const val MOD_ID = "text_overflow_scroll"
        const val MOD_NAME = "Scrollable Tooltips"
        const val MOD_VERSION = "1.4.0"
        const val SB_CLIENT_VERSION = "0.1.2-pre4"
        var inDungeons = false
        var inSkyblock = false
        var isOnHypixel = false
        var mc = Minecraft.getMinecraft()!!
        var config = Config
        var display: GuiScreen? = null
        var keyBinds = arrayOfNulls<KeyBinding>(3)
        var tickCount = 0
    }
}