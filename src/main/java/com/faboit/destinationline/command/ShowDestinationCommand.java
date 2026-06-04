package com.faboit.destinationline.command;

import com.faboit.destinationline.service.DestinationManager;
import com.faboit.destinationline.service.WaypointService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowDestinationCommand implements CommandExecutor {

    private final WaypointService waypointService;
    private final DestinationManager destinationManager;

    public ShowDestinationCommand(WaypointService waypointService, DestinationManager destinationManager) {
        this.waypointService = waypointService;
        this.destinationManager = destinationManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("destination.show")) {
            sender.sendMessage("§cYou do not have permission.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§eUsage: /showdestination <player> <destinationNumber>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found or offline: " + args[0]);
            return true;
        }

        int destinationNumber;
        try {
            destinationNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("§cDestination number must be numeric.");
            return true;
        }

        if (destinationManager.getDestination(destinationNumber).isEmpty()) {
            sender.sendMessage("§cDestination #" + destinationNumber + " is not configured.");
            return true;
        }

        waypointService.setActiveDestination(target, destinationNumber);
        sender.sendMessage("§aSet destination #" + destinationNumber + " for " + target.getName());
        if (!sender.equals(target)) {
            target.sendMessage("§bYour destination was updated by " + sender.getName() + ".");
        }
        return true;
    }
}
