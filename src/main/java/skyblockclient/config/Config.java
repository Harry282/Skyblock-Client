package skyblockclient.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.awt.*;
import java.io.File;

public class Config extends Vigilant {

    /*
    Dungeons
     */

    @Property(type = PropertyType.SWITCH, name = "Blood Ready Notify", description = "Notification when the watcher has finished spawning mobs.",
            category = "Dungeons", subcategory = "General")
    public boolean bloodReadyNotify = false;

    @Property(type = PropertyType.SWITCH, name = "Mimic Killed Message", description = "Sends a message in party chat when mimic is killed.",
            category = "Dungeons", subcategory = "General")
    public boolean mimicKillMessage = false;

    @Property(type = PropertyType.TEXT, name = "Mimic Message", description = "Message sent when mimic is detected to be killed. /sbclient mimicmessage to change.",
            category = "Dungeons", subcategory = "General")
    public String mimicMessage = "Mimic killed!";

    @Property(type = PropertyType.SWITCH, name = "Livid Finder",
            category = "Dungeons", subcategory = "Render")
    public boolean lividFinder = false;

    @Property(type = PropertyType.SWITCH, name = "Show Fels",
            category = "Dungeons", subcategory = "Render")
    public boolean showFels = false;

    @Property(type = PropertyType.SWITCH, name = "Show Shadow Assassins",
            category = "Dungeons", subcategory = "Render")
    public boolean showShadowAssassin = false;

    @Property(type = PropertyType.SWITCH, name = "Show Stealthy Mobs",
            category = "Dungeons", subcategory = "Render")
    public boolean showStealthy = false;

    /*
    Terminals
     */

    @Property(type = PropertyType.SWITCH, name = "Pingless Clicks", description = "Makes terminal clicks not dependant on ping.",
            category = "Terminals", subcategory = "Clicks")
    public boolean terminalPingless = false;

    @Property(type = PropertyType.SWITCH, name = "Block Incorrect Clicks",
            category = "Terminals", subcategory = "Clicks")
    public boolean terminalBlockClicks = false;

    @Property(type = PropertyType.SWITCH, name = "Middle Clicks",
            category = "Terminals", subcategory = "Clicks")
    public boolean terminalMiddleClick = false;

    @Property(type = PropertyType.SWITCH, name = "Terminal Kick Notify", description = "On screen notification if you get kicked out of a terminal.",
            category = "Terminals", subcategory = "Clicks")
    public boolean terminalKickNotify = false;

    @Property(type = PropertyType.SWITCH, name = "Hide Tooltips",
            category = "Terminals", subcategory = "Clicks")
    public boolean terminalHideTooltip = false;


    @Property(type = PropertyType.CHECKBOX, name = "Number Terminal",
            category = "Terminals", subcategory = "Highlight")
    public boolean terminalHighlightNumbers = false;

    @Property(type = PropertyType.CHECKBOX, name = "Starts With Letter Terminal",
            category = "Terminals", subcategory = "Highlight")
    public boolean terminalHighlightLetter = false;

    @Property(type = PropertyType.CHECKBOX, name = "Select All Color Terminal",
            category = "Terminals", subcategory = "Highlight")
    public boolean terminalHighlightColor = false;


    @Property(type = PropertyType.SWITCH, name = "Auto Terminals", description = "No auto terminals will work with this off.",
            category = "Terminals", subcategory = "Macro")
    public boolean terminalAuto = false;

    @Property(type = PropertyType.NUMBER, name = "Terminal Click Delay", description = "Time in ms between automatic terminal clicks.",
            category = "Terminals", subcategory = "Macro", increment = 10, max = 1000)
    public int terminalClickDelay = 200;

    @Property(type = PropertyType.CHECKBOX, name = "Auto Maze Terminal",
            category = "Terminals", subcategory = "Macro")
    public boolean terminalMaze = false;

    @Property(type = PropertyType.CHECKBOX, name = "Auto Number Terminal",
            category = "Terminals", subcategory = "Macro")
    public boolean terminalNumbers = false;

    @Property(type = PropertyType.CHECKBOX, name = "Auto Correct All Terminal",
            category = "Terminals", subcategory = "Macro")
    public boolean terminalCorrectAll = false;

