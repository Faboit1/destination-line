package com.faboit.destinationline.model;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class Destination {
    private final int number;
    private final String name;
    private final Location location;
    private final String displayText;
    private final Particle particle;
    private final Color color;
    private final float size;

    public Destination(int number, String name, Location location, String displayText, Particle particle, Color color, float size) {
        this.number = number;
        this.name = name;
        this.location = location;
        this.displayText = displayText;
        this.particle = particle;
        this.color = color;
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location.clone();
    }

    public String getDisplayText() {
        return displayText;
    }

    public Particle getParticle() {
        return particle;
    }

    public Color getColor() {
        return color;
    }

    public float getSize() {
        return size;
    }
}
