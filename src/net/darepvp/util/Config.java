/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.darepvp.util;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    private FileConfiguration Config;
    private File File;
    private String Name;

    public Config(JavaPlugin Plugin2, String Path2, String Name) {
        this.File = new File(Plugin2.getDataFolder() + Path2);
        this.File.mkdirs();
        this.File = new File(Plugin2.getDataFolder() + Path2, String.valueOf(Name) + ".yml");
        try {
            this.File.createNewFile();
        }
        catch (IOException var4_4) {
            // empty catch block
        }
        this.Name = Name;
        this.Config = YamlConfiguration.loadConfiguration((File)this.File);
    }

    public String getName() {
        return this.Name;
    }

    public FileConfiguration getConfig() {
        return this.Config;
    }

    public void setDefault(String Path2, Object Set2) {
        if (!this.getConfig().contains(Path2)) {
            this.Config.set(Path2, Set2);
            this.save();
        }
    }

    public void save() {
        try {
            this.Config.save(this.File);
        }
        catch (IOException var1_1) {
            // empty catch block
        }
    }

    public void reload() {
        this.Config = YamlConfiguration.loadConfiguration((File)this.File);
    }
}

