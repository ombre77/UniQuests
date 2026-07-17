package net.ody.uniQuests;

import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class UniQuestsFileManager {
    private final UniQuests plugin;
    private final Logger logger;
    private final File dataFolder;

    private File questsFolder;
    private File globalQuests;
    private File dailyQuests;
    private File weeklyQuests;
    private File monthlyQuests;

    private File playersData;
    private File playersStats;

    public UniQuestsFileManager(@NonNull UniQuests plugin){
        this.plugin=plugin;
        logger=plugin.getLogger();
        dataFolder=plugin.getDataFolder();
    }

    private File assureFile(String name){
        File file = new File(dataFolder, name);
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (created) {
                    plugin.getLogger().info("Created "+name+" file: " + file.getPath());
                } else {
                    plugin.getLogger().warning("Failed to create "+name+" file, report issue at " + plugin.site);
                }
            } catch (IOException e) {
                plugin.getLogger().warning("Error creating "+name+" file: " + e.getMessage());
            }
        }
        return file;
    }

    private File assureFolder(String name,File parent){
        File folder = new File(parent, name);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                plugin.getLogger().info("Created "+name+" folder: " + folder.getPath());
            } else {
                plugin.getLogger().warning("Failed to create "+name+" folder, report issue at " + plugin.site);
            }
        }
        return folder;
    }

    public void setup(){
        //Folders
        File dataFolder=plugin.getDataFolder();
        if (!dataFolder.exists()) {
            boolean created=dataFolder.mkdirs();
            if (created) {
                logger.info("Created plugin data folder:"+dataFolder.getPath());
            } else {
                logger.warning("Failed to create plugin data folder, report issue at "+plugin.site);
            }
        }

        questsFolder = assureFolder("quests",dataFolder);
        globalQuests=assureFolder("globals",questsFolder);
        dailyQuests=assureFolder("daily",questsFolder);
        weeklyQuests=assureFolder("weekly",questsFolder);
        monthlyQuests=assureFolder("monthly",questsFolder);


        //Files
        playersData=assureFile("playersData.json");
        playersStats=assureFile("playersStats.json");

    }

    private void checkForNull(File folder){
        if (folder==null){
            throw new IllegalStateException("UniQuestsFileManager.setup() must be called before any getter method.");
        }
    }

    public File getQuestsFolder() {
        if (questsFolder==null){
            throw new IllegalStateException("UniQuestsFileManager.setup() must be called before qetQuestsFolder(). Missing quests folder.");
        }
        return questsFolder;
    }

    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    public File getPlayersData(){
        if (questsFolder==null){
            throw new IllegalStateException("UniQuestsFileManager.setup() must be called before qetPlayersData(). Missing playerData file.");
        }
        return playersData;
    }

    public File getPlayersStats(){
        if (questsFolder==null){
            throw new IllegalStateException("UniQuestsFileManager.setup() must be called before qetPlayersStats(). Missing playerStats file.");
        }
        return playersStats;
    }

    public File getGlobalQuests() {
        checkForNull(globalQuests);
        return globalQuests;
    }

    public File getDailyQuests() {
        checkForNull(dailyQuests);
        return dailyQuests;
    }

    public File getWeeklyQuests() {
        checkForNull(weeklyQuests);
        return weeklyQuests;
    }

    public File getMonthlyQuests() {
        checkForNull(monthlyQuests);
        return monthlyQuests;
    }
}
