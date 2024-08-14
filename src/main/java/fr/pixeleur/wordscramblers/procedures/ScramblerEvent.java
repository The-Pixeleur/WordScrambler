package fr.pixeleur.wordscramblers.procedures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    private boolean wordGuessed;
    private BukkitRunnable endTimer;

    public ScramblerEvent(JavaPlugin plugin) {
        this.plugin = plugin;

        // Load configuration values
        this.eventPrefix = ChatColor.RED + plugin.getConfig().getString("event_prefix");
        this.timeForAnswering = plugin.getConfig().getInt("time_for_answering") * 60; // Convert minutes to seconds
        int timeBetweenEachEvent = plugin.getConfig().getInt("time_between_each_event") * 60; // Convert minutes to seconds
        this.caseSensitive = plugin.getConfig().getBoolean("case_sensitive");
        this.words = plugin.getConfig().getStringList("Words");
        this.eventStartMessage = plugin.getConfig().getString("event_start_message");
        this.eventEndWin = plugin.getConfig().getString("event_end_win");
        this.eventEndNobody = plugin.getConfig().getString("event_end_nobody");

        // Load and reconstruct the reward item
        Material material = Material.getMaterial(Objects.requireNonNull(plugin.getConfig().getString("event_reward_material")));
        int amount = plugin.getConfig().getInt("event_reward_amount");

        if (material != null) {
            this.eventReward = new ItemStack(material, amount);

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

        this.eventRewardsCount = plugin.getConfig().getInt("event_rewards_count");

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
        Random random = new Random();
        currentWord = words.get(random.nextInt(words.size()));
        scrambledWord = scrambleWord(currentWord);
        wordGuessed = false;

        // Announce the scrambled word
        Bukkit.broadcastMessage(eventPrefix + ChatColor.YELLOW + eventStartMessage
                .replace("%scrambledword%", ChatColor.GREEN + scrambledWord + ChatColor.YELLOW)
                .replace("%unscrambledword%", ChatColor.GREEN + currentWord + ChatColor.YELLOW));

        // Start the timer for answering
        startTime = System.currentTimeMillis();

        endTimer = new BukkitRunnable() {
            @Override
            public void run() {
                // If time is up and no one has guessed the word
                if (!wordGuessed && (System.currentTimeMillis() - startTime) >= timeForAnswering * 1000L) {
                    assert eventEndNobody != null;
                    Bukkit.broadcastMessage(eventPrefix + ChatColor.YELLOW + eventEndNobody
                            .replace("%scrambledword%", ChatColor.GREEN + scrambledWord + ChatColor.YELLOW)
                            .replace("%unscrambledword%", ChatColor.GREEN + currentWord + ChatColor.YELLOW));

                    cancel();
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
        String message = caseSensitive ? event.getMessage() : event.getMessage().toLowerCase();
        String wordToCheck = caseSensitive ? currentWord : currentWord.toLowerCase();

        if (message.contains(wordToCheck)) {
            long timeTaken = System.currentTimeMillis() - startTime;
            wordGuessed = true;
            endTimer.cancel(); // Cancel the end timer
            playerFoundWord(event.getPlayer(), timeTaken);
            event.setCancelled(true); // Cancel the event to prevent further processing
        }
    }

    private void playerFoundWord(Player player, long timeTaken) {
        long minutes = (timeTaken / 1000) / 60;
        long seconds = (timeTaken / 1000) % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        Bukkit.broadcastMessage(eventPrefix + ChatColor.YELLOW + eventEndWin
                .replace("%player%", ChatColor.GREEN + player.getName() + ChatColor.YELLOW)
                .replace("%time%", ChatColor.GREEN + timeString + ChatColor.YELLOW)
                .replace("%scrambledword%", ChatColor.GREEN + scrambledWord + ChatColor.YELLOW)
                .replace("%unscrambledword%", ChatColor.GREEN + currentWord + ChatColor.YELLOW));

        // Create a new ItemStack with the NBT data
        ItemStack rewardItem = eventReward.clone();
        rewardItem.setAmount(eventRewardsCount);

        // Give reward to the player
        Bukkit.getScheduler().runTask(plugin, () -> player.getInventory().addItem(rewardItem));
    }
}
