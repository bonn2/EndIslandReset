package net.bonn2.endislandreset.commands;

import net.bonn2.endislandreset.EndIslandReset;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StatusCommand implements CommandExecutor, TabCompleter {
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        LocalDateTime lastReset = LocalDateTime.ofEpochSecond(EndIslandReset.instance.config.lastEndReset, 0, ZoneOffset.UTC);
        LocalDateTime nextReset = lastReset.plus(EndIslandReset.instance.config.timeBetweenResets, EndIslandReset.instance.config.timeUnit);
        MiniMessage mm = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .build()
                )
                .build();
        sender.sendMessage(mm.deserialize("<dark_purple>The protected area of the end extends out <gold>" +
                EndIslandReset.instance.config.safeDistance +
                "<dark_purple> blocks.\nThe end last reset on <gold>" +
                lastReset.format(DateTimeFormatter.ISO_DATE_TIME) +
                "<dark_purple>\nThe next reset will be on the first server restart after <gold>" +
                nextReset.format(DateTimeFormatter.ISO_DATE_TIME)));
        return true;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside of a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
