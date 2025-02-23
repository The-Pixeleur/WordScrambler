# Word Scrambler Plugin for Spigot

![Word Scrambler](https://pixeleur.fr/IMAGES/iconJeux/Plugin_WordScramblerTitle.png)

## Overview

Word Scrambler is a fun and engaging chat game for Minecraft servers using Spigot. Every 15 minutes (customizable), a random word appears in the chat, and players race to unscramble it. The fastest player wins a reward! Customize the game to your liking with various options in the `config.yml` file.

![Word Scrambler in action](https://pixeleur.fr/IMAGES/iconJeux/Plugin_wordScramblerGif.gif)

## Features

- **Automatic Events**: A new word scrambler event starts every 15 minutes (configurable).
- **Customizable Rewards**: Use any in-game item as a reward.
- **Flexible Configuration**: Adjust the game settings to fit your server's needs.
- **Player Engagement**: A great way to keep your players entertained and active.

## Configuration

You can customize the following settings in the `config.yml` file:

- **Reward Item**: Define the item given as a reward.
- **Scrambled Words**: Set the words that will be scrambled.
- **Event Messages**: Customize the messages displayed during the event.
- **Plugin Prefix**: Change the prefix for plugin messages.
- **Answer Time**: Set the time allowed for players to answer.
- **Event Interval**: Adjust the time between each scrambler event.
- **Case Sensitivity**: Toggle case sensitivity for word answers.

## Installation

1. **Download the plugin**: [Word Scrambler Plugin](PUT_SPIGOT_LINK_HERE)
2. **Place the plugin**: Put the downloaded JAR file into your server's `plugins` folder.
3. **Restart the server**: Restart your Minecraft server to load the plugin.

## Commands

- **/wordrewardregister**: Use this command with an item in your hand to set it as the reward for the game.

## Example Configuration

```yaml
# ###################################################################################
#  __          __           _    _____                          _     _             #
#  \ \        / /          | |  / ____|                        | |   | |            #
#   \ \  /\  / /__  _ __ __| | | (___   ___ _ __ __ _ _ __ ___ | |__ | | ___ _ __   #
#    \ \/  \/ / _ \| '__/ _` |  \___ \ / __| '__/ _` | '_ ` _ \| '_ \| |/ _ \ '__|  #
#     \  /\  / (_) | | | (_| |  ____) | (__| | | (_| | | | | | | |_) | |  __/ |     #
#      \/  \/ \___/|_|  \__,_| |_____/ \___|_|  \__,_|_| |_| |_|_.__/|_|\___|_|     #
# ###################################################################################
# Word Scrambler Plugin Configuration                                               #
# Developed by Pixeleur Game Studio - 2025                                          #
# Support: https://discord.com/invite/2pssCZR                                       #
# Tutorial: https://www.spigotmc.org/resources/word-scrambler-reward-game.118505/   #
# ###################################################################################

# ========================
#  Basic Settings
# ========================
# Plugin prefix used in chat messages
event_prefix: '&c[WORD SCRAMBLERS] '

# ========================
#  Event Configuration
# ========================
# Time settings (in minutes)
time_for_answering: 2        # Duration players have to solve the puzzle
time_between_each_event: 15  # Cooldown between consecutive games

# Case sensitivity settings
case_sensitive: false        # true = Exact case match required
                             # false = "TEST", "Test", and "test" all accepted
                             # (For word "Test": 'test' & 'tESt' would work when false)

# ========================
#  Game Messages
# ========================
event_start_message: '&eFirst to find the word ''&a%scrambledword%''&e, will win the game.'
event_end_win: '&a%player%&e found the word &2%unscrambledword%&e in &a%time%&e!'
event_end_nobody: '&eNobody found the word. It was: ''&a%unscrambledword%&e'''

# ========================
#  Sound Effects
# ========================
# Valid sound names: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html
# Set to 'NONE' to disable any sound
win_sound: ENTITY_FIREWORK_ROCKET_LARGE_BLAST       # Played to winner
event_start_sound: ENTITY_FIREWORK_ROCKET_LAUNCH    # Game start sound
event_end_win_sound: ENTITY_FIREWORK_ROCKET_TWINKLE # Win celebration
event_end_nobody_sound: BLOCK_NOTE_BLOCK_BASS       # Played when nobody wins

# ========================
#  Victory Title Settings
# ========================
# Title display timings are in ticks (20 ticks = 1 second)
win_title_main: '&6&lVICTORY!'         # Main title text
win_title_sub: '&b%unscrambledword% &ffound in &e%time%'  # Subtitle text
win_title_fade_in: 15    # Fade-in duration (0.75 seconds)
win_title_stay: 60       # Display duration (3 seconds)
win_title_fade_out: 20   # Fade-out duration (1 second)

# ========================
#  Reward Configuration
# ========================
event_rewards_count: 5          # Number of items to give as reward
event_reward_material: IRON_INGOT  # Material type for reward

# NBT data for custom items (Base64 encoded serialized ItemStack)
# WARNING: Modify only if you understand Bukkit NBT encoding!
# NOTE: this is auto generated when using the /wordrewardregister
event_reward_nbt: "rO0ABXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGph\r\
  \ndmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAk\r\
  \nU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAkwABGtleXN0ABJMamF2YS9sYW5nL09iamVjdDtMAAZ2\r\
  \nYWx1ZXNxAH4ABHhwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAAA3QAAj09\r\
  \ndAABdnQABHR5cGV1cQB+AAYAAAADdAAeb3JnLmJ1a2tpdC5pbnZlbnRvcnkuSXRlbVN0YWNrc3IA\r\
  \nEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVy\r\
  \nhqyVHQuU4IsCAAB4cAAADv90AApJUk9OX0lOR09U\r\n"

# ========================
#  Word Bank
# ========================
# Add/remove words below (avoid special characters)
# TIP: Keep words between 4-15 characters for best gameplay
Words:
  - Pixeleur
  - Minecraft
  - Java
  - Spigot
  # Fun example of long word (45 letters)
  - Pneumonoultramicroscopicsilicovolcanoconiosis
