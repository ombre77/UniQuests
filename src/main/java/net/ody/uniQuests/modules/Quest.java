package net.ody.uniQuests.modules;

import java.util.List;

public class Quest {
    public String display_name;
    public String desc;
    public List<Requirement> requirements;
    public List<Price> price;   // taken from the player when the quest is completed/turned in
    public List<RewardEntry> reward;
    public String type;
    public String created;
    public String expire;
    public String id;
}