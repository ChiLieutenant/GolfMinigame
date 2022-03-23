package com.chilight.golf;

import de.leonhard.storage.Json;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CourtHandler {

    static public String getStringLocation(final Location l) {
        if (l == null) {
            return "";
        }
        return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
    }

    static public Location getLocationString(final String s) {
        if (s == null || s.trim() == "") {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 4) {
            final World w = Bukkit.getServer().getWorld(parts[0]);
            final int x = Integer.parseInt(parts[1]);
            final int y = Integer.parseInt(parts[2]);
            final int z = Integer.parseInt(parts[3]);
            return new Location(w, x, y, z);
        }
        return null;
    }


    public static Json courtData(){
        return new Json("courts", "plugins/GolfMinigame");
    }

    public static void createCourt(Player player, String courtname){
        if(!player.hasPermission("golf.admin")){
            player.sendMessage(ChatColor.RED + "You do not have enough permission!");
            return;
        }
        if(courtData().contains(courtname)){
            player.sendMessage(ChatColor.RED + "There is already a court named " + courtname);
            return;
        }
        courtData().set(courtname + ".location", null);
        courtData().set(courtname + ".isAvailable", true);
        player.sendMessage(ChatColor.GREEN + "You have successfully created a court named " + courtname + ". Please type /golf court (courtname) location to set the spawn location of this court.");
    }

    public static void removeCourt(Player player, String courtname){
        if(!player.hasPermission("golf.admin")){
            player.sendMessage(ChatColor.RED + "You do not have enough permission!");
            return;
        }
        if(!courtData().contains(courtname)){
            player.sendMessage(ChatColor.RED + "There is no court named " + courtname);
            return;
        }
        courtData().remove(courtname);
        player.sendMessage(ChatColor.GREEN + "You have successfully removed the court.");
    }

    public static void setLocation(Player player, String courtname){
        if(!player.hasPermission("golf.admin")){
            player.sendMessage(ChatColor.RED + "You do not have enough permission!");
            return;
        }
        if(!courtData().contains(courtname)){
            player.sendMessage(ChatColor.RED + "There is no court named " + courtname);
            return;
        }
        courtData().set(courtname + ".location", getStringLocation(player.getLocation()));
        player.sendMessage(ChatColor.GREEN + "You have successfully set the spawn location of this court.");
    }

    public static Location getCourtLocation(String courtname){
        if(courtData().get(courtname + ".location") == null) return null;
        return getLocationString(courtData().getString(courtname + ".location"));
    }

    public static void setCourtAvailable(String courtname, boolean available){
        courtData().set(courtname + ".isAvailable", available);
    }

    public static boolean isCourtAvailable(String courtname){
        if(getCourtLocation(courtname) == null) return false;
        return courtData().getBoolean(courtname + ".isAvailable");
    }

    public static String getAvailableCourt(){
        for(String court : courtData().getData().keySet()){
            if(isCourtAvailable(court)) return court;
        }
        return null;
    }

}
