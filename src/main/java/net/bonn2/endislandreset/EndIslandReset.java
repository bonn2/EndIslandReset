package net.bonn2.endislandreset;

import de.exlll.configlib.ConfigLib;
import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import de.tr7zw.nbtapi.NBTFile;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import net.bonn2.endislandreset.config.Config;
import net.bonn2.endislandreset.config.LoginLogger;
import net.bonn2.endislandreset.listeners.OnJoin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public final class EndIslandReset extends JavaPlugin {

    public static EndIslandReset instance;

    public final Path configFile = new File(getDataFolder(), "config.yml").toPath();
    public final YamlConfigurationProperties configProperties = ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
            .header(
                    """
                    Config for EndIslandReset
                    by bonn2
                    """
            )
            .setNameFormatter(NameFormatters.UPPER_UNDERSCORE)
            .setFieldFilter(field -> !field.getName().startsWith("internal"))
            .build();
    public Config config;

    public final Path loginsFile = new File(getDataFolder(), "logins.yml").toPath();
    public LoginLogger logins;

    @Override
    public void onEnable() {
        instance = this;

        // Load the config
        config = YamlConfigurations.update(
                configFile,
                Config.class,
                configProperties
        );

        // Load logins data
        logins = YamlConfigurations.update(
                loginsFile,
                LoginLogger.class
        );

        // Register listeners
        getServer().getPluginManager().registerEvents(new OnJoin(), this);

        // Check if there should be a reset or not
        try {
            checkForReset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void checkForReset() throws IOException {
        LocalDateTime lastReset = LocalDateTime.ofEpochSecond(config.lastEndReset, 0, ZoneOffset.UTC);
        if (config.timeUnit.between(lastReset, LocalDateTime.now()) >= config.timeBetweenResets) {
            // It is time for a reset
            getLogger().info(config.timeBetweenResets + " " + config.timeUnit + " have elapsed since last reset! Resetting end...");
            getLogger().info("Clearing previous end backup...");
            Stream<Path> files = Files.walk(Paths.get(config.endFolderName, "old"));
            files.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            getLogger().info("Moving old end region files...");
            Files.createDirectories(Path.of(config.endFolderName, "old", "region"));
            for (String file : Path.of(config.endFolderName, "DIM1", "region").toFile().list()) {
                boolean reset = true;
                for (String protectedName : config.regionFilesToKeep)
                    if (file.equals("r." + protectedName + ".mca")) {
                        reset = false;
                        break;
                    }
                if (!reset) continue;
                Files.move(
                        Path.of(config.endFolderName,"DIM1", "region", file),
                        Path.of(config.endFolderName, "old", "region", file),
                        StandardCopyOption.ATOMIC_MOVE);
            }
            getLogger().info("Moving old end entity files...");
            Files.createDirectories(Path.of(config.endFolderName, "old", "entities"));
            for (String file : Path.of(config.endFolderName, "DIM1", "entities").toFile().list()) {
                boolean reset = true;
                for (String protectedName : config.regionFilesToKeep)
                    if (file.equals("r." + protectedName + ".mca")) {
                        reset = false;
                        break;
                    }
                if (!reset) continue;
                Files.move(
                        Path.of(config.endFolderName,"DIM1", "entities", file),
                        Path.of(config.endFolderName, "old", "entities", file),
                        StandardCopyOption.ATOMIC_MOVE);
            }
            getLogger().info("Moving old end poi files...");
            Files.createDirectories(Path.of(config.endFolderName, "old", "poi"));
            for (String file : Path.of(config.endFolderName, "DIM1", "poi").toFile().list()) {
                boolean reset = true;
                for (String protectedName : config.regionFilesToKeep)
                    if (file.equals("r." + protectedName + ".mca")) {
                        reset = false;
                        break;
                    }
                if (!reset) continue;
                Files.move(
                        Path.of(config.endFolderName,"DIM1", "poi", file),
                        Path.of(config.endFolderName, "old", "poi", file),
                        StandardCopyOption.ATOMIC_MOVE);
            }

            // Change seed if enabled
            if (config.seedRandomization) {
                ReadWriteNBT nbt = NBTFile.readFrom(Path.of(config.endFolderName, "level.dat").toFile());
                nbt.getCompound("Data").getCompound("WorldGenSettings").setLong("seed", new Random().nextLong());
                nbt.writeCompound(new FileOutputStream(Path.of(config.endFolderName, "level.dat").toFile()));
            }

            // Set the last end reset time to now
            config.lastEndReset = Instant.now().toEpochMilli() / 1000;
            // Clear the login registry
            logins.loggedIn = new ArrayList<>();
            // Save the config files
            YamlConfigurations.save(configFile, Config.class, config, configProperties);
            YamlConfigurations.save(loginsFile, LoginLogger.class, logins);
        }
    }
}
