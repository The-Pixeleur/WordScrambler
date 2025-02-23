package fr.pixeleur.wordscramblers.procedures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.util.*;

public class ScramblerEvent implements Listener {

    private final JavaPlugin plugin;
    private final String eventPrefix;
    private final int timeForAnswering;
    private final boolean caseSensitive;
    private final List<String> words;
    private final String eventStartMessage;
    private final String eventEndWin;
    private final String eventEndNobody;
    private final ItemStack eventReward;
    private final int eventRewardsCount;

    private String currentWord;
    private String scrambledWord;
    private long startTime;
    private boolean isEventActive;
    private BukkitRunnable endTimer;

    private final String winSound;
    private final String eventStartSound;
    private final String eventEndWinSound;
    private final String eventEndNobodySound;

    private final String winTitleMain;
    private final String winTitleSub;
    private final int winTitleFadeIn;
    private final int winTitleStay;
    private final int winTitleFadeOut;


    public ScramblerEvent(JavaPlugin plugin) {
        this.plugin = plugin;

        // Load configuration values
        this.eventPrefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("event_prefix", "none"));
        this.timeForAnswering = plugin.getConfig().getInt("time_for_answering", 2) * 60; // Convert minutes to seconds
        int timeBetweenEachEvent = plugin.getConfig().getInt("time_between_each_event", 15) * 60; // Convert minutes to seconds
        this.caseSensitive = plugin.getConfig().getBoolean("case_sensitive", false);
        this.words = plugin.getConfig().getStringList("Words");

