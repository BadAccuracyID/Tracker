package dev.zeeppss.tracker.menus;

import dev.zeeppss.tracker.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GUIMenu1 implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Inventory invgui = Bukkit.createInventory(player, 27, ChatColor.GREEN + "Shop Trackers");

            ItemStack air = new ItemStack(Material.AIR);
            ItemStack compass = new ItemStack(Material.COMPASS);

            ItemMeta compassMeta = compass.getItemMeta();
            compassMeta.setDisplayName("§eBuy Trackers §7(Right Click)");
            ArrayList<String> compassLore = new ArrayList<>();
            compassLore.add("§7Buy to get Player Tracker!");
            compassMeta.setLore(compassLore);
            compass.setItemMeta(compassMeta);

            ItemStack[] menu_items = {air, air, air, air, air, air, air, air, air, air, air, air, air, compass};
            invgui.setContents(menu_items);
            player.openInventory(invgui);
        }
        return true;
    }

    @EventHandler
    public void ClickEvent(InventoryClickEvent e) {

        if (e.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GREEN + "Shop Trackers")) {
            Player player = (Player) e.getWhoClicked();

                ItemStack itemBuy = new ItemStack(Material.EMERALD, 2);
                switch (e.getCurrentItem().getType()) {
                    case COMPASS:
                        if (player.getInventory().contains(itemBuy)) {
                            player.performCommand("track give");
                            player.getInventory().removeItem(itemBuy);
                        } else {
                            player.closeInventory();
                            player.sendMessage(Main.prefix + ChatColor.RED + "Sorry, you not have 2 emerald!");
                        }
                        player.closeInventory();
                        break;
                }
                e.setCancelled(true);
        }
    }
}
