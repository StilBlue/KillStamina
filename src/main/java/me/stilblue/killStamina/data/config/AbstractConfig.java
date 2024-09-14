package me.stilblue.killStamina.data.config;

import me.stilblue.killStamina.KillStamina;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class AbstractConfig {

    private static final String path = KillStamina.getInstance().getDataFolder().getAbsolutePath();
    private static final String logName = "[SenzuBean] ";

    private String folderName;

    private String fileName;

    private FileConfiguration config;


    public void init(String folderName, String fileName) {
        this.fileName = fileName;
        this.folderName = folderName;

        File folder = new File(path + folderName);
        if (!folder.exists())
            folder.mkdirs();

        File file = new File(getFullPath());

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loadConfig();
    }

    public String getFullPath()
    {
        return (path + folderName + "/" + fileName).replace("//", "/");
    }

    public void save() {
        File file = new File(getFullPath());
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean delete()
    {
        File file = new File(getFullPath());
        return file.delete();
    }

    public void loadConfig() {
        File file = new File(getFullPath());
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void log(String log) {
        Bukkit.getLogger().info(logName + log);
    }
    public void warn(String log) {
        Bukkit.getLogger().warning(logName + log);
    }

    public abstract void reload();

    public static String getPath() {
        return path;
    }
    public static String getLogName() {return logName;}
    public String getFolderName() {
        return folderName;
    }
    public void setFolderName(String s) {
        folderName = s;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String s) {
        fileName = s;
    }
    public FileConfiguration getConfig() {
        return config;
    }

}