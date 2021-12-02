package skyblockclient.forge

import net.minecraftforge.common.ForgeVersion
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.Mixins

@MCVersion(ForgeVersion.mcVersion)
class FMLLoadingPlugin : IFMLLoadingPlugin {

    override fun getASMTransformerClass(): Array<String> {
        return emptyArray()
    }

    override fun getModContainerClass(): String? {
        return null
    }

    override fun getSetupClass(): String? {
        return null
    }

    override fun injectData(data: Map<String, Any>) {}

    override fun getAccessTransformerClass(): String? {
        return null
    }

    init {
        MixinBootstrap.init()
        Mixins.addConfiguration("mixins.skyblockclient.json")
        MixinEnvironment.getCurrentEnvironment().obfuscationContext = "searge"
    }
}