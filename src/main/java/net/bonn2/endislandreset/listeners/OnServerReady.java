package net.bonn2.endislandreset.listeners;

import net.bonn2.endislandreset.EndIslandReset;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class OnServerReady implements Listener {

    @EventHandler
    public void onServerReady(ServerLoadEvent event) {
        for (String command : EndIslandReset.instance.config.postResetCommands) {
            EndIslandReset.instance.getServer().dispatchCommand(
                    EndIslandReset.instance.getServer().getConsoleSender(),
                    command
            );
        }
    }
}
