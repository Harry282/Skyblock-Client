package skyblockclient.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.SortingBehavior
import skyblockclient.SkyblockClient.Companion.display
import skyblockclient.ui.BlockAnimationBlacklist
import skyblockclient.ui.HideModID
import skyblockclient.ui.ItemMacros
import java.awt.Color
import java.io.File
import java.util.function.Consumer

object Config : Vigilant(File("./config/sbclient/config.toml"), "SkyblockClient", sortingBehavior = Sorting) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Blood Ready Notify",
        description = "Notification when the watcher has finished spawning mobs.",
        category = "Dungeons",
        subcategory = "General"
    )
    var bloodReadyNotify = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Mimic Killed Message",
        description = "Sends a message in party chat when mimic is killed.",
        category = "Dungeons",
        subcategory = "General"
    )
    var mimicKillMessage = false

    @Property(
        type = PropertyType.TEXT,
        name = "Mimic Message",
        description = "Message sent when mimic is detected to be killed. /sbclient mimicmessage to change.",
        category = "Dungeons",
        subcategory = "General"
    )
    var mimicMessage = "Mimic killed!"

    @Property(
        type = PropertyType.SWITCH,
        name = "AFK Thorn Stun",
        description = "Makes right click toggled while holding Tribal Spear or Bonemerang in F4/M4",
        category = "Dungeons",
        subcategory = "General"
    )
    var afkThornStun = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Fast Spirit Leap",
        description = "Left click with spirit leap to leap to last door opener.",
        category = "Dungeons",
        subcategory = "Fast Leap"
    )
    var fastLeap = false

    @Property(
        type = PropertyType.TEXT,
        name = "Priority Leap Target",
        description = "Set a username to take priority over door opener. Ignored if empty.",
        category = "Dungeons",
        subcategory = "Fast Leap"
    )
    var fastLeapTarget = ""

    @Property(
        type = PropertyType.SWITCH,
        name = "Disable After Blood Open",
        category = "Dungeons",
        subcategory = "Fast Leap"
    )
    var fastLeapBloodDisable = false

    @Property(
        type = PropertyType.SELECTOR,
        name = "Arrow Align Solver",
        category = "Dungeons",
        subcategory = "F7",
        description = "Block Clicks: Blocks extra clicks\nOne Click: Right click an item frame to instantly complete it\nAuto: Look at an item frame to instantly complete it",
        options = ["Off", "Block Clicks", "One Click", "Auto"]
    )
    var arrowAlignSolver = 0

    @Property(
        type = PropertyType.SWITCH,
        name = "Arrow Align Sneak Override",
        description = "Hold crouch to override arrow align solver.",
        category = "Dungeons",
        subcategory = "F7"
    )
    var arrowAlignSneakOverride = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Full Size Simon Says Buttons",
        description = "Clicking on the obsidian behind the buttons will also register as a click.",
        category = "Dungeons",
        subcategory = "F7"
    )
    var simonSaysButtons = false

    @Property(
        type = PropertyType.SWITCH,
        name = "F7 Ghost Block",
        description = "Automatically creates ghost blocks to go to P3 from P2 on F7.",
        category = "Dungeons",
        subcategory = "F7"
    )
    var f7p3Ghost = false

    @Property(
        type = PropertyType.SWITCH,
        name = "M7 Ghost Block",
        description = "Automatically creates ghost blocks to go to P5 from P4 on M7.",
        category = "Dungeons",
        subcategory = "F7"
    )
    var m7p5Ghost = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Livid Finder",
        category = "Dungeons",
        subcategory = "Render"
    )
    var lividFinder = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Fels",
        category = "Dungeons",
        subcategory = "Render"
    )
    var showFels = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Shadow Assassins",
        category = "Dungeons",
        subcategory = "Render"
    )
    var showShadowAssassin = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Stealthy Mobs",
        category = "Dungeons",
        subcategory = "Render"
    )
    var showStealthy = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Auto Terminals",
        description = "No auto terminals will work with this off.",
        category = "Terminals",
        subcategory = "Auto"
    )
    var terminalAuto = false

    @Property(
        type = PropertyType.NUMBER,
        name = "Terminal Click Delay",
        description = "Time in ms between automatic terminal clicks.",
        category = "Terminals",
        subcategory = "Auto",
        increment = 10,
        min = 10,
        max = 1000
    )
    var terminalClickDelay = 200

    @Property(
        type = PropertyType.SWITCH,
        name = "Pingless Clicks",
        description = "Sends clicks before terminal GUI is updated. Not recommended on lower ping.",
        category = "Terminals",
        subcategory = "Clicks"
    )
    var terminalPingless = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Block Incorrect Clicks",
        category = "Terminals",
        subcategory = "Clicks"
    )
    var terminalBlockClicks = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Middle Clicks",
        category = "Terminals",
        subcategory = "Clicks"
    )
    var terminalMiddleClick = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Hide Tooltips",
        category = "Terminals",
        subcategory = "Clicks"
    )
    var terminalHideTooltip = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Terminal Highlight",
        description = "Highlight needed clicks in terminals",
        category = "Terminals",
        subcategory = "Highlight"
    )
    var terminalHighlight = false

    @Property(
        type = PropertyType.COLOR,
        name = "Terminal Highlight Color",
        description = "Default #55FF55AA.",
        category = "Terminals",
        subcategory = "Highlight"
    )
    var terminalColorHighlight = Color(85, 255, 85, 170)

    @Property(
        type = PropertyType.COLOR,
        name = "Terminal First Number Color",
        description = "Default #55FFFFFF.",
        category = "Terminals",
        subcategory = "Highlight"
    )
    var terminalColorNumberFirst = Color(85, 255, 255, 255)

    @Property(
        type = PropertyType.COLOR,
        name = "Terminal Second Number Color",
        description = "Default #55FFFFAA.",
        category = "Terminals",
        subcategory = "Highlight"
    )
    var terminalColorNumberSecond = Color(85, 255, 255, 170)

    @Property(
        type = PropertyType.COLOR,
        name = "Terminal Third Number Color",
        description = "Default #55FFFF55.",
        category = "Terminals",
        subcategory = "Highlight"
    )
    var terminalColorNumberThird = Color(85, 255, 255, 85)

    @Property(
        type = PropertyType.SWITCH,
        name = "Auto Experiments",
        description = "Automatically click Chronomatron and Ultrasequencer experiments.",
        category = "Experiment",
        subcategory = "Auto"
    )
    var experimentAuto = false

    @Property(
        type = PropertyType.NUMBER,
        name = "Experiment Click Delay",
        description = "Time in ms between automatic experiment clicks.",
        category = "Experiment",
        subcategory = "Auto",
        increment = 10,
        max = 1000
    )
    var experimentClickDelay = 200

    @Property(
        type = PropertyType.SWITCH,
        name = "Auto Exit Experiment",
        description = "Closes experiment at +3 superpair clicks.",
        category = "Experiment",
        subcategory = "Auto"
    )
    var experimentAutoExit = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Block Incorrect Clicks",
        category = "Experiment",
        subcategory = "Clicks"
    )
    var experimentBlockClicks = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Middle Clicks",
        category = "Experiment",
        subcategory = "Clicks"
    )
    var experimentMiddleClick = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Hide Tooltips",
        category = "Experiment",
        subcategory = "Highlight"
    )
    var experimentHideTooltips = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Experiment Highlight",
        description = "Highlights next clicks for experiments.",
        category = "Experiment",
        subcategory = "Highlight"
    )
    var experimentHighlight = false

    @Property(
        type = PropertyType.COLOR,
        name = "Experiment First Click Color",
        description = "Default #55FFFFFF.",
        category = "Experiment",
        subcategory = "Highlight"
    )
    var experimentColorNumberFirst = Color(85, 255, 255, 255)

    @Property(
        type = PropertyType.COLOR,
        name = "Experiment Second Click Color",
        description = "Default #55FFFFAA.",
        category = "Experiment",
        subcategory = "Highlight"
    )
    var experimentColorNumberSecond = Color(85, 255, 255, 170)

    @Property(
        type = PropertyType.COLOR,
        name = "Experiment Third Click Color",
        description = "Default #55FFFF55.",
        category = "Experiment",
        subcategory = "Highlight"
    )
    var experimentColorNumberThird = Color(85, 255, 255, 85)

    @Property(
        type = PropertyType.SELECTOR,
        name = "ESP Type",
        category = "ESP",
        options = ["Outline", "Box"]
    )
    var espType = 0

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "OutlineESP Width",
        category = "ESP",
        maxF = 10f
    )
    var espOutlineWidth = 1f

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Box Outline Opacity",
        category = "ESP",
    )
    var espBoxOutlineOpacity = 0.95f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Box Outline Width",
        category = "ESP",
        maxF = 10f
    )
    var espBoxOutlineWidth = 1f

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Box Opacity",
        category = "ESP",
    )
    var espBoxOpacity = 0.3f

    @Property(
        type = PropertyType.SWITCH,
        name = "ESP Bats",
        category = "ESP",
        subcategory = "Dungeon ESP"
    )
    var espBats = false

    @Property(
        type = PropertyType.SWITCH,
        name = "ESP Fels",
        category = "ESP",
        subcategory = "Dungeon ESP"
    )
    var espFels = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Shadow Assassins",
        category = "ESP",
        subcategory = "Dungeon ESP"
    )
    var espShadowAssassin = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Dungeon Minibosses",
        category = "ESP",
        subcategory = "Dungeon ESP"
    )
    var espMiniboss = false


    @Property(
        type = PropertyType.SWITCH,
        name = "Seperate Miniboss Colors",
        category = "ESP",
        subcategory = "Dungeon ESP"
    )
    var espSeperateMinibossColor = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Dungeon Starred Mobs",
        category = "ESP",
        subcategory = "Dungeon ESP"
    )
    var espStarMobs = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Starred Nametags",
        category = "ESP",
        subcategory = "Dungeon ESP"
    )
    var removeStarMobsNametag = false

    @Property(
        type = PropertyType.COLOR,
        name = "Livid Color",
        description = "Default #55FFFF.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorLivid = Color(85, 255, 255)

    @Property(
        type = PropertyType.COLOR,
        name = "Bat Color",
        description = "Default #2FEE2F.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorBats = Color(47, 238, 47)

    @Property(
        type = PropertyType.COLOR,
        name = "Fel Color",
        description = "Default #CB59FF.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorFels = Color(203, 89, 255)

    @Property(
        type = PropertyType.COLOR,
        name = "Shadow Assassin Color",
        description = "Default #AA00AA.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorShadowAssassin = Color(170, 0, 170)

    @Property(
        type = PropertyType.COLOR,
        name = "Miniboss Color",
        description = "Used for all minibosses except Shadow Assassins if seperate miniboss colors is off. Default #D70000.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorMiniboss = Color(215, 0, 0)

    @Property(
        type = PropertyType.COLOR,
        name = "Unstable Dragon Adventurer Color",
        description = "Default #B212E3.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorUnstable = Color(178, 18, 227)

    @Property(
        type = PropertyType.COLOR,
        name = "Young Dragon Adventurer Color",
        description = "Default #DDE4F0.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorYoung = Color(221, 228, 240)

    @Property(
        type = PropertyType.COLOR,
        name = "Superior Dragon Adventurer Color",
        description = "Default #F2DF11.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorSuperior = Color(242, 223, 17)

    @Property(
        type = PropertyType.COLOR,
        name = "Holy Dragon Adventurer Color",
        description = "Default #47D147.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorHoly = Color(71, 209, 71)

    @Property(
        type = PropertyType.COLOR,
        name = "Frozen Dragon Adventurer Color",
        description = "Default #A0DAEF.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorFrozen = Color(160, 218, 239)

    @Property(
        type = PropertyType.COLOR,
        name = "Angry Archaeologist Color",
        description = "Default #5555FF.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorAngryArchaeologist = Color(85, 85, 255)

    @Property(
        type = PropertyType.COLOR,
        name = "Star Mobs Color",
        description = "Default #FFFF00.",
        category = "ESP Colors",
        subcategory = "Dungeon ESP Colors",
        allowAlpha = false
    )
    var espColorStarMobs = Color(255, 255, 0)

    @Property(
        type = PropertyType.SWITCH,
        name = "Hide Potion Effects",
        category = "GUI",
        subcategory = "Inventory"
    )
    var hidePotionEffects = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Highlight Salvageable Items",
        category = "GUI",
        subcategory = "Inventory"
    )
    var overlaySalvageable = false

    @Property(
        type = PropertyType.COLOR,
        name = "Salvageable Items Color",
        description = "Default 55FFFFAA.",
        category = "GUI",
        subcategory = "Inventory"
    )
    var overlayColorSalvageable = Color(85, 255, 255, 170)

    @Property(
        type = PropertyType.COLOR,
        name = "Top Quality Salvageable Items Color",
        description = "Default 6AFF6AAA.",
        category = "GUI",
        subcategory = "Inventory"
    )
    var overlayColorTopSalvageable = Color(106, 255, 106, 170)

    @Property(
        type = PropertyType.BUTTON,
        name = "Item Macros",
        category = "Macros",
        placeholder = "Edit"
    )
    fun openItemMacros() {
        display = ItemMacros()
    }

    @Property(
        type = PropertyType.NUMBER,
        name = "Throw Delay",
        description = "Time between each bone throw in ms. Default 50.",
        category = "Macros",
        subcategory = "Bone Macro",
        max = 200,
        increment = 5
    )
    var boneThrowDelay = 50

    @Property(
        type = PropertyType.SWITCH,
        name = "Book Combine",
        description = "Button to combine tier 1 books in anvil.",
        category = "Macros",
        subcategory = "Book Anvil Combine"
    )
    var bookCombine = false

    @Property(
        type = PropertyType.NUMBER,
        name = "Book Combine Speed",
        description = "Minimum time between clicks. Increase if you get please slow down messages.",
        category = "Macros",
        subcategory = "Book Anvil Combine",
        max = 200,
        increment = 5
    )
    var bookCombineSpeed = 50

    @Property(
        type = PropertyType.SWITCH,
        name = "Book Combine Messages",
        description = "Shows chat messages when starting and stopping book combining.",
        category = "Macros",
        subcategory = "Book Anvil Combine"
    )
    var bookCombineMessage = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Rune Combine",
        description = "Button to combine runes at Rune Pedestal.",
        category = "Macros",
        subcategory = "Book Anvil Combine"
    )
    var runeCombine = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Skyblock Anti KB",
        category = "Misc",
    )
    var antiKBSkyblock = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Skyblock No Rotate",
        category = "Misc",
    )
    var noRotate = false

    @Property(
        type = PropertyType.BUTTON,
        name = "Mod ID Hider",
        category = "Misc",
        placeholder = "Edit"
    )
    fun openHideModID() {
        display = HideModID()
    }

    @Property(
        type = PropertyType.SWITCH,
        name = "No Blindness",
        description = "Disables blindness.",
        category = "Misc",
        subcategory = "Clear Sight"
    )
    var antiBlind = false

    @Property(
        type = PropertyType.SWITCH,
        name = "No Portal Effect",
        description = "Disables nether portal overlay.",
        category = "Misc",
        subcategory = "Clear Sight"
    )
    var antiPortal = false

    @Property(
        type = PropertyType.SWITCH,
        name = "No Water FOV",
        description = "Disables FOV change in water.",
        category = "Misc",
        subcategory = "Clear Sight"
    )
    var antiWaterFOV = false

    @Property(
        type = PropertyType.SWITCH,
        name = "No Shield Particle",
        description = "Gets rid of purple particles and hearts from wither impact.",
        category = "Misc",
        subcategory = "Clear Sight"
    )
    var noShieldParticles = false

    @Property(
        type = PropertyType.SWITCH,
        name = "No Block Animation",
        description = "Disable block animation on all swords with right click ability.",
        category = "Misc"
    )
    var noBlockAnimation = false

    @Property(
        type = PropertyType.BUTTON,
        name = "No Block Animation Blacklist",
        category = "Misc",
        placeholder = "Edit"
    )
    fun openBlockAnimationBlacklist() {
        display = BlockAnimationBlacklist()
    }

    @Property(
        type = PropertyType.SWITCH,
        name = "Gemstone ESP",
        category = "Mining",
        subcategory = "Gemstone ESP"
    )
    var gemstoneESP = false

    @Property(
        type = PropertyType.NUMBER,
        name = "Gemstone ESP Radius",
        category = "Mining",
        subcategory = "Gemstone ESP",
        max = 50
    )
    var gemstoneESPRadius = 20

    @Property(
        type = PropertyType.NUMBER,
        name = "Gemstone ESP Time",
        description = "Time in ms between scan cycles.",
        category = "Mining",
        subcategory = "Gemstone ESP",
        increment = 10,
        max = 5000
    )
    var gemstoneESPTime = 250

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Amber",
        category = "Mining",
        subcategory = "Gemstone ESP",
    )
    var gemstoneAmber = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Amethyst",
        category = "Mining",
        subcategory = "Gemstone ESP",
    )
    var gemstoneAmethyst = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Jade",
        category = "Mining",
        subcategory = "Gemstone ESP",
    )
    var gemstoneJade = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Jasper",
        category = "Mining",
        subcategory = "Gemstone ESP",
    )
    var gemstoneJasper = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Ruby",
        category = "Mining",
        subcategory = "Gemstone ESP",
    )
    var gemstoneRuby = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Sapphire",
        category = "Mining",
        subcategory = "Gemstone ESP",
    )
    var gemstoneSapphire = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Topaz",
        category = "Mining",
        subcategory = "Gemstone ESP",
    )
    var gemstoneTopaz = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Worm fishing ESP",
        description = "Detects lava in precursor remnants above Y=64.\n\nMade by TheStachelfisch",
        category = "Mining",
        subcategory = "Lava ESP"
    )
    var wormFishingLavaESP = false

    @Property(
        type = PropertyType.NUMBER,
        name = "Worm fishing ESP Radius",
        category = "Mining",
        subcategory = "Lava ESP",
        max = 150
    )
    var wormFishingLavaESPRadius = 50

    @Property(
        type = PropertyType.NUMBER,
        name = "Worm fishing ESP Time",
        description = "Time in ms between scan cycles.",
        category = "Mining",
        subcategory = "Lava ESP",
        increment = 10,
        max = 5000
    )
    var wormFishingLavaESPTime = 750

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Hide ESP near lava",
        description = "Hide ESP when near lava.",
        category = "Mining",
        subcategory = "Lava ESP",
    )
    var wormFishingLavaHideNear = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Turn off ESP after fishing up worm",
        description = "Only turns it off for current session.",
        category = "Mining",
        subcategory = "Lava ESP",
    )
    var wormFishingHideFished = true

    @Property(
        type = PropertyType.SWITCH,
        name = "[Force] Block Negative Hits",
        category = "Nether",
        subcategory = "Dojo"
    )
    var dojoForceBlockHits = false

    @Property(
        type = PropertyType.SWITCH,
        name = "[Discipline] Auto Swap Sword",
        category = "Nether",
        subcategory = "Dojo"
    )
    var dojoDisciplineAutoSwap = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Auto Blaze Attune",
        category = "Nether",
        subcategory = "Slayer"
    )
    var autoAttune = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Fire Pillar",
        category = "Nether",
        subcategory = "Slayer"
    )
    var showFirePillar = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Right Click Ghost Block",
        description = "Right click with any pickaxe to create ghost block.",
        category = "Misc",
        subcategory = "QOL"
    )
    var stonkGhostBlock = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Endstone Protector Timer",
        description = "Timer under your crosshair for when the Endstone Protector will spawn.",
        category = "Misc",
        subcategory = "QOL"
    )
    var endstoneProtectorTimer = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Anvil Uses",
        description = "Show NBT anvil uses of the skyblock items.",
        category = "Dev"
    )
    var showAnvilUses = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Force Skyblock",
        description = "Forces all features to enable, even if you are not on skyblock.",
        category = "Dev"
    )
    var forceSkyblock = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Terminal Info",
        category = "Dev"
    )
    var showTerminalInfo = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Custom Cape",
        category = "Dev"
    )
    var showCustomCapes = true

    init {
        setCategoryDescription(
            "ESP",
            "Disable Optifine fast render and Patcher entity culling."
        )
        listOf(
            "experimentColorNumberFirst",
            "experimentColorNumberSecond",
            "experimentColorNumberThird"
        ).forEach(Consumer { s: String ->
            addDependency(s, "experimentHighlight")
        })
        listOf(
            "terminalColorHighlight",
            "terminalColorNumberFirst",
            "terminalColorNumberSecond",
            "terminalColorNumberThird"
        ).forEach(Consumer { s: String ->
            addDependency(s, "terminalHighlight")
        })
        addDependency("mimicMessage", "mimicKillMessage")
        addDependency("espColorLivid", "lividFinder")
        addDependency("espColorBats", "espBats")
        addDependency("espColorFels", "espFels")
        addDependency("espColorShadowAssassin", "espShadowAssassin")
        listOf(
            "espColorUnstable",
            "espColorYoung",
            "espColorSuperior",
            "espColorHoly",
            "espColorFrozen",
            "espColorAngryArchaeologist"
        ).forEach(Consumer { s: String ->
            addDependency(s, "espSeperateMinibossColor")
        })
        listOf(
            "espColorStarMobs",
            "removeStarMobsNametag"
        ).forEach(Consumer { s: String ->
            addDependency(s, "espStarMobs")
        })
        addDependency("removeStarMobsNametag", "espStarMobs")
        listOf(
            "overlayColorSalvageable",
            "overlayColorTopSalvageable"
        ).forEach(Consumer { s: String ->
            addDependency(s, "overlaySalvageable")
        })
        listOf(
            "gemstoneESPRadius",
            "gemstoneESPTime",
            "gemstoneAmber",
            "gemstoneAmethyst",
            "gemstoneJade",
            "gemstoneJasper",
            "gemstoneRuby",
            "gemstoneSapphire",
            "gemstoneTopaz"
        ).forEach(Consumer { s: String ->
            addDependency(s, "gemstoneESP")
        })
        listOf(
            "wormFishingLavaESPRadius",
            "wormFishingLavaESPTime",
            "wormFishingLavaHideNear",
            "wormFishingHideFished"
        ).forEach(Consumer { s: String ->
            addDependency(s, "wormFishingLavaESP")
        })
    }

    fun init() {
        initialize()
    }

    private object Sorting : SortingBehavior() {
        override fun getCategoryComparator(): Comparator<in Category> = Comparator.comparingInt { o: Category ->
            configCategories.indexOf(o.name)
        }
    }

    private val configCategories = listOf(
        "Dungeons", "Terminals", "Experiment", "ESP", "ESP Colors", "GUI", "Macros", "Mining", "Nether", "Misc", "Dev"
    )
}
