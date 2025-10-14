package org.s1queence.api.rpg_skills;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

import static org.s1queence.S1queenceLib.lib;
import static org.s1queence.S1queenceLib.skillsManager;
import static org.s1queence.api.S1TextUtils.getConvertedTextFromConfig;
import static org.s1queence.api.S1Utils.sendActionBarMsg;

public class RPGPlayer {
    public static final String NBT_SKILL_PATH = "s1_rpg_skill_";
    @NotNull
    private final Player player;
    private final PersistentDataContainer data;

    public RPGPlayer(@NotNull Player player) {
        this.player = player;
        this.data = player.getPersistentDataContainer();
    }

    private NamespacedKey getSkillKey(String skillName) {
        return new NamespacedKey(lib, NBT_SKILL_PATH + skillName);
    }

    public void setSkill(RPGSkill skill, int value) {
        if (skill == null || !skillsManager.isRegistered(skill.getName())) return;

        data.set(getSkillKey(skill.getName()), PersistentDataType.INTEGER, value);
    }

    private String getConvertedText(String path, @Nullable String skillName, @Nullable Integer level, @Nullable Integer exp) {
        String configText = getConvertedTextFromConfig(lib.getTextConfig(), path, lib.getName());
        if (configText.equals(" ")) return " ";

        if (level != null) configText = configText.replace("%level%", level + "");
        if (exp != null) configText = configText.replace("%exp%", exp + "");
        if (skillName != null) configText = configText.replace("%skill_name%", skillName);

        return configText;
    }

    private float getDefaultIfNull(float f) {
        return f == 0.0f ? 1.0f : f;
    }

    public void playSound(String soundPath, Player player) {
        String absolutePath = String.join(".", "sounds", soundPath);

        YamlDocument optionsConfig = lib.getOptionsConfig();

        String sound = optionsConfig.getString(absolutePath + ".sound");
        if (sound == null) return;

        float volume = getDefaultIfNull(optionsConfig.getFloat(absolutePath + ".volume"));
        float pitch = getDefaultIfNull(optionsConfig.getFloat(absolutePath + ".pitch"));

        player.playSound(player, sound, volume, pitch);
    }

    private void sendSkillMessages(String path, @Nullable String skillName, @Nullable Integer level, @Nullable Integer exp) {
        String title = getConvertedText(path + ".title", skillName, level, exp);
        String subtitle = getConvertedText(path + ".subtitle", skillName, level, exp);
        String actionBar = getConvertedText(path + ".action_bar", skillName, level, exp);
        String chat = getConvertedText(path + ".chat", skillName, level, exp);

        if (!title.isEmpty() && !subtitle.isEmpty()) player.sendTitle(title, subtitle, 5, 100, 5);
        if (!chat.isEmpty()) player.sendMessage(chat);
        sendActionBarMsg(player, actionBar);
    }

    public void addExp(String skillName, int exp) {
        RPGSkill skill = skillsManager.getRegistered(skillName);
        Integer skillValue = getSkillValue(skillName);
        Integer level = getSkillLevel(skillName);
        if (skill == null || skillValue == null || level == null) return;

        ArrayList<Integer> levels = skill.getLevels();
        Integer levelExp = levels.get(level);
        Integer lastLevelExp = levels.get(levels.size() - 1);

        if (lastLevelExp.equals(levelExp)) {
            setSkill(skill, lastLevelExp);
            return;
        }

        Integer nextExp = levels.get(level + 1);

        int newExp = skillValue + exp;

        if (nextExp <= newExp) {
            for (int i = 0; i < levels.size(); i++) {
                if (levels.get(i) >= newExp) {
                    setLevel(skillName, i, true);
                    return;
                }
            }

            return;
        }

        setSkill(skill, newExp);
        playSound("experience_gain", player);
        sendSkillMessages("skills.experience_gain", skill.getDisplayName(), level, exp);
    }

    public void setLevel(String skillName, int newLevel, boolean showPlayer) {
        RPGSkill skill = skillsManager.getRegistered(skillName);
        if (skill == null || newLevel < 0) return;

        ArrayList<Integer> levels = skill.getLevels();

        for (int i = 0; i < levels.size(); i++) {
            if (i == newLevel || i + 1 == levels.size()) {
                setSkill(skill, levels.get(i));

                if (showPlayer) {
                    playSound("level_increase", player);
                    sendSkillMessages("skills.level_increase", skill.getDisplayName(), newLevel, null);
                }

                return;
            }

        }
    }

    @Nullable
    public Integer getSkillValue(String skillName) {
        if (!skillsManager.isRegistered(skillName)) return null;

        return data.get(getSkillKey(skillName), PersistentDataType.INTEGER);
    }

    @Nullable
    public Integer getSkillLevel(String skillName) {
        RPGSkill skill = skillsManager.getRegistered(skillName);
        Integer skillValue = getSkillValue(skillName);
        if (skill == null || skillValue == null) return null;

        ArrayList<Integer> levels = skill.getLevels();

        for (int i = 0; i < levels.size(); i++) {
            int currentExp = levels.get(i);
            int nextExp = i + 1 < levels.size() ? levels.get(i + 1) : currentExp;

            if ((skillValue >= currentExp && skillValue < nextExp) || (currentExp == nextExp && skillValue >= nextExp))
                return i;
        }

        return null;
    }

    public HashMap<String, Integer> getSkills() {
        HashMap<String, RPGSkill> registeredSkills = skillsManager.getRegisteredSkills();

        HashMap<String, Integer> buffer = new HashMap<>();

        registeredSkills.keySet().forEach(name -> {
            Integer skillValue = getSkillValue(name);
            if (skillValue == null) return;
            buffer.put(name, skillValue);
        });

        return buffer;
    }

}
