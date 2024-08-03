package net.bonn2.endislandreset.listeners;

import net.bonn2.endislandreset.EndIslandReset;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.List;

public class OnServerReady implements Listener {

    @EventHandler
    public void onServerReady(ServerLoadEvent event) {
        // Reset end gateways
        World world = Bukkit.getWorld(EndIslandReset.instance.config.endFolderName);
        if (world != null) {
            List<Block> gateways = List.of(
                    world.getBlockAt(96, 75, 0),
                    world.getBlockAt(91, 75, 29),
                    world.getBlockAt(77, 75, 56),
                    world.getBlockAt(56, 75, 77),
                    world.getBlockAt(29, 75, 91),
                    world.getBlockAt(-1, 75, 96),
                    world.getBlockAt(-30, 75, 91),
                    world.getBlockAt(-57, 75, 77),
                    world.getBlockAt(-78, 75, 56),
                    world.getBlockAt(-92, 75, 29),
                    world.getBlockAt(-96, 75, -1),
                    world.getBlockAt(-92, 75, -30),
                    world.getBlockAt(-78, 75, -57),
                    world.getBlockAt(-78, 75, -57),
                    world.getBlockAt(-57, 75, -78),
                    world.getBlockAt(-30, 75, -92),
                    world.getBlockAt(-30, 75, -92),
                    world.getBlockAt(0, 75, -96),
                    world.getBlockAt(29, 75, -92),
                    world.getBlockAt(56, 75, -78),
                    world.getBlockAt(77, 75, -57),
                    world.getBlockAt(91, 75, -30),
                    world.getBlockAt(91, 75, -30)
            );
            int count = 0;
            for (Block gateway : gateways) {
                if (gateway.getState() instanceof EndGateway oldGateway) {
                    // Reset gateway nbt
                    gateway.setType(Material.AIR);
                    gateway.setType(Material.END_GATEWAY);
                    // Copy important values from old gateway
                    EndGateway newGateway = (EndGateway) gateway.getState(false);
                    newGateway.setAge(oldGateway.getAge());
                    newGateway.setExactTeleport(oldGateway.isExactTeleport());
                    count++;
                }
            }
            EndIslandReset.instance.getLogger().info("Reset " + count + " end gateways!");
        }
        else {
            EndIslandReset.instance.getLogger().warning("Failed to get world to reset end gateways!");
        }
        // Start post reset commands
        for (String command : EndIslandReset.instance.config.postResetCommands) {
            EndIslandReset.instance.getServer().dispatchCommand(
                    EndIslandReset.instance.getServer().getConsoleSender(),
                    command
            );
        }
    }
}
