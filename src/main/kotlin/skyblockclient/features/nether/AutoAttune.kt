package skyblockclient.features.nether

import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import skyblockclient.SkyblockClient.Companion.inSkyblock
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.config.Config.autoAttune
import skyblockclient.events.ClickEvent
import skyblockclient.utils.Utils.rightClick

object AutoAttune {

    private val attunements = listOf(
        Attunement("ASHEN", Items.stone_sword, 0),
        Attunement("AURIC", Items.golden_sword, 0),
        Attunement("SPIRIT", Items.iron_sword, 1),
        Attunement("CRYSTAL", Items.diamond_sword, 1)
    )

    private val sword0 = listOf(
        "Firedust Dagger", "Kindlebane Dagger", "Pyrochaos Dagger"
    )

    private val sword1 = listOf(
        "Twilight Dagger", "Mawdredge Dagger", "Deathripper Dagger"
    )

    @SubscribeEvent
    fun onLeftClick(event: ClickEvent.LeftClickEvent) {
        if (!inSkyblock || !autoAttune || mc.objectMouseOver?.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) return
        val attunementArmorStand = mc.theWorld.loadedEntityList.filterIsInstance<EntityArmorStand>().firstOrNull {
            it.getDistanceSqToEntity(mc.thePlayer) < 36 && attunements.any { attunement ->
                it.name.contains(attunement.name)
            }
        } ?: return
        attunements.find {
            attunementArmorStand.name.contains(it.name)
        }?.let { swapTo(it) }
    }

    fun swapTo(attunement: Attunement) {
        val swordNames = if (attunement.type == 0) sword0 else sword1
        for (i in 0..8) {
            val item = mc.thePlayer.inventory.getStackInSlot(i) ?: continue
            if (swordNames.any { item.displayName.contains(it) }) {
                mc.thePlayer.inventory.currentItem = i
                if (item.item != attunement.sword) {
                    rightClick()
                }
            }
        }
    }

    data class Attunement(val name: String, val sword: Item, val type: Int)
}
