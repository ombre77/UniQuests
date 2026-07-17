package net.ody.uniQuests;

import net.ody.uniQuests.commands.QuestsCommand;
import net.ody.uniQuests.listeners.QuestDetailMenuListener;
import net.ody.uniQuests.listeners.QuestsMenuListener;
import net.ody.uniQuests.listeners.SeeQuestsMenuListener;
import net.ody.uniQuests.modules.Quest;
import net.ody.uniQuests.modules.QuestLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public final class UniQuests extends JavaPlugin {

    public Logger logger = getLogger();
    public UniQuestsFileManager fileManager=new UniQuestsFileManager(this);
    public String site="https://github.com/ombre77/UniQuests";
    private final QuestLoader questLoader= new QuestLoader(fileManager,logger);

    public Map<String, Quest> quests;

    @Override
    public void onEnable() {
        //register commands and listeners
        Objects.requireNonNull(getCommand("quests")).setExecutor(new QuestsCommand(this));
        getServer().getPluginManager().registerEvents(new QuestsMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new SeeQuestsMenuListener(this),this);
        getServer().getPluginManager().registerEvents(new QuestDetailMenuListener(this),this);

        //setup files and folders
        fileManager.setup();

        //load quests,player data and stats
        quests=questLoader.loadAllQuests();
        questLoader.loadPlayersData();
        questLoader.loadPlayersStats();

        logger.info("UniQuests loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
