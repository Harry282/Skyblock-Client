package skyblockclient.mixins;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skyblockclient.events.RightClickEvent;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "rightClickMouse", at = @At("HEAD"), cancellable = true)
    private void onReceivePacket(CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new RightClickEvent())) ci.cancel();
    }
}
