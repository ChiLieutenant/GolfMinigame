package com.chilight.golf;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InviteHandler {
    @Getter @Setter private PartyHandler partyHandler;
    @Getter @Setter private INVITETYPE type;
    @Getter @Setter private Player invited;
    @Getter @Setter private Player inviter;

    public InviteHandler(){
        InviteHandler handler = this;
        new BukkitRunnable(){
            @Override
            public void run() {
                if(Main.getInvites().contains(handler)){
                    invited.sendMessage(Methods.replaceColorCode("&4You didn't respond &7" + inviter.getName() + "&4's invite."));
                    inviter.sendMessage(Methods.replaceColorCode("&7" + invited.getName() + "&4 didn't respond your invite."));
                    Main.getInvites().remove(handler);
                } else{
                    cancel();
                }
            }
        }.runTaskLater(Main.getInstance(), 20*30);
    }

    public void doInvite(){
        if(type == INVITETYPE.PARTY_INVITE){
            partyHandler.addPlayer(invited);
        }
    }

    public enum INVITETYPE{
        PARTY_INVITE
    }
}