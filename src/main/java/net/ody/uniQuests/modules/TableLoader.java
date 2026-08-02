package net.ody.uniQuests.modules;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.ody.uniQuests.UniQuests;
import net.ody.uniQuests.UniQuestsFileManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TableLoader {
    private static final Set<String> VALID_TABLE_TYPES = Set.of("reward", "price", "requirement");
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Table.class,new TableDeserializer())
            .create();
    private final UniQuestsFileManager fileManager;
    private final Logger logger;
    private final UniQuests plugin;

    public TableLoader(UniQuests plugin){
        this.fileManager = plugin.fileManager;
        this.logger = plugin.logger;
        this.plugin=plugin;
    }

    public List<Table> loadAllTables(){
        List<Table> allTables = new ArrayList<>();
        File folder = fileManager.getTables();

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) {
            logger.warning("Could not load tables in folder " + folder.getPath());
            return allTables;
        }

        int loaded = 0;
        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                Table table = gson.fromJson(reader, Table.class);
                if (table == null || table.table_type == null) {
                    logger.warning("Table file " + file.getName() + " is missing \"table_type\"; skipping.");
                    continue;
                }
                if (!VALID_TABLE_TYPES.contains(table.table_type)) {
                    logger.warning("Table file " + file.getName() + " has unknown table_type \"" + table.table_type + "\"; skipping.");
                    continue;
                }
                if (table.id == null) {
                    table.id = file.getName().replaceFirst("\\.json$", "");
                }
                if (table.entries == null) {
                    table.entries = new ArrayList<>();
                }

                allTables.add(table);
                loaded++;
            } catch (IOException e) {
                logger.warning("Failed to read table file " + file.getName() + ": " + e.getMessage());
            } catch (JsonSyntaxException | IllegalStateException e) {
                logger.warning("Malformed JSON in table file " + file.getName() + ": " + e.getMessage());
            }
        }

        logger.info("Loaded " + loaded + " table(s) from " + folder.getPath() + ".");
        return allTables;
    }

    private static class TableDeserializer implements JsonDeserializer<Table> {
        @Override
        public Table deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject root = json.getAsJsonObject();

            Table table = new Table();
            table.id = root.has("id") && !root.get("id").isJsonNull() ? root.get("id").getAsString() : null;
            table.table_type = root.has("table_type") && !root.get("table_type").isJsonNull() ? root.get("table_type").getAsString() : null;

            Class<? extends TableEntry> entryClass = switch (table.table_type == null ? "" : table.table_type) {
                case "reward" -> RewardEntry.class;
                case "price" -> Price.class;
                case "requirement" -> Requirement.class;
                default -> null;
            };

            if (entryClass != null && root.has("entries")) {
                Type listType = TypeToken.getParameterized(List.class, entryClass).getType();
                table.entries = context.deserialize(root.get("entries"), listType);
            } else {
                table.entries = new ArrayList<>();
            }

            return table;
        }
    }

    public Table getTableById(String id){
        List<Table> filtered=plugin.tables.stream()
                .filter(table -> table.id.equals(id))
                .toList();

        if (filtered.isEmpty()){
            return null;
        }
        if (filtered.size()==1){
            return filtered.getFirst();
        }
        return filtered.getFirst();
    }
}
