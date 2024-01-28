package id.rajaopak.bungeemessage.command;

import id.rajaopak.bungeemessage.BungeeMessage;
import id.rajaopak.bungeemessage.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collections;
import java.util.stream.Collectors;

public class MessageHistoryCommand extends Command implements TabExecutor {

    private final BungeeMessage plugin;

    public MessageHistoryCommand(BungeeMessage plugin) {
        super("messagehistory", "bungeemessage.history", "msghistory", "msgh", "dmessagehistory", "dmsghistory", "dmsgh");
        this.plugin = plugin;
        this.setPermissionMessage(Common.color("&cYou don't have permission to do this!").toString());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("bungeemessage.history")) {
            sender.sendMessage(this.getPermissionMessage());
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(Common.color("&cUsage: /msghistory <player> <page>"));
            return;
        }

        ProxiedPlayer target = plugin.getProxy().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Common.color("&cPlayer not found!"));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(Common.color("&cUsage: /msghistory <player> <page>"));
            return;
        }

        int page;
        try {
            page = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Common.color("&cPage must be a number!"));
            return;
        }

        plugin.getMessageHistoryManager().sendHistory((ProxiedPlayer) sender, target, page);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length != 1) return this.plugin.getProxy().getPlayers().stream().map(CommandSender::getName).sorted().collect(Collectors.toList());
        return Collections.emptyList();
    }
}
