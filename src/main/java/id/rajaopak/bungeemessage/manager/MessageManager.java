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

import java.util.*;

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
        CommandSender target = ProxyServer.getInstance().getPlayer(to);

        if (togglePlayer.contains(this.plugin.getProxy().getPlayer(to))) {
            from.sendMessage(Common.color("&cYou can't message this player!"));
            return;
        }

        HashMap<CommandSender, Long> inner;
        if (this.messages.containsKey(target)) {
            inner = this.messages.get(target);
        } else {
            inner = new HashMap<>();
        }

        inner.remove(from);
        inner.put(from, System.currentTimeMillis() + (plugin.getConfigManager().getConfiguration().getLong("message-expire") * 1000L));
        if (this.messages.containsKey(target))
            this.messages.replace(target, inner);
        else
            this.messages.put(target, inner);

        formatMessage(from, message, target);
    }

    public void replyMessage(CommandSender from, String message) {
        if (this.messages.containsKey(from)) {
            HashMap<CommandSender, Long> sender = this.messages.get(from);
            // get the latest sender who messaged this player
            CommandSender target = sender.keySet().stream().max(Comparator.comparing(sender::get)).orElse(null);

            // check if target exists
            if (target == null) {
                from.sendMessage(Common.color("&cYou have nobody to whom you can reply."));
                return;
            }

            // check if cooldown has expired
            if (System.currentTimeMillis() < sender.get(target)) {
                sender.remove(target); // remove expired entry
                this.messages.replace(from, sender); // update map
                from.sendMessage(Common.color("&cYou have nobody to whom you can reply."));
                return;
            }

            if (this.plugin.getProxy().getPluginManager().getPlugin("PremiumVanish") != null) {
                if (target instanceof ProxiedPlayer) {
                    if (BungeeVanishAPI.isInvisible((ProxiedPlayer) target) && !from.hasPermission("dosmessage.msg.staff")) {
                        from.sendMessage(Common.color("&cPlayer not found."));
                        return;
                    }
                }
            }

            if (target instanceof ProxiedPlayer) {
                if (togglePlayer.contains(target)) {
                    from.sendMessage(Common.color("&cYou can't message this player!"));
                    return;
                }
            }

            HashMap<CommandSender, Long> receiver;

            if (this.messages.containsKey(target)) {
                receiver = this.messages.get(target);
            } else {
                receiver = new HashMap<>();
            }

            formatMessage(from, message, target);

            receiver.remove(from); // To make sure the player doesn't have double or more entries
            receiver.put(from, System.currentTimeMillis() + (plugin.getConfigManager().getConfiguration().getLong("message-expire") * 1000L));
            if (this.messages.containsKey(target))
                this.messages.replace(target, receiver);
            else
                this.messages.put(target, receiver);

            sender.remove(target); // To make sure the player doesn't have double or more entries
            sender.put(target, System.currentTimeMillis() + (plugin.getConfigManager().getConfiguration().getLong("message-expire") * 1000L));
            if (this.messages.containsKey(from))
                this.messages.replace(from, sender);
            else
                this.messages.put(from, sender);
        } else {
            from.sendMessage(Common.color("&cYou have nobody to whom you can reply."));
        }
    }

    private void formatMessage(CommandSender from, String message, CommandSender t) {
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

    public void clearCache() {
        this.messages.clear();
        this.spyPlayer.clear();
    }
}
