package net.ody.uniQuests.handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.modules.Price;
import net.ody.uniQuests.modules.Quest;
import net.ody.uniQuests.modules.Requirement;
import net.ody.uniQuests.modules.RewardEntry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class QuestHandler {
    public static ItemStack createQuestItem(Quest quest,UniQuests plugin) {
        ItemStack questItem = new ItemStack(Material.OAK_HANGING_SIGN);
        ItemMeta questMeta = questItem.getItemMeta();

        Component displayName = Component.text(quest.display_name,NamedTextColor.AQUA).decoration(TextDecoration.ITALIC,false);
        questMeta.displayName(displayName);

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(quest.desc, NamedTextColor.GRAY));

        lore.add(Component.text("Requirements:",NamedTextColor.GREEN).decoration(TextDecoration.ITALIC,false));
        for (Requirement requirement : quest.requirements) {
            String line = buildRequirement(requirement,plugin);
            lore.add(Component.text(line).decoration(TextDecoration.ITALIC,false));
        }

        questMeta.lore(lore);

        questItem.setItemMeta(questMeta);
        return questItem;
    }

    public static String buildRequirement(Requirement requirement, UniQuests plugin) {
        String verb;
        String subject;

        switch (requirement.type) {
            case "have" -> {
                verb = "Have";
                subject = requirement.item;
            }
            case "killed" -> {
                verb = "Kill";
                subject = requirement.mob;
            }
            case "placed" -> {
                verb = "Place";
                subject = requirement.block;
            }
            case "quest" -> {
                verb="Have completed";
                subject = plugin.quests.get(requirement.quest_id).display_name;
            }
            default -> {
                verb = "Unknown requirement (" + requirement.type + "):";
                subject = "";
            }
        }

        String subjectDisplayName = prettify(subject);

        return " - " + verb + " " + requirement.amount + " " + pluralize(subjectDisplayName, requirement.amount, requirement.type);
    }

    public static String buildPrice(Price price){
        String line;
        String priceType = price.type != null ? price.type : "item"; // default when JSON omits "type"
        if (priceType.equals("item")){
            line=" - "+price.amount+" "+prettify(price.item);
        } else if (priceType.equals("exp")) {
            line=" - "+price.amount+" "+price.exp+" of exp";
        } else {
            line=" - error.unrecognized_price="+priceType;
        }
        return line;
    }

    public static String buildReward(RewardEntry reward){
        String line;
        String rewardType = reward.type != null ? reward.type : "item";
        String chance = reward.chance != null ? reward.chance + "%" : "100%";

        if (rewardType.equals("item")){
            line=" - "+reward.amount+" "+prettify(reward.item);
        } else if (rewardType.equals("exp")) {
            line=" - "+reward.amount+" "+reward.exp+" of exp";
        } else {
            line=" - error.unrecognized_reward="+rewardType;
        }

        return line;
    }

    private static String prettify(String raw) {
        if (raw == null || raw.isEmpty()) {
            return "";
        }
        String[] parts = raw.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            sb.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1).toLowerCase(Locale.ROOT))
                    .append(" ");
        }
        return sb.toString().trim();
    }

    private static String pluralize(String word, int amount, String type) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        if (type.equals("placed")) {
            return word;
        }
        return amount == 1 ? word : word + "s";
    }
}