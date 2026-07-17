package net.ody.uniQuests.listeners;

import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.menus.QuestDetailMenu;
import net.ody.uniQuests.menus.SeeQuestsMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class QuestDetailMenuListener implements Listener {
    private final UniQuests plugin;

    public QuestDetailMenuListener(UniQuests plugin) {
        this.plugin=plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!event.getView().title().equals(QuestDetailMenu.MENU_TITLE)){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event){
        if (!event.getView().title().equals(QuestDetailMenu.MENU_TITLE)){
            return;
        }
        if (event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) {
            return;
        }
        Player player = (Player) event.getPlayer();
        String type = SeeQuestsMenu.getType(player);
        Bukkit.getScheduler().runTask(plugin, () -> SeeQuestsMenu.open(player, plugin, type));
    }
}
