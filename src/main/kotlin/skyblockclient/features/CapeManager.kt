package skyblockclient.features

import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.ResourceLocation
import skyblockclient.SkyblockClient
import java.io.File

object CapeManager {

    private val capes: MutableMap<String, Cape> = mutableMapOf()

    fun getCapeLocation(playerInfo: NetworkPlayerInfo): ResourceLocation? {
        if (!SkyblockClient.config.showCustomCapes) return null
        val gameProfile = playerInfo.gameProfile
        val cape = capes[gameProfile.id.toString()] ?: return null
        return if (cape.capeLocation.size == 1) cape.capeLocation[0] else {
            cape.capeLocation[(System.currentTimeMillis() / 100 % cape.capeLocation.size).toInt()]
        }
    }

    fun loadCape() {
        if (capes.isNotEmpty()) capes.clear()
        val dir = File(SkyblockClient.mc.mcDataDir, "config/sbclient")
        val cape = if (File(dir, "cape.gif").exists()) {
            Cape(File(dir, "cape.gif").inputStream(), true)
        } else if (File(SkyblockClient.mc.mcDataDir, "config/sbclient/cape.png").exists()) {
            Cape(File(dir, "cape.png").inputStream())
        } else null
        if (cape != null) {
            capes[SkyblockClient.mc.thePlayer.gameProfile.id.toString()] = cape
        }
        SkyblockClient.capesLoaded = true
    }
}
