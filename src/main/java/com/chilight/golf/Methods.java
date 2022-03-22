package com.chilight.golf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
}
