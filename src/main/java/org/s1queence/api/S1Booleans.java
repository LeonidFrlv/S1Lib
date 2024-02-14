package org.s1queence.api;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static org.s1queence.S1queenceLib.getLib;
import static org.s1queence.api.S1Utils.sendActionBarMsg;

public class S1Booleans {
    private static boolean checkIfFar(Player player, Location targetLocation) {
        int TargetPosX = targetLocation.getBlockX();
        int TargetPosY = targetLocation.getBlockY();
        int TargetPosZ = targetLocation.getBlockZ();

        int PlayerPosX = player.getLocation().getBlockX();
        int PlayerPosY = player.getEyeLocation().getBlockY();
        int PlayerPosZ = player.getLocation().getBlockZ();

        return (TargetPosX > PlayerPosX ? TargetPosX - PlayerPosX > 3 : PlayerPosX - TargetPosX > 3) || (TargetPosY > PlayerPosY ? TargetPosY - PlayerPosY > 2 : PlayerPosY - TargetPosY > 3) || (TargetPosZ > PlayerPosZ ? TargetPosZ - PlayerPosZ > 3 : PlayerPosZ - TargetPosZ > 3);
    }

    public static boolean isNotAllowableInteraction(Player player, Location targetLocation) {
        if (player.getGameMode().equals(GameMode.CREATIVE)) return false;

        if (!((Entity) player).isOnGround() && !player.isInWater()) {
            sendActionBarMsg(player, getLib().getTextConfig().getString("interact.on_air"));
            return true;
        }

        if (checkIfFar(player, targetLocation)) {
            sendActionBarMsg(player, getLib().getTextConfig().getString("interact.too_far"));
            return true;
        }

        if (player.isInsideVehicle()) {
            sendActionBarMsg(player, getLib().getTextConfig().getString("interact.from_vehicle"));
            return true;
        }

        return false;
    }

    public static boolean isLuck(double chance) {
        if (chance >= 100.0d) return true;
        if (chance <= 0.0d) return false;

        return (1 + Math.random() * 100) <= chance;
    }

}
