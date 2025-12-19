package com.bgsoftware.superiorskyblock.commands.player;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.commands.CommandTabCompletes;
import com.bgsoftware.superiorskyblock.commands.ISuperiorCommand;
import com.bgsoftware.superiorskyblock.commands.admin.CmdAdminGiveTeamPoints;
import com.bgsoftware.superiorskyblock.commands.admin.CmdAdminSetPlayerStats;
import com.bgsoftware.superiorskyblock.commands.arguments.CommandArguments;
import com.bgsoftware.superiorskyblock.core.formatting.Formatters;
import com.bgsoftware.superiorskyblock.core.messages.Message;
import com.bgsoftware.superiorskyblock.player.PlayerLocales;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdTeamInfo implements ISuperiorCommand {

    @Override
    public List<String> getAliases() {
        return Arrays.asList("teaminfo", "tinfo", "ti");
    }

    @Override
    public String getPermission() {
        return "superior.island.teaminfo";
    }

    @Override
    public String getUsage(java.util.Locale locale) {
        return "teaminfo [" +
                Message.COMMAND_ARGUMENT_PLAYER_NAME.getMessage(locale) + "/" +
                Message.COMMAND_ARGUMENT_ISLAND_NAME.getMessage(locale) + "]";
    }

    @Override
    public String getDescription(java.util.Locale locale) {
        return "View detailed team statistics and member contributions.";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return true;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        Island island = args.length == 1 ? CommandArguments.getIslandWhereStanding(plugin, sender).getIsland() :
                CommandArguments.getIsland(plugin, sender, args[1]).getIsland();

        if (island == null)
            return;

        java.util.Locale locale = PlayerLocales.getLocale(sender);

        StringBuilder infoMessage = new StringBuilder();
        
        // Header
        infoMessage.append("\n");
        infoMessage.append("&8&m----------------------------------------\n");
        infoMessage.append("<#97CFF5>&lTeam Information\n");
        infoMessage.append("&8&m----------------------------------------\n");
        infoMessage.append("\n");
        
        // Basic Info
        String teamName = island.getName().isEmpty() ? island.getOwner().getName() + "'s Team" : island.getName();
        infoMessage.append("<#97CFF5>&l| &fTeam: &7").append(teamName).append("\n");
        infoMessage.append("<#97CFF5>&l| &fOwner: &7").append(island.getOwner().getName()).append("\n");
        infoMessage.append("<#97CFF5>&l| &fMembers: &7").append(island.getIslandMembers(true).size()).append("\n");
        infoMessage.append("\n");
        
        // Team Points
        int teamPoints = CmdAdminGiveTeamPoints.getTeamPoints(island);
        infoMessage.append("<#97CFF5>&l| &fTeam Points: &a").append(Formatters.NUMBER_FORMATTER.format(teamPoints)).append("\n");
        infoMessage.append("\n");
        
        // Team Stats
        long teamFarming = CmdAdminSetPlayerStats.getTeamFarmingStat(island);
        long teamFishing = CmdAdminSetPlayerStats.getTeamFishingStat(island);
        long teamMoney = CmdAdminSetPlayerStats.getTeamMoneyStat(island);
        long teamTokens = CmdAdminSetPlayerStats.getTeamTokensStat(island);
        
        long farmingPoints = CmdAdminSetPlayerStats.calculateFarmingPoints(teamFarming);
        long fishingPoints = CmdAdminSetPlayerStats.calculateFishingPoints(teamFishing);
        long moneyPoints = CmdAdminSetPlayerStats.calculateMoneyPoints(teamMoney);
        long tokensPoints = CmdAdminSetPlayerStats.calculateTokensPoints(teamTokens);
        long totalPoints = farmingPoints + fishingPoints + moneyPoints + tokensPoints;
        
        infoMessage.append("<#97CFF5>&lTeam Statistics\n");
        infoMessage.append("<#97CFF5>&l| &fFarming: &7").append(Formatters.NUMBER_FORMATTER.format(teamFarming))
                .append(" &8| &a").append(farmingPoints).append(" &2points\n");
        infoMessage.append("<#97CFF5>&l| &fFishing: &7").append(Formatters.NUMBER_FORMATTER.format(teamFishing))
                .append(" &8| &a").append(fishingPoints).append(" &2points\n");
        infoMessage.append("<#97CFF5>&l| &fMoney: &7$").append(Formatters.NUMBER_FORMATTER.format(teamMoney))
                .append(" &8| &a").append(moneyPoints).append(" &2points\n");
        infoMessage.append("<#97CFF5>&l| &fTokens: &7").append(Formatters.NUMBER_FORMATTER.format(teamTokens))
                .append(" &8| &a").append(tokensPoints).append(" &2points\n");
        infoMessage.append("<#97CFF5>&l| &fTotal Leaderboard Points: &a").append(totalPoints).append("\n");
        infoMessage.append("\n");
        
        // Member Contributions
        infoMessage.append("<#97CFF5>&lMember Contributions\n");
        List<SuperiorPlayer> members = island.getIslandMembers(true);
        int rank = 1;
        for (SuperiorPlayer member : members) {
            long memberFarming = CmdAdminSetPlayerStats.getFarmingStat(member);
            long memberFishing = CmdAdminSetPlayerStats.getFishingStat(member);
            long memberMoney = CmdAdminSetPlayerStats.getMoneyStat(member);
            long memberTokens = CmdAdminSetPlayerStats.getTokensStat(member);
            
            long memberFarmingPts = CmdAdminSetPlayerStats.calculateFarmingPoints(memberFarming);
            long memberFishingPts = CmdAdminSetPlayerStats.calculateFishingPoints(memberFishing);
            long memberMoneyPts = CmdAdminSetPlayerStats.calculateMoneyPoints(memberMoney);
            long memberTokensPts = CmdAdminSetPlayerStats.calculateTokensPoints(memberTokens);
            long memberTotalPts = memberFarmingPts + memberFishingPts + memberMoneyPts + memberTokensPts;
            
            infoMessage.append("&8[&7#&f").append(rank).append("&8] <#97CFF5>").append(member.getName())
                    .append(" &8- &a").append(memberTotalPts).append(" &2points\n");
            rank++;
        }
        
        infoMessage.append("\n");
        infoMessage.append("&8&m----------------------------------------\n");
        infoMessage.append("&7Point System: &f1pt/10k farmed &8| &f1pt/100 fish\n");
        infoMessage.append("&f1pt/10M money &8| &f1pt/10k tokens\n");
        infoMessage.append("&8&m----------------------------------------\n");

        Message.CUSTOM.send(sender, infoMessage.toString(), false);
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        return args.length == 2 ? CommandTabCompletes.getPlayerIslandsExceptSender(plugin, sender, args[1],
                plugin.getSettings().isTabCompleteHideVanished()) : Collections.emptyList();
    }
}
