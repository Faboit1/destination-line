package com.faboit.destinationline.command;

import com.faboit.destinationline.model.Destination;
import com.faboit.destinationline.service.DestinationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ListDestinationsCommand implements CommandExecutor {

    private final DestinationManager destinationManager;

    public ListDestinationsCommand(DestinationManager destinationManager) {
        this.destinationManager = destinationManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("destination.list")) {
            sender.sendMessage("§cYou do not have permission.");
            return true;
        }

        Map<Integer, Destination> destinations = destinationManager.getDestinations();
        if (destinations.isEmpty()) {
            sender.sendMessage("§eNo destinations configured.");
            return true;
        }

        sender.sendMessage("§bConfigured destinations:");
        destinations.forEach((id, destination) -> sender.sendMessage("§7- #" + id + " §f" + destination.getName() + " §8(" + destination.getLocation().getWorld().getName() + ")"));
        return true;
    }
}
