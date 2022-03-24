package com.chilight.golf;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GolfGame {

    @Getter @Setter private PartyHandler party;
    @Getter @Setter private int turn;
    @Getter public static List<GolfGame> games = new ArrayList<>();
    @Getter private boolean isStarted;
    @Getter private HashMap<Player, Boolean> finished = new HashMap<>();
    @Getter private List<FastBoard> boards = new ArrayList<>();
    @Getter @Setter private String court;

    public GolfGame(PartyHandler party) {
        setParty(party);
        this.turn = 0;
        this.isStarted = false;
    }

    public List<String> getLine(){
        List<String> line = new ArrayList<>();
        line.add(ChatColor.translateAlternateColorCodes('&', "&r &r &r"));
        line.add(ChatColor.translateAlternateColorCodes('&', "&e&lParty Owner:"));
        line.add(ChatColor.translateAlternateColorCodes('&', "&r &r &e" + this.getParty().getOwner().getName()));
        line.add(ChatColor.translateAlternateColorCodes('&', "&r &r &r"));
        line.add(ChatColor.translateAlternateColorCodes('&', "&e&lScores:"));
        for(Player p : party.getPlayers()){
            line.add(ChatColor.translateAlternateColorCodes('&', "&r &r &e" + p.getName() + ": &7" + Methods.getPar(p) + " Par"));
        }
        return line;
    }

    public void startScoreboard(){
        for(Player p : getParty().getPlayers()){
            FastBoard board = new FastBoard(p);
            board.updateTitle(ChatColor.translateAlternateColorCodes('&', "&2&lGOLF MINIGAME"));
            board.updateLines(getLine());
            boards.add(board);
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!isStarted) {
                    for(FastBoard board : boards){
                        board.delete();
                    }
                    boards.clear();
                    this.cancel();
                    return;
                }
                for(FastBoard board : boards){
                    board.updateLines(getLine());
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);

    }

    public List<Player> unFinishedPlayers(){
        List<Player> players = new ArrayList<>();
        for(Player p : getParty().getPlayers()){
            if(!isFinished(p)) players.add(p);
        }
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
        if(turn > unFinishedPlayers().size() - 1) turn = unFinishedPlayers().size() - 1;
        if(!unFinishedPlayers().contains(player)) return false;
        return unFinishedPlayers().get(turn).equals(player);
    }

    public void changeTurn(){
        turn += 1;
        if(turn > unFinishedPlayers().size() - 1) turn = 0;
        unFinishedPlayers().get(turn).sendMessage(ChatColor.GREEN + "It is your turn to shoot the ball.");
    }

    public void start() {
        int i = 0;
        for(Player p : party.getPlayers()) {
            i++;
            p.teleport(Methods.getGolfLocation(CourtHandler.getCourtLocation(getCourt()), i));
            PuttListener.putBall(p, p.getLocation().add(0, 0.1, 0));
            p.getInventory().addItem(Main.getIron(), Main.getWedge(), Main.getPutter());
            Methods.setPar(p, 0);
        }
        unFinishedPlayers().get(0).sendMessage(ChatColor.GREEN + "It is your turn to shoot the ball.");
        CourtHandler.setCourtAvailable(getCourt(), false);
        startScoreboard();
        games.add(this);
        isStarted = true;
    }

    public void end() {
        Player winner = party.getPlayers().get(0);
        for(Player p : party.getPlayers()) {
            p.teleport(p.getWorld().getSpawnLocation());
            p.getInventory().clear();
            if(Methods.getPar(p) < Methods.getPar(winner)){
                winner = p;
            }
        }
        for(Player p : party.getPlayers()) {
            p.sendMessage(ChatColor.GRAY + winner.getName() + ChatColor.GREEN + " has won the game!");
            Methods.setPar(p, 0);
        }
        CourtHandler.setCourtAvailable(getCourt(), true);
        games.remove(this);
        isStarted = false;
    }
}
