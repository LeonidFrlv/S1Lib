package org.s1queence.api;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.inventory.ItemStack;

public class ItemStackNBTUtils {

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
}
