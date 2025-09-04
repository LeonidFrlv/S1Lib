package org.s1queence.api.interactive_display.grid;

public enum GridType {
    BUTTON ("button"),
    ENTITY_HOLDER ("entity_holder");

    final String name;

    GridType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static GridType fromString(String from) {
        if (from == null) return null;

        for (GridType type : GridType.values())
            if (type.toString().equalsIgnoreCase(from)) return type;

        return null;
    }

}
