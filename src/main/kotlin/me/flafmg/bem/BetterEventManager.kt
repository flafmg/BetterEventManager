package me.flafmg.bem

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import me.flafmg.bem.command.*
import me.flafmg.bem.listener.*
import me.flafmg.bem.manager.*
import me.flafmg.bem.util.infoLog
import me.flafmg.bem.util.warnLog
import me.flafmg.bem.util.successLog
import org.bukkit.plugin.java.JavaPlugin

class BetterEventManager : JavaPlugin() {
    lateinit var messagesConfig: ConfigManager
    lateinit var mainConfig: ConfigManager

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(false))
    }

    override fun onEnable() {
        CommandAPI.onEnable()
        warnLog("Loading Better Event Manager")

        messagesConfig = ConfigManager(this, "messages.yml")
        mainConfig = ConfigManager(this, "config.yml")

        EventManager.initialize(mainConfig)
        BypassManager.initialize(mainConfig)
        MaxPlayersManager.initialize(mainConfig)

        infoLog("Registering events and commands")

        registerEvents()
        registerCommands()

        successLog("Better Event Manager loaded")

        DisplayBrand()
    }

    override fun onDisable() {
        warnLog("Disabling Better Event Manager")

        infoLog("Unregistering commands")
        unregisterCommands()

        if (mainConfig.getBoolean("autoSaveEventChanges") == true) {
            mainConfig.save();
        }

        successLog("Better Event Manager disabled, Until next time!")
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(JoinEventListener(messagesConfig, mainConfig), this)
        server.pluginManager.registerEvents(BuildEventListener(messagesConfig), this)
        server.pluginManager.registerEvents(ChatEventListener(messagesConfig, mainConfig), this)
        server.pluginManager.registerEvents(DamageEventListener(messagesConfig), this)
        server.pluginManager.registerEvents(DropEventListener(messagesConfig), this)
        server.pluginManager.registerEvents(HardcoreDeathListener(messagesConfig), this)
        server.pluginManager.registerEvents(HardcoreKickListener(messagesConfig), this)
        server.pluginManager.registerEvents(LogoutEventListener(mainConfig), this)
        server.pluginManager.registerEvents(PickupEventListener(messagesConfig), this)
        server.pluginManager.registerEvents(PvmEventListener(messagesConfig, mainConfig), this)
        server.pluginManager.registerEvents(PvpEventListener(messagesConfig), this)
        server.pluginManager.registerEvents(JoinEventListener(messagesConfig, mainConfig), this)
        server.pluginManager.registerEvents(InteractEventListener(messagesConfig), this)
    }

    private fun registerCommands() {
        AllCommand(messagesConfig).register()
        ChatClearCommand(messagesConfig).register()
        ChatCommand(messagesConfig, mainConfig).register()
        DropClearCommand(messagesConfig).register()
        GlowCommand(messagesConfig).register()
        MaxPlayersCommand(messagesConfig).register()
        ReloadConfigCommand(mainConfig, messagesConfig).register()
        RemoveAllBypassCommand(messagesConfig).register()
        ReviveCommand(messagesConfig).register()
        SetSpecCommand(messagesConfig).register()
        SmiteCommand(messagesConfig).register()
        StatusCommand(messagesConfig).register()
        StickCommand(messagesConfig).register()
        ToggleSpecChatCommand(messagesConfig).register()

        BaseEventCommand.buildCommand(EventType.BUILD, messagesConfig).register()
        BaseEventCommand.buildCommand(EventType.PVP, messagesConfig).register()
        BaseEventCommand.buildCommand(EventType.CHAT, messagesConfig).register()
        BaseEventCommand.buildCommand(EventType.DROP, messagesConfig).register()
        BaseEventCommand.buildCommand(EventType.PICKUP, messagesConfig).register()
        BaseEventCommand.buildCommand(EventType.DAMAGE, messagesConfig).register()
        BaseEventCommand.buildCommand(EventType.INTERACT, messagesConfig).register()
        BaseEventCommand.buildCommand(EventType.PVM, messagesConfig).register()
        BaseEventCommand.buildCommand(EventType.HCK, messagesConfig, aliases = listOf("HardCoreKick")).register()
        BaseEventCommand.buildCommand(EventType.HCD, messagesConfig, aliases = listOf("HardCoreDeath")).register()
    }

    private fun unregisterCommands() {
        AllCommand(messagesConfig).unregister()
        ChatClearCommand(messagesConfig).unregister()
        ChatCommand(messagesConfig, mainConfig).unregister()
        DropClearCommand(messagesConfig).unregister()
        GlowCommand(messagesConfig).unregister()
        MaxPlayersCommand(messagesConfig).unregister()
        ReloadConfigCommand(mainConfig, messagesConfig).unregister()
        RemoveAllBypassCommand(messagesConfig).unregister()
        ReviveCommand(messagesConfig).unregister()
        SetSpecCommand(messagesConfig).unregister()
        SmiteCommand(messagesConfig).unregister()
        StatusCommand(messagesConfig).unregister()
        StickCommand(messagesConfig).unregister()
        ToggleSpecChatCommand(messagesConfig).unregister()

        BaseEventCommand.buildCommand(EventType.BUILD, messagesConfig).unregister()
        BaseEventCommand.buildCommand(EventType.PVP, messagesConfig).unregister()
        BaseEventCommand.buildCommand(EventType.CHAT, messagesConfig).unregister()
        BaseEventCommand.buildCommand(EventType.DROP, messagesConfig).unregister()
        BaseEventCommand.buildCommand(EventType.PICKUP, messagesConfig).unregister()
        BaseEventCommand.buildCommand(EventType.DAMAGE, messagesConfig).unregister()
        BaseEventCommand.buildCommand(EventType.INTERACT, messagesConfig).unregister()
        BaseEventCommand.buildCommand(EventType.PVM, messagesConfig).unregister()
        BaseEventCommand.buildCommand(EventType.HCK, messagesConfig).unregister()
        BaseEventCommand.buildCommand(EventType.HCD, messagesConfig).unregister()
    }

    private fun DisplayBrand() {
        val version = description.version
        val msg: String =   "\u001B[31m  ___ ___ \u001B[33m__  __ \u001B[0m\n" +
                "\u001B[31m | _ ) __|\u001B[33m  \\/  | \u001B[0m\n" +
                "\u001B[31m | _ \\ _|\u001B[33m| |\\/| | \u001B[0m\n" +
                "\u001B[31m |___/___|\u001B[33m_|  |_| \u001B[0m\n" +
                "\u001B[31m Better Event \u001B[33mManager \u001B[0m\n" +
                "\u001B[0m v:\u001B[34m $version  \u001B[0m\n"
        msg.split("\n").forEach { line -> server.logger.info(line) }
    }
}