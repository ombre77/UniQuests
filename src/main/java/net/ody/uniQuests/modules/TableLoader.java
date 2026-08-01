package net.ody.uniQuests.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.UniQuestsFileManager;

import java.util.List;
import java.util.logging.Logger;

public class TableLoader {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final UniQuestsFileManager fileManager;
    private final Logger logger;

    public TableLoader(UniQuests plugin){
        this.fileManager = plugin.fileManager;
        this.logger = plugin.logger;
    }

    public List<Table> loadAllTables(){

    }
}
