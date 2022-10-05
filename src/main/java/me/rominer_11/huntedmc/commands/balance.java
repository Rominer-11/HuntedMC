package me.rominer_11.huntedmc.commands;

import me.rominer_11.huntedmc.files.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getPlayerExact;

public class balance implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {

                Player target = getPlayerExact(args[0]);

                if (target != null) {

                    PlayerData.reload();

                    HashMap<String, Object> t_data = (HashMap<String, Object>) PlayerData.get().getConfigurationSection(String.valueOf(target.getUniqueId())).getValues(false);

                    Double balance = (Double) t_data.get("balance");

                    player.sendMessage(ChatColor.GOLD + "Player " + target.getDisplayName() + " has a balance of $" + balance);

                } else if (target == null) {

                    player.sendMessage(ChatColor.RED + "That player does not exist or is not online!");

                }

            }
        }
        return false;
    }
}
