package me.The_Coder.afkcoords;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;


/**
 * Created by Bailey on 12/3/13.
 */
public class CoordsListener implements Listener {
    public static Main plugin;

    public CoordsListener(Main cordsBroadcast)
    {
        plugin = cordsBroadcast;
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (plugin.stillplayers.containsKey(player.getName())) {
            plugin.getServer().broadcastMessage(
                    ChatColor.GRAY + player.getName() + " is not afk!");
            plugin.stillplayers.remove(player.getName());
            plugin.getallplayers.remove(player.getName());
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerSneak(PlayerToggleSneakEvent event)
    {
        Player player = event.getPlayer();
        if (plugin.stillplayers.containsKey(player.getName())) {
            plugin.getServer().broadcastMessage(
                    ChatColor.GRAY + player.getName() + " is not afk!");
            plugin.stillplayers.remove(player.getName());
            plugin.getallplayers.remove(player.getName());
        }
    }

    @EventHandler(priority= EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if (plugin.stillplayers.containsKey(player.getName())) {
            plugin.getServer().broadcastMessage(
                    ChatColor.GRAY + player.getName() + " is not afk!");
            plugin.stillplayers.remove(player.getName());
            plugin.getallplayers.remove(player.getName());
        }
    }
}