package org.s1queence.api.interactive_display.interact_event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.PluginManager;
import org.s1queence.S1queenceLib;
import org.s1queence.api.interactive_display.InteractiveDisplay;
import org.s1queence.api.interactive_display.grid.GridType;

import static org.s1queence.S1queenceLib.interactiveDisplayManager;

public class DisplayInteractEventCaller implements Listener {
    private final PluginManager pm;

    public DisplayInteractEventCaller(S1queenceLib plugin) {
        this.pm = plugin.getServer().getPluginManager();
    }

    @EventHandler
    private void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (e.isCancelled()) return;
        if (!e.getHand().equals(EquipmentSlot.HAND)) return;
        Entity entity = e.getRightClicked();
        if (!interactiveDisplayManager.isDisplayGridCell(entity)) return;

        InteractiveDisplay display = interactiveDisplayManager.fromEntity(entity);
        GridType gridType = interactiveDisplayManager.getGridType(entity);
        String gridName = interactiveDisplayManager.getGridName(entity);

        if (gridType == null || gridName == null || display == null) return;

        Player player = e.getPlayer();
        DisplayInteractEventAction action = player.isSneaking() ? DisplayInteractEventAction.SHIFT_RIGHT_CLICK : DisplayInteractEventAction.RIGHT_CLICK;
        boolean hitBox = interactiveDisplayManager.isHitBox(entity);

        pm.callEvent(new DisplayInteractEvent(player, entity, display, action, gridType, gridName, hitBox));
    }

    @EventHandler
    private void onPlayerEntityDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (!(e.getDamager() instanceof Player)) return;
        Entity entity = e.getEntity();
        if (!interactiveDisplayManager.isDisplayGridCell(entity)) return;

        InteractiveDisplay display = interactiveDisplayManager.fromEntity(entity);
        GridType gridType = interactiveDisplayManager.getGridType(entity);
        String gridName = interactiveDisplayManager.getGridName(entity);

        if (gridType == null || gridName == null || display == null) return;

        Player player = (Player) e.getDamager();
        DisplayInteractEventAction action = player.isSneaking() ? DisplayInteractEventAction.SHIFT_LEFT_CLICK : DisplayInteractEventAction.LEFT_CLICK;
        boolean hitBox = interactiveDisplayManager.isHitBox(entity);

        pm.callEvent(new DisplayInteractEvent(player, entity, display, action, gridType, gridName, hitBox));
    }


}
