package com.bgsoftware.superiorskyblock.commands.admin;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.commands.CommandTabCompletes;
import com.bgsoftware.superiorskyblock.commands.ISuperiorCommand;
import com.bgsoftware.superiorskyblock.commands.arguments.CommandArguments;
import com.bgsoftware.superiorskyblock.commands.arguments.IslandArgument;
import com.bgsoftware.superiorskyblock.core.messages.Message;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdAdminSetTeamPoints implements ISuperiorCommand {

    @Override
    public List<String> getAliases() {
        return Arrays.asList("setteampoints", "stp");
    }

    @Override
    public String getPermission() {
        return "superior.admin.setteampoints";
    }

    @Override
    public String getUsage(java.util.Locale locale) {
        return "admin setteampoints <" +
                Message.COMMAND_ARGUMENT_PLAYER_NAME.getMessage(locale) + "/" +
                Message.COMMAND_ARGUMENT_ISLAND_NAME.getMessage(locale) + "> <" +
                Message.COMMAND_ARGUMENT_AMOUNT.getMessage(locale) + ">";
    }

    @Override
    public String getDescription(java.util.Locale locale) {
        return "Set team points for an island.";
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

        if (amount < 0) {
            Message.INVALID_AMOUNT.send(sender, args[3]);
            return;
        }

        // Set team points
        CmdAdminGiveTeamPoints.setTeamPoints(island, amount);

        // Send confirmation message
        Message.CUSTOM.send(sender, "&a&lTeam Points | &7Successfully set team points for &f" + 
                island.getName() + " &7to &f" + amount + "&7.", true);
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 3) {
            return CommandTabCompletes.getOnlinePlayersAndIslands(plugin, args[2], false, null);
        }
        return Collections.emptyList();
    }
}
