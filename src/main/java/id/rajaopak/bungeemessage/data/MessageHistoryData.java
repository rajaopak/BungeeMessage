package id.rajaopak.bungeemessage.data;

import com.github.benmanes.caffeine.cache.Cache;
import id.rajaopak.bungeemessage.util.CaffeineFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MessageHistoryData {
    private final String playerName;
    private final UUID playerUUID;

    private final Cache<Instant, String> messageHistory;

    public MessageHistoryData(String playerName, UUID playerUUID) {
        this.playerName = playerName;
        this.playerUUID = playerUUID;
        this.messageHistory = CaffeineFactory.newBuilder().expireAfterWrite(5, TimeUnit.HOURS).build();
    }

    public void addMessageToHistory(Instant timestamp, String message) {
        this.messageHistory.put(timestamp, message);
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public HashMap<Instant, String> getMessageHistory() {
        return new HashMap<>(this.messageHistory.asMap());
    }
}
