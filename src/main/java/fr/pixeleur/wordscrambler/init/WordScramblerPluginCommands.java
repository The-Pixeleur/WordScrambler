package fr.pixeleur.wordscrambler.init;

import org.bukkit.plugin.java.JavaPlugin;
import fr.pixeleur.wordscrambler.commands.WordRewardRegister;

public class WordScramblerPluginCommands {
    public static void register(JavaPlugin plugin) {
        // Pass the plugin instance to the WordRewardRegister constructor
        WordRewardRegister wordRewardRegister = new WordRewardRegister(plugin);
        plugin.getCommand("WordRewardRegister").setExecutor(wordRewardRegister);
    }
}
