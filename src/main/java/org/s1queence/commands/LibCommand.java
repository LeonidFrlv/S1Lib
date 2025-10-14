package org.s1queence.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.s1queence.S1queenceLib;
import org.s1queence.api.YamlConfigUtil;

import static org.s1queence.S1queenceLib.lib;
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
        lib.setTextConfig(YamlConfigUtil.reloadConfig(lib.getTextConfig()));
        lib.setOptionsConfig(YamlConfigUtil.reloadConfig(lib.getOptionsConfig()));

        getPreprocessActionHandlers().clear();
        getDoubleRunnableActionHandlers().clear();

        String reloadMsg = getConvertedTextFromConfig(plugin.getTextConfig(), "onReload_msg", plugin.getName());
        if (sender instanceof Player) sender.sendMessage(reloadMsg);
        consoleLog(reloadMsg, plugin);
        return true;
    }
}
