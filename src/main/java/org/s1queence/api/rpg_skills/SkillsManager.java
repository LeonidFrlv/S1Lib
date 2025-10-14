package org.s1queence.api.rpg_skills;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SkillsManager {
    private final HashMap<String, RPGSkill> registeredSkills = new HashMap<>();

    @Nullable
    public RPGSkill getRegistered(String skillName) {
        return registeredSkills.get(skillName);
    }

    public boolean isRegistered(String skillName) {
        return getRegistered(skillName) != null;
    }

    public void setRegistered(RPGSkill skill, boolean register) {
        String name = skill.getName();
        if (isRegistered(name)) {
            if (!register) registeredSkills.remove(name);
            return;
        }

        if (!register) return;

        registeredSkills.put(name, skill);
    }

    public HashMap<String, RPGSkill> getRegisteredSkills() {
        return registeredSkills;
    }

    public RPGSkill createAndRegisterSkill(String name, String displayName, Integer... levelsAndExperience) {
        RPGSkill skill = new RPGSkill(name, displayName, levelsAndExperience);
        setRegistered(skill, true);
        return skill;
    }
}