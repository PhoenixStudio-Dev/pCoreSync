package com.phoenixstudio.pcoresync.config;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.io.File;
@Getter
public final class ConfigLoader {

    @Getter private static Config config;

    private ConfigLoader(Plugin plugin){
        config = ConfigManager.create(Config.class, (it) -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer());
            it.withBindFile(new File(plugin.getDataFolder(), "config.yml"));
            it.saveDefaults();
            it.load();
        });
    }

    public static void create(Plugin plugin){
        new ConfigLoader(plugin);
    }
}
