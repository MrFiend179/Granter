// Developed by Flubel
// Licensed under the FGPL - Flubel General Public License
// For more details, visit: https://flubel.com/license

package com.flubel.granter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Granter extends JavaPlugin implements CommandExecutor {

    private File dataFile;
    private FileConfiguration dataConfig;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI not found! This plugin requires PlaceholderAPI as an optional plugin to function.");
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("LuckPerms") == null) {
            getLogger().severe("LuckPerms not found! This plugin requires LuckPerms to function.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getCommand("grant").setExecutor(this);
        saveDefaultConfig();
        loadDataFile();
        getLogger().info("\u001B[38;2;23;138;214m###############################################\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m#                                             #\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m#      \u001B[33m        Granter  v1.0          \u001B[38;2;23;138;214m        #\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m#      \u001B[33m       Status: \u001B[32mStarted         \u001B[38;2;23;138;214m        #\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m#      \u001B[33m       Made by Fiend           \u001B[38;2;23;138;214m        #\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m#                                             #\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m###############################################\u001B[0m");

    }


    @Override
    public void onDisable() {
        saveDataFile();


        getLogger().info("\u001B[38;2;23;138;214m###############################################\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m#                                             #\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m#      \u001B[33m        Granter  v1.0          \u001B[38;2;23;138;214m        #\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m#      \u001B[33m       Status: \u001B[31mStopped         \u001B[38;2;23;138;214m        #\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m#      \u001B[33m       Made by Fiend           \u001B[38;2;23;138;214m        #\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m#                                             #\u001B[0m");
        getLogger().info("\u001B[38;2;23;138;214m###############################################\u001B[0m");

    }

    private void loadDataFile() {
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }


    private void saveDataFile() {
        try {
            FileConfiguration cleanedConfig = new YamlConfiguration();
            if (dataConfig.contains("grant_usage")) {
                cleanedConfig.set("grant_usage", dataConfig.getConfigurationSection("grant_usage"));
            }

            cleanedConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("granter.grant")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (label.equalsIgnoreCase("grantreset")) {

            if (!player.hasPermission("granter.grant.reset")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to reset grant data.");
                return true;
            }


            if (args.length != 1) {
                player.sendMessage(ChatColor.YELLOW + "Usage: /grantreset <player_name>");
                return true;
            }

            String targetName = args[0];

            Player targetPlayer = Bukkit.getPlayer(targetName);
            if (targetPlayer == null) {
                player.sendMessage(ChatColor.RED + "Player " + targetName + " not found.");
                return true;
            }

            String targetUUID = targetPlayer.getUniqueId().toString();

            String usageKeyReset = "grant_usage." + targetUUID;
            if (dataConfig.contains(usageKeyReset)) {
                dataConfig.set(usageKeyReset, null);
                saveDataFile();
                player.sendMessage(ChatColor.GREEN + "Successfully reset grant data for " + targetName + ".");
            } else {
                player.sendMessage(ChatColor.RED + "No grant data found for " + targetName + ".");
            }
            return true;
        }



        if (label.equalsIgnoreCase("grantinfo")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command.");
                return true;
            }

            if (!player.hasPermission("granter.grant.info")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to view grant data.");
                return true;
            }

            String playerUUID = player.getUniqueId().toString();
            String usageKey = "grant_usage." + playerUUID;

            int highestLimit = 0;
            for (int i = 1; i <= 100; i++) {
                if (player.hasPermission("granter.grant.limit" + i)) {
                    highestLimit = i;
                }
            }

            if (highestLimit == 0) {
                player.sendMessage(ChatColor.RED + "You don't have permission to grant ranks.");
                return true;
            }


            player.sendMessage(ChatColor.YELLOW + "You have granted:");
            player.sendMessage("  ");

            for (String rank : getConfig().getConfigurationSection("ranks").getKeys(false)) {
                String prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("ranks." + rank + ".prefix"));

                int used = dataConfig.getInt(usageKey + "." + rank + ".limit" + highestLimit, 0);

                player.sendMessage(ChatColor.GRAY + "- " + prefix + ChatColor.WHITE + ": " + ChatColor.RED + used);
            }
            player.sendMessage("  ");

            player.sendMessage(ChatColor.YELLOW + "You can still grant:");
            player.sendMessage("  ");

            for (String rank : getConfig().getConfigurationSection("ranks").getKeys(false)) {
                String prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("ranks." + rank + ".prefix"));

                int used = dataConfig.getInt(usageKey + "." + rank + ".limit" + highestLimit, 0);
                int maxLimit = getConfig().getInt("ranks." + rank + ".limits.limit" + highestLimit, 0);
                int remaining = maxLimit - used;

                player.sendMessage(ChatColor.GRAY + "- " + prefix + ChatColor.WHITE + ": " + ChatColor.GREEN + Math.max(remaining, 0));
                player.sendMessage("  ");

            }

            return true;
        }

        if (label.equalsIgnoreCase("grantreload")) {

            if (!player.hasPermission("granter.grant.reload")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to reload grant data.");
                return true;
            }


            loadDataFile();
            reloadConfig();

            player.sendMessage(ChatColor.GREEN + "Successfully reloaded the plugin's data and configuration.");
            return true;
        }


        if (args.length != 2) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /grant <player_name> <rank_name>");
            return true;
        }

        String targetName = args[0];
        String rankName = args[1].toLowerCase();

        if (!getConfig().contains("ranks")) {
            player.sendMessage(ChatColor.RED + "Rank configuration is missing!");
            return true;
        }

        if (!getConfig().contains("ranks." + rankName)) {
            player.sendMessage(ChatColor.RED + "Invalid rank! Available ranks: " +
                    ChatColor.YELLOW + String.join(", ", getConfig().getConfigurationSection("ranks").getKeys(false)));
            return true;
        }

        Map<String, Integer> limits = new HashMap<>();
        if (getConfig().contains("ranks." + rankName + ".limits")) {
            for (String limitKey : getConfig().getConfigurationSection("ranks." + rankName + ".limits").getKeys(false)) {
                limits.put(limitKey, getConfig().getInt("ranks." + rankName + ".limits." + limitKey));
            }
        }

        String selectedLimitKey = null;
        for (String limitKey : limits.keySet()) {
            if (player.hasPermission("granter.grant." + limitKey)) {
                selectedLimitKey = limitKey;
                break;
            }
        }

        if (selectedLimitKey == null) {
            player.sendMessage(ChatColor.RED + "You do not have permission to grant this rank with any limit.");
            return true;
        }

        int maxUses = limits.get(selectedLimitKey);

        String usageKey = "grant_usage." + player.getUniqueId() + "." + rankName + "." + selectedLimitKey;
        int timesUsed = dataConfig.getInt(usageKey, 0);

        if (timesUsed >= maxUses) {
            player.sendMessage(ChatColor.RED + "You have reached the limit for granting the rank " + rankName + ".");
            return true;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + targetName + " parent add " + rankName);
        player.sendMessage(ChatColor.GREEN + "Successfully granted " + rankName + " to " + targetName + ".");
        String rankPrefix = getConfig().getString("ranks." + rankName + ".prefix");
        if (rankPrefix == null) {
            player.sendMessage(ChatColor.RED + "Error: The rank prefix for " + rankName + " was not found in the config.");
        }

        List<String> messages = getConfig().getStringList("messages");

        if (messages.isEmpty()) {
            messages = Arrays.asList("&ePlayer &6&l{granter_player} &egranted {rank} &eto &6&l{granted_player}");
        }

        ChatColor[] colors = {ChatColor.RED, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.GREEN, ChatColor.BLUE};
        StringBuilder border = new StringBuilder();
        int colorIndex = 0;

        String decorSymbol = getConfig().getString("decor", "@");
        int decorSymbolLength = getConfig().getInt("decor_length", 46);


        for (int i = 0; i < decorSymbolLength; i++) {
            border.append(colors[colorIndex]).append(decorSymbol);
            colorIndex = (colorIndex + 1) % colors.length;
        }


        Bukkit.broadcastMessage(border.toString());

        for (String message : messages) {
            String formattedMessage = message
                    .replace("{granter_player}", player.getName())
                    .replace("{rank}", rankPrefix)
                    .replace("{granted_player}", targetName);

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', formattedMessage));
        }
        Bukkit.getLogger().info(rankName);
        Bukkit.getLogger().info(rankPrefix);


        Bukkit.broadcastMessage(border.toString());


        dataConfig.set(usageKey, timesUsed + 1);
        saveDataFile();

        return true;
    }
}
