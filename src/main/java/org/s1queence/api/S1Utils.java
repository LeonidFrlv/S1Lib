package org.s1queence.api;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class S1Utils {

    public static void sendActionBarMsg(Player player, String msg) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
    }

    private void forwardToAdmins(Collection<? extends Player> onlinePlayers, String message) {
        onlinePlayers.forEach(onlinePlayer -> {
            if (onlinePlayer.hasPermission("s1.perms.log")) onlinePlayer.sendMessage(message);
        });
    }

    public static void notifyAdminsAboutCommand(Collection<? extends Player> onlinePlayers, String command, String content, String targetName, String soundPath) {
        for (Player player : onlinePlayers) {
            if (!player.hasPermission("s1.perms.log")) continue;
            if (soundPath != null) player.playSound(player.getLocation(), soundPath, 1.0f, 1.0f);
            TextComponent msg = new TextComponent(targetName);
            msg.setColor(net.md_5.bungee.api.ChatColor.RED);
            msg.setUnderlined(true);
            msg.addExtra(ChatColor.DARK_GRAY + "(tp)");
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + targetName));
            if (command.equalsIgnoreCase("foradmins")) {
                msg.addExtra(ChatColor.GRAY  + " -> администрация:" + ChatColor.DARK_AQUA + content);
                player.spigot().sendMessage(msg);
                continue;
            }
            msg.addExtra(ChatColor.YELLOW + " использовал команду /" + ChatColor.RED + command);
            player.spigot().sendMessage(msg);
            player.sendMessage(ChatColor.GRAY + "Содержимое команды: " + content);
        }
    }

    public static void forwardToAdmins(String message, JavaPlugin plugin, String logPermission) {
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission(logPermission)) onlinePlayer.sendMessage(message);
        }
    }

    public static Location toCenterLocation(Location loc) {
        return new Location(loc.getWorld(), loc.getBlockX() + 0.5D, loc.getBlockY(), loc.getBlockZ() + 0.5D);
    }
}
