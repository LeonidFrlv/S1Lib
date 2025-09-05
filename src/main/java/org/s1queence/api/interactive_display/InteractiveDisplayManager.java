package org.s1queence.api.interactive_display;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.s1queence.api.interactive_display.grid.GridType;
import org.s1queence.api.interactive_display.grid.InteractiveGrid;

import java.util.*;

import static java.util.Optional.ofNullable;
import static org.s1queence.S1queenceLib.lib;

public class InteractiveDisplayManager {
    private Map<String, Map<String, InteractiveDisplay>> strLocationsDisplaysMap;
    public static final NamespacedKey occupiesAllKey = new NamespacedKey(lib, "s1queence-interactive-display-occupies-all");
    public static final NamespacedKey displayTypeKey = new NamespacedKey(lib, "s1queence-interactive-display-type");
    public static final NamespacedKey displayUID = new NamespacedKey(lib, "s1queence-interactive-display-uid");
    public static final NamespacedKey holderStringedLocationKey = new NamespacedKey(lib, "s1queence-interactive-display-block-holder-location");
    public static final NamespacedKey displayGridNameKey = new NamespacedKey(lib, "s1queence-interactive-display-grid-name");
    public static final NamespacedKey displayGridHitBoxKey = new NamespacedKey(lib, "s1queence-interactive-display-grid-hitBox");
    public static final NamespacedKey displayGridTypeKey = new NamespacedKey(lib, "s1queence-interactive-display-grid-type");

    public InteractiveDisplayManager() {
        fill();
    }

    private void fill() {
        List<World> worlds = lib.getServer().getWorlds();

        strLocationsDisplaysMap = new HashMap<>();
        Map<String, Entity> displayEntities = new HashMap<>();
        Map<String, Map<String, List<Interaction>>> displaysGrids = new HashMap<>();

        worlds.forEach(world -> world.getEntities().forEach(entity -> {
            String uid = getUID(entity);
            if (uid == null) return;

            if (isDisplayGridCell(entity)) {
                String name = getGridName(entity);
                if (name == null) return;

                Map<String, List<Interaction>> grids = ofNullable(displaysGrids.get(uid)).orElse(new HashMap<>());
                List<Interaction> grid = ofNullable(grids.get(name)).orElse(new ArrayList<>());
                grid.add((Interaction) entity);
                grids.put(name, grid);
                displaysGrids.put(uid, grids);
                return;
            }

            if (!isDisplayEntity(entity)) return;
            displayEntities.put(uid, entity);
        }));

        displayEntities.forEach((uid, entity) -> {
            String displayType = getDisplayType(entity);
            String stringedLocation = getHolderStringedLocation(entity);
            if (stringedLocation == null || displayType == null) return;

            Map<String, InteractiveDisplay> displays = ofNullable(strLocationsDisplaysMap.get(stringedLocation)).orElse(new HashMap<>());
            displays.put(uid, new InteractiveDisplay(entity, displaysGrids.get(uid)));
            strLocationsDisplaysMap.put(stringedLocation, displays);
        });

    }

