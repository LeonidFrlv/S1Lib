package org.s1queence.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.s1queence.S1queenceLib;
import org.s1queence.api.interactive_display.InteractiveDisplayManager;

import java.io.File;
import java.util.Objects;

import static org.s1queence.api.S1TextUtils.consoleLog;
import static org.s1queence.api.S1TextUtils.getConvertedTextFromConfig;
import static org.s1queence.api.countdown.CountDownAction.getDoubleRunnableActionHandlers;
import static org.s1queence.api.countdown.CountDownAction.getPreprocessActionHandlers;

public class LibCommand implements CommandExecutor {
    private final S1queenceLib plugin;
    public LibCommand(S1queenceLib plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length != 1) return false;
        if (!args[0].equalsIgnoreCase("reload")) return false;
        plugin.setLib(plugin);
        try {
            File textCfgFile = new File(plugin.getDataFolder(), "text.yml");
            if (!textCfgFile.exists()) plugin.setTextConfig(YamlDocument.create(new File(plugin.getDataFolder(), "text.yml"), Objects.requireNonNull(plugin.getResource("text.yml"))));
            plugin.getTextConfig().reload();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        getPreprocessActionHandlers().clear();
        getDoubleRunnableActionHandlers().clear();

        S1queenceLib.interactiveDisplayManager = new InteractiveDisplayManager();

        String reloadMsg = getConvertedTextFromConfig(plugin.getTextConfig(), "onReload_msg", plugin.getName());
        if (sender instanceof Player) sender.sendMessage(reloadMsg);
        consoleLog(reloadMsg, plugin);
        return true;
    }
}
