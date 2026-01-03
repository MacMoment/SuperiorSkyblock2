package com.bgsoftware.superiorskyblock.core.menu.button.impl;

import com.bgsoftware.superiorskyblock.api.menu.button.MenuTemplateButton;
import com.bgsoftware.superiorskyblock.api.menu.button.PagedMenuTemplateButton;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.commands.admin.CmdAdminSetPlayerStats;
import com.bgsoftware.superiorskyblock.core.formatting.Formatters;
import com.bgsoftware.superiorskyblock.core.itemstack.ItemBuilder;
import com.bgsoftware.superiorskyblock.core.menu.button.AbstractPagedMenuButton;
import com.bgsoftware.superiorskyblock.core.menu.button.PagedMenuTemplateButtonImpl;
import com.bgsoftware.superiorskyblock.core.menu.impl.MenuTeamInfo;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TeamInfoMemberButton extends AbstractPagedMenuButton<MenuTeamInfo.View, SuperiorPlayer> {

    private TeamInfoMemberButton(MenuTemplateButton<MenuTeamInfo.View> templateButton, MenuTeamInfo.View menuView) {
        super(templateButton, menuView);
    }

    @Override
    public void onButtonClick(InventoryClickEvent clickEvent) {
        // No action on click - just displays info
    }

    @Override
    public ItemStack modifyViewItem(ItemStack buttonItem) {
        if (pagedObject == null)
            return buttonItem;
        
        // Get member rank in the team
        List<SuperiorPlayer> members = menuView.getIsland().getIslandMembers(true);
        int rank = members.indexOf(pagedObject) + 1;
        
        // Get player stats
        long farming = CmdAdminSetPlayerStats.getFarmingStat(pagedObject);
        long fishing = CmdAdminSetPlayerStats.getFishingStat(pagedObject);
        long money = CmdAdminSetPlayerStats.getMoneyStat(pagedObject);
        long tokens = CmdAdminSetPlayerStats.getTokensStat(pagedObject);
        
        // Calculate points
        long farmingPoints = CmdAdminSetPlayerStats.calculateFarmingPoints(farming);
        long fishingPoints = CmdAdminSetPlayerStats.calculateFishingPoints(fishing);
        long moneyPoints = CmdAdminSetPlayerStats.calculateMoneyPoints(money);
        long tokensPoints = CmdAdminSetPlayerStats.calculateTokensPoints(tokens);
        long totalPoints = farmingPoints + fishingPoints + moneyPoints + tokensPoints;
        
        return new ItemBuilder(buttonItem)
                .replaceAll("{0}", pagedObject.getName())
                .replaceAll("{1}", pagedObject.getPlayerRole() + "")
                .replaceAll("{2}", String.valueOf(rank))
                .replaceAll("{3}", Formatters.NUMBER_FORMATTER.format(farming))
                .replaceAll("{4}", String.valueOf(farmingPoints))
                .replaceAll("{5}", Formatters.NUMBER_FORMATTER.format(fishing))
                .replaceAll("{6}", String.valueOf(fishingPoints))
                .replaceAll("{7}", Formatters.NUMBER_FORMATTER.format(money))
                .replaceAll("{8}", String.valueOf(moneyPoints))
                .replaceAll("{9}", Formatters.NUMBER_FORMATTER.format(tokens))
                .replaceAll("{10}", String.valueOf(tokensPoints))
                .replaceAll("{11}", String.valueOf(totalPoints))
                .asSkullOf(pagedObject)
                .build(pagedObject);
    }

    public static class Builder extends PagedMenuTemplateButtonImpl.AbstractBuilder<MenuTeamInfo.View, SuperiorPlayer> {

        @Override
        public PagedMenuTemplateButton<MenuTeamInfo.View, SuperiorPlayer> build() {
            return new PagedMenuTemplateButtonImpl<>(buttonItem, clickSound, commands, requiredPermission,
                    lackPermissionSound, nullItem, getButtonIndex(), TeamInfoMemberButton.class,
                    TeamInfoMemberButton::new);
        }

    }

}
