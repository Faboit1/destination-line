package com.faboit.destinationline.service;

import com.faboit.destinationline.model.Destination;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public class DestinationManager {
    private final JavaPlugin plugin;
    private final Map<Integer, Destination> destinations = new LinkedHashMap<>();

    public DestinationManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        destinations.clear();
        plugin.reloadConfig();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("destinations");
        if (section == null) {
            plugin.getLogger().warning("No destinations section found in config.yml");
            return;
        }

        for (String key : section.getKeys(false)) {
            int number;
            try {
                number = Integer.parseInt(key);
            } catch (NumberFormatException ex) {
                plugin.getLogger().warning("Invalid destination key (must be number): " + key);
                continue;
            }

            ConfigurationSection destinationSection = section.getConfigurationSection(key);
            if (destinationSection == null) {
                continue;
            }

            String worldName = destinationSection.getString("world", "world");
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                plugin.getLogger().warning("Destination " + number + " uses unknown world: " + worldName);
                continue;
            }

            String name = destinationSection.getString("name", "Destination " + number);
            String displayText = destinationSection.getString("displayText", name);

            double x = destinationSection.getDouble("x");
            double y = destinationSection.getDouble("y");
            double z = destinationSection.getDouble("z");
            Location location = new Location(world, x, y, z);

            Particle particle = Particle.VILLAGER_HAPPY;
            String particleName = destinationSection.getString("particle", particle.name());
            try {
                particle = Particle.valueOf(particleName.toUpperCase());
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().log(Level.WARNING, "Invalid particle for destination " + number + ": " + particleName + ", using default.");
            }

            int red = destinationSection.getInt("color.red", 255);
            int green = destinationSection.getInt("color.green", 255);
            int blue = destinationSection.getInt("color.blue", 0);
            float size = (float) destinationSection.getDouble("size", 1.0D);

            Destination destination = new Destination(number, name, location, displayText, particle, Color.fromRGB(red, green, blue), size);
            destinations.put(number, destination);
        }

        plugin.getLogger().info("Loaded " + destinations.size() + " destination(s)");
    }

    public Optional<Destination> getDestination(int number) {
        return Optional.ofNullable(destinations.get(number));
    }

    public Map<Integer, Destination> getDestinations() {
        return Collections.unmodifiableMap(destinations);
    }
}
