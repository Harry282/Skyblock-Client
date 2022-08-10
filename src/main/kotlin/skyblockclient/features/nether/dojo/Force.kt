package skyblockclient.features.nether.dojo

import net.minecraft.entity.monster.EntityZombie
import net.minecraft.init.Items
import net.minecraft.util.StringUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.events.ClickEvent
import skyblockclient.utils.LocationUtils.inSkyblock
import skyblockclient.utils.ScoreboardUtils

object Force {

    @SubscribeEvent
    fun onLeftClick(event: ClickEvent.LeftClickEvent) {
        if (!inSkyblock || !config.dojoForceBlockHits || !isForceDojo) return
        val entity = mc.objectMouseOver?.entityHit ?: return
        if (entity is EntityZombie && entity.getCurrentArmor(3)?.item == Items.leather_helmet) {
            event.isCanceled = true
        }
    }

    private val isForceDojo
        get() = ScoreboardUtils.sidebarLines.any { s ->
            StringUtils.stripControlCodes(ScoreboardUtils.cleanSB(s)) == "Challenge: Force"
        } || config.forceSkyblock
}
