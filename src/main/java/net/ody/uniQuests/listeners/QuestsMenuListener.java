package net.ody.uniQuests.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.menus.QuestsMenu;
import net.ody.uniQuests.menus.SeeQuestsMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class QuestsMenuListener implements Listener {
    private final UniQuests plugin;

    public QuestsMenuListener(UniQuests plugin){
        this.plugin=plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event){
        if (!event.getView().title().equals(QuestsMenu.MENU_TITLE)) {
            return;
        }
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        switch (slot){ //global quests
            case 10 ->{
                player.closeInventory();
                SeeQuestsMenu.open(player,plugin,"global");
            }
            case 12 -> {
                player.sendMessage(Component.text("This functionality isn't ready yet!", NamedTextColor.RED));
                return;
                //player.closeInventory();
                //SeeQuestsMenu.open(player,plugin,"daily");
            }
            case 14 -> {
                player.sendMessage(Component.text("This functionality isn't ready yet!", NamedTextColor.RED));
                return;
                //player.closeInventory();
                //SeeQuestsMenu.open(player,plugin,"weekly");
            }
            case 16 -> {
                player.sendMessage(Component.text("This functionality isn't ready yet!", NamedTextColor.RED));
                return;
                //player.closeInventory();
                //SeeQuestsMenu.open(player,plugin,"monthly");
            }
            case 22 -> {
                player.sendMessage(Component.text("This functionality isn't ready yet!", NamedTextColor.RED));
                return;
            }
            case 0->{
                SeeQuestsMenu.open(player,plugin,"followed");
            }
        }
    }
}
