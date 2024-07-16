package net.bonn2.endislandreset.listeners;

import net.bonn2.endislandreset.EndIslandReset;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class OnJoin implements Listener {

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        // If player logged out before the last end reset, is in the end, and is past the safe distance in
        // the x or z coordinate, teleport them to the safe location in the config
        if (EndIslandReset.instance.config.lastEndReset > event.getPlayer().getLastLogin()
        && event.getPlayer().getWorld().toString().equals(EndIslandReset.instance.config.endFolderName)
        && (Math.abs(event.getPlayer().getLocation().getBlockX()) >= EndIslandReset.instance.config.safeDistance
        || Math.abs(event.getPlayer().getLocation().getBlockZ()) >= EndIslandReset.instance.config.safeDistance)) {
            event.getPlayer().teleport(EndIslandReset.instance.config.safeLocation);
        }
    }
}
