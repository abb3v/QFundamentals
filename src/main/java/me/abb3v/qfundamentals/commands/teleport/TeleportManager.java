package me.abb3v.qfundamentals.commands.teleport;

import me.abb3v.qfundamentals.utils.Scheduler;
import me.abb3v.qfundamentals.utils.TeleportRequest;
import me.abb3v.qfundamentals.utils.LanguageManager;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeleportManager {
    private Map<UUID, TeleportRequest> requests = new ConcurrentHashMap<>();

    public void requestTeleport(Player requester, Player target) {
        UUID targetId = target.getUniqueId();
        requests.put(targetId, new TeleportRequest(requester, target));
        requester.sendMessage(LanguageManager.getMessage("TP.TPA.request_sent"));
        target.sendMessage(LanguageManager.getMessage("TP.TPA.request_received", requester.getName()));

        Scheduler.runLater(() -> expireRequest(requester, target, targetId), 2400); // 120 seconds in ticks
    }

    private void expireRequest(Player requester, Player target, UUID targetId) {
        if (requests.containsKey(targetId)) {
            requests.remove(targetId);
            sendMessage(requester, LanguageManager.getMessage("TP.TPA.request_expired", target.getName()));
        }
    }

    public boolean acceptTeleport(Player target) {
        UUID targetId = target.getUniqueId();
        TeleportRequest request = requests.remove(targetId);

        if (request == null) {
            target.sendMessage(LanguageManager.getMessage("TP.TPACCEPT.no_requests"));
            return false;
        }

        if (!request.getRequester().isOnline() || !target.isOnline()) {
            target.sendMessage(LanguageManager.getMessage("TP.TPACCEPT.teleport_fail"));
            return true;
        }

        performTeleportAsync(request.getRequester(), target);
        return true;
    }

    private void performTeleportAsync(Player requester, Player target) {
        target.teleportAsync(requester.getLocation()).thenAccept(result -> {
            if (result) {
                target.sendMessage(LanguageManager.getMessage("TP.TPACCEPT.teleport_success"));
                requester.sendMessage(LanguageManager.getMessage("TP.TPACCEPT.requester_accepted", target.getName()));
            } else {
                target.sendMessage(LanguageManager.getMessage("TP.TPACCEPT.teleport_unsafe"));
            }
        });
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }
}
