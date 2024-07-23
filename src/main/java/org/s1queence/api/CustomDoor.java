package org.s1queence.api;

import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;

public class CustomDoor {
    private final Block initialBlock;
    private final Block topHalf;
    private final Block bottomHalf;

    public CustomDoor(Block block) {
        if (!isDoor(block)) throw new NullPointerException();
        Bisected door = (Bisected) block.getBlockData();
        boolean isTopHalf = door.getHalf().equals(Bisected.Half.TOP);
        double y = isTopHalf ? -1 : 1;

        Block oppositeHalf = block.getWorld().getBlockAt(block.getLocation().clone().add(0, y, 0));
        this.initialBlock = block;

        topHalf = isTopHalf ? block : oppositeHalf;
        bottomHalf = isTopHalf ? oppositeHalf : block;
    }

    public Block getOppositeHalf() {
        return initialBlock.equals(bottomHalf) ? topHalf : bottomHalf;
    }

    public Block getBottomHalf() {
        return bottomHalf;
    }

    public Block getTopHalf() {
        return topHalf;
    }

    public static boolean isDoor(Block block) {
        String type = block.getType().toString();

        return type.contains("DOOR") && !type.contains("TRAP");
    }

}
