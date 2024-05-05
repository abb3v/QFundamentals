package me.abb3v.qfundamentals.utils;

import org.bukkit.entity.Player;

public class TeleportRequest {
    private final Player requester;
    private final Player target;
    private TeleportRequestStatus status;

    public TeleportRequest(Player requester, Player target) {
        this.requester = requester;
        this.target = target;
        this.status = TeleportRequestStatus.PENDING;
    }

    public Player getRequester() {
        return requester;
    }

    public Player getTarget() {
        return target;
    }

    public TeleportRequestStatus getStatus() {
        return status;
    }

    public void setStatus(TeleportRequestStatus status) {
        this.status = status;
    }

    enum TeleportRequestStatus {
        PENDING,
        ACCEPTED,
        DENIED,
        EXPIRED
    }
}

