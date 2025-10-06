package org.s1queence.api.logic_item;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LogicItemListener implements Listener {
    protected final JavaPlugin plugin;
    protected final YamlDocument itemsConfig;
    protected final YamlDocument textConfig;

    public LogicItemListener(@NotNull JavaPlugin plugin, @NotNull YamlDocument itemsConfig, @NotNull YamlDocument textConfig) {
        this.plugin = plugin;
        this.itemsConfig = itemsConfig;
        this.textConfig = textConfig;
    }

    private float getDefaultIfNull(float f) {
        return f == 0.0f ? 1.0f : f;
    }

    @Deprecated
    public void playSoundLocation(String soundPath, Location location) {
        World world = location.getWorld();
        if (world == null) return;

        String absolutePath = String.join(".", "sounds", soundPath);

        String sound = itemsConfig.getString(absolutePath + ".sound");
        if (sound == null) return;

        float volume = getDefaultIfNull(itemsConfig.getFloat(absolutePath + ".volume"));
        float pitch = getDefaultIfNull(itemsConfig.getFloat(absolutePath + ".pitch"));

        world.playSound(location, sound, volume, pitch);
    }

    public void playSoundPlayer(String soundPath, Player player) {
        String absolutePath = String.join(".", "sounds", soundPath);

        String sound = itemsConfig.getString(absolutePath + ".sound");
        if (sound == null) return;

        float volume = getDefaultIfNull(itemsConfig.getFloat(absolutePath + ".volume"));
        float pitch = getDefaultIfNull(itemsConfig.getFloat(absolutePath + ".pitch"));

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    protected static void clearCursor(Player player) {
        player.setItemOnCursor(null);
    }
}