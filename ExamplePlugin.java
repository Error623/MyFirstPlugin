package ru.example.newplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new ExampleListener(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin is Disabled");
    }
}

class ExampleListener implements Listener {
    private final ExamplePlugin plugin;

    public ExampleListener(ExamplePlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        BukkitTask task = new ExampleSelfCancelingTask(this.plugin, 5).runTaskTimer(this.plugin, 10, 20);
    }
}

class ExampleSelfCancelingTask extends BukkitRunnable {
    private final JavaPlugin plugin;
    private int counter;

    public ExampleSelfCancelingTask(JavaPlugin plugin, int counter) {
        this.plugin = plugin;
        if (counter <= 0) {
            throw new IllegalArgumentException("counter must be greater than 0");
        } else {
            this.counter = counter;
        }
    }

    @Override
    public void run() {
        if (counter > 0) {
            plugin.getServer().broadcastMessage("Commence greeting in " + counter--);
        } else {
            plugin.getServer().broadcastMessage("Welcome to my local server on Bukkit!");
            this.cancel();
        }
    }
}

