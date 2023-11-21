package org.s1queence.api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class S1Booleans {
    private boolean checkIfFar(Player player, Location TargetLocation) {
        int TargetPosX = TargetLocation.getBlockX();
        int TargetPosY = TargetLocation.getBlockY();
        int TargetPosZ = TargetLocation.getBlockZ();

        int PlayerPosX = player.getLocation().getBlockX();
        int PlayerPosY = player.getEyeLocation().getBlockY();
        int PlayerPosZ = player.getLocation().getBlockZ();

        return (TargetPosX > PlayerPosX ? TargetPosX - PlayerPosX > 3 : PlayerPosX - TargetPosX > 3) || (TargetPosY > PlayerPosY ? TargetPosY - PlayerPosY > 2 : PlayerPosY - TargetPosY > 3) || (TargetPosZ > PlayerPosZ ? TargetPosZ - PlayerPosZ > 3 : PlayerPosZ - TargetPosZ > 3);
    }

    public String isAllowableInteraction(Player player, Location TargetLocation) {
        String result = null;

        if (!((Entity) player).isOnGround() && !player.isInWater()) result = "Вы не можете сделать этого прыгая или падая";

        if (checkIfFar(player, TargetLocation)) result = "Слишком далеко";

        if (player.isInsideVehicle()) result = "Взаимодействие на транспортном средстве отключено";

        return result;
    }


}
