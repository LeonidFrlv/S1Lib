package org.s1queence.api;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class YamlConfigUtil {

    @NotNull
    public static YamlDocument createConfig(String configName, JavaPlugin plugin) {
        YamlDocument config;

        try {
            File file = new File(plugin.getDataFolder(), configName);

            config = file.exists()
                    ? YamlDocument.create(file)
                    : YamlDocument.create(file, Objects.requireNonNull(plugin.getResource(configName)));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return config;
    }

    @NotNull
    public static YamlDocument reloadConfig(@NotNull YamlDocument config) {
        try {
            config.reload();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return config;
    }

}
