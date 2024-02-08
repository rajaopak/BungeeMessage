package id.rajaopak.bungeemessage.manager;

import id.rajaopak.bungeemessage.BungeeMessage;
import id.rajaopak.bungeemessage.data.MessageHistoryData;
import id.rajaopak.bungeemessage.util.Common;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MessageHistoryManager {

    private final BungeeMessage plugin;
    private final List<MessageHistoryData> historyCache;

    public MessageHistoryManager(BungeeMessage plugin) {
        this.plugin = plugin;
        this.historyCache = new ArrayList<>();
    }

    public void addToHistory(ProxiedPlayer player, String message) {
        MessageHistoryData data = getFromPlayer(player);
        if (data == null) {
            data = new MessageHistoryData(player.getName(), player.getUniqueId());
            this.historyCache.add(data);
            return;
        }

        data.addMessageToHistory(Instant.now(), message);
    }

    public void removerFromHistory(ProxiedPlayer player) {
        MessageHistoryData data = getFromPlayer(player);
        if (data != null) {
            this.historyCache.remove(data);
        }
    }

    public MessageHistoryData getFromPlayer(ProxiedPlayer player) {
        return this.historyCache.stream().filter(data -> data.getPlayerUUID().equals(player.getUniqueId())).findAny().orElse(null);
    }

    public void sendHistory(ProxiedPlayer sender, ProxiedPlayer target, int page) {
        MessageHistoryData data = getFromPlayer(target);

        if (data != null) {
            int start = (page - 1) * 10;
            int end = Math.min(start + 10, data.getMessageHistory().size());

            sender.sendMessage(Common.color("&7Message history for &e" + target.getName() + "&7 (Page " + page + ")"));
            for (int i = start; i < end; i++) {
                sender.sendMessage(Common.color("&7- &8[" + target.getName() + "&8] &e" + data.getMessageHistory().values().stream().toList().get(i)) + "&8 | &a" +
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                .withLocale(Locale.US)
                                .withZone(ZoneId.of("Asia/Jakarta"))
                                .format(data.getMessageHistory().keySet().stream().toList().get(i)));
            }

            sender.sendMessage(Common.color("&7Type &e/msghistory <player> <page>&7 to view another page"));
        } else {
            sender.sendMessage(Common.color("&cThis player has no message history"));
        }
    }

}
