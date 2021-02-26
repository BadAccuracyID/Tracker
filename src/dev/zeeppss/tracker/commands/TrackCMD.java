package dev.zeeppss.tracker.commands;

import java.util.Iterator;

import dev.zeeppss.tracker.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TrackCMD implements CommandExecutor {
    public TrackCMD() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            this.printHelp(sender);
            return true;
        } else if (args[0].equals("give")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Main.prefix + ChatColor.RED + "Player only!");
                return true;
            } else {
                Player p = (Player) sender;
                if (args.length == 1) {
                    if (!p.hasPermission("track.give")) {
                        p.sendMessage(Main.prefix + ChatColor.RED + "Permission denied!");
                        return true;
                    } else {
                        p.getInventory().addItem(new ItemStack[]{Main.getTracker()});
                        p.sendMessage(Main.prefix + ChatColor.YELLOW + "Player Tracker has been added to your inventory!");
                        return true;
                    }
                } else if (!p.hasPermission("track.give.other")) {
                    p.sendMessage(Main.prefix + ChatColor.RED + "Permission denied!");
                    return true;
                } else {
                    Iterator var6 = Bukkit.getOnlinePlayers().iterator();

                    Player target;
                    do {
                        if (!var6.hasNext()) {
                            p.sendMessage(Main.prefix + ChatColor.RED + "Player not found!");
                            return true;
                        }

                        target = (Player)var6.next();
                    } while(!target.getName().equals(args[1]));

                    target.getInventory().addItem(new ItemStack[]{Main.getTracker()});
                    target.sendMessage("Player Tracker has been added to your inventory!");
                    p.sendMessage(Main.prefix + ChatColor.YELLOW + "Player Tracker has been added to " + target.getName() + "'s inventory!");
                    return true;
                }
            }
        } else if (args[0].equals("reload")) {
            if (!sender.hasPermission("tracker.reload")) {
                sender.sendMessage(Main.prefix + ChatColor.RED + "Permission denied!");
                return true;
            } else {
                Main.getInstance().reloadConfigFile();
                sender.sendMessage(Main.prefix + ChatColor.GREEN + "Config reloaded!");
                return true;
            }
        } else {
            this.printHelp(sender);
            return true;
        }
    }

    void printHelp(CommandSender sender) {
        sender.sendMessage(Main.prefix + ChatColor.YELLOW + "/track give <player> - Gives a track to your inventory / other player's");
        sender.sendMessage(Main.prefix + ChatColor.YELLOW + "/track reload - Reload configuration files for Player Tracker");
        sender.sendMessage( ChatColor.GRAY + "-- > Challenge From FacedApollo < --");
    }
}
