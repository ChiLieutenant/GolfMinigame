package com.chilight.golf;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GolfGame {

    @Getter @Setter private PartyHandler party;
    @Getter @Setter private int turn;
    @Getter public static List<GolfGame> games = new ArrayList<>();
    @Getter private boolean isStarted;
    @Getter private HashMap<Player, Boolean> finished = new HashMap<>();

    public GolfGame(PartyHandler party) {
        setParty(party);
        this.turn = 0;
        this.isStarted = false;
    }

    public List<Player> unFinishedPlayers(){
        List<Player> players = party.getPlayers();
        players.removeIf(pl -> isFinished(pl));
        return players;
    }

    public boolean isFinished(Player player){
        if(finished.containsKey(player)){
            return finished.get(player);
        }
        return false;
    }

    public boolean isAllPlayersFinished(){
        for(Player pl : getParty().getPlayers()) {
            if(!isFinished(pl)) return false;
        }
        return true;
    }

    public void finish(Player player){
        finished.put(player, true);
    }

    public boolean isInTurn(Player player){
        return unFinishedPlayers().get(turn).equals(player);
    }

    public void changeTurn(){
        turn += 1;
        if(turn > unFinishedPlayers().size() - 1) turn = 0;
        unFinishedPlayers().get(turn).sendMessage(ChatColor.GREEN + "It is your turn to shoot the ball.");
    }

    public void start() {
        for(Player p : party.getPlayers()) {
            PuttListener.putBall(p, p.getLocation().add(0, 0.1, 0));
            p.getInventory().addItem(Main.getIron(), Main.getWedge(), Main.getPutter());
        }
        games.add(this);
        isStarted = true;
    }

    public void end() {
        games.remove(this);
    }
}
