package org.s1queence.api.logic_item.ui_inventory_panel;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

import static org.s1queence.S1queenceLib.logicItemManager;

public class UIPanel {

    private InventoryType inventoryType;

    private String title;

    private final Inventory inv;

    private final int size;

    private final Map<Integer, String> UIPanelItems = new HashMap<>();

    private final JavaPlugin plugin;
    private final YamlDocument config;

    public UIPanel(int size, String title, JavaPlugin plugin, YamlDocument itemsConfig) {
        this.title = title;
        this.size = getValidSize(size);
        inv = createInterface();
        this.plugin = plugin;
        config = itemsConfig;
    }

    public UIPanel(InventoryType type, String title, JavaPlugin plugin, YamlDocument itemsConfig) {
        inventoryType = type;
        this.title = title;
        inv = createInterface();
        size = inv.getSize();
        this.plugin = plugin;
        config = itemsConfig;
    }

    public UIPanel(Inventory inv, JavaPlugin plugin, YamlDocument itemsConfig) {
        this.inv = inv;
        size = inv.getSize();
        this.plugin = plugin;
        config = itemsConfig;
        for (int i = 0; i < size; i++)
            UIPanelItems.put(i, logicItemManager.getUIPanelItemType(getItem(i)));
    }

    private Inventory createInterface() {
        return inventoryType == null ? Bukkit.createInventory(null, size, title): Bukkit.createInventory(null, inventoryType, title);
    }

    private int getValidSize(int size) {
        return (size >= 9 && size <= 54 && size % 9 == 0) ? size : 54;
    }

    public UIPanel setUIPanelItem(int slot, String type) {
        if (slot > size + 1) return this;
        inv.setItem(slot, new UIPanelItem(type).getItemStack());
        UIPanelItems.put(slot, type);
        return this;
    }

    public UIPanel setItem(int slot, ItemStack item) {
        if (slot > size + 1) return this;
        inv.setItem(slot, item);

        UIPanelItems.put(slot, logicItemManager.getUIPanelItemType(item));

        return this;
    }

    public ItemStack getItem(int slot) {
        if (slot > size + 1) return null;
        return inv.getItem(slot);
    }

    public ItemStack getUIPanelItem(String type) {
        for (int i = 0; i < UIPanelItems.size(); i++)
            if (type.equals(UIPanelItems.get(i))) return getItem(i);

        return null;
    }

    public Inventory getInventory() {
        return inv;
    }

    public UIPanel fillEmptySlotsWithItem(ItemStack item) {
        for (int i = 0; i < size; i++)
            if (inv.getItem(i) == null) inv.setItem(i, item);

        return this;
    }

}
