package net.bonn2.endislandreset.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
public class Config {
    @Comment("The name of the folder that the end resides in.")
    public String endFolderName = "world_the_end";

    @Comment("The name of the region files to protect. Default protects between x = -512, z = -512 and x = 511, z = 511")
    public List<String> regionFilesToKeep = List.of("0.0", "0.-1", "-1.0", "-1.-1");

    @Comment("How long should be between resets. Note: resets will occur on the first restart after the selected time has elapsed.")
    public long timeBetweenResets = 2;

    @Comment("The unit time_between_resets is in.")
    public ChronoUnit timeUnit = ChronoUnit.WEEKS;

    @Comment("Where to teleport players that logged out in regions that were reset.")
    public Location safeLocation = new Location(Bukkit.getWorld("world_the_end"), 0, 100, 0);

    @Comment("The world where SAFE_LOCATION is located.")
    public String safeLocationWorld = "world_the_end";

    @Comment("The distance from 0,0 that a player needs to be to be teleported to the safe location")
    public int safeDistance = 511;

    @Comment("Should the seed be randomized every reset.")
    public boolean seedRandomization = true;

    @Comment("The last time the end was reset. Warning: You should not need to change this unless you are trying to force the end to reset on the next reboot.")
    public long lastEndReset = 0;
}