package org.s1queence.api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.s1queence.S1queenceLib;

public class S1Booleans {
    private static boolean checkIfFar(Player player, Location TargetLocation) {
        int TargetPosX = TargetLocation.getBlockX();
        int TargetPosY = TargetLocation.getBlockY();
        int TargetPosZ = TargetLocation.getBlockZ();

        int PlayerPosX = player.getLocation().getBlockX();
        int PlayerPosY = player.getEyeLocation().getBlockY();
        int PlayerPosZ = player.getLocation().getBlockZ();

        return (TargetPosX > PlayerPosX ? TargetPosX - PlayerPosX > 3 : PlayerPosX - TargetPosX > 3) || (TargetPosY > PlayerPosY ? TargetPosY - PlayerPosY > 2 : PlayerPosY - TargetPosY > 3) || (TargetPosZ > PlayerPosZ ? TargetPosZ - PlayerPosZ > 3 : PlayerPosZ - TargetPosZ > 3);
    }

    public static String isAllowableInteraction(Player player, Location TargetLocation, S1queenceLib lib) {
        String result = null;

        if (!((Entity) player).isOnGround() && !player.isInWater()) result = lib.getTextConfig().getString("interact.on_air");

        if (checkIfFar(player, TargetLocation)) result = lib.getTextConfig().getString("interact.too_far");

        if (player.isInsideVehicle()) result = lib.getTextConfig().getString("interact.from_vehicle");

        return result;
    }

    public static boolean isLuck(double chance) {
        if (chance >= 100.0d) return true;
        if (chance <= 0.0d) return false;

        return (1 + Math.random() * 100) <= chance;
    }

}
