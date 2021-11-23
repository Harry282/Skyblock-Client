package skyblockclient.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.SortingBehavior
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
        name = "Block Incorrect Arrow Align Clicks",
        category = "Dungeons",
        subcategory = "F7"
    )
    var arrowAlign = false

    @Property(
        type = PropertyType.SWITCH,
        name = "One Click Arrow Align",
        description = "Complete each item frame in one click.",
        category = "Dungeons",
        subcategory = "F7"
    )
    var autoCompleteArrowAlign = false

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
        name = "F7 Ghost Block",
        description = "Automatically creates ghost blocks to go to P3 from P2 on F7. Might not work with boss messages hidden.",
        category = "Dungeons",
        subcategory = "F7"
    )
    var f7p3Ghost = false

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
        max = 1000
    )
    var terminalClickDelay = 200

    @Property(
        type = PropertyType.SWITCH,
        name = "Separate Auto Terminals",
        description = "Individual auto terminals control.",
        category = "Terminals",
        subcategory = "Auto"
    )
    var terminalAutoSeparate = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Auto Maze Terminal",
        category = "Terminals",
        subcategory = "Auto"
    )
    var terminalMaze = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Auto Number Terminal",
        category = "Terminals",
        subcategory = "Auto"
    )
    var terminalNumbers = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Auto Correct All Terminal",
        category = "Terminals",
        subcategory = "Auto"
    )
    var terminalCorrectAll = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Auto Starts With Letter Terminal",
        category = "Terminals",
        subcategory = "Auto"
    )
    var terminalLetter = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Auto Select All Color Terminal",
        category = "Terminals",
        subcategory = "Auto"
    )
    var terminalColor = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Pingless Clicks",
        description = "Experimental! Sends clicks before terminal GUI is updated.",
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
        name = "Number Terminal",
        category = "Terminals",
        subcategory = "Highlight"
    )
    var terminalHighlightNumbers = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Starts With Letter Terminal",
        category = "Terminals",
        subcategory = "Highlight"
    )
    var terminalHighlightLetter = false

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Select All Color Terminal",
        category = "Terminals",
        subcategory = "Highlight"
    )
    var terminalHighlightColor = false

    @Property(
        type = PropertyType.COLOR,
        name = "Terminal Highlight Color",
        description = "Default #55FF55AA.",
        category = "Terminals",
        subcategory = "Highlight Color"
    )
    var terminalColorHighlight = Color(85, 255, 85, 170)

    @Property(
        type = PropertyType.COLOR,
        name = "Terminal First Number Color",
        description = "Default #55FFFFFF.",
        category = "Terminals",
        subcategory = "Highlight Color"
    )
    var terminalColorNumberFirst = Color(85, 255, 255, 255)

    @Property(
        type = PropertyType.COLOR,
        name = "Terminal Second Number Color",
        description = "Default #55FFFFAA.",
        category = "Terminals",
        subcategory = "Highlight Color"
    )
    var terminalColorNumberSecond = Color(85, 255, 255, 170)

    @Property(
        type = PropertyType.COLOR,
        name = "Terminal Third Number Color",
        description = "Default #55FFFF55.",
        category = "Terminals",
        subcategory = "Highlight Color"
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
        name = "Terminal Click Delay",
        description = "Time in ms between automatic terminal clicks.",
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
        name = "Experiment Highlight",
        description = "Show next clicks for experiments.",
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
        subcategory = "Dungeon ESP",
        options = ["Outline", "Box"]
    )
    var espType = 0

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "OutlineESP Width",
        category = "ESP",
        subcategory = "Dungeon ESP",
        maxF = 10f
    )
    var espOutlineWidth = 1f

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Box Outline Opacity",
        category = "ESP",
        subcategory = "Dungeon ESP",
        maxF = 10f
    )
    var espBoxOutlineOpacity = 0.95f

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Box Opacity",
        category = "ESP",
        subcategory = "Dungeon ESP",
        maxF = 10f
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
        name = "Dungeon Starred Mobs",
        category = "ESP",
        subcategory = "Dungeon ESP"
    )
    var espStarMobs = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Starred Nametags",
        description = "Will likely improve performance",
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
        name = "Teleport No Rotate",
        description = "Most likely bannable! Prevents your view from being changed while holding any Wither Impact sword, AOTE, or AOTV.",
        category = "Misc",
        subcategory = "No Rotate"
    )
    var noRotate = false

    @Property(
        type = PropertyType.SWITCH,
        name = "No Rotate Auto Disable",
        description = "Keep this on unless you want to be banned faster.",
        category = "Misc",
        subcategory = "No Rotate"
    )
    var noRotateAutoDisable = true

    @Property(
        type = PropertyType.SWITCH,
        name = "No Shield Particle",
        description = "Gets rid of purple particles and hearts from wither impact.",
        category = "Misc",
        subcategory = "No Rotate"
    )
    var noShieldParticles = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Gemstone ESP",
        category = "Misc",
        subcategory = "Mining"
    )
    var gemstoneESP = false

    @Property(
        type = PropertyType.NUMBER,
        name = "Gemstone ESP Radius",
        category = "Misc",
        subcategory = "Mining",
        max = 50
    )
    var gemstoneESPRadius = 20

    @Property(
        type = PropertyType.NUMBER,
        name = "Gemstone ESP Time",
        description = "Time in ms between scan cycles.",
        category = "Misc",
        subcategory = "Mining",
        increment = 10,
        max = 5000
    )
    var gemstoneESPTime = 250

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Amber",
        category = "Misc",
        subcategory = "Mining",
    )
    var gemstoneAmber = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Amethyst",
        category = "Misc",
        subcategory = "Mining",
    )
    var gemstoneAmethyst = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Jade",
        category = "Misc",
        subcategory = "Mining",
    )
    var gemstoneJade = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Jasper",
        category = "Misc",
        subcategory = "Mining",
    )
    var gemstoneJasper = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Ruby",
        category = "Misc",
        subcategory = "Mining",
    )
    var gemstoneRuby = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Sapphire",
        category = "Misc",
        subcategory = "Mining",
    )
    var gemstoneSapphire = true

    @Property(
        type = PropertyType.CHECKBOX,
        name = "Topaz",
        category = "Misc",
        subcategory = "Mining",
    )
    var gemstoneTopaz = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Right Click Ghost Block",
        description = "Right click with a Stonk to create ghost block.",
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

    init {
        setCategoryDescription(
            "ESP",
            "Disable Optifine fast render and Patcher entity culling."
        )

        listOf(
            "terminalMaze",
            "terminalNumbers",
            "terminalCorrectAll",
            "terminalLetter",
            "terminalColor"
        ).forEach(Consumer { s: String ->
            addDependency(s, "terminalAutoSeparate")
        })
        listOf(
            "espColorUnstable",
            "espColorYoung",
            "espColorSuperior",
            "espColorHoly",
            "espColorFrozen",
            "espColorAngryArchaeologist"
        ).forEach(Consumer { s: String ->
            addDependency(s, "espMiniboss")
        })
        addDependency("removeStarMobsNametag", "espStarMobs")
        addDependency("overlayColorSalvageable", "overlaySalvageable")
        addDependency("overlayColorTopSalvageable", "overlaySalvageable")
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
        "Dungeons", "Terminals", "Enchanting", "ESP", "ESP Colors", "GUI", "Macros", "Misc", "Dev"
    )
}
