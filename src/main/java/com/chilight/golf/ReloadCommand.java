package com.chilight.golf;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class ReloadCommand implements CommandExecutor
{
    private final Main plugin = JavaPlugin.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }
        // GOLF PARTY COMMANDS
        else if(args[0].equalsIgnoreCase("party")) {
            Player player = (Player) sender;
            if (args.length == 1) return false;
            else if (args[1].equalsIgnoreCase("create")) {
                if(Methods.isPlayerInParty(player)) { player.sendMessage(ChatColor.RED + "You are already in a party!"); return true; }

                PartyHandler party = new PartyHandler();
                party.setOwner(player);
                party.create();
                return true;
            }
            else if (args[1].equalsIgnoreCase("disband")) {
                if(!Methods.isPlayerInParty(player)) { player.sendMessage(ChatColor.RED + "You are not in a party!"); return true; }
                if(!Methods.getParty(player).isOwner(player)) { player.sendMessage(ChatColor.RED + "You are not the owner of the party!"); return true; }

                Methods.getParty(player).deleteParty();
                return true;
            }
            else if (args[1].equalsIgnoreCase("leave")) {
                if(!Methods.isPlayerInParty(player)) { player.sendMessage(ChatColor.RED + "You are not in a party!"); return true; }

                Methods.getParty(player).removePlayer(player.getName());
                return true;
            }
            else if (args[1].equalsIgnoreCase("invite")) {
                if(args.length == 2) { return false; }
                Player invited = Bukkit.getPlayer(args[2]);
                if(!Methods.isPlayerInParty(player)) { player.sendMessage(ChatColor.RED + "You are not in a party!"); return true; }
                if(Methods.getGolfGameFromPlayer(player) != null) { player.sendMessage(ChatColor.RED + "Your game is already started!"); return true; }
                if(!Methods.getParty(player).isOwner(player)) { player.sendMessage(ChatColor.RED + "You are not the owner of the party!"); return true; }
                if(invited == null) { player.sendMessage(ChatColor.RED + "Given player is not online."); return true; }

                Methods.getParty(player).invitePlayer(invited.getName());
                return true;
            }
            else if (args[1].equalsIgnoreCase("remove")) {
                if(args.length == 2) { return false; }
                Player removed = Bukkit.getPlayer(args[2]);
                if(!Methods.isPlayerInParty(player)) { player.sendMessage(ChatColor.RED + "You are not in a party!"); return true; }
                if(Methods.getGolfGameFromPlayer(player) != null) { player.sendMessage(ChatColor.RED + "Your game is already started!"); return true; }
                if(!Methods.getParty(player).isOwner(player)) { player.sendMessage(ChatColor.RED + "You are not the owner of the party!"); return true; }
                if(removed == null) { player.sendMessage(ChatColor.RED + "Given player is not online."); return true; }

                Methods.getParty(player).removePlayer(removed.getName());
                return true;
            }
            else if(args[1].equalsIgnoreCase("accept")){
                for(InviteHandler handler : Main.getInvites()){
                    if(handler.getInvited().equals(player)){
                        handler.doInvite();
                        return true;
                    }
                }
                player.sendMessage(ChatColor.RED + "You don't have any party request.");
                return true;
            }
            else if (args[1].equalsIgnoreCase("start")) {
                if(!Methods.isPlayerInParty(player)) { player.sendMessage(ChatColor.RED + "You are not in a party!"); return true; }
                if(Methods.getGolfGameFromPlayer(player) != null) { player.sendMessage(ChatColor.RED + "Your game is already started!"); return true; }
                if(!Methods.getParty(player).isOwner(player)) { player.sendMessage(ChatColor.RED + "You are not the owner of the party!"); return true; }

                GolfGame game = new GolfGame(Methods.getParty(player));
                game.start();
                return true;
            }


        }
       // ---------------
        else if(args[0].equalsIgnoreCase("items")) {
            Player player = (Player) sender;
            player.getInventory().addItem(Main.getIron(), Main.getPutter(), Main.getWedge(), Main.getWhistle(), Main.golfBall());
            return true;
        }
        else if (args[0].equals("info"))
        {
            // Send message
            sender.sendMessage(ChatColor.WHITE + "[MiniGolf]" + ChatColor.GRAY + " Version " + plugin.getDescription().getVersion());
            return true;
        }
        else if (args[0].equals("reload"))
        {
            if (!sender.hasPermission("minigolf.reload"))
            {
                // Send message
                sender.sendMessage(ChatColor.WHITE + "[MiniGolf]" + ChatColor.RED + " You do not have permission to run this command.");
                return true;
            }

            // Reload config
            plugin.reload();

            // Send message
            sender.sendMessage(ChatColor.WHITE + "[MiniGolf]" + ChatColor.GRAY + " Config reloaded!");

            return true;
        }
        else
        {
            return false;
        }
        return false;
    }
}
