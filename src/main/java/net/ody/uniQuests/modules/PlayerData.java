package net.ody.uniQuests.modules;

import net.ody.uniQuests.utils.DateUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    public List<CompletedQuest> daily = new ArrayList<>();
    public List<CompletedQuest> weekly = new ArrayList<>();
    public List<CompletedQuest> monthly = new ArrayList<>();
    public List<CompletedQuest> quests = new ArrayList<>();
    public List<ActiveQuest> active = new ArrayList<>();

    public ActiveQuest getActive(String questId) {
        for (ActiveQuest activeQuest : active) {
            if (activeQuest.quest_id.equals(questId)) {
                return activeQuest;
            }
        }
        return null;
    }

    public boolean isCompleted(String questId) {
        for (List<CompletedQuest> list : List.of(daily, weekly,monthly, quests)) {
            for (CompletedQuest completedQuest : list) {
                if (completedQuest.quest_id.equals(questId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeActive(String questId) {
        return active.removeIf(activeQuest -> activeQuest.quest_id.equals(questId));
    }

    public void addCompleted(Quest quest, String type, JavaPlugin plugin){
        if (isCompleted(quest.id)){return;}
        List<CompletedQuest> Quests;
        switch (type){
            case "global" -> Quests=quests;
            case "daily" -> Quests=daily;
            case "weekly" -> Quests=weekly;
            case "monthly" -> Quests=monthly;
            default -> {
                return;
            }
        }
        Quests.add(new CompletedQuest(quest.id, DateUtils.getServerTime(plugin)));
    }
}