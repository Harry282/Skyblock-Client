package skyblockclient.commands;

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
            SkyblockClient.display = SkyblockClient.config.gui();
            return;
        }
        EntityPlayerSP player = (EntityPlayerSP) sender;
        String subcommand = args[0].toLowerCase();
        if (subcommand.equals("mimicmessage")) {
            args[0] = "";
            SkyblockClient.config.mimicMessage = String.join(" ", args).trim();
            player.addChatMessage(new ChatComponentText("§aMimic message changed to §f" + String.join(" ", args).trim()));
        }
    }

}
