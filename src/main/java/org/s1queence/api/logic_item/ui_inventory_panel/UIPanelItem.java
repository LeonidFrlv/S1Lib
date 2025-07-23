package org.s1queence.api.logic_item.ui_inventory_panel;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.s1queence.api.logic_item.LogicItem;

import static java.util.Optional.ofNullable;
import static org.s1queence.api.ItemStackNBTUtils.applyStringToNbt;
import static org.s1queence.api.S1TextUtils.createItemFromMap;
import static org.s1queence.api.logic_item.LogicItemManager.MAIN_NBT_ITEM_PATH;
import static org.s1queence.api.logic_item.LogicItemManager.UI_PANEL_NBT_ITEM_PATH;

public class UIPanelItem extends LogicItem {
    private final String uIPanelItemType;

    public static final String UI_PANEL_ITEM_TYPE = "interface_item";

    public UIPanelItem(@NotNull String type) {
        super(type, true);
        uIPanelItemType = type;
        itemStack = create();
    }

    @Override
    public ItemStack create() {
        ItemStack uiPanelItem = createItemFromMap(config.getSection(uIPanelItemType + ".item").getStringRouteMappedValues(true));
        if (uiPanelItem == null) return null;

        uiPanelItem.setType(Material.ENCHANTED_BOOK);
        hideEnchants(uiPanelItem);
        setHideTooltip(uiPanelItem);

        applyStringToNbt(uiPanelItem, MAIN_NBT_ITEM_PATH, UI_PANEL_ITEM_TYPE);
        applyStringToNbt(uiPanelItem, UI_PANEL_NBT_ITEM_PATH, uIPanelItemType);

        return uiPanelItem;
    }

    public void setHideTooltip(ItemStack item) {
        ItemMeta im = item.getItemMeta();
        if (im == null) return;
        boolean hide = ofNullable(config.getBoolean(uIPanelItemType + ".item.hide_tooltip")).orElse(false);
        im.setHideTooltip(hide);
        item.setItemMeta(im);
    }

}