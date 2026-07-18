package net.ody.uniQuests.handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.modules.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class QuestHandler {
    public static ItemStack createQuestItem(Quest quest, UniQuests plugin, Player player) {
        PlayerData playerData=plugin.getOrCreatePlayerData(player);
        boolean inCompletion=playerData.getActive(quest.id)!=null;
        boolean isCompleted=playerData.isCompleted(quest.id);

        Material material = isCompleted ? Material.GRAY_DYE : Material.OAK_HANGING_SIGN;
        ItemStack questItem = new ItemStack(material);
        ItemMeta questMeta = questItem.getItemMeta();

        if (inCompletion){
            questMeta.setEnchantmentGlintOverride(true);
        }

        String name;
        if (isCompleted){
            name="[COMPLETED]"+quest.display_name;
        } else{
            name= quest.display_name;
        }

        Component displayName = Component.text(name,NamedTextColor.AQUA).decoration(TextDecoration.ITALIC,false);
        questMeta.displayName(displayName);

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(quest.desc, NamedTextColor.GRAY));

        lore.add(Component.text("Requirements:",NamedTextColor.GREEN).decoration(TextDecoration.ITALIC,false));
        for (Requirement requirement : quest.requirements) {
            String line = buildRequirement(requirement,plugin,player);
            lore.add(Component.text(line).decoration(TextDecoration.ITALIC,false));
        }
        questMeta.lore(lore);

        questItem.setItemMeta(questMeta);
        return questItem;
    }

    public static String buildRequirement(Requirement requirement, UniQuests plugin,Player player) {
        String verb;
        String subject;
        PlayerData data=plugin.getOrCreatePlayerData(player);
        boolean inCompletion=data.getActive(requirement.quest_id)!=null;

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

        String strAmount;
        if (requirement.type.equals("have")){
            int currentAmount = countItems(player, requirement.item);
            strAmount = currentAmount + "/" + requirement.amount;
        }
        else if (inCompletion){
            ActiveQuest activeQuest =data.getActive(requirement.quest_id);
            Quest quest=plugin.quests.get(activeQuest.quest_id);
            String requirementId= String.valueOf(quest.requirements.indexOf(requirement));
            int currentProgress=activeQuest.progress.get(requirementId);
            strAmount=currentProgress+"/"+requirement.amount;
        } else{
            strAmount=""+requirement.amount;
        }

        return " - " + verb + " " + strAmount + " " + pluralize(subjectDisplayName, requirement.amount, requirement.type);
    }

    public static int countItems(Player player,String item){
        ItemStack[] items = player.getInventory().getContents();
        Material itemMat = Material.getMaterial(item.toUpperCase(Locale.ROOT));
        if (itemMat == null) {
            throw new IllegalArgumentException("Not a valid item: " + item);
        }
        int count = 0;
        for (ItemStack itemStack : items) {
            if (itemStack == null) {
                continue;
            }
            if (itemStack.getType().equals(itemMat)) {
                count += itemStack.getAmount();
            }
        }
        return count;
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
        String chance = reward.chance != null ? reward.chance + "%" : null;

        if (rewardType.equals("item")){
            line=" - "+reward.amount+" "+prettify(reward.item);
        } else if (rewardType.equals("exp")) {
            line=" - "+reward.amount+" "+reward.exp+" of exp";
        } else {
            line=" - error.unrecognized_reward="+rewardType;
        }

        if (chance!=null) {
            line += " chance:" + chance;
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