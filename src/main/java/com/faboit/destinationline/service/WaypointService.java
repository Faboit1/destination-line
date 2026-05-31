package com.faboit.destinationline.service;

import com.faboit.destinationline.model.Destination;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WaypointService {
    private static final DecimalFormat DISTANCE_FORMAT = new DecimalFormat("0.0");

    private final JavaPlugin plugin;
    private final DestinationManager destinationManager;
    private final Map<UUID, Integer> activeDestinations = new ConcurrentHashMap<>();

    private BukkitTask task;

    public WaypointService(JavaPlugin plugin, DestinationManager destinationManager) {
        this.plugin = plugin;
        this.destinationManager = destinationManager;
    }

    public void start() {
        long updateTicks = plugin.getConfig().getLong("settings.updateTicks", 5L);
        this.task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::updateAll, 1L, Math.max(1L, updateTicks));
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        activeDestinations.clear();
    }

    public boolean setActiveDestination(Player player, int destinationNumber) {
        if (destinationManager.getDestination(destinationNumber).isEmpty()) {
            return false;
        }
        activeDestinations.put(player.getUniqueId(), destinationNumber);
        destinationManager.getDestination(destinationNumber).ifPresent(destination ->
                player.sendMessage("§aActive destination: §f" + destination.getDisplayText())
        );
        return true;
    }

    public void clearDestination(Player player) {
        activeDestinations.remove(player.getUniqueId());
        player.sendMessage("§eDestination hidden.");
    }

    public Integer getActiveDestination(Player player) {
        return activeDestinations.get(player.getUniqueId());
    }

    private void updateAll() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Integer destinationNumber = activeDestinations.get(player.getUniqueId());
            if (destinationNumber == null) {
                continue;
            }

            Destination destination = destinationManager.getDestination(destinationNumber).orElse(null);
            if (destination == null) {
                activeDestinations.remove(player.getUniqueId());
                continue;
            }

            renderForPlayer(player, destination);
        }
    }

    private void renderForPlayer(Player player, Destination destination) {
        Location destinationLocation = destination.getLocation().clone().add(0.5, 1.0, 0.5);

        if (!player.getWorld().equals(destinationLocation.getWorld())) {
            sendActionBar(player, "§eDestination in world: §f" + destinationLocation.getWorld().getName());
            return;
        }

        Location from = player.getEyeLocation();
        double distance = from.distance(destinationLocation);
        if (distance < 1.0) {
            sendActionBar(player, "§aArrived at §f" + destination.getDisplayText());
            return;
        }

        double maxTrailLength = plugin.getConfig().getDouble("settings.maxTrailLength", 35.0D);
        double spacing = plugin.getConfig().getDouble("settings.trailSpacing", 1.2D);

        Vector direction = destinationLocation.toVector().subtract(from.toVector()).normalize();
        double drawUntil = Math.min(distance, maxTrailLength);

        Particle particle = destination.getParticle();
        Particle.DustOptions dustOptions = particle == Particle.REDSTONE
                ? new Particle.DustOptions(destination.getColor(), destination.getSize())
                : null;

        // Spawn a packet-only line for this player (spawnParticle(Player) is client-targeted).
        for (double current = 1.5D; current <= drawUntil; current += Math.max(0.3D, spacing)) {
            Location point = from.clone().add(direction.clone().multiply(current));
            if (dustOptions != null) {
                player.spawnParticle(particle, point, 1, dustOptions);
            } else {
                player.spawnParticle(particle, point, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }

        // Add a stronger marker around the destination.
        if (dustOptions != null) {
            player.spawnParticle(particle, destinationLocation, 18, 0.45D, 0.45D, 0.45D, 0.0D, dustOptions);
        } else {
            player.spawnParticle(particle, destinationLocation, 18, 0.45D, 0.45D, 0.45D, 0.0D);
        }

        sendActionBar(player, "§b" + destination.getDisplayText() + " §7(" + DISTANCE_FORMAT.format(distance) + "m)");
    }

    private void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}
