package id.rajaopak.bungeemessage.manager;

import id.rajaopak.bungeemessage.BungeeMessage;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    private final BungeeMessage plugin;
    private final HashMap<UUID, Long> cooldown;

    public CooldownManager(BungeeMessage plugin) {
        this.plugin = plugin;
        this.cooldown = new HashMap<>();
    }

    public void setCooldown(UUID uuid, long seconds) {
        if (this.plugin.getProxy().getPlayer(uuid).hasPermission("dosmessage.report.bypass-cooldown")) {
            return;
        }

        this.cooldown.put(uuid, System.currentTimeMillis() + (seconds * 1000));
    }

    public long getCooldown(UUID uuid) {
        if (this.cooldown.containsKey(uuid)) {
            return (this.cooldown.get(uuid) - System.currentTimeMillis()) / 1000;
        }
        return 0;
    }

    public boolean isCooldown(UUID uuid) {
        if (!this.plugin.getProxy().getPlayer(uuid).hasPermission("dosmessage.report.bypass-cooldown")) {
            if (this.cooldown.containsKey(uuid)) {
                if (this.cooldown.get(uuid) > System.currentTimeMillis()) {
                    return true;
                } else {
                    removeCooldown(uuid);
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void removeCooldown(UUID uuid) {
        this.cooldown.remove(uuid);
    }

    public void clearCooldown() {
        this.cooldown.clear();
    }

    public HashMap<UUID, Long> getCooldown() {
        return this.cooldown;
    }

}
