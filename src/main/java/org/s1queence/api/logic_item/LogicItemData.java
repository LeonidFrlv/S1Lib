package org.s1queence.api.logic_item;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LogicItemData {

    private final JavaPlugin plugin;
    private final YamlDocument config;
    private final String type;

    public LogicItemData(@NotNull JavaPlugin plugin, @NotNull YamlDocument config, @NotNull String type) {
        this.plugin = plugin;
        this.config = config;
        this.type = type;
    }

    public YamlDocument getConfig() {
        return config;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String getType() {
        return type;
    }

}
