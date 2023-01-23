package id.rajaopak.bungeemessage.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HelpMeData {

    private final String from;
    private final String helpMessage;
    private final String time;
    private final String serverName;

    public HelpMeData(String from, LocalDateTime time, String serverName, String helpMessage) {
        this.from = from;
        this.time = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy").format(time);
        this.serverName = serverName;
        this.helpMessage = helpMessage;
    }

    public String getFrom() {
        return from;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public String getTime() {
        return time;
    }

    public String getServerName() {
        return serverName;
    }
}
