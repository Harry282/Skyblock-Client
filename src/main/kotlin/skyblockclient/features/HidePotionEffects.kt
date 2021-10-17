package skyblockclient.features

import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.inSkyblock

fun hidePotionEffects(boolean: Boolean): Boolean {
    return if (config.hidePotionEffects && inSkyblock) false else boolean
}