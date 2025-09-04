package org.s1queence.api.interactive_display.interact_event;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.s1queence.api.interactive_display.InteractiveDisplay;
import org.s1queence.api.interactive_display.grid.GridType;

public class DisplayInteractEvent extends Event {
    @NotNull
    private final Player player;
    @NotNull
    private final Entity clickedEntity;
    @NotNull
    private final InteractiveDisplay clickedDisplay;
    @NotNull
    private final DisplayInteractEventAction actionType;
    @NotNull
    private final GridType gridType;
    @NotNull
    private final String gridName;
    @Nullable
    private final ItemStack itemInMainHand;
    @Nullable
    private final ItemStack itemInOffHand;
    private final boolean hitBox;
    private static final HandlerList handlers = new HandlerList();

    public DisplayInteractEvent(@NotNull Player who, @NotNull Entity clickedEntity, @NotNull InteractiveDisplay clickedDisplay, @NotNull DisplayInteractEventAction actionType, @NotNull GridType gridType, @NotNull String gridName, boolean hitBox) {
        player = who;
        this.clickedEntity = clickedEntity;
        this.clickedDisplay = clickedDisplay;
        this.actionType = actionType;
        this.gridType = gridType;
        this.gridName = gridName;
        this.hitBox = hitBox;

        PlayerInventory inv = player.getInventory();

        itemInMainHand = nullIfAir(inv.getItemInMainHand());
        itemInOffHand = nullIfAir(inv.getItemInOffHand());
    }

    private ItemStack nullIfAir(ItemStack item) {
        return item.getType().equals(Material.AIR) ? null : item;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public InteractiveDisplay getClickedDisplay() {
        return clickedDisplay;
    }

    @NotNull
    public GridType getGridType() {
        return gridType;
    }

    @NotNull
    public DisplayInteractEventAction getActionType() {
        return actionType;
    }

    @NotNull
    public String getGridName() {
        return gridName;
    }

    @NotNull
    public Entity getClickedEntity() {
        return clickedEntity;
    }

    @Nullable
    public ItemStack getItemInMainHand() {
        return itemInMainHand;
    }

    @Nullable
    public ItemStack getItemInOffHand() {
        return itemInOffHand;
    }

    public boolean isHitBox() {
        return hitBox;
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

