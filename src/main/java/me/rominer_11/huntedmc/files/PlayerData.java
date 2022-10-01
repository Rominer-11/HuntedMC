package me.rominer_11.huntedmc.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PlayerData {
    // Thank you, Kody Simpson! https://www.youtube.com/watch?v=3en6w7PNL08

    private static File file;
    private static FileConfiguration customFile;

    // Get file ready for use
    public static void init() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("HuntedMC").getDataFolder(), "PlayerData.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException e) {
                // f in the chat
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch(IOException e) {
            System.err.println("Could not save playerData.yml!");
        }
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}
