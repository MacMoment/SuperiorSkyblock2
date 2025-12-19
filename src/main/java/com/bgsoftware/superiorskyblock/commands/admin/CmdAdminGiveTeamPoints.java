package com.bgsoftware.superiorskyblock.commands.admin;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.persistence.PersistentDataContainer;
import com.bgsoftware.superiorskyblock.api.persistence.PersistentDataType;
import com.bgsoftware.superiorskyblock.commands.CommandTabCompletes;
import com.bgsoftware.superiorskyblock.commands.ISuperiorCommand;
import com.bgsoftware.superiorskyblock.commands.arguments.CommandArguments;
import com.bgsoftware.superiorskyblock.commands.arguments.IslandArgument;
import com.bgsoftware.superiorskyblock.core.messages.Message;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdAdminGiveTeamPoints implements ISuperiorCommand {

    public static final String TEAM_POINTS_KEY = "genphoria_team_points";

    @Override
    public List<String> getAliases() {
        return Arrays.asList("giveteampoints", "gtp", "addteampoints");
    }

    @Override
    public String getPermission() {
        return "superior.admin.giveteampoints";
    }

    @Override
    public String getUsage(java.util.Locale locale) {
        return "admin giveteampoints <" +
                Message.COMMAND_ARGUMENT_PLAYER_NAME.getMessage(locale) + "/" +
                Message.COMMAND_ARGUMENT_ISLAND_NAME.getMessage(locale) + "> <" +
                Message.COMMAND_ARGUMENT_AMOUNT.getMessage(locale) + ">";
    }

    @Override
    public String getDescription(java.util.Locale locale) {
        return "Give team points to an island.";
    }

    @Override
    public int getMinArgs() {
        return 4;
    }

    @Override
    public int getMaxArgs() {
        return 4;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return true;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        IslandArgument arguments = CommandArguments.getIsland(plugin, sender, args[2]);

        Island island = arguments.getIsland();

        if (island == null)
            return;

        int amount;
        try {
            amount = Integer.parseInt(args[3]);
        } catch (NumberFormatException ex) {
            Message.INVALID_AMOUNT.send(sender, args[3]);
            return;
        }

        // Get current team points
        PersistentDataContainer dataContainer = island.getPersistentDataContainer();
        int currentPoints = dataContainer.getOrDefault(TEAM_POINTS_KEY, PersistentDataType.INTEGER, 0);
        
        // Add new points
        int newPoints = currentPoints + amount;
        dataContainer.put(TEAM_POINTS_KEY, PersistentDataType.INTEGER, newPoints);
        island.savePersistentDataContainer();

        // Send confirmation message
        Message.CUSTOM.send(sender, "&a&lTeam Points | &7Successfully gave &f" + amount + 
                " &7team points to &f" + island.getName() + "&7. New total: &f" + newPoints, true);
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 3) {
            return CommandTabCompletes.getOnlinePlayersAndIslands(plugin, args[2], false, null);
        }
        return Collections.emptyList();
    }

    /**
     * Get the team points for an island.
     * @param island The island to get points for.
     * @return The number of team points.
     */
    public static int getTeamPoints(Island island) {
        if (island == null) return 0;
        PersistentDataContainer dataContainer = island.getPersistentDataContainer();
        return dataContainer.getOrDefault(TEAM_POINTS_KEY, PersistentDataType.INTEGER, 0);
    }

    /**
     * Set the team points for an island.
     * @param island The island to set points for.
     * @param points The number of team points.
     */
    public static void setTeamPoints(Island island, int points) {
        if (island == null) return;
        PersistentDataContainer dataContainer = island.getPersistentDataContainer();
        dataContainer.put(TEAM_POINTS_KEY, PersistentDataType.INTEGER, points);
        island.savePersistentDataContainer();
    }

    /**
     * Add team points to an island.
     * @param island The island to add points to.
     * @param points The number of team points to add.
     * @return The new total points.
     */
    public static int addTeamPoints(Island island, int points) {
        if (island == null) return 0;
        int currentPoints = getTeamPoints(island);
        int newPoints = currentPoints + points;
        setTeamPoints(island, newPoints);
        return newPoints;
    }

    /**
     * Remove team points from an island.
     * @param island The island to remove points from.
     * @param points The number of team points to remove.
     * @return The new total points.
     */
    public static int removeTeamPoints(Island island, int points) {
        if (island == null) return 0;
        int currentPoints = getTeamPoints(island);
        int newPoints = Math.max(0, currentPoints - points);
        setTeamPoints(island, newPoints);
        return newPoints;
    }
}
