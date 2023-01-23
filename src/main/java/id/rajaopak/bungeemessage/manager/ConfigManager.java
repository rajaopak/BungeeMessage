package id.rajaopak.bungeemessage.manager;

import id.rajaopak.bungeemessage.BungeeMessage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigManager {
    private final BungeeMessage plugin;
    private File configFile;
    private Configuration configuration;

    public ConfigManager(BungeeMessage plugin) {
        this.plugin = plugin;

        try {
            loadConfig();
        } catch (IOException e) {
            plugin.getLogger().severe("Cannot load the Config!");
            e.printStackTrace();
        }
    }

    public void loadConfig() throws IOException {
        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!configFile.exists()) {
            InputStream in = plugin.getResourceAsStream("config.yml");
            Files.copy(in, configFile.toPath());
        }

        configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
    }

    public void reloadConfig() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            ProxyServer.getInstance().getLogger().severe("Cannot reload the Config! There might be a problem with the config file.");
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, configFile);
        } catch (IOException e) {
            ProxyServer.getInstance().getLogger().severe("Cannot save the Config! There might be a problem with the config file.");
            e.printStackTrace();
        }
    }

    public File getConfigFile() {
        return configFile;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
