package org.s1queence.api;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

public class BlockUtils {
    public static boolean isDoubleChest(Block block) {
        return getDoubleChest(block) != null;
    }

    public static DoubleChest getDoubleChest(Block block) {
        Material type = block.getType();
        if (!type.equals(Material.CHEST)) return null;
        Chest chest = (Chest) block.getState();
        Inventory inv = chest.getInventory();
        if (!(inv instanceof DoubleChestInventory)) return null;

        return (DoubleChest) chest.getInventory().getHolder();
    }

}
