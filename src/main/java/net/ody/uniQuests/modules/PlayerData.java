package net.ody.uniQuests.modules;

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
}