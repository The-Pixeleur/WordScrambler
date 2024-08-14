package fr.pixeleur.wordscramblers;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pixeleur.wordscramblers.init.WordScramblerPluginCommands;
import fr.pixeleur.wordscramblers.init.WordScramblerPluginProcedures;

public final class WordScramblers extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        WordScramblerPluginCommands.register(this);
        WordScramblerPluginProcedures.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
