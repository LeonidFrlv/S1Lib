package org.s1queence.api.logic_item.ui_inventory_panel;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static org.s1queence.S1queenceLib.logicItemManager;

public class UIPanelItemClickEventCaller implements Listener {

    @EventHandler
    private void onPlayerInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (!logicItemManager.isUIPanelItem(item)) return;
        String type = logicItemManager.getUIPanelItemType(item);
        if (type == null) return;
        Bukkit.getPluginManager().callEvent(new UIPanelItemClickEvent((Player) e.getWhoClicked(), e.getClickedInventory(), e.getAction(), e.getCurrentItem(), type));
        e.setCancelled(true);
    }

}