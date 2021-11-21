package skyblockclient.command

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText
import skyblockclient.SkyblockClient.Companion.CHAT_PREFIX
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.display
import skyblockclient.config.Config.mimicMessage

class SkyblockClientCommands : CommandBase() {
    override fun getCommandName(): String {
        return "sbclient"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "/$commandName"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            display = config.gui()
            return
        }
        val player = sender as EntityPlayerSP
        val subcommand = args[0].lowercase()
        if (subcommand == "mimicmessage") {
            args[0] = ""
            val message = args.joinToString(" ").trim()
            mimicMessage = message
            player.addChatMessage(ChatComponentText("$CHAT_PREFIX §aMimic message changed to §f$message"))
        }
    }
}