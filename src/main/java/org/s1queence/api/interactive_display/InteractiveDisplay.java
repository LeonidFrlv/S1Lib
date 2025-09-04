package org.s1queence.api.interactive_display;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;

import java.util.List;
import java.util.Map;

public class InteractiveDisplay {
    private final Entity entity;
    private final Map<String, List<Interaction>> grids;

     public InteractiveDisplay(Entity entity, Map<String, List<Interaction>> grids) {
         this.entity = entity;
         this.grids = grids;
     }

    public Entity getEntity() {
        return entity;
    }

    public Map<String, List<Interaction>> getGrids() {
        return grids;
    }

}
