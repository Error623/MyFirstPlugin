package ru.example.newplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class NewPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Plugin is Enabled");
        new ExampleTask(this).runTaskTimer(this, 0L, 20L * 60);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin is Disabled");
    }
    public class ExampleTask extends BukkitRunnable {
        private final JavaPlugin plugin;

        private int counter;

        public ExampleTask(JavaPlugin plugin) {
            this.plugin = plugin;
            }
        @Override
        public void run() {
            plugin.getServer().broadcastMessage("Welcome to my local server!");
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("basic")) { // Обработка команды basic
            sender.sendMessage("Test");
            return true;
        } else if (command.getName().equalsIgnoreCase("hello")) { // Обработка команды hello
            if (args.length != 1) {
                sender.sendMessage("Use: /hello <name player's>");
                return false;
            }
            sender.sendMessage("Hello, " + args[0] + "!");
            return true;
        } else if (command.getName().equalsIgnoreCase("basic2")) { // Обработка команды basic2
            if (!sender.hasPermission("newplugin.admin")) {
                sender.sendMessage("You don't have the permission for this command!");
                return true;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                sender.sendMessage("Command done by administrator: " + player.getName());
            } else {
                sender.sendMessage("Command done by logged-in-players: ");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("check")) { // Обработка команды check
            if (args.length != 1) {
                sender.sendMessage(ChatColor.YELLOW + "Use: /check <player>");
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(args[0] + " is not online!");
                return true;
            }
            target.sendMessage("You are online!");
            sender.sendMessage("Player " + args[0] + " is online.");
            return true;
        } else if (command.getName().equalsIgnoreCase("offlineinfo")) { // Обработка команды offlineinfo
            if (args.length != 1) {
                sender.sendMessage(ChatColor.YELLOW + "Use: /offlineinfo <player>");
                return false;
            }
            org.bukkit.OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (offlinePlayer.hasPlayedBefore()) {
                sender.sendMessage("Информация о игроке " + offlinePlayer.getName() + ":");
                sender.sendMessage("UUID: " + offlinePlayer.getUniqueId());
                sender.sendMessage("Был онлайн: " + !offlinePlayer.isOnline());
                return true;
            } else {
                sender.sendMessage("Игрок " + args[0] + " не был найден.");
                return true;
            }
        }  else if (command.getName().equalsIgnoreCase("broadcast")) {
            if (!sender.hasPermission("newplugin.broadcast")) {
                sender.sendMessage("You do not have permission to broadcast!");
                return true;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("send.receive.message")) {
                    player.sendMessage("You were sent a message!"); // Отправка игроку, а не отправителю
                }
            }
            sender.sendMessage("Message has been broadcasted to eligible players!");
            return true;
        }
        return false; // Этот return выполняется, только если ни одна команда не совпала
    }
}
