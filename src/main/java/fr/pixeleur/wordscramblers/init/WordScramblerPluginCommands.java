package fr.pixeleur.wordscramblers.init;

import org.bukkit.plugin.java.JavaPlugin;
import fr.pixeleur.wordscramblers.commands.WordRewardRegister;

public class WordScramblerPluginCommands {
    public static void register(JavaPlugin plugin) {
        WordRewardRegister wordRewardRegister = new WordRewardRegister(plugin);
        plugin.getCommand("WordRewardRegister").setExecutor(wordRewardRegister);
    }
}
