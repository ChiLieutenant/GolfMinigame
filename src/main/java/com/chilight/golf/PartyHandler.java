package com.chilight.golf;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PartyHandler {
    @Getter private ArrayList<Player> players = new ArrayList<>();
    @Getter @Setter private Player owner;

    public void invitePlayer(String playername){
        Player invited = Bukkit.getPlayerExact(playername);
        if(invited == null){
            owner.sendMessage(Methods.replaceColorCode("&4This player doesn't exists."));
            return;
        }
        if(invited.getUniqueId() == owner.getUniqueId()) {
            owner.sendMessage(Methods.replaceColorCode("&4You can't invite yourself."));
            return;
        }
        if (players.contains(invited)) {
            owner.sendMessage(Methods.replaceColorCode("&4This player is already in the party."));
            return;
        }
        if(Methods.isPlayerInParty(invited)){
            owner.sendMessage(Methods.replaceColorCode("&4This player is already in a party."));
            return;
        }

        InviteHandler inviteHandler = new InviteHandler();
        inviteHandler.setInviter(owner);
        inviteHandler.setInvited(invited);
        inviteHandler.setPartyHandler(this);
        inviteHandler.setType(InviteHandler.INVITETYPE.PARTY_INVITE);
        Main.getInvites().add(inviteHandler);

        owner.sendMessage(Methods.replaceColorCode("&aYou successfully invited &7" + invited.getName() + "&a to your party."));
        invited.sendMessage(Methods.replaceColorCode("&aYou got a party invitation from &7" + owner.getName() + "."));
    }

    public void addPlayer(Player invited){
        players.add(invited);
        invited.sendMessage(Methods.replaceColorCode("&aYou accepted &7" + owner.getName() +"&a's party invite."));
        for(Player player : players){
            if(invited != player) player.sendMessage(Methods.replaceColorCode("&7"+ invited.getName() + "&a joined to party."));
        }
        owner.sendMessage(Methods.replaceColorCode("&7" + invited.getName() + "&a accepted your party invite."));
    }

    public void removePlayer(String playername){
        Player invited = Bukkit.getPlayerExact(playername);
        if(invited == null){
            owner.sendMessage(Methods.replaceColorCode("&4This player doesn't exists."));
            return;
        }
        if(invited.getUniqueId() == owner.getUniqueId()) {
            owner.sendMessage(Methods.replaceColorCode("&4You can't kick yourself."));
            return;
        }
        if (!players.contains(invited)) {
            owner.sendMessage(Methods.replaceColorCode("&4This player is already not in the party."));
            return;
        }
        invited.sendMessage(Methods.replaceColorCode("&4You kicked out from party."));
        players.remove(invited);
        for(Player p : players){
            p.sendMessage(Methods.replaceColorCode("&7" + invited.getName() + "&4 removed from the party."));
        }
        owner.sendMessage(Methods.replaceColorCode("&aYou successfully kicked &7" + invited.getName() + "&a from your party."));
    }

    public void playerLeave(Player p){
        players.remove(p);
        if(p.isOnline()) p.sendMessage(Methods.replaceColorCode("&4You are removed from the party."));
        String text = "";
        if(players.size() == 0){
            deleteParty();
            return;
        }
        if(owner.getUniqueId() == p.getUniqueId()){
            text = "&7" + owner.getName() + "&4 removed from the party, new leader is &7" + players.get(0).getName() + "&4.";
        }
        else{
            text = "&7" + owner.getName() + "&4 removed from the party.";
        }
        for(Player player : players){
            player.sendMessage(Methods.replaceColorCode(text));
        }
        setOwner(players.get(0));
    }

    public void deleteParty(){
        Main.getParties().remove(this);
        if(players.size() == 0) return;
        for(Player player : players){
            player.sendMessage(Methods.replaceColorCode("&4You are removed from the party."));
        }
    }

    public boolean isOwner(Player p){
        return owner.getUniqueId() == p.getUniqueId();
    }

    public void chat(Player p, String[] texta){
        String text = Methods.arrayToString(texta);
        for(Player player : players){
            player.sendMessage(Methods.replaceColorCode("&8[&aP&eC&8] &7" +p.getName() + "&r: " + text));
        }
    }

    public void create(){
        players.add(owner);
        Main.getParties().add(this);
        owner.sendMessage(Methods.replaceColorCode("&aYou successfully created a party."));
    }
}
