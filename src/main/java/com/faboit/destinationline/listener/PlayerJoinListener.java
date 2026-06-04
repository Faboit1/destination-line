package com.faboit.destinationline.listener;

import com.faboit.destinationline.service.DestinationManager;
import com.faboit.destinationline.service.WaypointService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final WaypointService waypointService;
    private final DestinationManager destinationManager;

    public PlayerJoinListener(WaypointService waypointService, DestinationManager destinationManager) {
        this.waypointService = waypointService;
        this.destinationManager = destinationManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (destinationManager.getDestination(1).isPresent()) {
            waypointService.setActiveDestination(player, 1);
        } else {
            player.sendMessage("§eNo destination #1 configured.");
        }
    }
}
