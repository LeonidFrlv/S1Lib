package org.s1queence.api.generated_items;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GeneratedItemsManager {
    private final Map<String, ItemStack> generatedItems = new HashMap<>();

    @Nullable
    public ItemStack getGenerated(String key) {
        return generatedItems.get(key);
    }

    public void addGenerated(String key, ItemStack item) {
        generatedItems.put(key, item);
    }

    public Map<String, ItemStack> getGeneratedItems() {
        return generatedItems;
    }

}
