package org.s1queence.api.interactive_display.grid;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.s1queence.api.interactive_display.InteractiveDisplayManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractiveGrid {
    private final InteractiveDisplayManager manager;
    private final BlockFace facing;
    private final Block block;
    private final String entityUid;
    private final TemplateGrid[] grids;

    public InteractiveGrid(InteractiveDisplayManager manager, BlockFace facing, Block block, String entityUid, TemplateGrid... grids) {
        this.manager = manager;
        this.facing = facing;
        this.block = block;
        this.entityUid = entityUid;
        this.grids = grids;
    }

    public Map<String, List<Interaction>> getGrids() {
        Map<String, List<Interaction>> buffer = new HashMap<>();
        for (TemplateGrid templateGrid : grids) {
            String name = templateGrid.getName();
            int[][] grid = templateGrid.getGrid();
            List<Interaction> interactions = new ArrayList<>();

            buffer.put(name, interactions);

            if (grid.length == 1) {
                Interaction interaction = spawnInteraction(1, 1, templateGrid);
                interactions.add(interaction);
                continue;
            }

            int size = templateGrid.getGridSize().size;

            for (int v = 1; v <= size; v++) {
                for (int h = 1; h <= size; h++) {
                    if (grid[v - 1][h - 1] == 0) continue;
                    interactions.add(spawnInteraction(h, v, templateGrid));
                }
            }
        }

        return buffer;
    }

    private Interaction spawnInteraction(int h, int v, TemplateGrid grid) {
        Location spawnLocation = getSquareInteractionSpawnLocation(h, v, grid);

        Interaction interaction = (Interaction) block.getWorld().spawnEntity(spawnLocation, EntityType.INTERACTION);
        interaction.setInteractionWidth(grid.getCellWidth());
        interaction.setInteractionHeight(grid.getCellHeight());

        manager.setCellGridName(interaction, grid.getName());
        manager.setHolderStringedLocation(interaction, block.getLocation());
        manager.setUID(interaction, entityUid);
        manager.setCellGridType(interaction, grid.getGridType().toString());
        manager.setHitBox(interaction, grid.isHitBox());

        return interaction;
    }

    private Location getSquareInteractionSpawnLocation(int h, int v, TemplateGrid grid) {
        World world = block.getWorld();

        final float width = grid.getCellWidth();
        int size = grid.getGridSize().size;
        final int x = block.getX();
        final int y = block.getY();
        final int z = block.getZ();

        double rotatedX = 0;
        double rotatedZ = 0;

        double xTranslation = grid.getXTranslation();
        double zTranslation = grid.getZTranslation();

        switch (facing) {
            case NORTH: {
                rotatedX = h + 0.5;
                rotatedZ = v + 0.5;
                break;
            }

            case SOUTH: {
                rotatedX = size - h + 1.5;
                rotatedZ = size - v + 1.5;
                break;
            }

            case EAST: {
                rotatedX = size - v + 1.5;
                rotatedZ = h + 0.5;
                break;
            }

            case WEST: {
                rotatedX = v + 0.5;
                rotatedZ = size - h + 1.5;
                break;
            }
        }

        double spawnX = getSpawnCoordinate(rotatedX, x, width);
        double spawnY = 1 + y + grid.getYTranslation() + 0.001;
        double spawnZ = getSpawnCoordinate(rotatedZ, z, width);

        GridSize gridSize = grid.getGridSize();
        if (gridSize.equals(GridSize.X1x1)) {
            switch (facing) {
                case NORTH: case SOUTH: {
                    spawnX -= zTranslation;
                    spawnZ += xTranslation;
                    break;
                }

                case EAST: case WEST: {
                    spawnX += xTranslation;
                    spawnZ -= zTranslation;
                    break;
                }
            }
        } else {
            switch (facing) {
                case NORTH: {
                    spawnX += zTranslation;
                    spawnZ -= xTranslation;
                    break;
                }

                case SOUTH: {
                    spawnX -= zTranslation;
                    spawnZ += xTranslation;
                    break;
                }

                case EAST: {
                    spawnX += xTranslation;
                    spawnZ += zTranslation;
                    break;
                }

                case WEST: {
                    spawnX -= xTranslation;
                    spawnZ -= zTranslation;
                    break;
                }
            }
        }



        return new Location(world, spawnX, spawnY, spawnZ);
    }


    private double getSpawnCoordinate(double c1, double c2, float width) {
        return c1 * width + c2 - width;
    }

}
