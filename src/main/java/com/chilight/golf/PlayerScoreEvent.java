package com.chilight.golf;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerScoreEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final int par;

    public PlayerScoreEvent(Player player, int par){
        this.par = par;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPar() {
        return par;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


}
