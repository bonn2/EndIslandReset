package net.bonn2.endislandreset.listeners;

import de.exlll.configlib.YamlConfigurations;
import net.bonn2.endislandreset.EndIslandReset;
import net.bonn2.endislandreset.config.LoginLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class OnJoin implements Listener {

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        // If player logged out before the last end reset, is in the end, and is past the safe distance in
        // the x or z coordinate, teleport them to the safe location in the config
        if (!EndIslandReset.instance.logins.loggedIn.contains(event.getPlayer().getUniqueId())) {
            // Add player to the logged in registry and save it
            EndIslandReset.instance.logins.loggedIn.add(event.getPlayer().getUniqueId());
            YamlConfigurations.save(EndIslandReset.instance.loginsFile, LoginLogger.class, EndIslandReset.instance.logins);
            // Check if player is in an unsafe position and teleport them if so
            if (event.getPlayer().getWorld().getName().equals(EndIslandReset.instance.config.endFolderName)
                    && (Math.abs(event.getPlayer().getLocation().getBlockX()) >= EndIslandReset.instance.config.safeDistance
                    || Math.abs(event.getPlayer().getLocation().getBlockZ()) >= EndIslandReset.instance.config.safeDistance)) {
                Location safeLocation = EndIslandReset.instance.config.safeLocation.clone();
                safeLocation.setWorld(Bukkit.getWorld(EndIslandReset.instance.config.safeLocationWorld));
                event.getPlayer().teleport(safeLocation);
            }
        }
    }
}
