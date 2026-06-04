package com.faboit.destinationline.command;

import com.faboit.destinationline.service.WaypointService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HideDestinationCommand implements CommandExecutor {

    private final WaypointService waypointService;

    public HideDestinationCommand(WaypointService waypointService) {
        this.waypointService = waypointService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("destination.hide")) {
            sender.sendMessage("§cYou do not have permission.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§eUsage: /hidedestination <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found or offline: " + args[0]);
            return true;
        }

        waypointService.clearDestination(target);
        sender.sendMessage("§aDestination hidden for " + target.getName());
        return true;
    }
}
