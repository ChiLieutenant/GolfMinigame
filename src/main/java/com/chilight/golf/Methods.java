package com.chilight.golf;

import de.leonhard.storage.Json;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Methods {
    public static String replaceColorCode(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public static boolean isPlayerInParty(Player p){
        boolean b = false;
        for(PartyHandler partyHandler : Main.getParties()){
            if(partyHandler.getPlayers().contains(p)){
                b = true;
                break;
            }
        }
        return b;
    }

    public static PartyHandler getParty(Player p){
        PartyHandler partyHandler = null;
        for(PartyHandler partyHandler1 : Main.getParties()){
            if(partyHandler1.getPlayers().contains(p)){
                partyHandler = partyHandler1;
                break;
            }
        }
        return partyHandler;
    }

    public static void doPartyLeave(Player p){
        PartyHandler partyHandler = getParty(p);
        if(partyHandler == null) return;
        partyHandler.playerLeave(p);
    }

    public static InviteHandler getPartyInvite(Player p, String name){
        Player inviter = Bukkit.getPlayerExact(name);
        if(inviter == null){
            p.sendMessage(Methods.replaceColorCode("&4That player doesn't exists."));
            return null;
        }
        InviteHandler inviteHandler = null;
        for(InviteHandler inviteHandler1 : Main.getInvites()){
            if(inviteHandler1.getInviter().getUniqueId() == inviter.getUniqueId() &&
                    inviteHandler1.getInvited().getUniqueId() == p.getUniqueId() &&
                    inviteHandler1.getType() == InviteHandler.INVITETYPE.PARTY_INVITE){
                inviteHandler = inviteHandler1;
                break;
            }
        }
        return inviteHandler;
    }
    public static String arrayToString(String[] list){
        return String.join(" ", list);
    }

    public static GolfGame getGolfGameFromPlayer(Player player){
        GolfGame golfGame = null;
        for(GolfGame golfGame1 : GolfGame.getGames()){
            if(golfGame1.getParty().getPlayers().contains(player)){
                golfGame = golfGame1;
            }
        }
        return golfGame;
    }

    public static Json getPlayerData(){
        return new Json("players", "plugins/GolfMinigame");
    }

    public static void loadData(Player player){
        getPlayerData().set(player.getUniqueId() + ".par", 0);
    }

    public static int getPar(Player player){
        return getPlayerData().getInt(player.getUniqueId() + ".par");
    }

    public static void setPar(Player player, int par){
        getPlayerData().set(player.getUniqueId() + ".par", par);
    }

    public static void addPar(Player player, int par){
        setPar(player, getPar(player) + par);
    }

    public static Location getRightSide(final Location location, final double distance) {
        final float angle = location.getYaw() / 60;
        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    public static Location getLeftSide(final Location location, final double distance) {
        final float angle = location.getYaw() / 60;
        return location.clone().add(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    public static Location getGolfLocation(Location mainLoc, int count){
        Location loc = null;
        if(count % 2 == 0){
            loc = getLeftSide(mainLoc, count);
        }else{
            loc = getRightSide(mainLoc, count);
        }
        return loc;
    }

    public static boolean isPartyInGame(PartyHandler party){
        for(GolfGame game : GolfGame.getGames()){
            if(game.getParty().equals(party) && game.isStarted()) return true;
        }
        return false;
    }

    public static GolfGame getGameFromParty(PartyHandler party){
        GolfGame golfGame = null;
        for(GolfGame game : GolfGame.getGames()){
            if(game.getParty().equals(party) && game.isStarted()) golfGame = game;
        }
        return golfGame;
    }

}
