package fr.pixeleur.wordscramblers.init;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pixeleur.wordscramblers.procedures.ScramblerEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WordScramblerPluginProcedures {

    public static void register(JavaPlugin plugin) {
        // Schedule ScramblerEvent to start 10 seconds after server is loaded (so fastest user can see the word)
        new BukkitRunnable() {
            @Override
            public void run() {
                new ScramblerEvent(plugin);
            }
        }.runTaskLaterAsynchronously(plugin, 20L * 10); // 20 ticks per second * 10 seconds
    }
}
