package skyblockclient;

import gg.essential.vigilance.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import skyblockclient.config.Config;
import skyblockclient.commands.SkyblockClientCommands;
import skyblockclient.features.*;
import skyblockclient.features.dungeons.*;
import skyblockclient.utils.SkyblockCheck;

import java.io.File;

@Mod(modid = SkyblockClient.MOD_ID, name = SkyblockClient.MOD_NAME, version = SkyblockClient.MOD_VERSION, clientSideOnly = true)
public class SkyblockClient {

    public static final String MOD_ID = "text_overflow_scroll";
    public static final String MOD_NAME = "Scrollable Tooltips";
    public static final String MOD_VERSION = "1.4.0";

//    public static GuiScreen display = null;
    public static Config config = new Config();
    public static KeyBinding[] keyBinds = new KeyBinding[3];
    public static int tickCount = 0;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File directory = new File(event.getModConfigurationDirectory(), "sbclient");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Vigilance.initialize();
        config.preload();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new SkyblockClientCommands());

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new AntiBlind());
        MinecraftForge.EVENT_BUS.register(new AnvilUses());
        MinecraftForge.EVENT_BUS.register(new BloodReadyMessage());
        MinecraftForge.EVENT_BUS.register(new BoneMacro());
        MinecraftForge.EVENT_BUS.register(new EndstoneProtectorTimer());
        MinecraftForge.EVENT_BUS.register(new GemstoneESP());
        MinecraftForge.EVENT_BUS.register(new GhostBlock());
        MinecraftForge.EVENT_BUS.register(new HiddenMobs());
        MinecraftForge.EVENT_BUS.register(new ItemOverlay());
        MinecraftForge.EVENT_BUS.register(new LividESP());
        MinecraftForge.EVENT_BUS.register(new MimicKilled());
        MinecraftForge.EVENT_BUS.register(new MinibossESP());
        MinecraftForge.EVENT_BUS.register(new NoRotate());
        MinecraftForge.EVENT_BUS.register(new StarMobESP());
        MinecraftForge.EVENT_BUS.register(new Terminals());
        MinecraftForge.EVENT_BUS.register(new WitherImpactParticles());

        keyBinds[0] = new KeyBinding("Open Settings", Keyboard.KEY_RSHIFT, "Skyblock Client");
        keyBinds[1] = new KeyBinding("Bone Macro", Keyboard.KEY_B, "Skyblock Client");
        keyBinds[2] = new KeyBinding("Ghost Block", Keyboard.KEY_G, "Skyblock Client");

        for (KeyBinding keyBind : keyBinds) {
            ClientRegistry.registerKeyBinding(keyBind);
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
//        if (display != null) {
//            try {
//                Minecraft.getMinecraft().displayGuiScreen(display);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            display = null;
//        }
        tickCount++;
        if (tickCount % 20 == 0) {
            if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
                if (!config.forceSkyblock) {
                    SkyblockCheck.checkForSkyblock();
                    SkyblockCheck.checkForDungeons();
                } else {
                    SkyblockCheck.inSkyblock = true;
                    SkyblockCheck.inDungeons = true;
                }
            }
            tickCount = 0;
        }
    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent event) {
        if (keyBinds[0].isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(config.gui());
        }
    }
}
