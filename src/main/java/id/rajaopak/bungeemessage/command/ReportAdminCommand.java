package id.rajaopak.bungeemessage.command;

import id.rajaopak.bungeemessage.BungeeMessage;
import id.rajaopak.bungeemessage.data.HelpMeData;
import id.rajaopak.bungeemessage.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReportAdminCommand extends Command implements TabExecutor {

    private final BungeeMessage plugin;

    public ReportAdminCommand(BungeeMessage plugin) {
        super("reportadmin", "dosmessage.reportadmin", "rta", "rtadmin");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("dosmessage.reportadmin")) {
            sender.sendMessage(Common.color("&cYou don't have permission to do this!"));
            return;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("history")) {
                if (args.length > 2) {
                    sender.sendMessage(Common.color("&cUsage: /reportadmin history [page]"));
                    return;
                }

                List<String> list = this.plugin.getHistoryFile().getConfiguration().getSection("report").getKeys().stream().toList();

                int page;
                if (args.length == 1) {
                    page = 1;
                } else {
                    page = Integer.parseInt(args[1]);
                }

                int finalPage = list.size() / 5;

                if (list.size() % 5 > 0) {
                    finalPage += 1;
                }

                if (list.size() > (page * 5)) {
                    sender.sendMessage(Common.color("&8============== &aReport &bHistory &8(&e" + page + "&8) &8=============="));
                    for (int i = (page - 1) * 5; i < ((page - 1) * 5) + 5; i++) {
                        sender.sendMessage(Common.color("&7" + (i + 1) + ". " +
                                "&aReport: &8[&7" + this.plugin.getHistoryFile().getConfiguration().getString("report." + Integer.parseInt(list.get(i)) + ".time")
                                + "&8] &8(&b" + this.plugin.getHistoryFile().getConfiguration().getString("report." + Integer.parseInt(list.get(i)) + ".from")
                                + " &8- &e" + this.plugin.getHistoryFile().getConfiguration().getString("report." + Integer.parseInt(list.get(i)) + ".server") + "&8) &7"
                                + this.plugin.getHistoryFile().getConfiguration().getString("report." + Integer.parseInt(list.get(i)) + ".message")));
                    }
                    sender.sendMessage(Common.color("&8============================================="));
                } else if (finalPage == page) {
                    sender.sendMessage(Common.color("&8============== &aReport &bHistory &8(&e" + page + "&8) &8=============="));
                    for (int i = list.size() % 5 != 0 ? list.size() - (list.size() % 5) : list.size() - 5; i < list.size(); i++) {
                        sender.sendMessage(Common.color("&7" + (i + 1) + ". " +
                                "&aReport: &8[&7" + this.plugin.getHistoryFile().getConfiguration().getString("report." + Integer.parseInt(list.get(i)) + ".time")
                                + "&8] &8(&b" + this.plugin.getHistoryFile().getConfiguration().getString("report." + Integer.parseInt(list.get(i)) + ".from")
                                + " &8- &e" + this.plugin.getHistoryFile().getConfiguration().getString("report." + Integer.parseInt(list.get(i)) + ".server") + "&8) &7"
                                + this.plugin.getHistoryFile().getConfiguration().getString("report." + Integer.parseInt(list.get(i)) + ".message")));
                    }
                    sender.sendMessage(Common.color("&8============================================="));
                } else {
                    sender.sendMessage(Common.color("&cUnknown history page"));
                }
            }

            if (args[0].equalsIgnoreCase("get")) {
                if (args.length > 3) {
                    sender.sendMessage(Common.color("&cUsage: /" + getName() + " <Player> [page]"));
                    return;
                }

                String playerName = args[1];
                List<HelpMeData> data = this.plugin.getReportManager().getMessage(playerName);

                if (data.size() == 0) {
                    sender.sendMessage(Common.color("&cPlayer didn't have any report yet!"));
                    return;
                }

                int page;
                if (args.length == 2) {
                    page = 1;
                } else {
                    page = Integer.parseInt(args[2]);
                }

                int finalPage = data.size() / 5;

                if (data.size() % 5 > 0) {
                    finalPage += 1;
                }

                if (data.size() > (page * 5)) {
                    sender.sendMessage(Common.color("&8========== &aReport &bHistory of &c" + playerName + " &8(&e" + page + "/" + finalPage + "&8) &8=========="));
                    for (int i = (page - 1) * 5; i < ((page - 1) * 5) + 5; i++) {
                        sender.sendMessage(Common.color("&7" + (i + 1) + ". " +
                                "&aReport: &8[&7" + data.get(i).getTime()
                                + "&8] &8(&b" + data.get(i).getFrom()
                                + " &8- &e" + data.get(i).getServerName() + "&8) &7"
                                + data.get(i).getHelpMessage()));
                    }
                    sender.sendMessage(Common.color("&8================================================="));
                } else if (finalPage == page) {
                    sender.sendMessage(Common.color("&8========== &aReport &bHistory of &c" + playerName + " &8(&e" + page + "/" + finalPage + "&8) &8=========="));
                    for (int i = data.size() % 5 != 0 ? data.size() - (data.size() % 5) : data.size() - 5; i < data.size(); i++) {
                        sender.sendMessage(Common.color("&7" + (i + 1) + ". " +
                                "&aReport: &8[&7" + data.get(i).getTime()
                                + "&8] &8(&b" + data.get(i).getFrom()
                                + " &8- &e" + data.get(i).getServerName() + "&8) &7"
                                + data.get(i).getHelpMessage()));
                    }
                    sender.sendMessage(Common.color("&8================================================="));
                } else {
                    sender.sendMessage(Common.color("&cUnknown history page"));
                }
            }
        } else {
            sender.sendMessage(Common.color("&cUsage: /reportadmin (history|get)"));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return List.of("history", "get");
        } else {
            if (args[0].equalsIgnoreCase("history")) {
                if (args.length == 2) {
                    List<String> list = this.plugin.getHistoryFile().getConfiguration().getSection("report").getKeys().stream().toList();
                    int finalPage = list.size() / 5;

                    if (list.size() % 5 > 0) {
                        finalPage += 1;
                    }

                    if (finalPage != 0) {
                        return IntStream.rangeClosed(1, finalPage).boxed().map(String::valueOf).toList();
                    }

                    return Collections.emptyList();
                }
            } else if (args[0].equalsIgnoreCase("get")) {
                if (args.length == 2) {
                    return this.plugin.getProxy().getPlayers().stream()
                            .map(ProxiedPlayer::getDisplayName)
                            .filter(s -> s.toLowerCase().startsWith(args[0]))
                            .sorted().collect(Collectors.toList());
                } else if (args.length == 3) {
                    List<HelpMeData> data = this.plugin.getReportManager().getMessage(args[1]);
                    int finalPage = data.size() / 5;

                    if (data.size() % 5 > 0) {
                        finalPage += 1;
                    }

                    if (finalPage != 0) {
                        return IntStream.rangeClosed(1, finalPage).boxed().map(String::valueOf).toList();
                    }

                    return Collections.emptyList();
                }
            }
            return Collections.emptyList();
        }
    }
}