    public boolean placeDisplayByPlayer(InteractiveDisplayData data, Block block, Player player, double xTranslation, double yTranslation, double zTranslation) {
        if (data.isOnlyFullSturdyFace() && !block.getBlockData().isFaceSturdy(BlockFace.UP, BlockSupport.FULL)) return false;

        Location blockLocation = block.getLocation().clone();

        Map<String, InteractiveDisplay> displays = strLocationsDisplaysMap.get(blockLocation.toString());

        if (displays != null && !displays.isEmpty()) {
            if (data.isOccupiesAll()) return false;
            List<InteractiveDisplay> displaysList = new ArrayList<>(displays.values());
            if (isOccupiesAll(displaysList.get(0))) return false;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        EntityType entityType = data.getEntityType();
        if (entityType.equals(EntityType.BLOCK_DISPLAY) && !item.getType().isBlock()) return false;

        BlockFace facing = player.getFacing();

        Location spawnLocation = blockLocation.add(0.5 + xTranslation, 1 + yTranslation, 0.5 + zTranslation);
        Entity entity = block.getWorld().spawnEntity(spawnLocation, entityType);
        setRotationFromFacing(facing, entity);

        if (entityType.equals(EntityType.BLOCK_DISPLAY)) {
            ((BlockDisplay)entity).setBlock(Bukkit.createBlockData(item.getType()));
        } else {
            ((ItemDisplay)entity).setItemStack(item);
        }

        if (!player.getGameMode().equals(GameMode.CREATIVE)) item.setAmount(item.getAmount() - 1);

        String uid = UUID.randomUUID().toString();

        setHolderStringedLocation(entity, block.getLocation());
        setUID(entity, uid);
        setDisplayType(entity, data.getType());
        setOccupiesAll(entity, data.isOccupiesAll());

        Map<String, List<Interaction>> grids = new InteractiveGrid(this, facing, block, uid, data.getTemplateGrids()).getGrids();

        registerDisplay(new InteractiveDisplay(entity, grids));

        return true;
    }

    public void setRotationFromFacing(BlockFace facing, Entity entity) {
        Map<BlockFace, float[]> facingRotation = new HashMap<>() {{
            put(BlockFace.NORTH, new float[]{0f, 0f});
            put(BlockFace.EAST, new float[]{90f, 0f});
            put(BlockFace.SOUTH, new float[]{180f, 0f});
            put(BlockFace.WEST, new float[]{270f, 0f});
        }};

        float[] data = facingRotation.get(facing);
        if (data == null) return;

        entity.setRotation(data[0], data[1]);
    }


    public void registerDisplay(InteractiveDisplay display) {
        String holderSLocation = getHolderStringedLocation(display.getEntity());
        String uid = getUID(display.getEntity());

        if (holderSLocation == null || uid == null) return;

        Map<String, InteractiveDisplay> displays = ofNullable(strLocationsDisplaysMap.get(holderSLocation)).orElse(new HashMap<>());
        strLocationsDisplaysMap.put(holderSLocation, displays);

        if (displays.get(uid) != null) return;
        displays.put(uid, display);
    }

    public void unregisterDisplay(InteractiveDisplay display) {
        String holderSLocation = getHolderStringedLocation(display.getEntity());
        String uid = getUID(display.getEntity());

        if (holderSLocation == null || uid == null) return;

        Map<String, InteractiveDisplay> displays = strLocationsDisplaysMap.get(holderSLocation);
        if (displays == null) return;

        displays.remove(uid);
    }

    public InteractiveDisplay fromEntity(Entity entity) {
        if (entity == null) return null;

        String holderSLocation = getHolderStringedLocation(entity);
        String uid = getUID(entity);

        if (holderSLocation == null || uid == null) return null;

        Map<String, InteractiveDisplay> displays = strLocationsDisplaysMap.get(holderSLocation);
        if (displays == null) return null;

        return displays.get(uid);
    }

    public void removeFromBlock(Block block) {
        Map<String, InteractiveDisplay> displays = strLocationsDisplaysMap.get(block.getLocation().toString());
        if (displays == null) return;

        List<InteractiveDisplay> cloned = new ArrayList<>(displays.values());

        cloned.forEach(display -> removeDisplay(display, true, true));
    }

    public void removeDisplay(InteractiveDisplay display, boolean dropEntityItem, boolean dropOtherItems) {
        display.getGrids().forEach((name, interactions) -> interactions.forEach(interaction -> {
            List<Entity> passengers = interaction.getPassengers();

            if (!passengers.isEmpty()) {
                passengers.forEach(passenger -> {
                    if (!dropOtherItems) {
                        passenger.remove();
                        return;
                    }

                    dropItemAndRemove(passenger);
                });
            }

            interaction.remove();

        }));

        unregisterDisplay(display);

        if (dropEntityItem) {
            dropItemAndRemove(display.getEntity());
            return;
        }

        display.getEntity().remove();
    }

    private void dropItemAndRemove(Entity entity) {
        World world = entity.getWorld();

        if (entity.getType().equals(EntityType.ITEM_DISPLAY)) {
            ItemDisplay itemEntity = (ItemDisplay) entity;
            ItemStack item = itemEntity.getItemStack();
            if (item != null) world.dropItemNaturally(entity.getLocation(), item);
        }

        if (entity.getType().equals(EntityType.BLOCK_DISPLAY)) {
            BlockDisplay blockDisplay = (BlockDisplay) entity;
            ItemStack item = new ItemStack(blockDisplay.getBlock().getMaterial());
            if (!item.getType().equals(Material.AIR)) world.dropItemNaturally(entity.getLocation(), item);
        }

        entity.remove();
    }

    public boolean pickupDisplay(Player player, InteractiveDisplay toPickup) {
        PlayerInventory pInv = player.getInventory();
        if (!pInv.getItemInMainHand().getType().equals(Material.AIR)) return false;

        Entity entity = toPickup.getEntity();

        ItemStack item = entity.getType().equals(EntityType.BLOCK_DISPLAY)
                ? new ItemStack(((BlockDisplay) entity).getBlock().getMaterial())
                : ((ItemDisplay) entity).getItemStack();

        if (item == null) return false;

        pInv.setItemInMainHand(item);
        removeDisplay(toPickup, false, true);
        return true;
    }

    public boolean placeItemToHolder(Player player, Entity itemHolder, Transformation transformation) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().equals(Material.AIR)) return false;

