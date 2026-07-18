package net.ody.uniQuests.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.modules.ActiveQuest;
import net.ody.uniQuests.modules.PlayerData;
import net.ody.uniQuests.modules.Quest;
import net.ody.uniQuests.modules.Requirement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class QuestProgressListener implements Listener {
    private final UniQuests plugin;

    public QuestProgressListener(UniQuests plugin){
        this.plugin=plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        Player player=event.getEntity().getKiller();
        if (player==null){
            return;
        }
        trackProgress(player,"killed",event.getEntityType().name());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        trackProgress(event.getPlayer(), "placed", event.getBlock().getType().name());
    }

    private void trackProgress(Player player, String requirementType, String subject) {
        PlayerData data = plugin.getOrCreatePlayerData(player);
        if (data.active.isEmpty()) {
            return;
        }

        boolean changed = false;
        for (ActiveQuest activeQuest : data.active) {
            Quest quest = plugin.quests.get(activeQuest.quest_id);
            if (quest == null || quest.requirements == null) {
                continue;
            }

            for (int i = 0; i < quest.requirements.size(); i++) {
                Requirement requirement = quest.requirements.get(i);
                if (!requirement.type.equals(requirementType)) {
                    continue;
                }

                String target = requirementType.equals("killed") ? requirement.mob : requirement.block;
                if (target == null || !target.equalsIgnoreCase(subject)) {
                    continue;
                }

                String key = String.valueOf(i);
                int current = activeQuest.progress.getOrDefault(key, 0);
                if (current >= requirement.amount) {
                    continue; // this requirement is already maxed out
                }

                int updated = current + 1;
                activeQuest.progress.put(key, updated);
                changed = true;

                if (updated == requirement.amount) {
                    player.sendMessage(Component.text("Requirement complete for ", NamedTextColor.GREEN)
                            .append(Component.text(quest.display_name, NamedTextColor.AQUA))
                            .append(Component.text("!", NamedTextColor.GREEN)));
                }
            }
        }

        if (changed) {
            plugin.questLoader.savePlayersData(plugin.playersData);
        }
    }
}
