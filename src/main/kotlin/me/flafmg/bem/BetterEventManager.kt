package me.flafmg.bem

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

    override fun onEnable() {
        warnLog("Loading Better Event Manager")
        messagesConfig = ConfigManager(this, "messages.yml")
        mainConfig = ConfigManager(this, "config.yml")
        EventManager.initialize(mainConfig)
        BypassManager.initialize(mainConfig)
        infoLog("registering events and commands")
        registerEvents()
        registerCommands()
        successLog("Better Event Manager loaded")
        coolText()
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
        getCommand("build")?.setExecutor(CommandBuilder(EventType.BUILD, messagesConfig, true, true))
        getCommand("chat")?.setExecutor(ChatCommand(messagesConfig, mainConfig))
        getCommand("damage")?.setExecutor(CommandBuilder(EventType.DAMAGE, messagesConfig, true, true))
        getCommand("drop")?.setExecutor(CommandBuilder(EventType.DROP, messagesConfig, true, true))
        getCommand("hcd")?.setExecutor(CommandBuilder(EventType.HCD, messagesConfig, true, true))
        getCommand("hck")?.setExecutor(CommandBuilder(EventType.HCK, messagesConfig, true, true))
        getCommand("pickup")?.setExecutor(CommandBuilder(EventType.PICKUP, messagesConfig, true, true))
        getCommand("pvp")?.setExecutor(CommandBuilder(EventType.PVP, messagesConfig, true, true))
        getCommand("pvm")?.setExecutor(CommandBuilder(EventType.PVM, messagesConfig, true, true))
        getCommand("setspec")?.setExecutor(SetSpecCommand(messagesConfig))
        getCommand("revive")?.setExecutor(ReviveCommand(messagesConfig))
        getCommand("chatclear")?.setExecutor(ChatClearCommand(messagesConfig))
        getCommand("dropclear")?.setExecutor(DropClearCommand(messagesConfig))
        getCommand("smite")?.setExecutor(SmiteCommand(messagesConfig))
        getCommand("glow")?.setExecutor(GlowCommand(messagesConfig))
        getCommand("all")?.setExecutor(AllCommand(messagesConfig))
        getCommand("maxplayers")?.setExecutor(MaxPlayersCommand(messagesConfig))
        getCommand("interact")?.setExecutor(CommandBuilder(EventType.INTERACT, messagesConfig, true, true))
        getCommand("reloadconfig")?.setExecutor(ReloadConfigCommand(mainConfig, messagesConfig))
        getCommand("status")?.setExecutor(StatusCommand(messagesConfig))
        getCommand("removeallbypass")?.setExecutor(RemoveAllBypassCommand(messagesConfig))
    }

    private fun coolText() {
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