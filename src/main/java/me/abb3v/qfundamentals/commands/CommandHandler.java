package me.abb3v.qfundamentals.commands;

import me.abb3v.qfundamentals.QFundamentals;
import me.abb3v.qfundamentals.commands.teleport.TPAC;
import me.abb3v.qfundamentals.commands.teleport.TPAcceptC;
import me.abb3v.qfundamentals.commands.teleport.TeleportManager;
import org.bukkit.command.PluginCommand;

public class CommandHandler {
    private final QFundamentals plugin;
    private TeleportManager teleportManager;

    public CommandHandler(QFundamentals plugin) {
        this.plugin = plugin;
        this.teleportManager = new TeleportManager();
    }

    public void registerCommands() {
        // Register TPA Command
        TPAC tpaCommand = new TPAC(teleportManager);
        PluginCommand tpaPluginCommand = plugin.getCommand("tpa");
        if (tpaPluginCommand != null) {
            tpaPluginCommand.setExecutor(tpaCommand);
            tpaPluginCommand.setTabCompleter(tpaCommand);
        }

        // Register TPAccept Command
        PluginCommand tpacceptPluginCommand = plugin.getCommand("tpaccept");
        if (tpacceptPluginCommand != null) {
            tpacceptPluginCommand.setExecutor(new TPAcceptC(teleportManager));
        }

        // Register QReload Command
        PluginCommand qreloadPluginCommand = plugin.getCommand("qreload");
        if (qreloadPluginCommand != null) {
            qreloadPluginCommand.setExecutor(new QReloadC(plugin, plugin.getConfigManager()));
        }
    }
}
