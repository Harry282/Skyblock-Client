package skyblockclient.mixins;

import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import skyblockclient.SkyblockClient;

@Mixin(InventoryEffectRenderer.class)
public class MixinInventoryEffectRenderer {
    @ModifyVariable(method = "updateActivePotionEffects", at = @At(value = "STORE"))
    public boolean updateActivePotionEffects(boolean hasVisibleEffect) {
        if (SkyblockClient.Companion.getConfig().getHidePotionEffects() && SkyblockClient.Companion.getInSkyblock()) {
            return false;
        } else return hasVisibleEffect;
    }
}
