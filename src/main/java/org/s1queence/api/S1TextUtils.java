package org.s1queence.api;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.s1queence.plugin.libs.block.implementation.Section;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.s1queence.api.S1Booleans.isLuck;

public class S1TextUtils {

    public static String getConvertedTextFromConfig(@NotNull YamlDocument textConfig, @NotNull String path, @NotNull String pluginName) {
        String title = "[" + ChatColor.GOLD + pluginName + ChatColor.WHITE + "]";
        String msg = textConfig.getString(path);

        if (msg == null)  {
            String nullMsgError = "&6%plugin% FATAL ERROR: Text is null. We recommend that you delete the text.yml file from the plugin folder and use RELOAD.";
            return ChatColor.translateAlternateColorCodes('&', nullMsgError.replace("%plugin%", title));
        }

        return (ChatColor.translateAlternateColorCodes('&', msg.replace("%plugin%", title)));
    }

    public static String removeAllChatColorCodesFromString(String toRemove) {
        String translated = ChatColor.translateAlternateColorCodes('&', toRemove);
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < translated.length(); i++) {
            char current = translated.charAt(i);
            char prev = i != 0 ? translated.charAt(i - 1) : current;
            if (current == '§' || prev == '§') continue;
            buffer.append(current);
        }

        return buffer.toString();
    }

    public static String getStringLocation(String delimiter, Location loc) {
        return String.join(delimiter, String.valueOf(loc.getBlockX()), String.valueOf(loc.getBlockY()), String.valueOf(loc.getBlockZ()));
    }

    public static Location getLocationFromString(char delimiter, String from, World world) {
        int delimiterCount = 0;
        for (char current : from.toCharArray()) {
            if (current == delimiter) delimiterCount++;
        }

        if (delimiterCount != 2) return null;

        double x = 0;
        double y = 0;
        double z = 0;
        StringBuilder coordinate = new StringBuilder();
        for (int i = 0; i < from.length(); i++) {
            char currentI = from.charAt(i);
            if (currentI == delimiter) {
                x = Double.parseDouble(coordinate.toString());
                coordinate = new StringBuilder();
                for (int k = i + 1; k < from.length(); k++) {
                    char currentK = from.charAt(k);
                    if (currentK == delimiter) {
                        y = Double.parseDouble(coordinate.toString());
                        coordinate = new StringBuilder();
                        for (int j = k + 1; j < from.length(); j++) {
                            char currentJ = from.charAt(j);
                            coordinate.append(currentJ);
                            if (j == from.length() - 1) z = Double.parseDouble(coordinate.toString());
                        }
                        break;
                    }
                    coordinate.append(currentK);
                }
                break;
            }
            coordinate.append(currentI);
        }

        return new Location(world, x, y, z);
    }
    public static String getTextWithInsertedPlayerName(String str, String toInsert) {
        return ChatColor.translateAlternateColorCodes('&', str.replace("%username%", toInsert));
    }

    public static String getTextWithInsertedProgressBar(String percent, String toInsert, String pb, String charPercentColor) {
        return (ChatColor.translateAlternateColorCodes('&', toInsert.replace("%progress_bar%", pb).replace("%percent%", ChatColor.getByChar(charPercentColor) + percent)));
    }

    public static void consoleLog(String msgToConsoleLog, JavaPlugin plugin) {
        plugin.getServer().getConsoleSender().sendMessage(msgToConsoleLog);
    }

    private static boolean isItemPropertyNonDefaultOrNull(Object property) {
        return property != null && (!(property instanceof String) || !((String) property).equalsIgnoreCase("default"));
    }

    @SuppressWarnings("unchecked")
    public static ItemStack createItemFromMap(Map<String, Object> mappedItem) {
        if (mappedItem.get("material") == null) return null;
        Material material = Material.getMaterial(mappedItem.get("material").toString().toUpperCase());
        if (material == null) return null;

        Object amount = mappedItem.get("amount") instanceof Integer ? mappedItem.get("amount") : 1;
        if ((int)amount != 1) amount = Math.min(Math.max((int)amount, 1), material.getMaxStackSize());
        ItemStack is = new ItemStack(material, (int)amount);

        ItemMeta im = is.getItemMeta();

        if (im == null) return null;

        String name = (String)mappedItem.get("name");
        if (isItemPropertyNonDefaultOrNull(name)) im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        Object cmd = mappedItem.get("cmd");
        if (isItemPropertyNonDefaultOrNull(cmd) && cmd instanceof Integer) im.setCustomModelData((int)cmd);
        Object configLore = mappedItem.get("lore");
        if (isItemPropertyNonDefaultOrNull(configLore)) {
            List<String> lore = new ArrayList<>();
            for (String row : ((List<String>) configLore)) {
                lore.add(ChatColor.translateAlternateColorCodes('&', row));
            }
            im.setLore(lore);
        }

        is.setItemMeta(im);
        return is;
    }

    private static int getAmount(int min, int max) {
        if (min > max) return 1;
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    @SuppressWarnings("unchecked")
    public static ItemStack createItemFromMapWithChanceAndRandomAmount(Map<String, Object> mappedItem) {
        Object dropChance = mappedItem.get("drop_chance");
        if (dropChance instanceof Double) {
            if (!isLuck((double)dropChance)) return null;
        }

        if (mappedItem.get("material") == null) return null;
        Material material = Material.getMaterial(mappedItem.get("material").toString().toUpperCase());
        if (material == null) return null;

        Object amountValue = mappedItem.get("amount");
        Object amount = amountValue instanceof Integer || amountValue instanceof Section ? amountValue : 1;
        if (amount instanceof Section) {
            Map<String, Object> minMax = ((Section) mappedItem.get("amount")).getStringRouteMappedValues(true);
            Object min = minMax.get("min");
            Object max = minMax.get("max");
            if (min instanceof Integer && max instanceof Integer) {
                amount = getAmount((int)min, (int)max);
            } else {
                amount = 1;
            }
        }

        if ((int)amount != 1) amount = Math.min(Math.max((int)amount, 1), material.getMaxStackSize());

        ItemStack is = new ItemStack(material, (int)amount);

        ItemMeta im = is.getItemMeta();

        if (im == null) return null;

        String name = (String)mappedItem.get("name");
        if (isItemPropertyNonDefaultOrNull(name)) im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        Object cmd = mappedItem.get("cmd");
        if (isItemPropertyNonDefaultOrNull(cmd) && cmd instanceof Integer) im.setCustomModelData((int)cmd);

        Object configLore = mappedItem.get("lore");
        if (isItemPropertyNonDefaultOrNull(configLore)) {
            List<String> lore = new ArrayList<>();
            for (String row : ((List<String>) configLore)) {
                lore.add(ChatColor.translateAlternateColorCodes('&', row));
            }
            im.setLore(lore);
        }

        is.setItemMeta(im);
        return is;
    }


}
