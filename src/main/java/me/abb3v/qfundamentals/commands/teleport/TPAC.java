package me.abb3v.qfundamentals.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.abb3v.qfundamentals.utils.LanguageManager;

public class TPAC implements CommandExecutor, TabCompleter {
    private final TeleportManager tpManager;

    public TPAC(TeleportManager teleportManager) {
        this.tpManager = teleportManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LanguageManager.getMessage("general.player_only"));
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(LanguageManager.getMessage("TP.TPA.usage"));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (player.equals(target)) {
            player.sendMessage(LanguageManager.getMessage("TP.TPA.cannot_teleport_self"));
            return true;
        }

        tpManager.requestTeleport(player, target);
        return true;
    }
}
