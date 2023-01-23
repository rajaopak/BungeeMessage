package id.rajaopak.bungeemessage;

import id.rajaopak.bungeemessage.command.*;
import id.rajaopak.bungeemessage.file.HistoryFile;
import id.rajaopak.bungeemessage.listener.PlayerListener;
import id.rajaopak.bungeemessage.manager.ConfigManager;
import id.rajaopak.bungeemessage.manager.CooldownManager;
import id.rajaopak.bungeemessage.manager.MessageManager;
import id.rajaopak.bungeemessage.manager.ReportManager;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeMessage extends Plugin {

    private CooldownManager cooldownManager;
    private MessageManager messageManager;
    private ReportManager reportManager;
    private ConfigManager configManager;
    private HistoryFile historyFile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.cooldownManager = new CooldownManager(this);
        this.messageManager = new MessageManager(this);
        this.reportManager = new ReportManager(this);
        this.configManager = new ConfigManager(this);
        this.historyFile = new HistoryFile(this);

        this.getProxy().getPluginManager().registerCommand(this, new MsgCommand(this));
        this.getProxy().getPluginManager().registerCommand(this, new ReplyCommand(this));
        this.getProxy().getPluginManager().registerCommand(this, new SocialSpyCommand(this));
        this.getProxy().getPluginManager().registerCommand(this, new ToggleCommand(this));
        this.getProxy().getPluginManager().registerCommand(this, new ReportCommand(this));
        this.getProxy().getPluginManager().registerCommand(this, new ReportAdminCommand(this));
        this.getProxy().getPluginManager().registerCommand(this, new ReloadCommand(this));

        this.getProxy().getPluginManager().registerListener(this, new PlayerListener(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        messageManager.clearCache();
        reportManager.clearCache();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public HistoryFile getHistoryFile() {
        return historyFile;
    }

    public String getFromFormat() {
        return getConfigManager().getConfiguration().getString("message-format-from");
    }

    public String getToFormat() {
        return getConfigManager().getConfiguration().getString("message-format-to");
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}
