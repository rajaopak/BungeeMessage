package id.rajaopak.bungeemessage.manager;

import id.rajaopak.bungeemessage.BungeeMessage;
import id.rajaopak.bungeemessage.data.HelpMeData;
import net.md_5.bungee.config.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportManager {

    private final BungeeMessage plugin;
    private final List<HelpMeData> data;

    public ReportManager(BungeeMessage plugin) {
        this.plugin = plugin;
        this.data = new ArrayList<>();
    }

    public void setData(String from, LocalDateTime time, String serverName, String message) {
        this.data.add(new HelpMeData(from, time, serverName, message));
    }

    public void removeData(String from) {
        this.data.removeIf(helpMeData -> helpMeData.getFrom().equals(from));
    }

    public List<HelpMeData> getMessage(String from) {
        List<HelpMeData> data = new ArrayList<>();
        Configuration file = this.plugin.getHistoryFile().getConfiguration();

        this.plugin.getHistoryFile().getConfiguration().getSection("report").getKeys().forEach(s -> {
            String k = "report." + s;

            if (file.getString(k + ".from").equalsIgnoreCase(from)) {
                data.add(new HelpMeData(file.getString(k + ".from"),
                        LocalDateTime.parse(file.getString(k + ".time"), DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")),
                        file.getString(k + ".server"), file.getString(k + ".message")));
            }
        });

        return data;
    }

    public void storeHelpMeToHistory(HelpMeData helpMe) {
        Configuration file = this.plugin.getHistoryFile().getConfiguration();
        int count = file.getSection("report").getKeys().size() + 1;

        file.set("report." + count + ".from", helpMe.getFrom());
        file.set("report." + count + ".time", helpMe.getTime());
        file.set("report." + count + ".server", helpMe.getServerName());
        file.set("report." + count + ".message", helpMe.getHelpMessage());
        this.plugin.getHistoryFile().saveConfig();
    }

    public void clearCache() {
        data.clear();
    }
}
