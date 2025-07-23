package org.s1queence.api.logic_item;

import de.tr7zw.nbtapi.NBT;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static org.s1queence.S1queenceLib.logicItemManager;
import static org.s1queence.api.S1TextUtils.createItemFromMap;

public abstract class LogicItem extends ItemStack {

    private String logicItemType;
    protected JavaPlugin plugin;
    protected YamlDocument config;

    protected ItemStack itemStack;

    public LogicItem(@NotNull String type, boolean isUIPanelItem) {
        if (logicItemManager.getUIPanelItem(type) == null && logicItemManager.getLogicItem(type) == null) return;

        LogicItemData data = isUIPanelItem ? logicItemManager.getUIPanelItem(type) : logicItemManager.getLogicItem(type);

        this.logicItemType = type;
        this.config = data.getConfig();
        this.plugin = data.getPlugin();
    }

    protected ItemStack getItemStackFromConfig() {
        return createItemFromMap(config.getSection(logicItemType + ".item").getStringRouteMappedValues(true));
    }

    public void setLoreFirstLine(ItemStack item, String to) {
        if (item == null) return;
        ItemMeta im = item.getItemMeta();
        if (im == null) return;

        List<String> lore = ofNullable(im.getLore()).orElse(new ArrayList<>());
        if (lore.isEmpty()) {
            lore.add(0, to);
        } else {
            lore.set(0, to);
        }

        im.setLore(lore);
        item.setItemMeta(im);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public abstract ItemStack create();

    public void killItem(ItemStack item) {
        item.setAmount(0);
    }

    public void replaceItem(ItemStack item, ItemStack toReplace) {
        item.setType(toReplace.getType());

        NBT.modify(item, nbt -> {
            nbt.clearNBT();
            nbt.clearCustomNBT();
            nbt.mergeCompound(NBT.itemStackToNBT(toReplace));
        });

        item.setItemMeta(toReplace.getItemMeta());
    }

    // tnx Chat-GPT
    public static Color convertHex(String hex) {
        if (hex.startsWith("#")) hex = hex.substring(1);

        try {
            int rgb = Integer.parseInt(hex, 16);

            return Color.fromRGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
        } catch (Exception ignored) {}

        return null;
    }

    public void setItemColor(ItemStack item, String hex) {
        ItemMeta im = item.getItemMeta();
        if (!(im instanceof LeatherArmorMeta)) return;
        LeatherArmorMeta colored = (LeatherArmorMeta) im;

        colored.setColor(convertHex(hex));
        item.setItemMeta(colored);
    }

    public void removeAndHideArmorModifier(ItemStack item) {
        ItemMeta im = item.getItemMeta();
        if (im == null) return;

        AttributeModifier armor = new AttributeModifier(
                new NamespacedKey(plugin, UUID.randomUUID().toString()),
                0.0d,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.ANY
        );
        im.addAttributeModifier(Attribute.GENERIC_ARMOR, armor);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(im);
    }

    protected void hideEnchants(ItemStack item) {
        ItemMeta im = item.getItemMeta();
        if (im == null) return;
        im.setEnchantmentGlintOverride(false);
        item.setItemMeta(im);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public YamlDocument getConfig() {
        return config;
    }
}
