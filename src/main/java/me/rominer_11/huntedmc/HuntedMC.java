package me.rominer_11.huntedmc;

import me.rominer_11.huntedmc.commands.balance;
import me.rominer_11.huntedmc.files.PlayerData;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class HuntedMC extends JavaPlugin implements Listener {

    public static HuntedMC plugin;

    @Override
    public void onEnable() {

        // Plugin startup logic

        // Config.yml
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // playerData.yml
        PlayerData.init();
        PlayerData.get().options().copyDefaults(true);
        PlayerData.save();

        // Commands & events
        this.getCommand("balance").setExecutor(new balance());
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {

        // Plugin shutdown logic

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        if (!PlayerData.get().contains(String.valueOf(event.getPlayer().getUniqueId()))) {

            HashMap<String, Object> data = new HashMap<>();

            data.put("username", event.getPlayer().getDisplayName());
            data.put("balance", 1.00);

            PlayerData.get().set(String.valueOf(event.getPlayer().getUniqueId()), data);

            PlayerData.save();
            System.out.println("Data created for " + event.getPlayer().getDisplayName());

        }

        event.setJoinMessage("");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        event.setQuitMessage("");

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer != null) {

            PlayerData.reload();

            HashMap<String, Object> v_data = (HashMap<String, Object>) PlayerData.get().getConfigurationSection(String.valueOf(player.getUniqueId())).getValues(false);
            HashMap<String, Object> k_data = (HashMap<String, Object>) PlayerData.get().getConfigurationSection(String.valueOf(killer.getUniqueId())).getValues(false);

            double balance = (double) v_data.get("balance");
            v_data.put("balance", 0.00);
            k_data.put("balance", (double) k_data.get("balance") + balance);

            PlayerData.get().set(String.valueOf(player.getUniqueId()), v_data);
            PlayerData.get().set(String.valueOf(killer.getUniqueId()), k_data);

            PlayerData.save();

        }

        player.setGameMode(GameMode.SPECTATOR);

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {

            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Tracker")) {

                player.getInventory().setItemInMainHand(compass(player));

            }

        }

    }

    public static ItemStack compass(Player target) {

        // Stolen from https://www.spigotmc.org/threads/creating-a-lodestone-compass.457381/

        org.bukkit.inventory.ItemStack compass = new org.bukkit.inventory.ItemStack(Material.COMPASS);
        CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
        compassMeta.setDisplayName("Tracker");
        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(target.getLocation());
        compass.setItemMeta(compassMeta);

        return compass;

    }

}
