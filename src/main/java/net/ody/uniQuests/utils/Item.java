package net.ody.uniQuests.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private static Component noItalic(Component component) {
        return component.decoration(TextDecoration.ITALIC, false);
    }

    public static ItemStack createItem(Material material, Component name, List<Component> lore, ItemRarity rarity) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setRarity(rarity);

        List<Component> componentLore = new ArrayList<>();
        for (Component line : lore) {
            componentLore.add(noItalic(line));
        }

        meta.displayName(noItalic(name));
        meta.lore(componentLore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, Component name, List<Component> lore) {
        return createItem(material, name, lore, ItemRarity.UNCOMMON);
    }
}
