package org.s1queence.api.rpg_skills;

import java.util.ArrayList;
import java.util.Arrays;

public class RPGSkill {
    private final String name;
    private final String displayName;
    private final ArrayList<Integer> levelsExperience;

    public RPGSkill(String name, String displayName, Integer... levelsExperience) {
        this.name = name;
        this.displayName = displayName;
        this.levelsExperience = new ArrayList<>(Arrays.asList(levelsExperience));
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ArrayList<Integer> getLevels() {
        return levelsExperience;
    }
}
