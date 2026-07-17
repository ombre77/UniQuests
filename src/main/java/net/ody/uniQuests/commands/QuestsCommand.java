package net.ody.uniQuests.commands;

import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.menus.QuestsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QuestsCommand implements CommandExecutor {
    private final UniQuests plugin;

    public QuestsCommand(UniQuests plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }

        QuestsMenu.open(player,plugin);
        return true;
    }
}