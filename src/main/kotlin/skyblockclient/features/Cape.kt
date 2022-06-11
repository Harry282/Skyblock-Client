package skyblockclient.features

import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import skyblockclient.SkyblockClient.Companion.mc
import java.io.InputStream
import javax.imageio.ImageIO

class Cape(private val cape: InputStream, animated: Boolean = false) {

    val capeLocation: ArrayList<ResourceLocation> = arrayListOf()

    init {
        try {
            if (animated) {
                ImageIO.getImageReadersByFormatName("gif").next().run {
                    input = ImageIO.createImageInputStream(cape)
                    for (i in 0 until getNumImages(true)) {
                        capeLocation.add(
                            mc.textureManager.getDynamicTextureLocation(
                                "sbclientDevCape$i",
                                DynamicTexture(read(i))
                            )
                        )
                    }
                }
            } else {
                ImageIO.getImageReadersByFormatName("png").next().run {
                    input = ImageIO.createImageInputStream(cape)
                    capeLocation.add(
                        mc.textureManager.getDynamicTextureLocation(
                            "sbclientDevCape",
                            DynamicTexture(read(0))
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
