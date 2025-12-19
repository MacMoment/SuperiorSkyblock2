package com.bgsoftware.superiorskyblock.commands.admin;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.persistence.PersistentDataContainer;
import com.bgsoftware.superiorskyblock.api.persistence.PersistentDataType;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.commands.CommandTabCompletes;
import com.bgsoftware.superiorskyblock.commands.ISuperiorCommand;
import com.bgsoftware.superiorskyblock.commands.arguments.CommandArguments;
import com.bgsoftware.superiorskyblock.core.messages.Message;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdAdminSetPlayerStats implements ISuperiorCommand {

    // Keys for player stats stored in player persistent data
    public static final String FARMING_STAT_KEY = "genphoria_farming_stat";
    public static final String FISHING_STAT_KEY = "genphoria_fishing_stat";
    public static final String MONEY_STAT_KEY = "genphoria_money_stat";
    public static final String TOKENS_STAT_KEY = "genphoria_tokens_stat";

    @Override
    public List<String> getAliases() {
        return Arrays.asList("setplayerstats", "sps", "setstats");
    }

    @Override
    public String getPermission() {
        return "superior.admin.setplayerstats";
    }

    @Override
    public String getUsage(java.util.Locale locale) {
        return "admin setplayerstats <" +
                Message.COMMAND_ARGUMENT_PLAYER_NAME.getMessage(locale) + "> <farming/fishing/money/tokens> <" +
                Message.COMMAND_ARGUMENT_AMOUNT.getMessage(locale) + ">";
    }

    @Override
    public String getDescription(java.util.Locale locale) {
        return "Set player stats for leaderboard tracking.";
    }

    @Override
    public int getMinArgs() {
        return 5;
    }

    @Override
    public int getMaxArgs() {
        return 5;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return true;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        SuperiorPlayer targetPlayer = CommandArguments.getPlayer(plugin, sender, args[2]);

        if (targetPlayer == null)
            return;

        String statType = args[3].toLowerCase();
        long amount;
        try {
            amount = Long.parseLong(args[4]);
        } catch (NumberFormatException ex) {
            Message.INVALID_AMOUNT.send(sender, args[4]);
            return;
        }

        if (amount < 0) {
            Message.INVALID_AMOUNT.send(sender, args[4]);
            return;
        }

        String statKey;
        String statName;
        switch (statType) {
            case "farming":
                statKey = FARMING_STAT_KEY;
                statName = "Farming";
                break;
            case "fishing":
                statKey = FISHING_STAT_KEY;
                statName = "Fishing";
                break;
            case "money":
                statKey = MONEY_STAT_KEY;
                statName = "Money";
                break;
            case "tokens":
                statKey = TOKENS_STAT_KEY;
                statName = "Tokens";
                break;
            default:
                Message.CUSTOM.send(sender, "&c&lError | &7Invalid stat type. Use: farming, fishing, money, or tokens", true);
                return;
        }

        // Set the stat
        setPlayerStat(targetPlayer, statKey, amount);

        // Send confirmation message
        Message.CUSTOM.send(sender, "&a&lStats | &7Successfully set &f" + statName + " &7stat for &f" + 
                targetPlayer.getName() + " &7to &f" + amount + "&7.", true);
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 3) {
            return CommandTabCompletes.getOnlinePlayers(plugin, args[2], false);
        } else if (args.length == 4) {
            return CommandTabCompletes.getCustomComplete(args[3], "farming", "fishing", "money", "tokens");
        }
        return Collections.emptyList();
    }

    /**
     * Get a player's stat value.
     * @param player The player to get the stat for.
     * @param statKey The stat key.
     * @return The stat value.
     */
    public static long getPlayerStat(SuperiorPlayer player, String statKey) {
        if (player == null) return 0;
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        return dataContainer.getOrDefault(statKey, PersistentDataType.LONG, 0L);
    }

    /**
     * Set a player's stat value.
     * @param player The player to set the stat for.
     * @param statKey The stat key.
     * @param value The stat value.
     */
    public static void setPlayerStat(SuperiorPlayer player, String statKey, long value) {
        if (player == null) return;
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        dataContainer.put(statKey, PersistentDataType.LONG, value);
        player.savePersistentDataContainer();
    }

    /**
     * Add to a player's stat value.
     * @param player The player to add the stat to.
     * @param statKey The stat key.
     * @param amount The amount to add.
     * @return The new stat value.
     */
    public static long addPlayerStat(SuperiorPlayer player, String statKey, long amount) {
        if (player == null) return 0;
        long current = getPlayerStat(player, statKey);
        long newValue = current + amount;
        setPlayerStat(player, statKey, newValue);
        return newValue;
    }

    /**
     * Get the farming stat for a player.
     */
    public static long getFarmingStat(SuperiorPlayer player) {
        return getPlayerStat(player, FARMING_STAT_KEY);
    }

    /**
     * Get the fishing stat for a player.
     */
    public static long getFishingStat(SuperiorPlayer player) {
        return getPlayerStat(player, FISHING_STAT_KEY);
    }

    /**
     * Get the money stat for a player.
     */
    public static long getMoneyStat(SuperiorPlayer player) {
        return getPlayerStat(player, MONEY_STAT_KEY);
    }

    /**
     * Get the tokens stat for a player.
     */
    public static long getTokensStat(SuperiorPlayer player) {
        return getPlayerStat(player, TOKENS_STAT_KEY);
    }

    /**
     * Calculate points for farming (1 point per 10k farmed).
     */
    public static long calculateFarmingPoints(long farmingStat) {
        return farmingStat / 10000;
    }

    /**
     * Calculate points for fishing (1 point per 100 fish caught).
     */
    public static long calculateFishingPoints(long fishingStat) {
        return fishingStat / 100;
    }

    /**
     * Calculate points for money (1 point per 10 million).
     */
    public static long calculateMoneyPoints(long moneyStat) {
        return moneyStat / 10000000;
    }

    /**
     * Calculate points for tokens (1 point per 10k tokens).
     */
    public static long calculateTokensPoints(long tokensStat) {
        return tokensStat / 10000;
    }

    /**
     * Get the total team farming stat for an island.
     */
    public static long getTeamFarmingStat(Island island) {
        if (island == null) return 0;
        long total = 0;
        for (SuperiorPlayer member : island.getIslandMembers(true)) {
            total += getFarmingStat(member);
        }
        return total;
    }

    /**
     * Get the total team fishing stat for an island.
     */
    public static long getTeamFishingStat(Island island) {
        if (island == null) return 0;
        long total = 0;
        for (SuperiorPlayer member : island.getIslandMembers(true)) {
            total += getFishingStat(member);
        }
        return total;
    }

    /**
     * Get the total team money stat for an island.
     */
    public static long getTeamMoneyStat(Island island) {
        if (island == null) return 0;
        long total = 0;
        for (SuperiorPlayer member : island.getIslandMembers(true)) {
            total += getMoneyStat(member);
        }
        return total;
    }

    /**
     * Get the total team tokens stat for an island.
     */
    public static long getTeamTokensStat(Island island) {
        if (island == null) return 0;
        long total = 0;
        for (SuperiorPlayer member : island.getIslandMembers(true)) {
            total += getTokensStat(member);
        }
        return total;
    }

    /**
     * Calculate total team points for an island.
     */
    public static long calculateTotalTeamPoints(Island island) {
        if (island == null) return 0;
        long farmingPoints = calculateFarmingPoints(getTeamFarmingStat(island));
        long fishingPoints = calculateFishingPoints(getTeamFishingStat(island));
        long moneyPoints = calculateMoneyPoints(getTeamMoneyStat(island));
        long tokensPoints = calculateTokensPoints(getTeamTokensStat(island));
        return farmingPoints + fishingPoints + moneyPoints + tokensPoints;
    }
}
