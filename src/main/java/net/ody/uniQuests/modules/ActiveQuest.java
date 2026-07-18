package net.ody.uniQuests.modules;

import java.util.HashMap;
import java.util.Map;

public class ActiveQuest {
    public String quest_id;
    public long started_at;
    public Map<String, Integer> progress = new HashMap<>(); // requirement index (as string) -> current amount

    public ActiveQuest(String questId, long startedAt) {
        this(questId, startedAt, 0);
    }

    public ActiveQuest(String questId, long startedAt, int requirementCount) {
        this.quest_id = questId;
        this.started_at = startedAt;
        for (int i = 0; i < requirementCount; i++) {
            this.progress.put(String.valueOf(i), 0);
        }
    }

}