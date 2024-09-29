package fr.pixeleur.wordscramblers;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.pixeleur.wordscramblers.init.WordScramblerPluginCommands;
import fr.pixeleur.wordscramblers.init.WordScramblerPluginProcedures;

import java.io.File;

public final class WordScramblers extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;

    @Override
    public void onEnable() {
        createCustomConfig();
        WordScramblerPluginCommands.register(this);
        WordScramblerPluginProcedures.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return customConfig;
    }

    @Override
    public void saveConfig() {
        try {
            customConfig.save(customConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reloadConfig() {
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }
}