    @Property(type = PropertyType.CHECKBOX, name = "Auto Starts With Letter Terminal",
            category = "Terminals", subcategory = "Macro")
    public boolean terminalLetter = false;

    @Property(type = PropertyType.CHECKBOX, name = "Auto Select All Color Terminal",
            category = "Terminals", subcategory = "Macro")
    public boolean terminalColor = false;

    /*
    Bone Macro
     */

    @Property(type = PropertyType.NUMBER, name = "Bone Throw Time", description = "Time between each bone throw in ms. Default 50.",
            category = "Bone Macro", subcategory = "Settings", max = 200)
    public int boneThrowDelay = 50;

    @Property(type = PropertyType.NUMBER, name = "Swap Delay Time", description = "Time from first bone throw to bow swap in ms. Can also be set with /sbclient swapdelay. Default 1300.",
            category = "Bone Macro", subcategory = "Settings", increment = 10, max = 3000)
    public int boneSwapDelay = 1300;

    @Property(type = PropertyType.NUMBER, name = "Swap Delay Variable", description = "Variable throw delay in ms. Default 10.",
            category = "Bone Macro", subcategory = "Settings", max = 100)
    public int boneSwapVariable = 10;

    /*
    ESP
     */

    @Property(type = PropertyType.SELECTOR, name = "ESP Type",
            category = "ESP", subcategory = "ESP", options = {"Outline", "Box", "Outlined Box"})
    public int espType = 0;

    @Property(type = PropertyType.DECIMAL_SLIDER, name = "OutlineESP Width",
            category = "ESP", subcategory = "ESP", maxF = 10f)
    public float espOutlineWidth = 3f;

    @Property(type = PropertyType.SWITCH, name = "OutlineESP Bats",
            category = "ESP", subcategory = "ESP")
    public boolean espBats = false;

    @Property(type = PropertyType.SWITCH, name = "OutlineESP Fels",
            category = "ESP", subcategory = "ESP")
    public boolean espFels = false;

    @Property(type = PropertyType.SWITCH, name = "OutlineESP Shadow Assassins",
            category = "ESP", subcategory = "ESP")
    public boolean espShadowAssassin = false;

    @Property(type = PropertyType.SWITCH, name = "OutlineESP Dungeon Minibosses",
            category = "ESP", subcategory = "ESP")
    public boolean espMiniboss = false;

    @Property(type = PropertyType.SWITCH, name = "OutlineESP Dungeon Starred Mobs",
            category = "ESP", subcategory = "ESP")
    public boolean espStarMobs = false;

    /*
    Gui
     */

    @Property(type = PropertyType.SWITCH, name = "Hide Potion Effects",
            category = "Gui", subcategory = "Inventory")
    public boolean hidePotionEffects = false;

    @Property(type = PropertyType.SWITCH, name = "Highlight Salvageable Items",
            category = "Gui", subcategory = "Inventory")
    public boolean overlaySalvageable = false;

    @Property(type = PropertyType.COLOR, name = "Salvageable Items Color", description = "Default 55FFFFAA.",
            category = "Gui", subcategory = "Inventory")
    public Color overlayColorSalvageable = new Color(85, 255, 255, 170);

    @Property(type = PropertyType.COLOR, name = "Top Quality Salvageable Items Color", description = "Default 6AFF6AAA.",
            category = "Gui", subcategory = "Inventory")
    public Color overlayColorTopSalvageable = new Color(106, 255, 106, 170);

    /*
    Colors
     */

    @Property(type = PropertyType.COLOR, name = "Terminal Highlight Color", description = "Default #55FF55AA.",
            category = "Colors", subcategory = "Terminal")
    public Color terminalColorHighlight = new Color(85, 255, 85, 170);

    @Property(type = PropertyType.COLOR, name = "Terminal First Number Color", description = "Default #55FFFFFF.",
            category = "Colors", subcategory = "Terminal")
    public Color terminalColorNumberFirst = new Color(85, 255, 255, 255);

    @Property(type = PropertyType.COLOR, name = "Terminal Second Number Color", description = "Default #55FFFFAA.",
            category = "Colors", subcategory = "Terminal")
    public Color terminalColorNumberSecond = new Color(85, 255, 255, 170);

