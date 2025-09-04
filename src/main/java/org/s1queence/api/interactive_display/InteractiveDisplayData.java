package org.s1queence.api.interactive_display;

import org.bukkit.entity.EntityType;
import org.s1queence.api.interactive_display.grid.TemplateGrid;

public abstract class InteractiveDisplayData {
    private final String type;
    private final TemplateGrid[] templateGrids;
    private final boolean onlyFullSturdyFace;
    private final boolean occupiesAll;
    private final EntityType entityType;

    public InteractiveDisplayData(String type, boolean blockDisplay, boolean onlyFullSturdyFace, boolean occupiesAll, TemplateGrid... grids) {
        this.type = type;
        this.entityType = blockDisplay ? EntityType.BLOCK_DISPLAY : EntityType.ITEM_DISPLAY;
        this.onlyFullSturdyFace = onlyFullSturdyFace;
        this.occupiesAll = occupiesAll;
        this.templateGrids = grids;
    }

    public String getType() {
        return type;
    }

    public TemplateGrid[] getTemplateGrids() {
        return templateGrids;
    }

    public boolean isOnlyFullSturdyFace() {
        return onlyFullSturdyFace;
    }

    public boolean isOccupiesAll() {
        return occupiesAll;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
