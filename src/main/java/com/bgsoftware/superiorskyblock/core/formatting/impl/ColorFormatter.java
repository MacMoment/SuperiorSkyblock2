package com.bgsoftware.superiorskyblock.core.formatting.impl;

import com.bgsoftware.superiorskyblock.core.ServerVersion;
import com.bgsoftware.superiorskyblock.core.Text;
import com.bgsoftware.superiorskyblock.core.formatting.IFormatter;
import com.bgsoftware.superiorskyblock.external.text.ITextFormatter;
import org.bukkit.ChatColor;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorFormatter implements IFormatter<String> {

    private static final ColorFormatter INSTANCE = new ColorFormatter();

    private static final List<ITextFormatter> TEXT_FORMATTERS = new LinkedList<>();

    static {
        TEXT_FORMATTERS.add(ChatColorTextFormatter.INSTANCE);
        if (ServerVersion.isAtLeast(ServerVersion.v1_16))
            TEXT_FORMATTERS.add(HexTextFormatter.INSTANCE);
    }

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("([&§])(\\{HEX:([0-9A-Fa-f]*)})");
    private static final Pattern ANGLE_BRACKET_HEX_PATTERN = Pattern.compile("<#([0-9A-Fa-f]{3}|[0-9A-Fa-f]{6})>");

    public static ColorFormatter getInstance() {
        return INSTANCE;
    }

    private ColorFormatter() {

    }

    @Override
    public String format(String value) {
        if (Text.isBlank(value))
            return "";

        String output = value;
        for (ITextFormatter formatter : TEXT_FORMATTERS) {
            output = formatter.format(output);
        }

        return output;
    }

    private static class ChatColorTextFormatter implements ITextFormatter {

        private static final ChatColorTextFormatter INSTANCE = new ChatColorTextFormatter();

        @Override
        public String format(String value) {
            return ChatColor.translateAlternateColorCodes('&', value);
        }

    }

    private static class HexTextFormatter implements ITextFormatter {

        private static final HexTextFormatter INSTANCE = new HexTextFormatter();

        @Override
        public String format(String value) {
            // Handle &{HEX:RRGGBB} or §{HEX:RRGGBB} format
            while (true) {
                Matcher matcher = HEX_COLOR_PATTERN.matcher(value);

                if (!matcher.find())
                    break;

                value = matcher.replaceFirst(parseHexColor(matcher.group(3)));
            }

            // Handle <#RRGGBB> format
            while (true) {
                Matcher matcher = ANGLE_BRACKET_HEX_PATTERN.matcher(value);

                if (!matcher.find())
                    break;

                value = matcher.replaceFirst(parseHexColor(matcher.group(1)));
            }

            return value;
        }

        private static String parseHexColor(String hexColor) {
            if (hexColor.length() != 6 && hexColor.length() != 3)
                return hexColor;

            StringBuilder magic = new StringBuilder(ChatColor.COLOR_CHAR + "x");
            int multiplier = hexColor.length() == 3 ? 2 : 1;

            for (char ch : hexColor.toCharArray()) {
                for (int i = 0; i < multiplier; i++)
                    magic.append(ChatColor.COLOR_CHAR).append(ch);
            }

            return magic.toString();
        }

    }

}
