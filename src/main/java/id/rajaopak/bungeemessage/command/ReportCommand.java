package id.rajaopak.bungeemessage.command;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import id.rajaopak.bungeemessage.BungeeMessage;
import id.rajaopak.bungeemessage.data.HelpMeData;
import id.rajaopak.bungeemessage.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReportCommand extends Command implements TabExecutor {

    private final BungeeMessage plugin;

    public ReportCommand(BungeeMessage plugin) {
        super("report", "dosmessage.report", "dreport");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("dosmessage.report")) {
            sender.sendMessage(Common.color("&cYou don't have permission to do this!"));
            return;
        }

        if (!(sender instanceof ProxiedPlayer p)) {
            sender.sendMessage(Common.color("&cOnly player can execute this command!"));
            return;
        }

        System.out.println(this.plugin.getCooldownManager().isCooldown(p.getUniqueId()));
        if (this.plugin.getCooldownManager().isCooldown((p.getUniqueId()))) {
            sender.sendMessage(Common.color("&cYou can only sent another report in " + Common.formatTime((int) this.plugin.getCooldownManager().getCooldown(p.getUniqueId()))));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(Common.color("&6Usage: /" + this.getName() + " <message>"));
            return;
        }

        StringBuilder message = new StringBuilder();

        for (String arg : args) {
            message.append(arg).append(" ");
        }

        message = new StringBuilder(message.substring(0, message.length() - 1));

        if (message.isEmpty()) {
            sender.sendMessage(Common.color("&6Usage: /" + this.getName() + " <message>"));
            return;
        }

        StringBuilder finalMessage = message;
        this.plugin.getProxy().getPlayers().forEach(pp -> {
            if (pp.hasPermission("dosmessage.report.notify")) {
                pp.sendMessage(Common.color("&8[&c&lReport&8] &8(&e" + p.getName() + " &8- &b" + p.getServer().getInfo().getName() + "&8) &7" + finalMessage));
            }
        });

        this.plugin.getCooldownManager().setCooldown(p.getUniqueId(), this.plugin.getConfigManager().getConfiguration().getInt("report-cooldown"));
        this.plugin.getReportManager().storeHelpMeToHistory(new HelpMeData(sender.getName(), LocalDateTime.now(), p.getServer().getInfo().getName(), message.toString()));
        this.plugin.getReportManager().setData(sender.getName(), LocalDateTime.now(), p.getServer().getInfo().getName(), message.toString());
    }


    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
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
}
