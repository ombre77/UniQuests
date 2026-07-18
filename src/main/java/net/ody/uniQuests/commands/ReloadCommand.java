package net.ody.uniQuests.commands;

import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.modules.QuestLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    private final UniQuests plugin;

    public ReloadCommand(UniQuests plugin){
        this.plugin=plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {return false;}
        QuestLoader loader=plugin.questLoader;
        plugin.quests=loader.loadAllQuests();
        plugin.playersData=loader.loadPlayersData();
        plugin.playersStats=loader.loadPlayersStats();
        return true;
    }
}
