package id.rajaopak.bungeemessage.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

    public static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-f])");

    public static String translateHexColor(String string) {

        Matcher matcher = HEX_PATTERN.matcher(string);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static TextComponent color(String s) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&', translateHexColor(s)));
    }

    public static TextComponent color(List<String> s) {
        StringBuilder builder = new StringBuilder();

        s.forEach(builder::append);

        return color(builder.toString());
    }

    public static void log(String s) {
        ProxyServer.getInstance().getLogger().info(s);
    }

    public static String formatTime(int seconds) {
        if (seconds == 0) {
            return "0s";
        }

        long minute = seconds / 60;
        seconds = seconds % 60;
        long hour = minute / 60;
        minute = minute % 60;
        long day = hour / 24;
        hour = hour % 24;

        StringBuilder time = new StringBuilder();
        if (day != 0) {
            time.append(day).append("d ");
        }
        if (hour != 0) {
            time.append(hour).append("h ");
        }
        if (minute != 0) {
            time.append(minute).append("m ");
        }
        if (seconds != 0) {
            time.append(seconds).append("s");
        }

        return time.toString().trim();
    }
}
