package org.s1queence.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import static org.s1queence.api.countdown.CountDownAction.isPlayerInCountDownAction;

public class DebugListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onPlayerPlaceBlock(BlockPlaceEvent e) {
        if (isPlayerInCountDownAction(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onPlayerDropItem(PlayerDropItemEvent e) {
        if (isPlayerInCountDownAction(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onPlayerPickupArrow(PlayerPickupArrowEvent e) {
        if (isPlayerInCountDownAction(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onPlayerPickupItem(EntityPickupItemEvent e) {
        Entity en = e.getEntity();
        if (!(en instanceof Player)) return;
        if (isPlayerInCountDownAction((Player) en)) e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onPlayerDestroyBlock(BlockBreakEvent e) {
        if (isPlayerInCountDownAction(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onPlayerInteract(PlayerInteractEvent e) {
        if (isPlayerInCountDownAction(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (isPlayerInCountDownAction(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onPlayerVehicle(VehicleEnterEvent e) {
        Entity en = e.getEntered();
        if (!(en instanceof Player)) return;
        if (isPlayerInCountDownAction((Player) en)) e.setCancelled(true);
    }

}
