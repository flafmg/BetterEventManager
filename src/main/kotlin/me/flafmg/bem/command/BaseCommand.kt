package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.BooleanArgument
import dev.jorel.commandapi.CommandAPI.unregister
import dev.jorel.commandapi.executors.CommandArguments
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.infoLog

open class BaseCommand(
    protected val commandName: String,
    protected val messagesConfig: ConfigManager,
    aliases: List<String> = emptyList(),
    val hasSilent: Boolean = true,
) {
    protected val baseCommand: CommandAPICommand = CommandAPICommand(commandName)
        .withAliases(*aliases.toTypedArray())
        .withPermission("bettereventmanager.command.$commandName")

    open fun register() {
        infoLog(" Registering command: $commandName")
        if(hasSilent) {
            baseCommand.withArguments(BooleanArgument("silent").setOptional(true))
        }
        baseCommand.register()
    }

    open fun unregister() {
        infoLog(" Unregistering command: $commandName")
        unregister(commandName)
    }

    protected fun isSilent(args: CommandArguments): Boolean {
        return args.get("silent") as? Boolean ?: false
    }
}