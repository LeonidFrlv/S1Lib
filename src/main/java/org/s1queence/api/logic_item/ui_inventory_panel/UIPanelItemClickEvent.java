package org.s1queence.api.logic_item.ui_inventory_panel;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UIPanelItemClickEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final String uIPanelItemType;
    private final Player player;
    private final InventoryAction action;
    private final Inventory clickedInventory;
    private final ItemStack clicked;

    public UIPanelItemClickEvent(@NotNull Player player, @NotNull Inventory clickedInventory, @NotNull InventoryAction action, @NotNull ItemStack clicked, @NotNull String uIPanelItemType) {
        this.uIPanelItemType = uIPanelItemType;
        this.player = player;
        this.action = action;
        this.clicked = clicked;
        this.clickedInventory = clickedInventory;
    }

    public String getUIPanelItemType() {
        return uIPanelItemType;
    }

    public Player getPlayer() {
        return player;
    }

    public InventoryAction getAction() {
        return action;
    }

    public ItemStack getClickedItem() {
        return clicked;
    }

    public Inventory getClickedInventory() {
        return clickedInventory;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}