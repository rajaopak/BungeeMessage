package id.rajaopak.bungeemessage.command;

import id.rajaopak.bungeemessage.BungeeMessage;
import id.rajaopak.bungeemessage.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

    private final BungeeMessage plugin;

    public ReloadCommand(BungeeMessage plugin) {
        super("dosmsgreload", "dosmessage.reload", "dmsgreload", "dmreload", "dmsgr", "dmr");
        this.plugin = plugin;
        this.setPermissionMessage(Common.color("&cYou don't have permission to do this!").toString());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("dosmessage.reply")) {
            sender.sendMessage(Common.color("&cYou don't have permission to do this!"));
            return;
        }

        plugin.getConfigManager().reloadConfig();
        plugin.getHistoryFile().reloadConfig();
        sender.sendMessage(Common.color("&aSuccessfully reloading the config!"));
    }
}
