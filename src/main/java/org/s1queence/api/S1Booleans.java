package org.s1queence.api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static org.s1queence.S1queenceLib.getLib;

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
        if (!((Entity) player).isOnGround() && !player.isInWater()) {
            player.sendMessage(getLib().getTextConfig().getString("interact.on_air"));
            return false;
        }

        if (checkIfFar(player, targetLocation)) {
            player.sendMessage(getLib().getTextConfig().getString("interact.too_far"));
            return false;
        }

        if (player.isInsideVehicle()) {
            player.sendMessage(getLib().getTextConfig().getString("interact.from_vehicle"));
            return false;
        }

        return true;
    }

    public static boolean isLuck(double chance) {
        if (chance >= 100.0d) return true;
        if (chance <= 0.0d) return false;

        return (1 + Math.random() * 100) <= chance;
    }

}
