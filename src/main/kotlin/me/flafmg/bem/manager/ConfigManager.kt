package me.flafmg.bem.manager

import me.flafmg.bem.util.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.InputStreamReader

class ConfigManager(private val plugin: JavaPlugin, private val resourceFileName: String) {
    private val pluginFolder: File = plugin.dataFolder
    private val configFile: File = File(pluginFolder, resourceFileName)
    private val config: FileConfiguration

    init {
        checkAndLoadResource()
        config = YamlConfiguration.loadConfiguration(configFile)
    }

    private fun checkAndLoadResource() {
        val resourceStream = javaClass.classLoader.getResourceAsStream(resourceFileName)
            ?: throw IllegalArgumentException("Resource file '$resourceFileName' not found in resources folder.")

        if (!configFile.exists()) {
            pluginFolder.mkdirs()
            configFile.createNewFile()
            val reader = InputStreamReader(resourceStream)
            val defaultConfig = YamlConfiguration.loadConfiguration(reader)
            defaultConfig.save(configFile)
            successLog("Config file '$resourceFileName' created and loaded successfully.")
        } else {
            infoLog("Config file '$resourceFileName' already exists. Loading...")
        }
    }

    fun <T> get(key: String): T? {
        return if (config.contains(key)) {
            config.get(key) as? T ?: run {
                errLog("Value for key '$key' is not of expected type.")
                null
            }
        } else {
            warnLog("Key '$key' not found in configuration.")
            null
        }
    }

    fun set(key: String, value: Any) {
        config.set(key, value)
    }

    fun getString(key: String): String? = get(key)
    fun getInt(key: String): Int? = get(key)
    fun getBoolean(key: String): Boolean? = get(key)
    fun getDouble(key: String): Double? = get(key)
    fun getList(key: String): List<Any>? = get(key)
    fun getStringList(key: String): List<String>? = get(key)
    fun getIntList(key: String): List<Int>? = get(key)
    fun getDoubleList(key: String): List<Double>? = get(key)

    fun save() {
        try {
            config.save(configFile)
            successLog("Configuration file '$resourceFileName' saved successfully.")
        } catch (e: Exception) {
            errLog("Failed to save configuration file '$resourceFileName': ${e.message}")
        }
    }

    fun reload() {
        try {
            config.load(configFile)
            successLog("Configuration file '$resourceFileName' reloaded successfully.")
        } catch (e: Exception) {
            errLog("Failed to reload configuration file '$resourceFileName': ${e.message}")
        }
    }
}
