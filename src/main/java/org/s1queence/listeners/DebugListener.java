package org.s1queence.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import static org.s1queence.api.countdown.CountDownAction.CDA_WALK_SPEED;

public class DebugListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getWalkSpeed() == CDA_WALK_SPEED) e.getPlayer().setWalkSpeed(0.2f);
    }

    @EventHandler
    private void onPlayerJoin(PlayerRespawnEvent e) {
        if (e.getPlayer().getWalkSpeed() == CDA_WALK_SPEED) e.getPlayer().setWalkSpeed(0.2f);
    }
}
