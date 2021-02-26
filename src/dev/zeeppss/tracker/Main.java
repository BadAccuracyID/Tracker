package dev.zeeppss.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dev.zeeppss.tracker.actionbars.ActionBar;
import dev.zeeppss.tracker.actionbars.ActionBarEvent;
import dev.zeeppss.tracker.commands.TrackCMD;
import dev.zeeppss.tracker.menus.GUIMenu;
import dev.zeeppss.tracker.menus.GUIMenu1;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    private static Main plugin;
    public static HashMap<Player, Player> tracks = new HashMap();
    public static String prefix;
    public static String format_messages;
    public static String item_name;
    public static List<String> item_lore = new ArrayList();
    public static boolean gui;
    public static boolean defCompass;

    public Main() {
    }

    public static Main getInstance() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        Track2.everyFiveTicks();
        Track2.everySecond();
        this.getServer().getPluginManager().registerEvents(new GUIMenu(), this);
        this.getServer().getPluginManager().registerEvents(new GUIMenu1(), this);
        this.getCommand("tracker").setExecutor(new GUIMenu1());
        this.getCommand("track").setExecutor(new TrackCMD());
        this.saveDefaultConfig();
        this.reloadConfigFile();
    }

    public void reloadConfigFile() {
        String previous_name = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("track.name"));
        if (item_name != null) {
            previous_name = item_name;
        }

        this.reloadConfig();
        prefix = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix") + " ");
        gui = this.getConfig().getBoolean("gui");
        defCompass = this.getConfig().getBoolean("defaultcompass");
        format_messages = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("formatmsg"));
        item_name = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("track.name"));
        item_lore.clear();

        ItemStack item = getTracker();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(previous_name);
        item.setItemMeta(meta);
    }

    public static ItemStack getTracker() {
        ItemStack tracker = new ItemStack(Material.COMPASS);
        ItemMeta tracker_meta = tracker.getItemMeta();
        tracker_meta.setDisplayName(item_name);
        tracker_meta.setLore(item_lore);
        tracker.setItemMeta(tracker_meta);
        return tracker;
    }

    public static void refreshTracker() {
        Iterator var0 = Bukkit.getOnlinePlayers().iterator();

        while(var0.hasNext()) {
            Player p = (Player)var0.next();
            World world = p.getWorld();
            Player nearest = null;
            double nearest_range = 0.0D;
            Iterator var6 = Bukkit.getOnlinePlayers().iterator();

            while(var6.hasNext()) {
                Player target = (Player)var6.next();
                if (target.getWorld().equals(world) && !target.equals(p)) {
                    double x = Math.abs(p.getLocation().getX() - target.getLocation().getX());
                    double y = Math.abs(p.getLocation().getY() - target.getLocation().getY());
                    double z = Math.abs(p.getLocation().getZ() - target.getLocation().getZ());
                    double range = Math.sqrt(Math.pow(x, 2.0D) + Math.pow(y, 2.0D) + Math.pow(z, 2.0D));

                    if (nearest == null) {
                        nearest = target;
                        nearest_range = range;
                    }

                    if (range < nearest_range) {
                        nearest = target;
                        nearest_range = range;
                    }
                }
            }

            if (nearest == null) {
                tracks.remove(p);
                p.setCompassTarget(p.getLocation());
                return;
            }

            tracks.put(p, nearest);
        }

    }

    public static String getFormat(Player player, Player target, double range) {
        String new_format = format_messages;
        new_format = new_format.replace("{name}", player.getName());
        new_format = new_format.replace("{target}", target.getName());
        new_format = new_format.replace("{health}", String.valueOf((int)getHealth(player, target, target.getMaxHealth())));
        new_format = new_format.replace("{food}", String.valueOf((int)getFood(player, target, target.getFoodLevel())));
        return new_format.replace("{range}", String.valueOf((int)range));
    }

    public static void setBar(final Player player, Player target, double range) {
        ActionBarEvent.using.put(player, true);
        ActionBar bar = new ActionBar();
        bar.setMessage(getFormat(player, target, range)).send(player);
        Bukkit.getScheduler().runTaskLater(getInstance(), new Runnable() {

            public void run() {
                ItemStack tracker = Main.getTracker();
                tracker.setAmount(player.getItemInHand().getAmount());
                if (!player.getItemInHand().equals(tracker)) {
                    ActionBarEvent.using.put(player, false);
                }
            }
        }, 60L);
    }

    public static void checkHand() {
        Iterator var0 = Bukkit.getOnlinePlayers().iterator();

        while(true) {
            while(var0.hasNext()) {
                Player player = (Player)var0.next();
                if (!tracks.containsKey(player)) {
                    return;
                }

                ItemStack tracker = getTracker();
                tracker.setAmount(player.getItemInHand().getAmount());
                if (!player.getItemInHand().equals(tracker) && (!player.getItemInHand().getType().equals(Material.COMPASS) || !defCompass)) {
                    player.setCompassTarget(player.getLocation());
                } else {
                    Player target = (Player)tracks.get(player);
                    player.setCompassTarget(target.getLocation());
                    setBar(player, target, getRange(player.getLocation(), target.getLocation()));
                }
            }
            return;
        }
    }

    public static double getHealth(Player player, Player target, double health) {
        health = target.getHealth();

        if (health == 20) {
        }
        return health;
    }

    public static double getFood(Player player, Player target, double food) {
        food = target.getFoodLevel();

        if (food == 20) {
        }
        return food;
    }

    public static double getRange(Location loc1, Location loc2) {
        double x = Math.abs(loc1.getX() - loc2.getX());
        double y = Math.abs(loc1.getY() - loc2.getY());
        double z = Math.abs(loc1.getZ() - loc2.getZ());
        return Math.sqrt(Math.pow(x, 2.0D) + Math.pow(y, 2.0D) + Math.pow(z, 2.0D));
    }
}

