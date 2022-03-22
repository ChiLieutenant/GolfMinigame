package com.chilight.golf.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerHitBallEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    @Getter @Setter private final Entity ball;
    @Getter @Setter private boolean cancelled;

    public PlayerHitBallEvent(Player player, Entity ball){
        this.ball = ball;
        this.player = player;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


}