        // Apply the translation for the event messages
        this.eventStartMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("event_start_message", "none"));
        this.eventEndWin = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("event_end_win", "none"));
        this.eventEndNobody = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("event_end_nobody", "none"));

        // Ajoutez ce code dans le constructeur après le chargement des autres valeurs
        this.winSound = plugin.getConfig().getString("win_sound", "ENTITY_PLAYER_LEVELUP");
        // Inside the constructor after loading other configs
        this.eventStartSound = plugin.getConfig().getString("event_start_sound", "ENTITY_PLAYER_LEVELUP");
        this.eventEndWinSound = plugin.getConfig().getString("event_end_win_sound", "ENTITY_PLAYER_LEVELUP");
        this.eventEndNobodySound = plugin.getConfig().getString("event_end_nobody_sound", "ENTITY_PLAYER_LEVELUP");

        this.winTitleMain = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("win_title_main", "&aCongratulations!"));
        this.winTitleSub = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("win_title_sub", "&eYou won!"));
        this.winTitleFadeIn = plugin.getConfig().getInt("win_title_fade_in", 10);
        this.winTitleStay = plugin.getConfig().getInt("win_title_stay", 70);
        this.winTitleFadeOut = plugin.getConfig().getInt("win_title_fade_out", 20);

        // Load and reconstruct the reward item
        Material material = Material.getMaterial(plugin.getConfig().getString("event_reward_material", "IRON_INGOT"));
        this.eventRewardsCount = plugin.getConfig().getInt("event_rewards_count", 2);

        if (material != null) {
            this.eventReward = new ItemStack(material, 1);

            // Load NBT data from the config
            try {
                String data = plugin.getConfig().getString("event_reward_nbt");
                if (data != null) {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                    BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

                    // Read the serialized ItemStack
                    ItemStack itemStack = (ItemStack) dataInput.readObject();

                    // Set the ItemMeta of the eventReward to the ItemMeta of the itemStack
                    this.eventReward.setItemMeta(itemStack.getItemMeta());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.eventReward = new ItemStack(Material.AIR); // Fallback to AIR if material is invalid
        }

        // Initialize isEventActive
        this.isEventActive = false;

        // Register event listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Schedule the repeating task asynchronously
        new BukkitRunnable() {
            @Override
            public void run() {
                startScramblerEvent();
            }
        }.runTaskTimerAsynchronously(plugin, 0, timeBetweenEachEvent * 20L); // 20 ticks = 1 second
    }

    private void startScramblerEvent() {
        if (isEventActive) {
            return; // Don't start a new event if one is already active
        }

        Random random = new Random();
        currentWord = words.get(random.nextInt(words.size()));
        scrambledWord = scrambleWord(currentWord);
        isEventActive = true;

        // Fetch color codes from config
        String scrambledWordColor = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("scrambled_word_color", ""));
        String defaultColor = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("default_color", ""));

        // Announce the scrambled word
        Bukkit.broadcastMessage(eventPrefix + defaultColor + eventStartMessage
                .replace("%scrambledword%", scrambledWordColor + scrambledWord + defaultColor)
                .replace("%unscrambledword%", scrambledWordColor + currentWord + defaultColor));
        // play the sound effect
        playSoundForAll(eventStartSound);

        // Start the timer for answering
        startTime = System.currentTimeMillis();

        endTimer = new BukkitRunnable() {
            @Override
            public void run() {
                // If time is up and no one has guessed the word
                if (isEventActive && (System.currentTimeMillis() - startTime) >= timeForAnswering * 1000L) {
                    endEvent(null, 0);
                }
            }
        };
        endTimer.runTaskTimerAsynchronously(plugin, 0, 20L); // Check every second
    }

    private String scrambleWord(String word) {
        List<Character> characters = new ArrayList<>();
        for (char c : word.toCharArray()) {
            characters.add(c);
        }
        Collections.shuffle(characters);
        StringBuilder scrambled = new StringBuilder();
        for (char c : characters) {
            scrambled.append(c);
        }
        return scrambled.toString();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!isEventActive) {
            return; // Ignore chat messages if no event is active
        }

        String message = caseSensitive ? event.getMessage() : event.getMessage().toLowerCase();
        String wordToCheck = caseSensitive ? currentWord : currentWord.toLowerCase();

        if (message.contains(wordToCheck)) {
            long timeTaken = System.currentTimeMillis() - startTime;
            event.setCancelled(true); // Cancel the event to prevent further processing
            Bukkit.getScheduler().runTask(plugin, () -> endEvent(event.getPlayer(), timeTaken));
        }
    }

    private void endEvent(Player winner, long timeTaken) {
        if (!isEventActive) {
            return; // Prevent multiple calls to endEvent
        }

        isEventActive = false;
        endTimer.cancel(); // Cancel the end timer

        if (winner != null) {
            playerFoundWord(winner, timeTaken);
            playSoundForAll(eventEndWinSound);
        } else {
            // Fetch color codes from config
            String scrambledWordColor = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("scrambled_word_color", ""));
            String defaultColor = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("default_color", ""));

            playSoundForAll(eventEndNobodySound);

            Bukkit.broadcastMessage(eventPrefix + defaultColor + eventEndNobody
                    .replace("%scrambledword%", scrambledWordColor + scrambledWord + defaultColor)
                    .replace("%unscrambledword%", scrambledWordColor + currentWord + defaultColor));
        }
    }

    private void playerFoundWord(Player player, long timeTaken) {
        long minutes = (timeTaken / 1000) / 60;
        long seconds = (timeTaken / 1000) % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);

        // Fetch color codes from config
        String playerColor = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("player_color", "&a&l"));
        String timeColor = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("time_color", "&a&l"));
        String defaultColor = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("default_color", "&a&l"));

        Bukkit.broadcastMessage(eventPrefix + defaultColor + eventEndWin
                .replace("%player%", playerColor + player.getName() + defaultColor)
                .replace("%time%", timeColor + timeString + defaultColor)
                .replace("%scrambledword%", playerColor + scrambledWord + defaultColor)
                .replace("%unscrambledword%", playerColor + currentWord + defaultColor));

        // Créez et envoyez le titre
        String titleMain = winTitleMain
                .replace("%player%", player.getName())
                .replace("%time%", timeString)
                .replace("%unscrambledword%", currentWord);

        String titleSub = winTitleSub
                .replace("%player%", player.getName())
                .replace("%time%", timeString)
                .replace("%unscrambledword%", currentWord);

        player.sendTitle(
                titleMain,
                titleSub,
                winTitleFadeIn,
                winTitleStay,
                winTitleFadeOut
        );

        // Jouer le son
        try {
            Sound sound = Sound.valueOf(winSound);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound configured: " + winSound);
            // Son par défaut si le son configuré est invalide
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }

        // Create a new ItemStack with the NBT data
        ItemStack rewardItem = eventReward.clone();
        rewardItem.setAmount(eventRewardsCount);

        // Give reward to the player
        player.getInventory().addItem(rewardItem);
    }


    private void playSoundForAll(String soundName) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Sound sound;
            try {
                sound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid sound configured: " + soundName);
                sound = Sound.ENTITY_PLAYER_LEVELUP; // Default sound
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
            }
        });
    }

}