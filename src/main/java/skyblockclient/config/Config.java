package skyblockclient.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class Config extends Vigilant {

    /*
    Category: Dungeons
    */

        /*
        Subcategory: General
        */
        public static Config INSTANCE = new Config();
        @Property(type = PropertyType.SWITCH, name = "Blood Ready Notify", description = "Notification when the watcher has finished spawning mobs.",
                category = "Dungeons", subcategory = "General")
        public boolean bloodReadyNotify = false;
        @Property(type = PropertyType.SWITCH, name = "Mimic Killed Message", description = "Sends a message in party chat when mimic is killed.",
                category = "Dungeons", subcategory = "General")
        public boolean mimicKillMessage = false;
        @Property(type = PropertyType.TEXT, name = "Mimic Message", description = "Message sent when mimic is detected to be killed. /sbclient mimicmessage to change.",
                category = "Dungeons", subcategory = "General")
        public String mimicMessage = "Mimic killed!";

        /*
        Subcategory: Render
        */
        @Property(type = PropertyType.SWITCH, name = "F7 Ghost Block", description = "Automatically creates ghost blocks to go to P3 from P2 on F7.",
                category = "Dungeons", subcategory = "General")
        public boolean f7p3Ghost = false;
        @Property(type = PropertyType.SWITCH, name = "Livid Finder",
                category = "Dungeons", subcategory = "Render")
        public boolean lividFinder = false;
        @Property(type = PropertyType.SWITCH, name = "Show Fels",
                category = "Dungeons", subcategory = "Render")
        public boolean showFels = false;
        @Property(type = PropertyType.SWITCH, name = "Show Shadow Assassins",
                category = "Dungeons", subcategory = "Render")
        public boolean showShadowAssassin = false;

    /*
    Category: ESP
    */

        /*
        Category: Dungeon ESP
        */
        @Property(type = PropertyType.SWITCH, name = "Show Stealthy Mobs",
                category = "Dungeons", subcategory = "Render")
        public boolean showStealthy = false;
        @Property(type = PropertyType.SELECTOR, name = "ESP Type",
                category = "ESP", subcategory = "Dungeon ESP", options = {"Outline", "Box", "Outlined Box"})
        public int espType = 0;
        @Property(type = PropertyType.DECIMAL_SLIDER, name = "OutlineESP Width",
                category = "ESP", subcategory = "Dungeon ESP", maxF = 10f)
        public float espOutlineWidth = 3f;
        @Property(type = PropertyType.SWITCH, name = "ESP Bats",
                category = "ESP", subcategory = "Dungeon ESP")
        public boolean espBats = false;
        @Property(type = PropertyType.SWITCH, name = "ESP Fels",
                category = "ESP", subcategory = "Dungeon ESP")
        public boolean espFels = false;
        @Property(type = PropertyType.SWITCH, name = "Shadow Assassins",
                category = "ESP", subcategory = "Dungeon ESP")
        public boolean espShadowAssassin = false;
        @Property(type = PropertyType.SWITCH, name = "Dungeon Minibosses",
                category = "ESP", subcategory = "Dungeon ESP")
        public boolean espMiniboss = false;

        /*
        Subcategory: Dungeon ESP Colors
        */
        @Property(type = PropertyType.SWITCH, name = "Dungeon Starred Mobs",
                category = "ESP", subcategory = "Dungeon ESP")
        public boolean espStarMobs = false;
        @Property(type = PropertyType.COLOR, name = "Livid Color", description = "Default #55FFFF.",
                category = "ESP", subcategory = "Dungeon ESP Colors", allowAlpha = false)
        public Color espColorLivid = new Color(85, 255, 255);
        @Property(type = PropertyType.COLOR, name = "Bat Color", description = "Default #2FEE2F.",
                category = "ESP", subcategory = "Dungeon ESP Colors", allowAlpha = false)
        public Color espColorBats = new Color(47, 238, 47);
        @Property(type = PropertyType.COLOR, name = "Fel Color", description = "Default #CB59FF.",
                category = "ESP", subcategory = "Dungeon ESP Colors", allowAlpha = false)
        public Color espColorFels = new Color(203, 89, 255);
        @Property(type = PropertyType.COLOR, name = "Shadow Assassin Color", description = "Default #AA00AA.",
                category = "ESP", subcategory = "Dungeon ESP Colors", allowAlpha = false)
        public Color espColorShadowAssassin = new Color(170, 0, 170);
        @Property(type = PropertyType.COLOR, name = "Unstable Dragon Adventurer Color", description = "Default #B212E3.",
                category = "ESP", subcategory = "Dungeon ESP Colors", allowAlpha = false)
        public Color espColorUnstable = new Color(178, 18, 227);
        @Property(type = PropertyType.COLOR, name = "Young Dragon Adventurer Color", description = "Default #DDE4F0.",
                category = "ESP", subcategory = "Dungeon ESP Colors", allowAlpha = false)
        public Color espColorYoung = new Color(221, 228, 240);
        @Property(type = PropertyType.COLOR, name = "Superior Dragon Adventurer Color", description = "Default #F2DF11.",
                category = "ESP", subcategory = "Dungeon ESP Colors", allowAlpha = false)
        public Color espColorSuperior = new Color(242, 223, 17);
        @Property(type = PropertyType.COLOR, name = "Holy Dragon Adventurer Color", description = "Default #47D147.",
                category = "ESP", subcategory = "Dungeon ESP Colors", allowAlpha = false)
        public Color espColorHoly = new Color(71, 209, 71);
        @Property(type = PropertyType.COLOR, name = "Frozen Dragon Adventurer Color", description = "Default #A0DAEF.",
                category = "ESP", subcategory = "Dungeon ESP Colors", allowAlpha = false)
        public Color espColorFrozen = new Color(160, 218, 239);
        @Property(type = PropertyType.COLOR, name = "Angry Archaeologist Color", description = "Default #5555FF.",
                category = "ESP", subcategory = "Dungeon ESP Colors", allowAlpha = false)
        public Color espColorAngryArchaeologist = new Color(85, 85, 255);

    /*
    Category: GUI
    */

        /*
        Subcategory: Inventory
        */
        @Property(type = PropertyType.COLOR, name = "Star Mobs Color", description = "Default #FFFF00.",
                category = "ESP", subcategory = "Dungeon ESP Colors", allowAlpha = false)
        public Color espColorStarMobs = new Color(255, 255, 0);
        @Property(type = PropertyType.SWITCH, name = "Hide Potion Effects",
                category = "GUI", subcategory = "Inventory")
        public boolean hidePotionEffects = false;
        @Property(type = PropertyType.SWITCH, name = "Highlight Salvageable Items",
                category = "GUI", subcategory = "Inventory")
        public boolean overlaySalvageable = false;
        @Property(type = PropertyType.COLOR, name = "Salvageable Items Color", description = "Default 55FFFFAA.",
                category = "GUI", subcategory = "Inventory")
        public Color overlayColorSalvageable = new Color(85, 255, 255, 170);

    /*
    Category: Macros
    */

        /*
        Subcategory: Bone Macro
        */
        @Property(type = PropertyType.COLOR, name = "Top Quality Salvageable Items Color", description = "Default 6AFF6AAA.",
                category = "GUI", subcategory = "Inventory")
        public Color overlayColorTopSalvageable = new Color(106, 255, 106, 170);

    /*
    Category: Misc
    */

        /*
        Subcategory: Clear Sight
        */
        @Property(type = PropertyType.NUMBER, name = "Throw Delay", description = "Time between each bone throw in ms. Default 50.",
                category = "Macros", subcategory = "Bone Macro", max = 200)
        public int boneThrowDelay = 50;
        @Property(type = PropertyType.SWITCH, name = "No Blindness", description = "Disables blindness.",
                category = "Misc", subcategory = "Clear Sight")
        public boolean antiBlind = false;

        /*
        Subcategory: No Rotate
        */
        @Property(type = PropertyType.SWITCH, name = "No Portal Effect", description = "Disables nether portal overlay.",
                category = "Misc", subcategory = "Clear Sight")
        public boolean antiPortal = false;
        @Property(type = PropertyType.SWITCH, name = "Teleport No Rotate", description = "Prevents your view from being changed while holding any Wither Impact sword, AOTE, or AOTV.",
                category = "Misc", subcategory = "No Rotate")
        public boolean noRotate = false;
        @Property(type = PropertyType.SWITCH, name = "No Rotate Auto Disable", description = "Keep this on unless you want to be banned faster.",
                category = "Misc", subcategory = "No Rotate")
        public boolean noRotateAutoDisable = true;

        /*
        Subcategory: Mining
        */
        @Property(type = PropertyType.SWITCH, name = "No Shield Particle", description = "Gets rid of purple particles and hearts from wither impact.",
                category = "Misc", subcategory = "No Rotate")
        public boolean noShieldParticles = false;
        @Property(type = PropertyType.SWITCH, name = "Gemstone ESP",
                category = "Misc", subcategory = "Mining")
        public boolean gemstoneESP = false;
        @Property(type = PropertyType.NUMBER, name = "Gemstone ESP Radius",
                category = "Misc", subcategory = "Mining", max = 50)
        public int gemstoneESPRadius = 20;

        /*
        Subcategory: QOL
        */
        @Property(type = PropertyType.NUMBER, name = "Gemstone ESP Time", description = "Time in ms between scan cycles.",
                category = "Misc", subcategory = "Mining", increment = 10, max = 5000)
        public int gemstoneESPTime = 250;
        @Property(type = PropertyType.SWITCH, name = "Right Click Ghost Block", description = "Right click with a Stonk to create ghost block.",
                category = "Misc", subcategory = "QOL")
        public boolean stonkGhostBlock = false;

    /*
    Category: Terminals
    */

        /*
        Subcategory: Auto
        */
        @Property(type = PropertyType.SWITCH, name = "Endstone Protector Timer", description = "Timer under your crosshair for when the Endstone Protector will spawn.",
                category = "Misc", subcategory = "QOL")
        public boolean endstoneProtectorTimer = false;
        @Property(type = PropertyType.SWITCH, name = "Auto Terminals", description = "No auto terminals will work with this off.",
                category = "Terminals", subcategory = "Auto")
        public boolean terminalAuto = false;
        @Property(type = PropertyType.NUMBER, name = "Terminal Click Delay", description = "Time in ms between automatic terminal clicks.",
                category = "Terminals", subcategory = "Auto", increment = 10, max = 1000)
        public int terminalClickDelay = 200;
        @Property(type = PropertyType.SWITCH, name = "Separate Auto Terminals", description = "Individual auto terminals control.",
                category = "Terminals", subcategory = "Auto")
        public boolean terminalAutoSeparate = false;
        @Property(type = PropertyType.CHECKBOX, name = "Auto Maze Terminal",
                category = "Terminals", subcategory = "Auto")
        public boolean terminalMaze = true;
        @Property(type = PropertyType.CHECKBOX, name = "Auto Number Terminal",
                category = "Terminals", subcategory = "Auto")
        public boolean terminalNumbers = true;
        @Property(type = PropertyType.CHECKBOX, name = "Auto Correct All Terminal",
                category = "Terminals", subcategory = "Auto")
        public boolean terminalCorrectAll = true;
        @Property(type = PropertyType.CHECKBOX, name = "Auto Starts With Letter Terminal",
                category = "Terminals", subcategory = "Auto")
        public boolean terminalLetter = true;

        /*
        Subcategory: Clicks
        */
        @Property(type = PropertyType.CHECKBOX, name = "Auto Select All Color Terminal",
                category = "Terminals", subcategory = "Auto")
        public boolean terminalColor = true;
        @Property(type = PropertyType.SWITCH, name = "Custom Clicks", description = "Use Skyblock Client's click handling.",
                category = "Terminals", subcategory = "Clicks")
        public boolean terminalCustomClicks = true;
        @Property(type = PropertyType.SWITCH, name = "Pingless Clicks", description = "Sends clicks before terminal GUI is updated.",
                category = "Terminals", subcategory = "Clicks", hidden = true)
        public boolean terminalPingless = false;
        @Property(type = PropertyType.SWITCH, name = "Block Incorrect Clicks",
                category = "Terminals", subcategory = "Clicks")
        public boolean terminalBlockClicks = false;
        @Property(type = PropertyType.SWITCH, name = "Middle Clicks",
                category = "Terminals", subcategory = "Clicks")
        public boolean terminalMiddleClick = false;

        /*
        Subcategory: Highlight
        */
        @Property(type = PropertyType.SWITCH, name = "Hide Tooltips",
                category = "Terminals", subcategory = "Clicks")
        public boolean terminalHideTooltip = false;
        @Property(type = PropertyType.CHECKBOX, name = "Number Terminal",
                category = "Terminals", subcategory = "Highlight")
        public boolean terminalHighlightNumbers = false;
        @Property(type = PropertyType.CHECKBOX, name = "Starts With Letter Terminal",
                category = "Terminals", subcategory = "Highlight")
        public boolean terminalHighlightLetter = false;

        /*
        Subcategory: Highlight Color
        */
        @Property(type = PropertyType.CHECKBOX, name = "Select All Color Terminal",
                category = "Terminals", subcategory = "Highlight")
        public boolean terminalHighlightColor = false;
        @Property(type = PropertyType.COLOR, name = "Terminal Highlight Color", description = "Default #55FF55AA.",
                category = "Terminals", subcategory = "Highlight Color")
        public Color terminalColorHighlight = new Color(85, 255, 85, 170);
        @Property(type = PropertyType.COLOR, name = "Terminal First Number Color", description = "Default #55FFFFFF.",
                category = "Terminals", subcategory = "Highlight Color")
        public Color terminalColorNumberFirst = new Color(85, 255, 255, 255);
        @Property(type = PropertyType.COLOR, name = "Terminal Second Number Color", description = "Default #55FFFFAA.",
                category = "Terminals", subcategory = "Highlight Color")
        public Color terminalColorNumberSecond = new Color(85, 255, 255, 170);

    /*
    Category: Testing
    */
    @Property(type = PropertyType.COLOR, name = "Terminal Third Number Color", description = "Default #55FFFF55.",
            category = "Terminals", subcategory = "Highlight Color")
    public Color terminalColorNumberThird = new Color(85, 255, 255, 85);
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

        setCategoryDescription("ESP",
                "Disable Optifine fast render and Patcher entity culling.");

        Arrays.asList(
                "espColorUnstable",
                "espColorYoung",
                "espColorSuperior",
                "espColorHoly",
                "espColorFrozen",
                "espColorAngryArchaeologist"
        ).forEach(s -> addDependency(s, "espMiniboss"));

        addDependency("overlayColorSalvageable", "overlaySalvageable");
        addDependency("overlayColorTopSalvageable", "overlaySalvageable");

        Arrays.asList(
                "terminalMaze",
                "terminalNumbers",
                "terminalCorrectAll",
                "terminalLetter",
                "terminalColor"
        ).forEach(s -> addDependency(s, "terminalAutoSeparate"));

        Arrays.asList(
                "terminalPingless",
                "terminalBlockClicks",
                "terminalMiddleClick"
        ).forEach(s -> addDependency(s, "terminalCustomClicks"));

    }

}
