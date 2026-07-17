package net.ody.uniQuests.modules;

public class CompletedQuest {
    public String quest_id;
    public long completed_at;

    public CompletedQuest() {
    }

    public CompletedQuest(String questId, long completedAt) {
        this.quest_id = questId;
        this.completed_at = completedAt;
    }
}
