package me.stilblue.killStamina.schedulers;

import me.stilblue.killStamina.data.PlayerData;
import me.stilblue.killStamina.data.PlayerDataAccess;
import me.stilblue.killStamina.data.PlayerDataManager;
import me.stilblue.killStamina.data.config.GlobalConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class AutoSaveTask {
    private static AutoSaveTask instance;
    private final Plugin plugin;
    private final PlayerDataManager playerDataManager;
    private final PlayerDataAccess playerDataAccess;
    private BukkitTask task;
    public void reload() {
        stopTimer();
        startTimer();
    }

    public AutoSaveTask(Plugin plugin, PlayerDataManager playerDataManager, PlayerDataAccess playerDataAccess) {
        instance = this;
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
        this.playerDataAccess = playerDataAccess;
    }

    public void startTimer() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(
                plugin,
                () -> {
                    List<PlayerData> listData = playerDataManager.getAllLoadedData();
                    listData.forEach(playerDataAccess::update);
                },
                GlobalConfig.getInstance().getAutoSaveTime(),
                GlobalConfig.getInstance().getAutoSaveTime()
        );
    }

    public void stopTimer() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public static AutoSaveTask getInstance() {
        return instance;
    }
}
