package me.abb3v.qfundamentals.commands;

import me.abb3v.qfundamentals.QFundamentals;
import me.abb3v.qfundamentals.utils.ConfigManager;
import me.abb3v.qfundamentals.utils.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class QReloadC implements CommandExecutor {
    private QFundamentals plugin;
    private ConfigManager configManager;

    public QReloadC(QFundamentals plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("qfundamentals.reload")) {
            sender.sendMessage("§cYou do not have permission to perform this command.");
            return true;
        }

        if (!configManager.loadConfig()) {
            sender.sendMessage("§cFailed to reload the configuration file.");
            return true;
        }
        sender.sendMessage("§aConfiguration reloaded successfully.");

        LanguageManager.initializeDefaultLocale(configManager.getLocale());
        sender.sendMessage("§aLanguage files reloaded successfully.");

        return true;
    }
}
