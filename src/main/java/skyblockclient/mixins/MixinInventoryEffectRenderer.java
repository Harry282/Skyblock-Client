package skyblockclient.mixins;

import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import skyblockclient.utils.SkyblockCheck;


@Mixin(InventoryEffectRenderer.class)
public class MixinInventoryEffectRenderer {

    @ModifyVariable(method = "updateActivePotionEffects", at = @At(value = "STORE"))
    public boolean hasVisibleEffect_updateActivePotionEffects(boolean hasVisibleEffect) {
        if (skyblockclient.SkyblockClient.config.hidePotionEffects && SkyblockCheck.inSkyblock) {
            return false;
        } else {
            return hasVisibleEffect;
        }
    }
}
