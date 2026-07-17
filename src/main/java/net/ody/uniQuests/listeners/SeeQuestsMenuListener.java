package net.ody.uniQuests.listeners;

import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.menus.QuestDetailMenu;
import net.ody.uniQuests.menus.SeeQuestsMenu;
import net.ody.uniQuests.modules.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class SeeQuestsMenuListener implements Listener {
    private final UniQuests plugin;

    public SeeQuestsMenuListener(UniQuests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event){
        if (!event.getView().title().equals(SeeQuestsMenu.MENU_TITLE)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getSlot();
        ItemStack item=event.getCurrentItem();
        Player player=(Player) event.getWhoClicked();

        if (item == null || item.getType().isAir()) {
            return;
        }

        String type = SeeQuestsMenu.getType(player);
        int page = SeeQuestsMenu.getPage(player);
        if (slot == SeeQuestsMenu.PREV_PAGE_SLOT) {
            SeeQuestsMenu.open(player, plugin, type, page - 1);
            return;
        }

        if (slot == SeeQuestsMenu.NEXT_PAGE_SLOT) {
            SeeQuestsMenu.open(player, plugin, type, page + 1);
            return;
        }

        Quest clicked = SeeQuestsMenu.getQuestAtSlot(plugin, player, slot);
        if (clicked != null) {
            QuestDetailMenu.open(player,plugin,clicked);
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        if (!event.getView().title().equals(SeeQuestsMenu.MENU_TITLE)) {
            return;
        }
        if (event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) {
            return;
        }
        SeeQuestsMenu.clearState((Player) event.getPlayer());
    }
}
