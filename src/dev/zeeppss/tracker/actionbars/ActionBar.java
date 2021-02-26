package dev.zeeppss.tracker.actionbars;

import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {
    private PacketPlayOutChat packet;

    public ActionBar() {
    }

    public ActionBar setMessage(String text) {
        this.packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text + "\"}"),(byte) 2);
        return this;
    }

    public void send(Player player) {
        if (this.packet != null) {
            CraftPlayer p = (CraftPlayer)player;
            p.getHandle().playerConnection.sendPacket(this.packet);
        }
    }
}
