package dev.zeeppss.tracker.actionbars;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.entity.Player;

public class ActionBarEvent {
    public static HashMap<Player, Boolean> using = new HashMap();

    public ActionBarEvent() {
    }

    public static void clearBar(Player p) {
        if (!(Boolean)using.get(p)) {
            ActionBar bar = new ActionBar();
            bar.setMessage("").send(p);
        }

    }

    public static void clearAll() {
        Iterator var0 = using.entrySet().iterator();

        while(var0.hasNext()) {
            Entry e = (Entry)var0.next();
            if (!(Boolean)e.getValue()) {
                clearBar((Player)e.getKey());
            }
        }

    }
}

