package id.rajaopak.bungeemessage.manager;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import id.rajaopak.bungeemessage.BungeeMessage;
import id.rajaopak.bungeemessage.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MessageManager {

    private final HashMap<CommandSender, HashMap<CommandSender, Long>> messages;

    private final List<CommandSender> spyPlayer;

    private final List<ProxiedPlayer> togglePlayer;

    private final BungeeMessage plugin;

    public MessageManager(BungeeMessage plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
        this.spyPlayer = new ArrayList<>();
        this.togglePlayer = new ArrayList<>();

        this.spyPlayer.add(this.plugin.getProxy().getConsole());
    }

    public void sendMessage(CommandSender from, String to, String message) {
        CommandSender t = (CommandSender) ProxyServer.getInstance().getPlayer(to);

        if (togglePlayer.contains(this.plugin.getProxy().getPlayer(to))) {
            from.sendMessage(Common.color("&cYou can't message this player!"));
            return;
        }

        HashMap<CommandSender, Long> inner;
        if (this.messages.containsKey(t)) {
            inner = this.messages.get(t);
        } else {
            inner = new HashMap<>();
        }

        inner.put(from, System.currentTimeMillis() + (plugin.getConfigManager().getConfiguration().getLong("message-expire") * 1000L));
        this.messages.put(t, inner);

        t.sendMessage(Common.color(plugin.getFromFormat().replaceAll("\\{from}", from.getName()).replaceAll("\\{msg}", message)
                .replaceAll("\\{server}", from.getName().equals(this.plugin.getProxy().getConsole().getName()) ? "PROXY" : ((ProxiedPlayer) from).getServer().getInfo().getName())));
        from.sendMessage(Common.color(plugin.getToFormat().replaceAll("\\{to}", t.getName()).replaceAll("\\{msg}", message)
                .replaceAll("\\{server}",
                        t.getName().equals(this.plugin.getProxy().getConsole().getName()) ? "PROXY" : ((ProxiedPlayer) t).getServer().getInfo().getName())));

        spyPlayer.forEach(spy -> {
            TextComponent fromPlayer = Common.color("&b" + from.getName());
            fromPlayer.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(Common.color("&7Server: &e" + (Objects.equals(from, this.plugin.getProxy().getConsole()) ? "PROXY" : ((ProxiedPlayer) from).getServer().getInfo().getName())).getText() + "\n\n" +
                                    Common.color("&aClick to teleport!").getText()).create()));
            if (!from.getName().equals(this.plugin.getProxy().getConsole().getName())) {
                fromPlayer.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/send " + spy.getName() + " " + ((ProxiedPlayer) from).getServer().getInfo().getName()));
            }

            TextComponent toPlayer = Common.color("&b" + t.getName());
            toPlayer.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(Common.color("&7Server: &e" + (Objects.equals(t, this.plugin.getProxy().getConsole()) ? "PROXY" : ((ProxiedPlayer) t).getServer().getInfo().getName())).getText() + "\n\n" +
                                    Common.color("&aClick to teleport!").getText()).create()));
            if (!t.getName().equals(this.plugin.getProxy().getConsole().getName())) {
                toPlayer.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/send " + spy.getName() + " " + ((ProxiedPlayer) t).getServer().getInfo().getName()));
            }

            ComponentBuilder text = new ComponentBuilder(Common.color("&8[&e&lSPY&8] ")).append(fromPlayer).appendLegacy(Common.color(" &8-> &b").getText()).append(toPlayer).appendLegacy(Common.color("&7: " + message).getText());

            spy.sendMessage(text.create());
        });
    }

    public void replyMessage(CommandSender from, String message) {
        if (this.messages.containsKey(from)) {
            HashMap<CommandSender, Long> sender = this.messages.get(from);

            this.messages.get(from).keySet().stream().findFirst().ifPresentOrElse(to -> {

                if (sender.get(to) > System.currentTimeMillis()) {
                    if (this.plugin.getProxy().getPluginManager().getPlugin("PremiumVanish") != null) {
                        if (BungeeVanishAPI.isInvisible(this.plugin.getProxy().getPlayer(to.getName()))) {
                            from.sendMessage(Common.color("&cPlayer not found."));
                            return;
                        }
                    }

                    if (togglePlayer.contains(this.plugin.getProxy().getPlayer(to.getName()))) {
                        from.sendMessage(Common.color("&cYou can't message this player!"));
                        return;
                    }

                    HashMap<CommandSender, Long> receiver;

                    if (this.messages.containsKey(to)) {
                        receiver = this.messages.get(to);
                    } else {
                        receiver = new HashMap<>();
                    }

                    to.sendMessage(Common.color(plugin.getFromFormat().replaceAll("\\{from}", from.getName()).replaceAll("\\{msg}", message)
                            .replaceAll("\\{server}", from.getName().equals(this.plugin.getProxy().getConsole().getName()) ? "PROXY" : ((ProxiedPlayer) from).getServer().getInfo().getName())));
                    from.sendMessage(Common.color(plugin.getToFormat().replaceAll("\\{to}", to.getName()).replaceAll("\\{msg}", message)
                            .replaceAll("\\{server}",
                                    to.getName().equals(this.plugin.getProxy().getConsole().getName()) ? "PROXY" : ((ProxiedPlayer) to).getServer().getInfo().getName())));

                    spyPlayer.forEach(spy -> {
                        TextComponent fromPlayer = Common.color("&b" + from.getName());
                        fromPlayer.setHoverEvent(
                                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder(Common.color("&7Server: &e" + (Objects.equals(from, this.plugin.getProxy().getConsole()) ? "PROXY" : ((ProxiedPlayer) from).getServer().getInfo().getName())).getText() + "\n\n" +
                                                Common.color("&aClick to teleport!").getText()).create()));
                        if (!from.getName().equals(this.plugin.getProxy().getConsole().getName())) {
                            fromPlayer.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/send " + spy.getName() + " " + ((ProxiedPlayer) from).getServer().getInfo().getName()));
                        }

                        TextComponent toPlayer = Common.color("&b" + to.getName());
                        toPlayer.setHoverEvent(
                                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder(Common.color("&7Server: &e" + (Objects.equals(to, this.plugin.getProxy().getConsole()) ? "PROXY" : ((ProxiedPlayer) to).getServer().getInfo().getName())).getText() + "\n\n" +
                                                Common.color("&aClick to teleport!").getText()).create()));
                        if (!to.getName().equals(this.plugin.getProxy().getConsole().getName())) {
                            toPlayer.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/send " + spy.getName() + " " + ((ProxiedPlayer) to).getServer().getInfo().getName()));
                        }

                        ComponentBuilder text = new ComponentBuilder(Common.color("&8[&e&lSPY&8] ")).append(fromPlayer).appendLegacy(Common.color(" &8-> &b").getText()).append(toPlayer).appendLegacy(Common.color("&7: " + message).getText());

                        spy.sendMessage(text.create());
                    });

                    receiver.put(from, System.currentTimeMillis() + (plugin.getConfigManager().getConfiguration().getLong("message-expire") * 1000L));
                    this.messages.put(to, receiver);

                    sender.put(to, System.currentTimeMillis() + (plugin.getConfigManager().getConfiguration().getLong("message-expire") * 1000L));
                    this.messages.put(from, sender);
                } else {
                    sender.remove(to);
                    this.messages.remove(from, sender);
                    from.sendMessage(Common.color("&cYou have nobody to whom you can reply."));
                }
            }, () -> from.sendMessage(Common.color("&cYou have nobody to whom you can reply.")));
        } else {
            from.sendMessage(Common.color("&cYou have nobody to whom you can reply."));
        }
    }

    public void toggleSocialSpy(ProxiedPlayer player) {
        if (this.spyPlayer.contains(player)) {
            this.spyPlayer.remove(player);
            player.sendMessage(Common.color("&8[&e&l!&8] &cSocialSpy inactive!"));
        } else {
            this.spyPlayer.add(player);
            player.sendMessage(Common.color("&8[&e&l!&8] &aSocialSpy active!"));
        }
    }

    public void toggleMessage(ProxiedPlayer player) {
        if (this.togglePlayer.contains(player)) {
            this.togglePlayer.remove(player);
            player.sendMessage(Common.color("&8[&e&l!&8] &cToggle message inactive!"));
        } else {
            this.togglePlayer.add(player);
            player.sendMessage(Common.color("&8[&e&l!&8] &aToggle message active!"));
        }
    }

    public void removeSocialSpy(ProxiedPlayer player) {
        if (!player.hasPermission("dosmessage.socialspy")) {
            return;
        }

        this.spyPlayer.remove(player);
    }

    public void clearCache() {
        this.messages.clear();
        this.spyPlayer.clear();
    }

}
