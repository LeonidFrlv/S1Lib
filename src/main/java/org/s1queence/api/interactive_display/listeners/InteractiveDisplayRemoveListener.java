package org.s1queence.api.interactive_display.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import static org.s1queence.S1queenceLib.interactiveDisplayManager;

public class InteractiveDisplayRemoveListener implements Listener {

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        interactiveDisplayManager.removeFromBlock(e.getBlock());
    }

    @EventHandler // лёд тает
    private void onBlockFade(BlockFadeEvent e) {
        if (e.isCancelled()) return;
        interactiveDisplayManager.removeFromBlock(e.getBlock());
    }

    @EventHandler // блок меняется из-за физики (падает например)
    private void onBlockPhysics(BlockPhysicsEvent e) {
        if (e.isCancelled()) return;
        interactiveDisplayManager.removeFromBlock(e.getSourceBlock());
    }

    @EventHandler // энтити изменяет блок
    private void onBlockChangeByEntity(EntityChangeBlockEvent e) {
        if (e.isCancelled()) return;
        interactiveDisplayManager.removeFromBlock(e.getBlock());
    }

    @EventHandler // блок взрывается
    private void onBlockExplode(BlockExplodeEvent e) {
        if (e.isCancelled()) return;
        interactiveDisplayManager.removeFromBlock(e.getBlock());
    }

    @EventHandler // блок сгорает
    private void onBlockBurn(BlockBurnEvent e) {
        if (e.isCancelled()) return;
        interactiveDisplayManager.removeFromBlock(e.getBlock());
    }

    @EventHandler // блок притягивается поршнем
    private void onBlockPistonRetract(BlockPistonRetractEvent e) {
        if (e.isCancelled()) return;
        interactiveDisplayManager.removeFromBlock(e.getBlock());
    }

    @EventHandler // блок отталкивается поршнем
    private void onBlockPistonExtend(BlockPistonExtendEvent e) {
        if (e.isCancelled()) return;
        interactiveDisplayManager.removeFromBlock(e.getBlock());
    }


}
