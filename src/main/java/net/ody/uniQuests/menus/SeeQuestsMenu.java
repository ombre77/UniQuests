package net.ody.uniQuests.menus;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.handlers.QuestHandler;
import net.ody.uniQuests.modules.PlayerData;
import net.ody.uniQuests.modules.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static net.ody.uniQuests.utils.Item.createItem;

public class SeeQuestsMenu {
    public static final Component MENU_TITLE=Component.text("Quests");
    private static final int MENU_SIZE = 9*6; //3 rows

    public static final int QUESTS_START_SLOT = 9;
    public static final int QUESTS_END_SLOT = 44; // inclusive
    public static final int QUESTS_PER_PAGE = QUESTS_END_SLOT - QUESTS_START_SLOT + 1; // 45

    public static final int PREV_PAGE_SLOT = 46;
    public static final int NEXT_PAGE_SLOT = 52;

    //track each player view
    private record MenuState(String type, int page) {}
    private static final Map<UUID, MenuState> playerState = new HashMap<>();

    public static void open(Player player, UniQuests plugin, String type) {
        open(player, plugin, type, 0);
    }

    public static void open(Player player, UniQuests plugin,String type,int page) {
        Inventory inv = Bukkit.createInventory(null, MENU_SIZE, MENU_TITLE);

        ItemStack filler = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta=filler.getItemMeta();
        fillerMeta.displayName(Component.text(""));
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < MENU_SIZE; i++) {
            inv.setItem(i, filler);
        }

        List<Quest> quests = getQuestsForType(plugin, type,player);
        switch (type) {
            case "global" -> {
                ItemStack header=createItem(Material.KNOWLEDGE_BOOK,
                        Component.text("Quests",NamedTextColor.AQUA),
                        List.of(Component.text("Your quests",NamedTextColor.DARK_GRAY),
                                Component.text("Page "+(page+1)+"/"+getTotalPages(quests),NamedTextColor.GRAY)));
                inv.setItem(4,header);
            }
            case "daily" -> {
                ItemStack header=createItem(Material.KNOWLEDGE_BOOK,
                        Component.text("Daily Quests",NamedTextColor.AQUA),
                        List.of(Component.text("Your daily quests",NamedTextColor.DARK_GRAY),
                                Component.text("Page "+(page+1)+"/"+getTotalPages(quests),NamedTextColor.GRAY)));
                inv.setItem(4,header);
            }
            case "weekly" -> {
                ItemStack header=createItem(Material.KNOWLEDGE_BOOK,
                        Component.text("Weekly Quests",NamedTextColor.AQUA),
                        List.of(Component.text("Your weekly quests",NamedTextColor.DARK_GRAY),
                                Component.text("Page "+(page+1)+"/"+getTotalPages(quests),NamedTextColor.GRAY)));
                inv.setItem(4,header);
            }
            case "monthly" -> {
                ItemStack header=createItem(Material.KNOWLEDGE_BOOK,
                        Component.text("Monthly Quests",NamedTextColor.AQUA),
                        List.of(Component.text("Your monthly quests",NamedTextColor.DARK_GRAY),
                                Component.text("Page "+(page+1)+"/"+getTotalPages(quests),NamedTextColor.GRAY)));
                inv.setItem(4,header);
            }
            case "followed" -> {
                quests=new ArrayList<>();
                PlayerData playerData=plugin.getOrCreatePlayerData(player);
                for (Map.Entry<String,Quest> mapEntry:plugin.quests.entrySet()){
                    Quest quest=mapEntry.getValue();
                    if (playerData.getActive(quest.id)!=null){
                        quests.add(quest);
                    }
                }

                ItemStack header=createItem(Material.KNOWLEDGE_BOOK,
                        Component.text("Your Ongoing Quests",NamedTextColor.AQUA),
                        List.of(Component.text("Page "+(page+1)+"/"+getTotalPages(quests),NamedTextColor.GRAY)));
                inv.setItem(4,header);
            }
        }

        fillQuests(inv, quests, page,plugin,player);
        fillNavigation(inv, quests, page);

        player.openInventory(inv);
        playerState.put(player.getUniqueId(), new MenuState(type, page));
    }

    private static List<Quest> getQuestsForType(UniQuests plugin, String type,Player player) {
        List<Quest> filtered = new ArrayList<>();
        for (Quest quest : plugin.quests.values()) {
            if (!type.equals("followed")){
                if (type.equals(quest.type)) {
                    filtered.add(quest);
                }}
            else {
                PlayerData playerData=plugin.getOrCreatePlayerData(player);
                if (playerData.getActive(quest.id)!=null){
                    filtered.add(quest);
                }
            }
        }
        return filtered;
    }

    private static void fillQuests(Inventory inv, List<Quest> quests, int page,UniQuests plugin,Player player) {
        int totalPages = getTotalPages(quests);
        page = Math.clamp(page, 0, totalPages - 1);

        int fromIndex = page * QUESTS_PER_PAGE;
        int toIndex = Math.min(fromIndex + QUESTS_PER_PAGE, quests.size());

        int slot = QUESTS_START_SLOT;
        for (int i = fromIndex; i < toIndex; i++) {
            Quest quest = quests.get(i);
            inv.setItem(slot, QuestHandler.createQuestItem(quest,plugin,player));
            slot++;
        }
    }

    private static void fillNavigation(Inventory inv, List<Quest> quests, int page) {
        int totalPages = getTotalPages(quests);

        if (page > 0) {
            ItemStack prev = createItem(Material.ARROW,
                    Component.text("Previous Page", NamedTextColor.GREEN),
                    List.of(Component.text("Page " + page + " / " + totalPages, NamedTextColor.GRAY)));
            inv.setItem(PREV_PAGE_SLOT, prev);
        }

        if (page < totalPages - 1) {
            ItemStack next = createItem(Material.ARROW,
                    Component.text("Next Page", NamedTextColor.GREEN),
                    List.of(Component.text("Page " + (page + 2) + " / " + totalPages, NamedTextColor.GRAY)));
            inv.setItem(NEXT_PAGE_SLOT, next);
        }
    }

    private static int getTotalPages(List<Quest> quests) {
        return Math.max(1, (int) Math.ceil((double) quests.size() / QUESTS_PER_PAGE));
    }

    public static Quest getQuestAtSlot(UniQuests plugin, Player player, int slot) {
        if (slot < QUESTS_START_SLOT || slot > QUESTS_END_SLOT) {
            return null;
        }
        MenuState state = playerState.get(player.getUniqueId());
        if (state == null) {
            return null;
        }
        List<Quest> quests = getQuestsForType(plugin, state.type(),player);
        int index = state.page() * QUESTS_PER_PAGE + (slot - QUESTS_START_SLOT);
        if (index < 0 || index >= quests.size()) {
            return null;
        }
        return quests.get(index);
    }

    public static String getType(Player player) {
        MenuState state = playerState.get(player.getUniqueId());
        return state != null ? state.type() : "global";
    }

    public static int getPage(Player player) {
        MenuState state = playerState.get(player.getUniqueId());
        return state != null ? state.page() : 0;
    }

    public static void clearState(Player player) {
        playerState.remove(player.getUniqueId());
    }
}