    @Property(type = PropertyType.COLOR, name = "Terminal Third Number Color", description = "Default #55FFFF55.",
            category = "Colors", subcategory = "Terminal")
    public Color terminalColorNumberThird = new Color(85, 255, 255, 85);

    @Property(type = PropertyType.COLOR, name = "Bat Color", description = "Default #2FEE2F.",
            category = "Colors", subcategory = "Outline ESP", allowAlpha = false)
    public Color espColorBats = new Color(47, 238, 47);

    @Property(type = PropertyType.COLOR, name = "Fel Color", description = "Default #CB59FF.",
            category = "Colors", subcategory = "Outline ESP", allowAlpha = false)
    public Color espColorFels = new Color(203, 89, 255);

    @Property(type = PropertyType.COLOR, name = "Shadow Assassin Color", description = "Default #AA00AA.",
            category = "Colors", subcategory = "Outline ESP", allowAlpha = false)
    public Color espColorShadowAssassin = new Color(170, 0, 170);

    @Property(type = PropertyType.COLOR, name = "Livid Color", description = "Default #55FFFF.",
            category = "Colors", subcategory = "Outline ESP", allowAlpha = false)
    public Color espColorLivid = new Color(85, 255, 255);

    /*
    Misc
     */

    @Property(type = PropertyType.SWITCH, name = "Endstone Protector Timer", description = "Timer under your crosshair for when the Endstone Protector will spawn.",
            category = "Misc", subcategory = "QOL")
    public boolean endstoneProtectorTimer = false;

    @Property(type = PropertyType.SWITCH, name = "Right Click Ghost Block", description = "Right click with a Stonk to create ghost block. Can also be done with a keybind.",
            category = "Misc", subcategory = "QOL")
    public boolean stonkGhostBlock = false;

    @Property(type = PropertyType.SWITCH, name = "No Blindness", description = "Disables effects of blindness.",
            category = "Misc", subcategory = "Clear Sight")
    public boolean antiBlind = false;

    @Property(type = PropertyType.SWITCH, name = "No Portal Effect", description = "Disables nether portal overlay.",
            category = "Misc", subcategory = "Clear Sight")
    public boolean antiPortal = false;

    @Property(type = PropertyType.SWITCH, name = "Teleport No Rotate", description = "Prevents your view from being changed while holding any Wither Impact sword, AOTE, or AOTV.",
            category = "Misc", subcategory = "No Rotate")
    public boolean noRotate = false;

    @Property(type = PropertyType.SWITCH, name = "No Rotate Auto Disable", description = "Keep this on unless you want to be banned faster.",
            category = "Misc", subcategory = "No Rotate")
    public boolean noRotateAutoDisable = true;

    @Property(type = PropertyType.SWITCH, name = "No Shield Particle", description = "Gets rid of purple particles and hearts from wither impact.",
            category = "Misc", subcategory = "No Rotate")
    public boolean noShieldParticles;

    @Property(type = PropertyType.SWITCH, name = "Gemstone ESP", description = "",
            category = "Misc", subcategory = "Mining")
    public boolean gemstoneESP = false;

    @Property(type = PropertyType.NUMBER, name = "Gemstone ESP Radius", description = "",
            category = "Misc", subcategory = "Mining", max = 50)
    public int gemstoneESPRadius = 20;

    /*
    Dev Testing
     */

    @Property(type = PropertyType.SWITCH, name = "Show Anvil Uses", description = "Show NBT anvil uses of the skyblock items.",
            category = "Testing")
    public boolean showAnvilUses = false;

    @Property(type = PropertyType.SWITCH, name = "Force Skyblock", description = "Forces all features to enable, even if you are not on skyblock.",
            category = "Testing")
    public boolean forceSkyblock = false;

    @Property(type = PropertyType.SWITCH, name = "Shift Click Terminals", description = "Shift clicks auto terminals.",
            category = "Testing")
    public boolean terminalShiftClick = false;



    public Config() {
        super(new File("./config/sbclient/config.toml"), "SkyblockClient");
        initialize();
        setCategoryDescription("Customization",
                "Use /vigilance to change the colors of the settings menu");
        setCategoryDescription("ESP",
                "Disable Optifine fast render and Patcher entity culling.");
    }
}
