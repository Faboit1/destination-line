package com.faboit.destinationline;

import com.faboit.destinationline.command.HideDestinationCommand;
import com.faboit.destinationline.command.ListDestinationsCommand;
import com.faboit.destinationline.command.ShowDestinationCommand;
import com.faboit.destinationline.listener.PlayerJoinListener;
import com.faboit.destinationline.service.DestinationManager;
import com.faboit.destinationline.service.WaypointService;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DestinationLinePlugin extends JavaPlugin {

    private DestinationManager destinationManager;
    private WaypointService waypointService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.destinationManager = new DestinationManager(this);
        this.destinationManager.reload();

        this.waypointService = new WaypointService(this, destinationManager);
        this.waypointService.start();

        registerCommands();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(waypointService, destinationManager), this);
    }

    @Override
    public void onDisable() {
        if (waypointService != null) {
            waypointService.stop();
        }
    }

    private void registerCommands() {
        PluginCommand showCommand = Objects.requireNonNull(getCommand("showdestination"));
        showCommand.setExecutor(new ShowDestinationCommand(waypointService, destinationManager));

        PluginCommand hideCommand = Objects.requireNonNull(getCommand("hidedestination"));
        hideCommand.setExecutor(new HideDestinationCommand(waypointService));

        PluginCommand listCommand = Objects.requireNonNull(getCommand("listdestinations"));
        listCommand.setExecutor(new ListDestinationsCommand(destinationManager));
    }
}
