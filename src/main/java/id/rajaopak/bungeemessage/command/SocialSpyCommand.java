package id.rajaopak.bungeemessage.command;

import id.rajaopak.bungeemessage.BungeeMessage;
import id.rajaopak.bungeemessage.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SocialSpyCommand extends Command {

    private final BungeeMessage plugin;

    public SocialSpyCommand(BungeeMessage plugin) {
        super("socialspy", "dosmessage.socialspy", "sspy", "dss", "dossocialspy", "dosspy");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("dosmessage.socialspy")) {
            sender.sendMessage(Common.color("&cYou don't have permission to do this!"));
            return;
        }

        if (!(sender instanceof ProxiedPlayer player)) {
            sender.sendMessage(Common.color("&cOnly player can execute this command!"));
            return;
        }

        this.plugin.getMessageManager().toggleSocialSpy(player);
    }
}
