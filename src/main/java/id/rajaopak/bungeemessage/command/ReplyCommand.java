package id.rajaopak.bungeemessage.command;

import id.rajaopak.bungeemessage.BungeeMessage;
import id.rajaopak.bungeemessage.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCommand extends Command {

    private final BungeeMessage plugin;

    public ReplyCommand(BungeeMessage plugin) {
        super("reply", "dosmessage.reply", "dreply", "dr", "r");
        this.plugin = plugin;
        this.setPermissionMessage(Common.color("&cYou don't have permission to do this!").toString());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("dosmessage.reply")) {
            sender.sendMessage(Common.color("&cYou don't have permission to do this!"));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(Common.color("&6Usage: /" + this.getName() + " <message>"));
            return;
        }

        StringBuilder message = new StringBuilder();

        for (String s : args) {
            message.append(s).append(" ");
        }

        message = new StringBuilder(message.substring(0, message.length() - 1));

        if (message.isEmpty()) {
            sender.sendMessage(Common.color("&6Usage: /" + this.getName() + " <message>"));
            return;
        }

        plugin.getMessageManager().replyMessage(sender, message.toString());
    }
}
