package skyblockclient.mixins;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skyblockclient.features.AntiKBHook;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinPlayerHandler {
    @Inject(method = "handleExplosion", at = @At(value = "RETURN"))
    private void handleExplosion(S27PacketExplosion packet, CallbackInfo ci) {
        AntiKBHook.INSTANCE.handleExplosion(packet);
    }

    @Inject(method = "handleEntityVelocity", at = @At(value = "HEAD"), cancellable = true)
    public void handleEntityVelocity(S12PacketEntityVelocity packet, CallbackInfo ci) {
        if (AntiKBHook.INSTANCE.handleEntityVelocity(packet)) ci.cancel();
    }
}
