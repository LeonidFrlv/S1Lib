package org.s1queence.api.generated_items;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static org.s1queence.api.ItemStackNBTUtils.applyStringToNbt;
import static org.s1queence.api.logic_item.LogicItemManager.MAIN_NBT_ITEM_PATH;

public class GeneratedItemsManager {
    private final Map<String, ItemStack> generatedItems = new HashMap<>();

    @Nullable
    public ItemStack getGenerated(String key) {
        return generatedItems.get(key);
    }

    public void addGenerated(String key, ItemStack item) {
        applyStringToNbt(item, MAIN_NBT_ITEM_PATH, key);
        generatedItems.put(key, item);
    }

    public Map<String, ItemStack> getGeneratedItems() {
        return generatedItems;
    }

}
