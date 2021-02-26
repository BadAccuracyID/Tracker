package dev.zeeppss.tracker;

import dev.zeeppss.tracker.actionbars.ActionBarEvent;
import org.bukkit.Bukkit;

public class Track2 {
    public Track2() {
    }

    public static void everyFiveTicks() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            public void run() {
                Main.checkHand();
                Main.refreshTracker();
            }
        }, 0L, 5L);
    }

    public static void everySecond() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            public void run() {
                ActionBarEvent.clearAll();
            }
        }, 0L, 20L);
    }
}


