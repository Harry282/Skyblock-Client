package skyblockclient.features.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skyblockclient.SkyblockClient;
import skyblockclient.events.RenderLivingEntityEvent;
import skyblockclient.utils.OutlineUtils;
import skyblockclient.utils.RenderUtils;
import skyblockclient.utils.SkyblockCheck;

import java.awt.*;

public class MinibossESP {

    @SubscribeEvent
    public void renderLiving(RenderLivingEntityEvent event) {
        if (!SkyblockCheck.inDungeons || !SkyblockClient.config.espMiniboss || SkyblockClient.config.espType != 0) return;
        if (event.entity instanceof EntityPlayer) {
            if (event.entity.getName().equals("Lost Adventurer")) {
                if (event.entity.getCurrentArmor(0) != null) {
                    switch (event.entity.getCurrentArmor(0).getDisplayName()) {
                        case "§6Unstable Dragon Boots":
                            OutlineUtils.outlineESP(event, new Color(178, 18, 227));
                            return;
                        case "§6Young Dragon Boots":
                            OutlineUtils.outlineESP(event, new Color(221, 228, 240));
                            return;
                        case "§6Superior Dragon Boots":
                            OutlineUtils.outlineESP(event, new Color(242, 223, 17));
                            return;
                        case "§6Holy Dragon Boots":
                            OutlineUtils.outlineESP(event, new Color(71, 209, 71));
                            return;
                        case "§6Frozen Blaze Boots":
                            OutlineUtils.outlineESP(event, new Color(160, 218, 239));
                    }
                }
            } else if (event.entity.getName().equals("Diamond Guy") && event.entity.getCurrentArmor(0) != null &&
                    event.entity.getCurrentArmor(0).getDisplayName().startsWith("§6Perfect Boots - Tier")) {
                OutlineUtils.outlineESP(event, new Color(85, 85, 255));
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!SkyblockCheck.inDungeons || !SkyblockClient.config.espMiniboss || SkyblockClient.config.espType == 0) return;
        for (Entity e : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (e instanceof EntityPlayer) {
                EntityPlayer entity = (EntityPlayer) e;
                if (entity.getName().equals("Lost Adventurer")) {
                    if (entity.getCurrentArmor(0) != null) {
                        switch (entity.getCurrentArmor(0).getDisplayName()) {
                            case "§6Unstable Dragon Boots":
                                RenderUtils.drawEntityBox(entity, new Color(178, 18, 227), SkyblockClient.config.espType == 2, event.partialTicks);
                                return;
                            case "§6Young Dragon Boots":
                                RenderUtils.drawEntityBox(entity, new Color(221, 228, 240), SkyblockClient.config.espType == 2, event.partialTicks);
                                return;
                            case "§6Superior Dragon Boots":
                                RenderUtils.drawEntityBox(entity, new Color(242, 223, 17), SkyblockClient.config.espType == 2, event.partialTicks);
                                return;
                            case "§6Holy Dragon Boots":
                                RenderUtils.drawEntityBox(entity, new Color(71, 209, 71), SkyblockClient.config.espType == 2, event.partialTicks);
                                return;
                            case "§6Frozen Blaze Boots":
                                RenderUtils.drawEntityBox(entity, new Color(160, 218, 239), SkyblockClient.config.espType == 2, event.partialTicks);
                        }
                    }
                } else if (entity.getName().equals("Diamond Guy") && entity.getCurrentArmor(0) != null &&
                        entity.getCurrentArmor(0).getDisplayName().startsWith("§6Perfect Boots - Tier")) {
                    RenderUtils.drawEntityBox(entity, new Color(85, 85, 255), SkyblockClient.config.espType == 2, event.partialTicks);
                }
            }
        }
    }

}
