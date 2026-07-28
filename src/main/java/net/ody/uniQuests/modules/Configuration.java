package net.ody.uniQuests.modules;

import net.ody.uniQuests.UniQuests;
import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {

    public boolean disabling;
    public boolean deleting;
    public int deletingDelay;
    public boolean trashBin;
    public int trashDelay;

    public Configuration(UniQuests plugin){
        FileConfiguration configFile=plugin.getConfig();

        disabling=configFile.getBoolean("enable-disabling");
        deleting=configFile.getBoolean("enable-deleting");
        deletingDelay= configFile.getInt("deleting-delay");
        trashBin= configFile.getBoolean("trash-bin");
        trashDelay=configFile.getInt("trash-delay");
    }
}