        if (!getGridType(itemHolder).equals(GridType.ENTITY_HOLDER) || !itemHolder.getPassengers().isEmpty()) return false;

        ItemDisplay itemDisplay = (ItemDisplay) itemHolder.getWorld().spawnEntity(itemHolder.getLocation(), EntityType.ITEM_DISPLAY);
        itemHolder.addPassenger(itemDisplay);
        itemDisplay.setTransformation(transformation);
        setRotationFromFacing(player.getFacing(), itemDisplay);
        itemDisplay.setItemStack(item);
        if (!player.getGameMode().equals(GameMode.CREATIVE)) item.setAmount(item.getAmount() - 1);

        return true;
    }

    public boolean takeOrSwapItemWithHolder(Player player, Entity itemHolder) {
        ItemStack item = player.getInventory().getItemInMainHand();

        List<Entity> passengers = itemHolder.getPassengers();
        if (!getGridType(itemHolder).equals(GridType.ENTITY_HOLDER) || passengers.isEmpty()) return false;

        Entity entity = passengers.get(0);
        if (!(entity instanceof ItemDisplay)) return false;

        ItemDisplay itemDisplay = (ItemDisplay) entity;

        if (itemDisplay.getItemStack() != null) player.getInventory().setItemInMainHand(itemDisplay.getItemStack());

        if (item.getType().equals(Material.AIR)) {
            itemDisplay.remove();
            return true;
        }

        itemDisplay.setItemStack(item);
        return true;
    }


    // rotateDisplay(InteractiveDisplay toRotate, BlockFace facing) - поворачиваться должны и grids тоже
    // мало того, что должны grids тоже поворачиваться - passengers, которые в них тоже)
    // ну или они итак крутятся - это тестить надо кароче, хотя по сути не должны


    public boolean isDisplayGridCell(Entity entity) {
        return getGridType(entity) != null && getGridName(entity) != null && entity instanceof Interaction;
    }

    public boolean isDisplayEntity(Entity entity) {
        return getDisplayType(entity) != null && getUID(entity) != null;
    }

    public String getUID(Entity entity) {
        if (entity == null) return null;
        return entity.getPersistentDataContainer().get(displayUID, PersistentDataType.STRING);
    }

    public void setUID(Entity entity, String uid) {
        entity.getPersistentDataContainer().set(displayUID, PersistentDataType.STRING, uid);
    }

    public String getDisplayType(Entity entity) {
        if (entity == null) return null;
        return entity.getPersistentDataContainer().get(displayTypeKey, PersistentDataType.STRING);
    }

    public void setDisplayType(Entity entity, String uid) {
        entity.getPersistentDataContainer().set(displayTypeKey, PersistentDataType.STRING, uid);
    }

    private String getHolderStringedLocation(Entity entity) {
        if (entity == null) return null;
        return entity.getPersistentDataContainer().get(holderStringedLocationKey, PersistentDataType.STRING);
    }

    public void setHolderStringedLocation(Entity entity, Location loc) {
        entity.getPersistentDataContainer().set(holderStringedLocationKey, PersistentDataType.STRING, loc.toString());
    }

    private boolean isOccupiesAll(InteractiveDisplay display) {
        Entity entity = display.getEntity();
        if (entity == null) return false;
        return Boolean.TRUE.equals(entity.getPersistentDataContainer().get(occupiesAllKey, PersistentDataType.BOOLEAN));
    }

    private void setOccupiesAll(Entity entity, boolean b) {
        entity.getPersistentDataContainer().set(occupiesAllKey, PersistentDataType.BOOLEAN,  b);
    }

    public String getGridName(Entity entity) {
        if (entity == null) return null;
        return entity.getPersistentDataContainer().get(displayGridNameKey, PersistentDataType.STRING);
    }

    public void setCellGridName(Entity entity, String name) {
        entity.getPersistentDataContainer().set(displayGridNameKey, PersistentDataType.STRING, name);
    }

    public boolean isHitBox(Entity entity) {
        if (entity == null) return false;
        return Boolean.TRUE.equals(entity.getPersistentDataContainer().get(displayGridHitBoxKey, PersistentDataType.BOOLEAN));
    }

    public void setHitBox(Entity entity, boolean b) {
        entity.getPersistentDataContainer().set(displayGridHitBoxKey, PersistentDataType.BOOLEAN, b);
    }

    public GridType getGridType(Entity entity) {
        if (entity == null) return null;
        return GridType.fromString(entity.getPersistentDataContainer().get(displayGridTypeKey, PersistentDataType.STRING));
    }

    public void setCellGridType(Entity entity, String type) {
        entity.getPersistentDataContainer().set(displayGridTypeKey, PersistentDataType.STRING, type);
    }

}
