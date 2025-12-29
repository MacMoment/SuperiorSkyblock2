package com.bgsoftware.superiorskyblock.island.upgrade.cost;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.upgrades.cost.UpgradeCost;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.commands.admin.CmdAdminGiveTeamPoints;

import java.math.BigDecimal;

public class TeamPointsUpgradeCost extends UpgradeCostAbstract {

    private static final SuperiorSkyblockPlugin plugin = SuperiorSkyblockPlugin.getPlugin();

    public TeamPointsUpgradeCost(BigDecimal value) {
        super(value, "teampoints");
    }

    @Override
    public boolean hasEnoughBalance(SuperiorPlayer superiorPlayer) {
        Island island = superiorPlayer.getIsland();
        if (island == null) return false;
        
        int teamPoints = CmdAdminGiveTeamPoints.getTeamPoints(island);
        return BigDecimal.valueOf(teamPoints).compareTo(cost) >= 0;
    }

    @Override
    public void withdrawCost(SuperiorPlayer superiorPlayer) {
        Island island = superiorPlayer.getIsland();
        if (island == null) return;
        
        int currentPoints = CmdAdminGiveTeamPoints.getTeamPoints(island);
        int costAmount = cost.intValue();
        int newPoints = Math.max(0, currentPoints - costAmount);
        CmdAdminGiveTeamPoints.setTeamPoints(island, newPoints);
    }

    @Override
    public UpgradeCost clone(BigDecimal cost) {
        return new TeamPointsUpgradeCost(cost);
    }

}
