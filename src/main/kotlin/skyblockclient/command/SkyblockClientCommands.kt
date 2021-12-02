package skyblockclient.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import skyblockclient.SkyblockClient.Companion.config
import skyblockclient.SkyblockClient.Companion.display
import skyblockclient.config.Config.mimicMessage
import skyblockclient.utils.Utils.modMessage

class SkyblockClientCommands : CommandBase() {
    override fun getCommandName(): String {
        return "sbclient"
    }

    override fun getCommandAliases(): List<String> {
        return listOf(
            "skyblockclient",
            "sbc"
        )
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
        val subcommand = args[0].lowercase()
        if (subcommand == "mimicmessage") {
            args[0] = ""
            val message = args.joinToString(" ").trim()
            mimicMessage = message
            modMessage("§aMimic message changed to §f$message")
        }
    }
}