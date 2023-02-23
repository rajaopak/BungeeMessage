package id.rajaopak.bungeemessage.command;

import id.rajaopak.bungeemessage.BungeeMessage;
import id.rajaopak.bungeemessage.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collections;
import java.util.stream.Collectors;

public class ToggleCommand extends Command implements TabExecutor {

    private final BungeeMessage plugin;

    public ToggleCommand(BungeeMessage plugin) {
        super("togglemsg", "dosmessage.togglemsg", "tmsg", "dtmsg", "dostogglemsg");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("dosmessage.togglemsg")) {
            sender.sendMessage(Common.color("&cYou don't have permission to do this!"));
            return;
        }

        if (!(sender instanceof ProxiedPlayer p)) {
            sender.sendMessage(Common.color("&cOnly player can execute this command!"));
            return;
        }

        if (args.length == 0) {
            this.plugin.getMessageManager().toggleMessage(p);
        } else if (args.length == 1) {
            if (sender.hasPermission("dosmessage.togglemsg.other")) {
                if (this.plugin.getProxy().getPlayer(args[0]) != null) {
                    sender.sendMessage(Common.color("&cPlayer not found."));
                    return;
                }

                this.plugin.getMessageManager().toggleMessage(this.plugin.getProxy().getPlayer(args[0]));
            } else {
                sender.sendMessage(Common.color("&cYou don't have permission to do this!"));
            }
        } else {
            if (sender.hasPermission("dosmessage.togglemsg.other")) {
                sender.sendMessage(Common.color("&cUsage: /togglemsg [<target>]"));
                return;
            }

            sender.sendMessage(Common.color("&cUsage: /togglemsg"));
        }

    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

        if (args.length == 1) {
            return this.plugin.getProxy().getPlayers().stream().map(ProxiedPlayer::getDisplayName).sorted().collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
