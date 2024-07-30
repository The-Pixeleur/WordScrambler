package fr.pixeleur.wordscrambler;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pixeleur.wordscrambler.init.WordScramblerPluginCommands;
import fr.pixeleur.wordscrambler.init.WordScramblerPluginProcedures;

public final class WordScrambler extends JavaPlugin {

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
