package dev.zeeppss.tracker.menus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import dev.zeeppss.tracker.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class GUIMenu implements Listener {
    public GUIMenu() {
    }

    public static Inventory getInventory(Player player) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, "Trackers Menu");
        Map<Player, Integer> targets = new HashMap();
        Iterator var3 = Bukkit.getOnlinePlayers().iterator();

        while(var3.hasNext()) {
            Player x = (Player)var3.next();
            if (!x.equals(player) && x.getWorld().equals(player.getWorld())) {
                int range = (int) Main.getRange(player.getLocation(), x.getLocation());
                targets.put(x, range);
            }
        }

        SortedSet<Entry<Player, Integer>> sortedTargets = sort(targets);
        Iterator var12 = sortedTargets.iterator();

        while(var12.hasNext()) {
            Entry<Player, Integer> e = (Entry)var12.next();
            int range = (Integer)e.getValue();
            Player target = (Player)e.getKey();
            List<String> lore = new ArrayList();
            lore.add(Main.getFormat(player, target, (double)range));
            ItemStack head = new ItemStack(Material.SKULL_ITEM);
            SkullMeta head_meta = (SkullMeta)head.getItemMeta();
            head_meta.setOwner(target.getName());
            head_meta.setDisplayName(Main.getFormat(player, target, (double)range));
            head.setItemMeta(head_meta);
            inv.addItem(new ItemStack[]{head});
        }

        return inv;
    }

    @EventHandler
    public void onCompassClick(PlayerInteractEvent event) {
        if (Main.gui) {
            if ((event.getPlayer().getItemInHand().equals(Main.getTracker()) || event.getPlayer().getItemInHand().getType().equals(Material.COMPASS) && Main.defCompass) && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
                event.getPlayer().openInventory(getInventory(event.getPlayer()));
            }

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory().getName().equals("Tracker")) {
            event.setCancelled(true);
        }

    }

    public static SortedSet<Entry<Player, Integer>> sort(Map<Player, Integer> map) {
        SortedSet<Entry<Player, Integer>> treeMap = new TreeSet(new Comparator<Entry<Player, Integer>>() {
            public int compare(Entry<Player, Integer> e1, Entry<Player, Integer> e2) {
                return ((Integer)e1.getValue()).compareTo((Integer)e2.getValue());
            }
        });
        treeMap.addAll(map.entrySet());
        return treeMap;
    }
}
