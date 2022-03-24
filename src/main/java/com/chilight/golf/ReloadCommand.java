package com.chilight.golf;

import org.bukkit.Bukkit;
import org.bukkit.block.data.type.Bed;
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
        // GOLF COURT COMMANDS
        else if(args[0].equalsIgnoreCase("court")) {
            Player player = (Player) sender;
            if(args.length == 3){
                if(args[1].equalsIgnoreCase("create")){
                    CourtHandler.createCourt(player, args[2]);
                }
                if(args[1].equalsIgnoreCase("remove")){
                    CourtHandler.removeCourt(player, args[2]);
                }
                if(args[1].equalsIgnoreCase("location")){
                    CourtHandler.setLocation(player, args[2]);
                }
            }
        }
        // GOLF PARTY COMMANDS
        else if(args[0].equalsIgnoreCase("party")) {
            Player player = (Player) sender;
            if (args.length == 1) return false;
            else if (args[1].equalsIgnoreCase("find")) {
                if(Methods.isPlayerInParty(player)) { player.sendMessage(ChatColor.RED + "You are already in a party!"); return true; }
                if(Main.getAvailableParties().isEmpty()){
                    player.sendMessage(ChatColor.RED + "You have created a new party since there is no available parties at the moment.");
                    PartyHandler party = new PartyHandler();
                    party.setOwner(player);
                    party.create();
                    party.setPublic(true);
                    return false;
                }
                PartyHandler partyHandler = Main.getAvailableParties().get(0);
                partyHandler.addPlayer(player);
            }
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
            else if (args[1].equalsIgnoreCase("public")) {
                if(!Methods.isPlayerInParty(player)) { player.sendMessage(ChatColor.RED + "You are not in a party!"); return true; }
                if(!Methods.getParty(player).isOwner(player)) { player.sendMessage(ChatColor.RED + "You are not the owner of the party!"); return true; }

                Methods.getParty(player).setPublic(true);
                player.sendMessage(ChatColor.GREEN + "You have successfully switched your party status to public.");
                return true;
            }
            else if (args[1].equalsIgnoreCase("private")) {
                if(!Methods.isPlayerInParty(player)) { player.sendMessage(ChatColor.RED + "You are not in a party!"); return true; }
                if(!Methods.getParty(player).isOwner(player)) { player.sendMessage(ChatColor.RED + "You are not the owner of the party!"); return true; }

                Methods.getParty(player).setPublic(false);
                player.sendMessage(ChatColor.GREEN + "You have successfully switched your party status to private.");
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
                        Main.getInvites().remove(handler);
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
                if(CourtHandler.getAvailableCourt() == null) { player.sendMessage(ChatColor.RED + "There is no available court at the moment. Please try again later."); return true;}
                GolfGame game = new GolfGame(Methods.getParty(player));
                game.setCourt(CourtHandler.getAvailableCourt());
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
