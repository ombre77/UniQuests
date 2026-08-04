package net.ody.uniQuests.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.handlers.QuestHandler;
import net.ody.uniQuests.modules.Table;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveRewardTableCommand implements CommandExecutor {
    private final UniQuests plugin;

    public GiveRewardTableCommand(UniQuests plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        // block/console senders are trusted by default (only ops can place/edit command blocks);
        // player senders still need isOp()
        if (sender instanceof Player player && !player.isOp()) {
            player.sendMessage(Component.text("You are not op!", NamedTextColor.RED));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /giveRewardTable <player> <tableId>", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(Component.text("Player '" + args[0] + "' not found or offline.", NamedTextColor.RED));
            return true;
        }

        String tableId = args[1];
        Table table = plugin.tableLoader.getTableById(tableId);

        if (table == null || !"reward".equals(table.table_type)) {
            sender.sendMessage(Component.text("No reward table found with id '" + tableId + "'.", NamedTextColor.RED));
            return true;
        }

        QuestHandler.giveRewardTable(table, target);
        sender.sendMessage(Component.text("Gave reward table '" + tableId + "' to " + target.getName() + ".", NamedTextColor.GREEN));
        return true;
    }
}
