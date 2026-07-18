package net.ody.uniQuests.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.menus.QuestDetailMenu;
import net.ody.uniQuests.menus.SeeQuestsMenu;
import net.ody.uniQuests.modules.ActiveQuest;
import net.ody.uniQuests.modules.PlayerData;
import net.ody.uniQuests.modules.Quest;
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

        if (event.getSlot() != QuestDetailMenu.START_QUEST_SLOT) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Quest quest = QuestDetailMenu.getQuest(player);
        if (quest == null) {
            return;
        }

            PlayerData data = plugin.getOrCreatePlayerData(player);
        if (data.isCompleted(quest.id)) {
            player.sendMessage(Component.text("You have already completed this quest.", NamedTextColor.RED));
            return;
        }
        if (data.getActive(quest.id) != null) {
            data.removeActive(quest.id);
            plugin.questLoader.savePlayersData(plugin.playersData);

            player.sendMessage(Component.text("Quest abandoned: ", NamedTextColor.RED)
                    .append(Component.text(quest.display_name, NamedTextColor.AQUA)));

            QuestDetailMenu.open(player, plugin, quest);
            return;
        }

        data.active.add(new ActiveQuest(quest.id, System.currentTimeMillis(),quest.requirements.size()));
        plugin.questLoader.savePlayersData(plugin.playersData);

        player.sendMessage(Component.text("Quest started: ", NamedTextColor.GREEN)
                .append(Component.text(quest.display_name, NamedTextColor.AQUA)));

        QuestDetailMenu.open(player, plugin, quest);
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event){
        if (!event.getView().title().equals(QuestDetailMenu.MENU_TITLE)){
            return;
        }
        Player player = (Player) event.getPlayer();
        if (event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) {
            return;
        }
        QuestDetailMenu.clearState(player);
        String type = SeeQuestsMenu.getType(player);
        Bukkit.getScheduler().runTask(plugin, () -> SeeQuestsMenu.open(player, plugin, type));
    }
}
