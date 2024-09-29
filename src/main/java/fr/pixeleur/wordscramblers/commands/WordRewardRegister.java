package fr.pixeleur.wordscramblers.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayOutputStream;

public class WordRewardRegister implements CommandExecutor {
    private final JavaPlugin plugin;

    public WordRewardRegister(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("wordscramblers.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "You must hold an item in your main hand.");
            return true;
        }

        // Save item details to the config
        FileConfiguration config = plugin.getConfig();
        config.set("event_reward_material", itemInHand.getType().toString());

        // Save NBT data to the config
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Save every type of data for the ItemStack, this will make the ItemStack fully cloneable
            dataOutput.writeObject(itemInHand);

            // Serialize that array to a Base64 String.
            String data = Base64Coder.encodeLines(outputStream.toByteArray());

            config.set("event_reward_nbt", data);
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "An error occurred while trying to save the item's NBT data.");
            return true;
        }

        plugin.saveConfig();

        player.sendMessage(ChatColor.GREEN + "The current item in your hand has been registered as the reward item.");
        return true;
    }
}