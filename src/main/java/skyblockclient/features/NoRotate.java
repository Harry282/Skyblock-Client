package skyblockclient.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.events.ReceivePacketEvent;
import skyblockclient.utils.SkyblockCheck;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class NoRotate {

    private static final List<String> swordNames = Arrays.asList("Hyperion", "Astraea", "Scylla", "Valkyrie", "Aspect of the End", "Aspect of the Void");

    @SubscribeEvent
    public void receivePacket(ReceivePacketEvent event) throws NoSuchFieldException, IllegalAccessException {
        if (!SkyblockCheck.inSkyblock) return;
        if (event.packet instanceof S08PacketPlayerPosLook) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            ItemStack item = player.getHeldItem();
            if (item == null) return;
            if (SkyblockClient.config.noRotate && swordNames.stream().anyMatch(s -> item.getDisplayName().contains(s))) {
                S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.packet;
                if (SkyblockClient.config.noRotateAutoDisable) {
                    if (packet.getPitch() % 1 == 0 && packet.getYaw() % 1 == 0) return;
                }
                Field yaw = packet.getClass().getDeclaredField("field_148936_d");
                Field pitch = packet.getClass().getDeclaredField("field_148937_e");
                yaw.setAccessible(true);
                pitch.setAccessible(true);
                yaw.setFloat(packet, player.rotationYaw);
                pitch.setFloat(packet, player.rotationPitch);
            }
        }
    }

}
