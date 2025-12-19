package com.bgsoftware.superiorskyblock.commands.player;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.commands.CommandTabCompletes;
import com.bgsoftware.superiorskyblock.commands.ISuperiorCommand;
import com.bgsoftware.superiorskyblock.core.ObjectsPools;
import com.bgsoftware.superiorskyblock.core.events.plugin.PluginEventsFactory;
import com.bgsoftware.superiorskyblock.core.messages.Message;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdToggle implements ISuperiorCommand {

    @Override
    public List<String> getAliases() {
        return Arrays.asList("toggle", "togglemenu");
    }

    @Override
    public String getPermission() {
        return "superior.island.toggle";
    }

    @Override
    public String getUsage(java.util.Locale locale) {
        return "toggle <border/blocks/menu>";
    }

    @Override
    public String getDescription(java.util.Locale locale) {
        return Message.COMMAND_DESCRIPTION_TOGGLE.getMessage(locale);
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
        return false;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        SuperiorPlayer superiorPlayer = plugin.getPlayers().getSuperiorPlayer(sender);

        // If called as "togglemenu" without args, or "toggle menu", toggle menu mode
        String toggleMode = args.length == 1 ? "menu" : args[1];

        if (toggleMode.equalsIgnoreCase("border")) {
            if (!superiorPlayer.hasPermission("superior.island.toggle.border")) {
                Message.NO_COMMAND_PERMISSION.send(sender, "superior.island.toggle.border");
                return;
            }

            if (!PluginEventsFactory.callPlayerToggleBorderEvent(superiorPlayer))
                return;

            if (superiorPlayer.hasWorldBorderEnabled()) {
                Message.TOGGLED_WORLD_BORDER_OFF.send(superiorPlayer);
            } else {
                Message.TOGGLED_WORLD_BORDER_ON.send(superiorPlayer);
            }

            superiorPlayer.toggleWorldBorder();
            try (ObjectsPools.Wrapper<Location> wrapper = ObjectsPools.LOCATION.obtain()) {
                superiorPlayer.updateWorldBorder(plugin.getGrid().getIslandAt(((Player) sender).getLocation(wrapper.getHandle())));
            }
        } else if (toggleMode.equalsIgnoreCase("blocks")) {
            if (!superiorPlayer.hasPermission("superior.island.toggle.blocks")) {
                Message.NO_COMMAND_PERMISSION.send(sender, "superior.island.toggle.blocks");
                return;
            }

            if (!PluginEventsFactory.callPlayerToggleBlocksStackerEvent(superiorPlayer))
                return;

            if (superiorPlayer.hasBlocksStackerEnabled()) {
                Message.TOGGLED_STACKED_BLOCKS_OFF.send(superiorPlayer);
            } else {
                Message.TOGGLED_STACKED_BLOCKS_ON.send(superiorPlayer);
            }

            superiorPlayer.toggleBlocksStacker();
        } else if (toggleMode.equalsIgnoreCase("menu")) {
            if (!PluginEventsFactory.callPlayerTogglePanelEvent(superiorPlayer))
                return;

            if (superiorPlayer.hasToggledPanel()) {
                superiorPlayer.setToggledPanel(false);
                Message.PANEL_TOGGLE_OFF.send(superiorPlayer);
            } else {
                superiorPlayer.setToggledPanel(true);
                Message.PANEL_TOGGLE_ON.send(superiorPlayer);
            }
        } else {
            Message.INVALID_TOGGLE_MODE.send(superiorPlayer, toggleMode);
        }

    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        return args.length == 2 ? CommandTabCompletes.getCustomComplete(args[1], var ->
                sender.hasPermission("superior.island.toggle." + var) || var.equals("menu"), "border", "blocks", "menu") : Collections.emptyList();
    }

}
