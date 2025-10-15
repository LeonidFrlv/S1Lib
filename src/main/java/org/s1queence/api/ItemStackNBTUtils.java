package org.s1queence.api;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.inventory.ItemStack;

public class ItemStackNBTUtils {

    public static final String CANT_CHANGE_NAME_NBT_PATH = "s1_my_custom_item_cant_change_name";
    public static final String CANT_CHANGE_LORE_NBT_PATH = "s1_my_custom_item_cant_change_lore";

    public static void applyStringToNbt(ItemStack item, String path, String toApply) {
        NBT.modify(item, nbt -> {
            nbt.setString(path, toApply);
        });
    }

    public static String getStringFromNbt(ItemStack item, String path) {
        return NBT.get(item, nbt -> (String) nbt.getString(path));
    }

    public static void applyBooleanToNbt(ItemStack item, String path, boolean toApply) {
        NBT.modify(item, nbt -> {
            nbt.setBoolean(path, toApply);
        });
    }

    public static boolean getBooleanFromNbt(ItemStack item, String path) {
        return NBT.get(item, nbt -> (boolean) nbt.getBoolean(path));
    }

    public static void applyIntToNbt(ItemStack item, String path, int toApply) {
        NBT.modify(item, nbt -> {
            nbt.setInteger(path, toApply);
        });
    }

    public static int getIntegerFromNbt(ItemStack item, String path) {
        return NBT.get(item, nbt -> (int) nbt.getInteger(path));
    }

    public static void setCantChangeLore(ItemStack item, boolean b) {
        applyBooleanToNbt(item, CANT_CHANGE_LORE_NBT_PATH, b);
    }

    public static void setCantChangeName(ItemStack item, boolean b) {
        applyBooleanToNbt(item, CANT_CHANGE_NAME_NBT_PATH, b);
    }

    public static boolean isCantChangeName(ItemStack item) {
        return getBooleanFromNbt(item, CANT_CHANGE_NAME_NBT_PATH);
    }

    public static boolean isCantChangeLore(ItemStack item) {
        return getBooleanFromNbt(item, CANT_CHANGE_LORE_NBT_PATH);
    }

}
