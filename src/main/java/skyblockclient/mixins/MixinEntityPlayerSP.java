package skyblockclient.mixins;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skyblockclient.utils.ServerRotateUtils;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {
    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    private void onUpdateWalkingPlayerPre(CallbackInfo ci) {
        ServerRotateUtils.INSTANCE.handlePre();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"))
    private void onUpdateWalkingPlayerPost(CallbackInfo ci) {
        ServerRotateUtils.INSTANCE.handlePost();
    }
}
