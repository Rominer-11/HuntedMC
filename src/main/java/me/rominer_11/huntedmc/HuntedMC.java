package me.rominer_11.huntedmc;

import me.rominer_11.huntedmc.files.PlayerData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class HuntedMC extends JavaPlugin implements Listener {

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
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = (Player) event.getEntity();
        Player killer = player.getKiller();

        if (killer instanceof Player) {
            HashMap<String, Object> v_data = (HashMap<String, Object>) PlayerData.get().get(String.valueOf(player.getUniqueId()));
            HashMap<String, Object> k_data = (HashMap<String, Object>) PlayerData.get().get(String.valueOf(killer.getUniqueId()));

            Double balance = (Double) v_data.get("balance");
            v_data.put("balance", 0.00);
            k_data.put("balance", balance);
        }

    }

}
