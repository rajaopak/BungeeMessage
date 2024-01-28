package id.rajaopak.bungeemessage.listener;

import id.rajaopak.bungeemessage.BungeeMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    private final BungeeMessage plugin;

    public PlayerListener(BungeeMessage plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();

        this.plugin.getReportManager().removeData(p.getName());
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        ProxiedPlayer p = (ProxiedPlayer) e.getSender();
        String message = e.getMessage();

        if (message.startsWith("/")) {
            return;
        }

        this.plugin.getMessageHistoryManager().addToHistory(p, message);
    }

}
