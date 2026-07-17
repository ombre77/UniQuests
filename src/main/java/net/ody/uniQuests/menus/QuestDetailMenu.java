package net.ody.uniQuests.menus;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.handlers.QuestHandler;
import net.ody.uniQuests.modules.Price;
import net.ody.uniQuests.modules.Quest;
import net.ody.uniQuests.modules.Requirement;
import net.ody.uniQuests.modules.RewardEntry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static net.ody.uniQuests.utils.Item.createItem;

public class QuestDetailMenu {

    public static final Component MENU_TITLE= Component.text("Quest Details");
    private static final int MENU_SIZE = 9*3; //3 rows

    public static void open(Player player, UniQuests plugin, Quest quest) {
        Inventory inv = Bukkit.createInventory(null, MENU_SIZE, MENU_TITLE);

        ItemStack filler = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta=filler.getItemMeta();
        fillerMeta.displayName(Component.text(""));
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < MENU_SIZE; i++) {
            inv.setItem(i, filler);
        }

        ItemStack header=createItem(Material.ENDER_EYE,
                Component.text("Quest Details", NamedTextColor.AQUA),
                List.of(Component.text("Check multiple infos on a quest", NamedTextColor.GRAY)));
        inv.setItem(4,header);

        String deleteMsg;
        if (quest.type.equals("global")) {
            deleteMsg = "No expiration";
        } else {
            deleteMsg = "Expired the "+quest.expire;
        }
        ItemStack time=createItem(Material.CLOCK,
                Component.text("Deletion Date",NamedTextColor.GOLD),
                List.of(Component.text(deleteMsg,NamedTextColor.GRAY)));
        inv.setItem(21,time);

        //details: name, desc, requirements, cost and reward
        ItemStack questNameItem=createItem(Material.ENCHANTED_BOOK,
                Component.text(quest.display_name,NamedTextColor.GREEN),
                List.of(Component.text("Name of the quest",NamedTextColor.DARK_GRAY)));
        inv.setItem(11,questNameItem);

        ItemStack questDescItem=createItem(Material.CREEPER_BANNER_PATTERN,
                Component.text(quest.desc,NamedTextColor.GREEN),
                List.of(Component.text("Desc of the quest",NamedTextColor.DARK_GRAY)));
        inv.setItem(12,questDescItem);

        List<Component> RequireLore=new ArrayList<>();
        for (Requirement requirement:quest.requirements){
            String line=QuestHandler.buildRequirement(requirement,plugin);
            RequireLore.add(Component.text(line,NamedTextColor.GREEN));
        }
        RequireLore.add(Component.text("Requirements of the quest to validate it",NamedTextColor.DARK_GRAY));

        ItemStack questRequireItem=createItem(Material.HOPPER,
                Component.text("Requirements",NamedTextColor.GREEN),
                RequireLore);
        inv.setItem(13,questRequireItem);

        List<Component> CostLore=new ArrayList<>();
        for (Price price:quest.price){
            String line=QuestHandler.buildPrice(price);
            CostLore.add(Component.text(line,NamedTextColor.GREEN));
        }
        CostLore.add(Component.text("Cost (or price) of the quest",NamedTextColor.DARK_GRAY));

        ItemStack questCostItem=createItem(Material.GOLD_INGOT,
                Component.text("Cost",NamedTextColor.GREEN),
                CostLore);
        inv.setItem(14,questCostItem);

        List<Component> RewardsLore=new ArrayList<>();
        for (RewardEntry reward:quest.reward){
            String line=QuestHandler.buildReward(reward);
            RewardsLore.add(Component.text(line,NamedTextColor.GREEN));
        }
        RewardsLore.add(Component.text("Rewards of the quest for completing it",NamedTextColor.DARK_GRAY));

        ItemStack questRewardItem=createItem(Material.EMERALD,
                Component.text("Rewards",NamedTextColor.GREEN),
                RewardsLore);
        inv.setItem(15,questRewardItem);

        ItemStack moreInfo=createItem(Material.PAPER,
                Component.text("More infos on the quest",NamedTextColor.GOLD),
                List.of(
                        Component.text("Quest id: "+quest.id,NamedTextColor.DARK_GRAY),
                        Component.text("Created the "+quest.created,NamedTextColor.DARK_GRAY),
                        Component.text("Deleted the "+quest.expire, NamedTextColor.DARK_GRAY),
                        Component.text("Type: "+quest.type,NamedTextColor.DARK_GRAY)
                ));
        inv.setItem(23,moreInfo);

        player.openInventory(inv);
    }
}
