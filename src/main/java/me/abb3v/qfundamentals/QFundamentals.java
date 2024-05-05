package me.abb3v.qfundamentals;

import me.abb3v.qfundamentals.commands.CommandHandler;
import me.abb3v.qfundamentals.utils.ConfigManager;
import me.abb3v.qfundamentals.utils.LanguageManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class QFundamentals extends JavaPlugin {
    private CommandHandler commandHandler;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(getLogger());

        getLogger().info("§b┌────────────────────────────────┐");
        getLogger().info("§b│§f    QFundamentals - Version 1.0    §b│");
        getLogger().info("§b│§f   Developed by Abbev   §b│");
        getLogger().info("§b│§f       Loading plugin...       §b│");


        ConfigManager configManager = new ConfigManager(getLogger());

        if (configManager.loadConfig()) {
            getLogger().info("§a│§f  Config Loaded ⚡  §a│");
        } else {
            getLogger().info("§c│§f  Config Failed  §c│");
        }

        Locale locale = configManager.getLocale();
        LanguageManager.initializeDefaultLocale(locale);

        commandHandler = new CommandHandler(this);
        commandHandler.registerCommands();

        getLogger().info("§b│§f  Thanks for using QFundamentals!  §b│");
        getLogger().info("§b└────────────────────────────────┘");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static QFundamentals getInstance() {
        return getPlugin(QFundamentals.class);
    }
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
