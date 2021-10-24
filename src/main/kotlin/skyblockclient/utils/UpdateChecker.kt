package skyblockclient.utils

import com.google.gson.JsonParser
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import skyblockclient.SkyblockClient

object UpdateChecker {
    fun hasUpdate(): Int {
        try {
            val client: CloseableHttpClient =
                HttpClients.custom().setUserAgent("SkyblockClient/${SkyblockClient.SB_CLIENT_VERSION}").build()
            val httpGet = HttpGet("https://api.github.com/repos/Harry282/Skyblock-Client/releases")
            val response = EntityUtils.toString(client.execute(httpGet).entity)
            val jsonArray = JsonParser().parse(response).asJsonArray
            val version = jsonArray[0].asJsonObject["tag_name"]
            return DefaultArtifactVersion(version.asString).compareTo(DefaultArtifactVersion(SkyblockClient.SB_CLIENT_VERSION))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }
}
