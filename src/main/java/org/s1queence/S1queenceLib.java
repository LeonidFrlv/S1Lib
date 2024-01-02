package org.s1queence;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.plugin.java.JavaPlugin;
import org.s1queence.commands.LibCommand;
import org.s1queence.listeners.DebugListener;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.s1queence.api.S1TextUtils.consoleLog;
import static org.s1queence.api.S1TextUtils.getConvertedTextFromConfig;

public class S1queenceLib extends JavaPlugin {
    private YamlDocument textConfig;
    private static S1queenceLib lib;

    @Override
    public void onEnable() {
        saveResource("README.txt", false);

        try {
            textConfig = YamlDocument.create(new File(getDataFolder(), "text.yml"), Objects.requireNonNull(getResource("text.yml")));
        } catch (IOException ignored) {

        }
        consoleLog(getConvertedTextFromConfig(textConfig, "onEnable_msg", this.getName()), this);


        lib = this;
        Objects.requireNonNull(getServer().getPluginCommand("s1lib")).setExecutor(new LibCommand(this));

        getServer().getPluginManager().registerEvents(new DebugListener(), this);
    }

    @Override
    public void onDisable() {
        consoleLog(getConvertedTextFromConfig(textConfig, "onDisable_msg", this.getName()), this);
    }
    public static S1queenceLib getLib() {
        return lib;
    }
    public void setLib(S1queenceLib newState) {
        lib = newState;
    }

    public YamlDocument getTextConfig() {
        return textConfig;
    }
    public void setTextConfig(YamlDocument newState) {
        textConfig = newState;
    }
}