package org.s1queence.api.logic_item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static org.s1queence.api.ItemStackNBTUtils.getStringFromNbt;

public class LogicItemManager {
    public static final String UI_PANEL_NBT_ITEM_PATH = "s1_my_custom_interface_item_type";
    public static final String MAIN_NBT_ITEM_PATH = "s1_my_custom_item_id";

    private final Map<String, LogicItemData> registeredItems = new HashMap<>();
    private final Map<String, LogicItemData> registeredUIPanelItems = new HashMap<>();

    public void registerUIPanelItem(LogicItemData data) {
        registeredUIPanelItems.put(data.getType(), data);
    }

    public void registerItem(LogicItemData data) {
        String type = data.getType();

        registeredItems.put(type, data);
    }

    public String getLogicItemType(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR)) return null;
        String s = getStringFromNbt(item, MAIN_NBT_ITEM_PATH);
        s = s.isEmpty() ? null : s;
        if (registeredItems.get(s) == null) s = null;
        return s;
    }

    public String getUIPanelItemType(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR)) return null;
        String s = getStringFromNbt(item, UI_PANEL_NBT_ITEM_PATH);
        s = s.isEmpty() ? null : s;
        if (registeredUIPanelItems.get(s) == null) s = null;
        return s;
    }

    public boolean isLogicItem(ItemStack item) {
        String type = getLogicItemType(item);
        if (type == null) return false;
        return registeredItems.get(type) != null;
    }

    public boolean isUIPanelItem(ItemStack item) {
        String type = getUIPanelItemType(item);
        if (type == null) return false;
        return registeredUIPanelItems.get(type) != null;
    }

    public LogicItemData getLogicItem(String type) {
        return registeredItems.get(type);
    }

    public LogicItemData getUIPanelItem(String type) {
        return registeredUIPanelItems.get(type);
    }
}
