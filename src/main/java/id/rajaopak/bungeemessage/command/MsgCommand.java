package id.rajaopak.bungeemessage.command;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import de.myzelyam.api.vanish.VanishAPI;
import id.rajaopak.bungeemessage.BungeeMessage;
import id.rajaopak.bungeemessage.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class MsgCommand extends Command implements TabExecutor {

    private final BungeeMessage plugin;

    public MsgCommand(BungeeMessage plugin) {
        super("message", "dosmessage.msg", "dmessage", "dmsg", "msg", "whisper", "dwhisper");
        this.plugin = plugin;
        this.setPermissionMessage(Common.color("&cYou don't have permission to do this!").toString());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("dosmessage.msg")) {
            sender.sendMessage(Common.color("&cYou don't have permission to do this!"));
            return;
        }

        if (args.length <= 1) {
            sender.sendMessage(Common.color("&6Usage: /" + this.getName() + " <target> <message>"));
            return;
        }

        if (sender.getName().equalsIgnoreCase(args[0])) {
            sender.sendMessage(Common.color("&cYou can't message your self!"));
            return;
        }

        if (ProxyServer.getInstance().getPlayer(args[0]) == null) {
            sender.sendMessage(Common.color("&cPlayer not found."));
            return;
        }

        if (this.plugin.getProxy().getPluginManager().getPlugin("PremiumVanish") != null) {
            if (BungeeVanishAPI.isInvisible(this.plugin.getProxy().getPlayer(args[0])) && !sender.hasPermission("dosmessage.msg.staff")) {
                sender.sendMessage(Common.color("&cPlayer not found."));
                return;
            }
        }

        StringBuilder message = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        message = new StringBuilder(message.substring(0, message.length() - 1));

        if (message.isEmpty()) {
            sender.sendMessage(Common.color("&6Usage: /" + this.getName() + " <target> <message>"));
            return;
        }

        plugin.getMessageManager().sendMessage(sender, args[0], message.toString());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

        if (args.length == 1 || args.length == 2) {
            return this.plugin.getProxy().getPlayers().stream()
                    .map(proxiedPlayer -> {
                        if (this.plugin.getProxy().getPluginManager().getPlugin("PremiumVanish") != null) {
                            if (!BungeeVanishAPI.isInvisible(proxiedPlayer)) {
                                return proxiedPlayer;
                            } else {
                                return null;
                            }
                        }
                        return proxiedPlayer;
                    })
                    .filter(Objects::nonNull)
                    .map(ProxiedPlayer::getName)
                    .filter(s -> {
                        if (args.length == 2) {
                            return s.toLowerCase().startsWith(args[0]);
                        }

                        return true;
                    })
                    .sorted(String::compareToIgnoreCase).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
