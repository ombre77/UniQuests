package net.ody.uniQuests;

import net.ody.uniQuests.commands.QuestsCommand;
import net.ody.uniQuests.commands.ReloadCommand;
import net.ody.uniQuests.listeners.QuestDetailMenuListener;
import net.ody.uniQuests.listeners.QuestProgressListener;
import net.ody.uniQuests.listeners.QuestsMenuListener;
import net.ody.uniQuests.listeners.SeeQuestsMenuListener;
import net.ody.uniQuests.modules.PlayerData;
import net.ody.uniQuests.modules.PlayerStats;
import net.ody.uniQuests.modules.Quest;
import net.ody.uniQuests.modules.QuestLoader;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public final class UniQuests extends JavaPlugin {

    public Logger logger = getLogger();
    public UniQuestsFileManager fileManager=new UniQuestsFileManager(this);
    public String site="https://github.com/ombre77/UniQuests";
    public final QuestLoader questLoader= new QuestLoader(fileManager,logger);

    public Map<String, Quest> quests;
    public Map<String, PlayerData> playersData;
    public Map<String, PlayerStats> playersStats;

    @Override
    public void onEnable() {
        //register commands and listeners
        Objects.requireNonNull(getCommand("quests")).setExecutor(new QuestsCommand(this));
        Objects.requireNonNull(getCommand("questreload")).setExecutor(new ReloadCommand(this));

        getServer().getPluginManager().registerEvents(new QuestsMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new SeeQuestsMenuListener(this),this);
        getServer().getPluginManager().registerEvents(new QuestDetailMenuListener(this),this);
        getServer().getPluginManager().registerEvents(new QuestProgressListener(this),this);

        //setup files and folders
        fileManager.setup();

        //load quests,player data and stats
        quests=questLoader.loadAllQuests();
        playersData=questLoader.loadPlayersData();
        playersStats=questLoader.loadPlayersStats();

        logger.info("UniQuests loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public PlayerData getOrCreatePlayerData(Player player) {
        String uuid = player.getUniqueId().toString();
        return playersData.computeIfAbsent(uuid, id -> new PlayerData());
    }
}
