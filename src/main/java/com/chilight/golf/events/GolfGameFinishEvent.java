package com.chilight.golf.events;

import com.chilight.golf.GolfGame;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GolfGameFinishEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter @Setter private final Player player;
    @Getter @Setter private final int par;
    @Getter @Setter private final GolfGame golfGame;

    public GolfGameFinishEvent(Player player, int par, GolfGame golfGame){
        this.par = par;
        this.player = player;
        this.golfGame = golfGame;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


}
