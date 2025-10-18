package org.s1queence.api.generated_items;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static org.s1queence.api.ItemStackNBTUtils.applyStringToNbt;
import static org.s1queence.api.ItemStackNBTUtils.getStringFromNbt;
import static org.s1queence.api.logic_item.LogicItemManager.MAIN_NBT_ITEM_PATH;

public class GeneratedItemsManager {
    public static final String NBT_ITEM_COLOR_PATH = "s1_my_custom_item_color";

    private final Map<String, ItemStack> generatedItems = new HashMap<>();

    @Nullable
    public ItemStack getGenerated(String key) {
        return generatedItems.get(key);
    }

    public void addGenerated(String key, ItemStack item, boolean applyKeyToNbt) {
        boolean hasColor = key.contains("%");

        if (hasColor) {
            String[] parts = key.split("%");
            if (parts.length != 2) return;

            applyStringToNbt(item, MAIN_NBT_ITEM_PATH, parts[0]);
            applyStringToNbt(item, NBT_ITEM_COLOR_PATH, parts[1]);
        }

        if (applyKeyToNbt && !hasColor) applyStringToNbt(item, MAIN_NBT_ITEM_PATH, key);
        generatedItems.put(key, item);
    }

    public Map<String, ItemStack> getGeneratedItems() {
        return generatedItems;
    }

    public boolean isGenerated(ItemStack item) {
        String type = getGeneratedType(item);
        if (type == null || type.isEmpty()) return false;
        if (getGenerated(type) != null) return true;

        return !getAltColors(type).isEmpty();
    }

    public String getGeneratedType(ItemStack item) {
        return getStringFromNbt(item, MAIN_NBT_ITEM_PATH);
    }

    public String getColor(ItemStack item) {
        return getStringFromNbt(item, NBT_ITEM_COLOR_PATH);
    }

    public void removeGenerated(String key) {
        generatedItems.remove(key);
    }

    public Map<String, ItemStack> getAltColors(String key) {
        Map<String, ItemStack> buffer = new HashMap<>();

        generatedItems.forEach((k, item) -> {
            if (!getGeneratedType(item).startsWith(key)) return;
            buffer.put(k, item);
        });

        return buffer;
    }

}
