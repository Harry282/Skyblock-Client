package skyblockclient.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.SortingBehavior
import java.io.File

object ConfigKT : Vigilant(File("./config/sbclient/config.toml"), "SkyblockClient", sortingBehavior = Sorting) {

    fun init() {
        initialize()
    }

    private val configCategories = listOf(
        "Dungeons", "Terminals", "Gui", "ESP", "ESP Colors", "Dev"
    )

    private object Sorting : SortingBehavior() {
        override fun getCategoryComparator(): Comparator<in Category?> = Comparator { o1, o2 ->
            return@Comparator configCategories.indexOf(o1?.name) - configCategories.indexOf(o2?.name)
        }
    }
}