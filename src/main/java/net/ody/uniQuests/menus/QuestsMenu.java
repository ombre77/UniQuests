package net.ody.uniQuests.menus;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.ody.uniQuests.UniQuests;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static net.ody.uniQuests.utils.Item.createItem;

public class QuestsMenu {
    public static final Component MENU_TITLE= Component.text("Quests Menu");
    private static final int MENU_SIZE = 27; //3 rows

    public static void open(Player player, UniQuests plugin) {
        Inventory inv = Bukkit.createInventory(null, MENU_SIZE, MENU_TITLE);

        ItemStack filler = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta=filler.getItemMeta();
        fillerMeta.displayName(Component.text(""));
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < MENU_SIZE; i++) {
            inv.setItem(i, filler);
        }

        ItemStack header=createItem(Material.ENDER_EYE,
                Component.text("Quest Menu",NamedTextColor.AQUA),
                List.of(Component.text("Check your quests and the leaderboard!", NamedTextColor.GRAY)));
        inv.setItem(4,header);

        ItemStack globalQuests=createItem(Material.KNOWLEDGE_BOOK,
                Component.text("Quests"),
                List.of(Component.text("Click to see your quests!",NamedTextColor.DARK_GRAY)));
        ItemStack dailyQuests=createItem(Material.CLOCK,
                Component.text("Daily Quests"),
                List.of(Component.text("Click to see your daily quests!",NamedTextColor.DARK_GRAY)));
        ItemStack weeklyQuests=createItem(Material.CLOCK,
                Component.text("Weekly Quests"),
                List.of(Component.text("Click to see your weekly quests!",NamedTextColor.DARK_GRAY)));
        ItemStack monthlyQuests=createItem(Material.CLOCK,
                Component.text("Monthly Quests"),
                List.of(Component.text("Click to see your monthly quests!",NamedTextColor.DARK_GRAY)));
        inv.setItem(10,globalQuests);
        inv.setItem(12,dailyQuests);
        inv.setItem(14,weeklyQuests);
        inv.setItem(16,monthlyQuests);

        ItemStack leaderboard=createItem(Material.NETHER_STAR,
                Component.text("Leaderboard"),
                List.of(Component.text("Click to see the leaderboard!",NamedTextColor.GRAY)));
        inv.setItem(22,leaderboard);

        player.openInventory(inv);
    }


}
