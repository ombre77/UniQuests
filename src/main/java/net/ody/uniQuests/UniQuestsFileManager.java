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
    private File trashbin;

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
        trashbin=assureFolder("TrashBin",dataFolder);


        //Files
        playersData=assureFile("playersData.json");
        playersStats=assureFile("playersStats.json");

        //config
        plugin.saveDefaultConfig();

    }

    private void checkForNull(File folder){
        if (folder==null){
            throw new IllegalStateException("UniQuestsFileManager.setup() must be called before any getter method.");
        }
    }

    public File getQuestsFolder() {
        if (questsFolder==null){
            throw new IllegalStateException("UniQuestsFileManager.setup() must be called before getQuestsFolder(). Missing quests folder.");
        }
        return questsFolder;
    }

    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    public File getPlayersData(){
        if (questsFolder==null){
            throw new IllegalStateException("UniQuestsFileManager.setup() must be called before getPlayersData(). Missing playerData file.");
        }
        return playersData;
    }

    public File getPlayersStats(){
        if (questsFolder==null){
            throw new IllegalStateException("UniQuestsFileManager.setup() must be called before getPlayersStats(). Missing playerStats file.");
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

    public File getTrashbin() {
        return trashbin;
    }

    public File getTrash(String name){
        return new File(trashbin,name);
    }

    public File getQuestFile(String name, String type) {
        File folder = switch (type) {
            case "global" -> getGlobalQuests();
            case "daily" -> getDailyQuests();
            case "weekly" -> getWeeklyQuests();
            case "monthly" -> getMonthlyQuests();
            default -> throw new IllegalArgumentException("Unknown quest type: " + type);
        };
        String fileName = name.endsWith(".json") ? name : name + ".json";
        return new File(folder, fileName);
    }
}
