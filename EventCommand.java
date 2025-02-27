package com.ayesti.eventplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * EventCommand - Handles the /events command.
 * Author: Ayesti
 */
public class EventCommand implements CommandExecutor {

    private final EventPlugin plugin;

    public EventCommand(EventPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            // Reload the config.yml file
            plugin.reloadConfig();
            sender.sendMessage("Config reloaded!");
            return true;
        }
        return false;
    }
}
