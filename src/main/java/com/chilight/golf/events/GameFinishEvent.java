package com.chilight.golf.events;

import com.chilight.golf.GolfGame;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameFinishEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter @Setter private final GolfGame golfGame;

    public GameFinishEvent(GolfGame golfGame){
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
