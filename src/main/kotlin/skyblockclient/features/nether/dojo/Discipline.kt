package skyblockclient.features.nether.dojo

import net.minecraft.entity.monster.EntityZombie
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.util.StringUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.ScoreboardUtils

object Discipline {

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || !inSkyblock || !config.dojoDisciplineAutoSwap || !isDisciplineDojo) return
        val entity = mc.objectMouseOver?.entityHit ?: return
        if (entity is EntityZombie) {
            swapTo(
                when (entity.getCurrentArmor(3)?.item) {
                    Items.leather_helmet -> Items.wooden_sword
                    Items.iron_helmet -> Items.iron_sword
                    Items.golden_helmet -> Items.golden_sword
                    Items.diamond_helmet -> Items.diamond_sword
                    else -> return
                }
            )
        }
    }

    private fun swapTo(item: Item) {
        for (i in 0..8) {
            if (mc.thePlayer.inventory.getStackInSlot(i)?.item == item) {
                mc.thePlayer.inventory.currentItem = i
                return
            }
        }
    }

    private val isDisciplineDojo
        get() = ScoreboardUtils.sidebarLines.any { s ->
            StringUtils.stripControlCodes(ScoreboardUtils.cleanSB(s)) == "Challenge: Discipline"
        } || config.forceSkyblock
}
