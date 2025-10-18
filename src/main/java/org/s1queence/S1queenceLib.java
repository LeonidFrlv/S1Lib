package org.s1queence;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.s1queence.api.YamlConfigUtil;
import org.s1queence.api.generated_items.GeneratedItemsManager;
import org.s1queence.api.interactive_display.InteractiveDisplayManager;
import org.s1queence.api.interactive_display.interact_event.DisplayInteractEventCaller;
import org.s1queence.api.interactive_display.listeners.InteractiveDisplayRemoveListener;
import org.s1queence.api.logic_item.LogicItemData;
import org.s1queence.api.logic_item.LogicItemManager;
import org.s1queence.api.logic_item.ui_inventory_panel.EmptyItemListener;
import org.s1queence.api.logic_item.ui_inventory_panel.UIPanelItemClickEventCaller;
import org.s1queence.api.rpg_skills.SkillsManager;
import org.s1queence.commands.LibCommand;
import org.s1queence.api.countdown.listeners.DebugListener;

import java.util.Objects;

import static org.s1queence.api.S1TextUtils.consoleLog;
import static org.s1queence.api.S1TextUtils.getConvertedTextFromConfig;

public class S1queenceLib extends JavaPlugin {
    private YamlDocument textConfig;
    private YamlDocument optionsConfig;
    public static S1queenceLib lib;
    public static InteractiveDisplayManager interactiveDisplayManager;
    public static LogicItemManager logicItemManager;
    public static GeneratedItemsManager generatedItemsManager;
    public static SkillsManager skillsManager;

    public static String EMPTY_ITEM_KEY = "empty_item";

    @Override
    public void onEnable() {

        textConfig = YamlConfigUtil.createConfig("text.yml", this);
        optionsConfig = YamlConfigUtil.createConfig("options.yml", this);

        consoleLog(getConvertedTextFromConfig(textConfig, "onEnable_msg", this.getName()), this);

        lib = this;
        Objects.requireNonNull(getServer().getPluginCommand("s1lib")).setExecutor(new LibCommand(this));

        logicItemManager = new LogicItemManager();
        skillsManager = new SkillsManager();
        generatedItemsManager = new GeneratedItemsManager();

        logicItemManager.registerUIPanelItem(new LogicItemData(this, optionsConfig, EMPTY_ITEM_KEY));

        setupInteractiveDisplayManagerUpdater();

        getServer().getPluginManager().registerEvents(new DebugListener(), this);
        getServer().getPluginManager().registerEvents(new DisplayInteractEventCaller(this), this);
        getServer().getPluginManager().registerEvents(new InteractiveDisplayRemoveListener(), this);
        getServer().getPluginManager().registerEvents(new UIPanelItemClickEventCaller(), this);
        getServer().getPluginManager().registerEvents(new EmptyItemListener(), this);
    }

    public void setupInteractiveDisplayManagerUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                interactiveDisplayManager = new InteractiveDisplayManager();
            }
        }.runTaskTimer(this, 0, 60);
    }

    @Override
    public void onDisable() {
        consoleLog(getConvertedTextFromConfig(textConfig, "onDisable_msg", this.getName()), this);
    }

    public static S1queenceLib getLib() {
        return lib;
    }

    public YamlDocument getTextConfig() {
        return textConfig;
    }
    public void setTextConfig(YamlDocument newState) {
        textConfig = newState;
    }

    public YamlDocument getOptionsConfig() {
        return optionsConfig;
    }

    public void setOptionsConfig(YamlDocument optionsConfig) {
        this.optionsConfig = optionsConfig;
    }
}