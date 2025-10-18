package org.s1queence.api.logic_item.ui_inventory_panel;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static org.s1queence.S1queenceLib.logicItemManager;

public class EmptyItemListener implements Listener {
    @EventHandler
    private void onPlayerUiPanelClick(InventoryClickEvent e) {
        if (logicItemManager.isEmptyItem(e.getCurrentItem())) e.setCancelled(true);
    }

}