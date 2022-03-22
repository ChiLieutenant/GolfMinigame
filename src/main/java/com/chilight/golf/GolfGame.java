package com.chilight.golf;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GolfGame {

    @Getter @Setter private PartyHandler party;
    @Getter @Setter private int turn;
    @Getter public static List<GolfGame> games = new ArrayList<>();
    @Getter private boolean isStarted;

    public void GolfGame(PartyHandler party) {
        setParty(party);
        this.turn = 0;
    }

    public boolean isInTurn(Player player){
        return party.getPlayers().get(turn).equals(player);
    }

    public void changeTurn(){
        turn += 1;
        if(turn > party.getPlayers().size()) turn = 0;
        party.getPlayers().get(turn).sendMessage(ChatColor.GREEN + "It is your turn to shoot the ball.");
    }

    public void start() {
        for(Player p : party.getPlayers()) {
            PuttListener.putBall(p);
        }
        games.add(this);
        isStarted = true;
    }

    public void end() {
        games.remove(this);
    }
}
