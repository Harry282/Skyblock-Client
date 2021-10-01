package skyblockclient.features;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.events.ReceivePacketEvent;
import skyblockclient.utils.SkyblockCheck;

public class WitherImpactParticles {

    @SubscribeEvent
    public void receivePacket(ReceivePacketEvent event) {
        if (!SkyblockCheck.inSkyblock || !SkyblockClient.config.noShieldParticles) return;
        if (event.packet instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles) event.packet;
            if (packet.getParticleType().equals(EnumParticleTypes.SPELL_WITCH) ||
                    packet.getParticleType().equals(EnumParticleTypes.HEART)) {
                EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                Vec3 particlePos = new Vec3(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate());
                if (particlePos.squareDistanceTo(player.getPositionVector()) <= 11 * 11) {
                    event.setCanceled(true);
                }
            }
        }
    }

}
