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
# ####################################################################################
#  __          __           _    _____                          _     _             #
#  \ \        / /          | |  / ____|                        | |   | |            #
#   \ \  /\  / /__  _ __ __| | | (___   ___ _ __ __ _ _ __ ___ | |__ | | ___ _ __   #
#    \ \/  \/ / _ \| '__/ _` |  \___ \ / __| '__/ _` | '_ ` _ \| '_ \| |/ _ \ '__|  #
#     \  /\  / (_) | | | (_| |  ____) | (__| | | (_| | | | | | | |_) | |  __/ |     #
#      \/  \/ \___/|_|  \__,_| |_____/ \___|_|  \__,_|_| |_| |_|_.__/|_|\___|_|     #
# ####################################################################################
# Made by Pixeleur game studio - 2024
# Discord server support link : https://discord.com/invite/2pssCZR
# tutorial link : PUT SPIGOT LINK HERE

# The plugin prefix
event_prefix: '[Word Scrambler]'

# Event message
event_start_message: ' First to find the word ''%scrambledword%'' will win the game.'
event_end_win: ' user %player% found the word in %time%!'
event_end_nobody: ' Nobody found the word. It was ''%unscrambledword%'''

# Time allowed for answering the scrambled word (in minutes)
time_for_answering: 2

# Time between each scrambler event (in minutes)
time_between_each_event: 15

# Is it case sensitive (Should uppercase be required or not)
case_sensitive: false # False = for word "Test" : 'test' & 'Test' & 'tESt' will work for example
                      # True = for word "Test" : 'test' & 'tESt' will not work. Only 'Test' will work

# The reward item and how many of this item should be given
event_rewards_count: 5

event_reward_material: IRON_INGOT
event_reward_nbt: "rO0ABXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGph\r\
  \nYmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAk\r\
  \nU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAkwABGtleXN0ABJMamF2YS9sYW5nL09iamVjdDtMAAZ2\r\
  \nYWx1ZXNxAH4ABHhwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAABHQAAj09\r\
  \ndAABdnQABHR5cGV0AAZhbW91bnR1cQB+AAYAAAAEdAAeb3JnLmJ1a2tpdC5pbnZlbnRvcnkuSXRl\r\
  \nbVN0YWNrc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxh\r\
  \nbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAADv90AApJUk9OX0lOR09Uc3EAfgAOAAAABQ==\r\n"

# List of words for the scrambling game
Words:
- Pixeleur
- Minecraft
- Java
- Spigot
- Pneumonoultramicroscopicsilicovolcanoconiosis
