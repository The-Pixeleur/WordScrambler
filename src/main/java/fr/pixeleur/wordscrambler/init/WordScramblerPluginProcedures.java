package fr.pixeleur.wordscrambler.init;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pixeleur.wordscrambler.procedures.ScramblerEvent;

public class WordScramblerPluginProcedures {

    public static void register(JavaPlugin plugin) {
        new ScramblerEvent(plugin);
    }
}
