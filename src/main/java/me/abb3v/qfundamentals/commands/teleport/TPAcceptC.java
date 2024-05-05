package me.abb3v.qfundamentals.commands.teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.abb3v.qfundamentals.utils.LanguageManager;

public class TPAcceptC implements CommandExecutor {
    private final TeleportManager tpManager;

    public TPAcceptC(TeleportManager teleportManager) {
        this.tpManager = teleportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LanguageManager.getMessage("general.player_only"));
            return true;
        }

        Player player = (Player) sender;
        if (!tpManager.acceptTeleport(player)) {
            player.sendMessage(LanguageManager.getMessage("TP.TPACCEPT.no_requests"));
            return true;
        }

        player.sendMessage(LanguageManager.getMessage("TP.TPACCEPT.teleport_accepted"));
        return true;
    }
}
