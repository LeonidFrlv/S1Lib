package org.s1queence.api.interactive_display;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class InteractiveDisplay {
    private final Entity entity;
    private final Map<String, List<Interaction>> grids;
    @Nullable
    private List<Entity> relatedEntities;

    public InteractiveDisplay(Entity entity, Map<String, List<Interaction>> grids, @Nullable List<Entity> relatedEntities) {
        this.entity = entity;
        this.grids = grids;
        this.relatedEntities = relatedEntities;
    }

    public Entity getEntity() {
        return entity;
    }

    public Map<String, List<Interaction>> getGrids() {
        return grids;
    }

    @Nullable
    public List<Entity> getRelatedEntities() {
        return relatedEntities;
    }

    protected void addRelatedEntity(Entity toAdd) {
        relatedEntities = ofNullable(relatedEntities).orElse(new ArrayList<>());
        relatedEntities.add(toAdd);
    }
}
