package skyblockclient.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import skyblockclient.SkyblockClient;

public class SkyblockClientCommands extends CommandBase {

    @Override
    public String getCommandName() {
        return "sbclient";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            Minecraft.getMinecraft().displayGuiScreen(SkyblockClient.config.gui());
            return;
        }
        EntityPlayerSP player = (EntityPlayerSP) sender;
        String subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "mimicmessage":
                args[0] = "";
                SkyblockClient.config.mimicMessage = String.join(" ", args).trim();
                player.addChatMessage(new ChatComponentText("§aMimic message changed to §f" + String.join(" ", args).trim()));
                break;
            case "swapdelay":
                if (args.length == 2) {
                    try {
                        SkyblockClient.config.boneSwapDelay = Integer.parseInt(args[1]);
                        player.addChatMessage(new ChatComponentText("§aSwap delay changed to " + args[1]));
                    } catch (NumberFormatException e) {
                        player.addChatMessage(new ChatComponentText("§cPlease provide a valid number!"));
                    }
                } else {
                    player.addChatMessage(new ChatComponentText("§cPlease provide a valid number!"));
                }
                break;
        }
    }

}
