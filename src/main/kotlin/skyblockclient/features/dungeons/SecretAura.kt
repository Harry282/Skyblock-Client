package skyblockclient.features.dungeons

import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntitySkull
import net.minecraft.util.*
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.mc
import skyblockclient.utils.LocationUtils.inDungeons
import skyblockclient.utils.Rotation
import skyblockclient.utils.ServerRotateUtils
import skyblockclient.utils.VecUtils

object SecretAura {
    private val clicked: MutableSet<BlockPos> = mutableSetOf()

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!config.secretAura || !inDungeons || event.phase != TickEvent.Phase.START ) return
        getFirstValidBlock()?.let {
            val axisAlignedBB = mc.theWorld.getBlockState(it).block.getSelectedBoundingBox(mc.theWorld, it)
            val rotation = VecUtils.getRotation(
                mc.thePlayer.getPositionEyes(1f), Vec3(
                    axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0,
                    axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0,
                    axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0
                )
            )
            if (config.secretAuraRotate) {
                ServerRotateUtils.queueRotation(rotation)
            }
            getInteractData(
                it, rotation, if (isWitherEssence(it)) config.witherEssenceRange else config.chestRange
            )?.run {
                ServerRotateUtils.queueAction {
                    clickBlock(blockPos, sideHit, hitVec)
                    if (mc.theWorld.getBlockState(it).block == Blocks.lever) {
                        clickBlock(blockPos, sideHit, hitVec)
                    }
                    clicked.add(it)
                }
            }
        }
    }

    @SubscribeEvent
    fun onUnload(event: WorldEvent.Unload) {
        clicked.clear()
    }

    private fun getFirstValidBlock(): BlockPos? {
        val vec3i = Vec3i(7, 9, 7)
        return BlockPos.getAllInBox(BlockPos(mc.thePlayer).add(vec3i), BlockPos(mc.thePlayer).subtract(vec3i))
            .firstOrNull { isValidBlock(it) && inDistance(it) && it !in clicked }
    }

    fun isValidBlock(blockPos: BlockPos): Boolean = when (mc.theWorld.getBlockState(blockPos).block) {
        Blocks.skull -> isWitherEssence(blockPos)
        Blocks.lever, Blocks.chest, Blocks.trapped_chest -> true
        else -> false
    }

    private fun isWitherEssence(blockPos: BlockPos) =
        (mc.theWorld.getTileEntity(blockPos) as? TileEntitySkull)?.playerProfile?.id?.toString() == "26bb1a8d-7c66-31c6-82d5-a9c04c94fb02"

    private fun inDistance(blockPos: BlockPos): Boolean {
        val isEssence = isWitherEssence(blockPos)
        val dist = blockPos.distanceSq(
            mc.thePlayer.posX,
            mc.thePlayer.posY + if (isEssence) 0.0 else mc.thePlayer.getEyeHeight().toDouble(),
            mc.thePlayer.posZ
        ).toFloat()
        return dist < if (isEssence) config.witherEssenceRange * config.witherEssenceRange else config.chestRange * config.chestRange
    }

    private fun getInteractData(blockPos: BlockPos, rotation: Rotation, dist: Float): MovingObjectPosition? {
        val eyePos = mc.thePlayer.getPositionEyes(1.0f)
        val lookVec = VecUtils.getVecFromRotation(rotation)
        val rayVec = eyePos.addVector(lookVec.xCoord * dist, lookVec.yCoord * dist, lookVec.zCoord * dist)
        return mc.theWorld.getBlockState(blockPos).block.collisionRayTrace(mc.theWorld, blockPos, eyePos, rayVec)
    }

    private fun clickBlock(blockPos: BlockPos, enumFacing: EnumFacing, vec3: Vec3) {
        // Find item for player to hold
        val oldItem = mc.thePlayer.inventory.currentItem
        for (i in 0..8) {
            val item = mc.thePlayer.inventory.getStackInSlot(i) ?: continue
            if (StringUtils.stripControlCodes(item.displayName).contains(config.secretAuraItem)) {
                mc.thePlayer.inventory.currentItem = i
                mc.playerController.onPlayerRightClick(
                    mc.thePlayer, mc.theWorld, mc.thePlayer.heldItem, blockPos, enumFacing, vec3
                )
                mc.thePlayer.inventory.currentItem = oldItem
            }
        }
    }
}
