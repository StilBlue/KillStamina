package me.stilblue.killStamina;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.stilblue.killStamina.data.PlayerData;
import me.stilblue.killStamina.data.PlayerDataAccess;
import me.stilblue.killStamina.data.PlayerDataManager;
import me.stilblue.killStamina.data.StaminaCostManager;
import me.stilblue.killStamina.data.commands.ReloadCommand;
import me.stilblue.killStamina.data.config.AbstractConfig;
import me.stilblue.killStamina.data.config.GlobalConfig;
import me.stilblue.killStamina.data.config.MessageConfig;
import me.stilblue.killStamina.data.config.StaminaConfig;
import me.stilblue.killStamina.listeners.PlayerDataListener;
import me.stilblue.killStamina.listeners.PlayerListener;
import me.stilblue.killStamina.papi.StaminaExpansion;
import me.stilblue.killStamina.schedulers.AutoSaveTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class KillStamina extends JavaPlugin {
    private static KillStamina instance;
    private PlaceholderExpansion placeholderAPI;

    private PlayerDataManager playerDataManager;
    private PlayerDataAccess playerDataAccess;
    private StaminaCostManager staminaCostManager;

    @Override
    public void onEnable() {
        // Initialize variables
        instance = this;
        staminaCostManager = new StaminaCostManager();
        playerDataManager = new PlayerDataManager();
        playerDataAccess = new PlayerDataAccess(this);

        // Setup Config
        GlobalConfig.getInstance().init();
        StaminaConfig.getInstance().init(staminaCostManager);
        MessageConfig.getInstance().init();

        // Setup Async PlayerData System
        PlayerDataListener dataListener = new PlayerDataListener(playerDataManager, playerDataAccess);
        Bukkit.getPluginManager().registerEvents(dataListener, this);

        // Setup player hit and kill listener
        PlayerListener playerListener = new PlayerListener(staminaCostManager, playerDataManager);
        Bukkit.getPluginManager().registerEvents(playerListener, this);

        // Setup PlaceholderAPI
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getLogger().info(AbstractConfig.getLogName() + "PlaceholderAPI Found!");
            placeholderAPI = new StaminaExpansion(this);
            placeholderAPI.register();
        }

        // Schedule Async AutoSave task
        AutoSaveTask autoSaveTask = new AutoSaveTask(this, playerDataManager, playerDataAccess);
        autoSaveTask.reload();

        // Register Commands
        getCommand("killstamina").setExecutor(new ReloadCommand());
    }

    @Override
    public void onDisable() {
        List<PlayerData> dataList = playerDataManager.getAllLoadedData();
        dataList.forEach(playerDataAccess::update);
    }

    public static KillStamina getInstance() {
        return instance;
    }

    public PlaceholderExpansion getPlaceholderAPI() {
        return placeholderAPI;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public PlayerDataAccess getPlayerDataAccess() {
        return playerDataAccess;
    }

    public StaminaCostManager getStaminaManager() {
        return staminaCostManager;
    }
}
