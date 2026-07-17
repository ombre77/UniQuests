package net.ody.uniQuests.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.ody.uniQuests.UniQuestsFileManager;
import net.ody.uniQuests.utils.DateUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class QuestLoader {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final UniQuestsFileManager fileManager;
    private final Logger logger;

    public QuestLoader(UniQuestsFileManager fileManager, Logger logger) {
        this.fileManager = fileManager;
        this.logger = logger;
    }

    public Map<String,Quest> loadAllQuests(){
        Map<String, Quest> allQuests = new LinkedHashMap<>();
        Type mapType = new TypeToken<Map<String, Quest>>() {}.getType();

        Map<String, File> typeFolders = new LinkedHashMap<>();
        typeFolders.put("global", fileManager.getGlobalQuests());
        typeFolders.put("daily", fileManager.getDailyQuests());
        typeFolders.put("weekly", fileManager.getWeeklyQuests());
        typeFolders.put("monthly", fileManager.getMonthlyQuests());

        for (Map.Entry<String, File> entry : typeFolders.entrySet()) {
            String type = entry.getKey();
            File folder = entry.getValue();

            File[] files = loadQuestFolder(folder);
            if (files == null) {
                continue;
            }

            int loaded = 0;
            for (File file : files){
                try (FileReader reader=new FileReader(file)){
                    Map<String,Quest> fileQuests=gson.fromJson(reader,mapType);
                    if (fileQuests !=null){
                        for (Map.Entry<String,Quest> questEntry : fileQuests.entrySet()) {
                            Quest quest = questEntry.getValue();
                            quest.type = type;
                            int expireDelay = getExpireDelay(quest);
                            quest.expire= DateUtils.addDays(quest.created,expireDelay);
                            quest.id=questEntry.getKey();
                        }
                        allQuests.putAll(fileQuests);
                        loaded += fileQuests.size();
                    }
                } catch (IOException e) {
                    logger.warning("Failed to read quest file " + file.getName() + ": " + e.getMessage());
                } catch (com.google.gson.JsonSyntaxException e) {
                    logger.warning("Malformed JSON in quest file " + file.getName() + ": " + e.getMessage());
                }
            }
            logger.info("Loaded " + loaded + " " + type + " quest(s) from " + folder.getPath() + ".");
        }

        logger.info("Loaded " + allQuests.size() + " quest(s) total.");
        return allQuests;
    }

    private static int getExpireDelay(Quest quest) {
        int expireDelay;
        switch (quest.type){
            case "daily"->{
                expireDelay=1;
            }
            case "weekly"->{
                expireDelay=7;
            }
            case "monthly"->{
                expireDelay=30;
            }
            default -> {expireDelay=0;}
        }
        return expireDelay;
    }

    private File[] loadQuestFolder(File folder){
        File[] files=folder.listFiles((dir,name)->name.endsWith(".json"));
        if (files==null) {
            logger.warning("Could not load quests in folder "+folder.getPath());
            return null;
        }
        return files;
    }

    public Map<String, PlayerData> loadPlayersData() {
        Type mapType = new TypeToken<Map<String, PlayerData>>() {}.getType();
        return loadJsonFile(fileManager.getPlayersData(), mapType, "playersData.json");
    }

    public Map<String, PlayerStats> loadPlayersStats() {
        Type mapType = new TypeToken<Map<String, PlayerStats>>() {}.getType();
        return loadJsonFile(fileManager.getPlayersStats(), mapType, "playersStats.json");
    }

    private <T> Map<String, T> loadJsonFile(java.io.File file, Type mapType, String labelForLogging) {
        try {
            String content = Files.readString(file.toPath()).trim();
            if (content.isEmpty()) {
                return new HashMap<>();
            }
            Map<String, T> result = gson.fromJson(content, mapType);
            return result != null ? result : new HashMap<>();
        } catch (IOException e) {
            logger.warning("Failed to read " + labelForLogging + ": " + e.getMessage());
            return new HashMap<>();
        } catch (com.google.gson.JsonSyntaxException e) {
            logger.warning("Malformed JSON in " + labelForLogging + ": " + e.getMessage());
            return new HashMap<>();
        }
    }

    public void savePlayersData(Map<String, PlayerData> data) {
        saveJsonFile(fileManager.getPlayersData(), data, "playersData.json");
    }

    public void savePlayersStats(Map<String, PlayerStats> stats) {
        saveJsonFile(fileManager.getPlayersStats(), stats, "playersStats.json");
    }

    private void saveJsonFile(java.io.File file, Object data, String labelForLogging) {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            logger.warning("Failed to write " + labelForLogging + ": " + e.getMessage());
        }
    }
}
