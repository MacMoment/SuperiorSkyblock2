package com.bgsoftware.superiorskyblock.core.menu.impl;

import com.bgsoftware.common.annotations.Nullable;
import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.menu.Menu;
import com.bgsoftware.superiorskyblock.api.menu.view.MenuView;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.core.io.MenuParserImpl;
import com.bgsoftware.superiorskyblock.core.menu.AbstractPagedMenu;
import com.bgsoftware.superiorskyblock.core.menu.MenuIdentifiers;
import com.bgsoftware.superiorskyblock.core.menu.MenuParseResult;
import com.bgsoftware.superiorskyblock.core.menu.button.impl.TeamInfoMemberButton;
import com.bgsoftware.superiorskyblock.core.menu.view.AbstractPagedMenuView;
import com.bgsoftware.superiorskyblock.core.menu.view.IIslandMenuView;
import com.bgsoftware.superiorskyblock.core.menu.view.args.IslandViewArgs;
import com.bgsoftware.superiorskyblock.commands.admin.CmdAdminGiveTeamPoints;
import com.bgsoftware.superiorskyblock.commands.admin.CmdAdminSetPlayerStats;
import com.bgsoftware.superiorskyblock.core.formatting.Formatters;

import java.util.List;

public class MenuTeamInfo extends AbstractPagedMenu<MenuTeamInfo.View, IslandViewArgs, SuperiorPlayer> {

    private MenuTeamInfo(MenuParseResult<View> parseResult) {
        super(MenuIdentifiers.MENU_TEAM_INFO, parseResult, false);
    }

    @Override
    protected View createViewInternal(SuperiorPlayer superiorPlayer, IslandViewArgs args,
                                      @Nullable MenuView<?, ?> previousMenuView) {
        return new View(superiorPlayer, previousMenuView, this, args);
    }

    public void refreshViews(Island island) {
        refreshViews(view -> view.island.equals(island));
    }

    @Nullable
    public static MenuTeamInfo createInstance() {
        MenuParseResult<View> menuParseResult = MenuParserImpl.getInstance().loadMenu("team-info.yml",
                null, new TeamInfoMemberButton.Builder());
        return menuParseResult == null ? null : new MenuTeamInfo(menuParseResult);
    }

    public static class View extends AbstractPagedMenuView<MenuTeamInfo.View, IslandViewArgs, SuperiorPlayer> implements IIslandMenuView {

        private final Island island;

        View(SuperiorPlayer inventoryViewer, @Nullable MenuView<?, ?> previousMenuView,
             Menu<View, IslandViewArgs> menu, IslandViewArgs args) {
            super(inventoryViewer, previousMenuView, menu);
            this.island = args.getIsland();
        }

        @Override
        public Island getIsland() {
            return this.island;
        }

        @Override
        public String replaceTitle(String title) {
            String teamName = island.getName().isEmpty() ? island.getOwner().getName() + "'s Team" : island.getName();
            int teamPoints = CmdAdminGiveTeamPoints.getTeamPoints(island);
            long totalLeaderboardPoints = CmdAdminSetPlayerStats.calculateTotalTeamPoints(island);
            
            return title
                    .replace("{0}", teamName)
                    .replace("{1}", String.valueOf(island.getIslandMembers(true).size()))
                    .replace("{2}", Formatters.NUMBER_FORMATTER.format(teamPoints))
                    .replace("{3}", Formatters.NUMBER_FORMATTER.format(totalLeaderboardPoints));
        }

        @Override
        protected List<SuperiorPlayer> requestObjects() {
            return island.getIslandMembers(true);
        }

    }

}
