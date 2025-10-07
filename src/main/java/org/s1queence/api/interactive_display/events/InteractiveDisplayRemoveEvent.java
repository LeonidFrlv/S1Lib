package org.s1queence.api.interactive_display.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.s1queence.api.interactive_display.InteractiveDisplay;

public class InteractiveDisplayRemoveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    @NotNull
    private final InteractiveDisplay display;

    public InteractiveDisplayRemoveEvent(@NotNull InteractiveDisplay display) {
        this.display = display;
    }

    @NotNull
    public InteractiveDisplay getDisplay() {
        return display;
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

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
