package me.stilblue.killStamina.listeners;

import me.stilblue.killStamina.data.PlayerData;
import me.stilblue.killStamina.data.PlayerDataAccess;
import me.stilblue.killStamina.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlayerDataListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final PlayerDataAccess playerDataAccess;
    private final Executor ioExecutor = Executors.newSingleThreadExecutor();

    public PlayerDataListener(PlayerDataManager playerDataManager, PlayerDataAccess playerDataAccess) {
        this.playerDataManager = playerDataManager;
        this.playerDataAccess = playerDataAccess;
    }

    @EventHandler
    public void onJoin (AsyncPlayerPreLoginEvent event) {
        UUID playerId = event.getUniqueId();
        PlayerData data = playerDataAccess.read(playerId);
        if (data == null) {
            data = new PlayerData(playerId);
            playerDataAccess.update(data);
        }
        playerDataManager.addPlayerData(data);

        if (data.getRefreshTime() != PlayerDataManager.getDailyTimeStamp()) {
            data.setRefreshTime(PlayerDataManager.getDailyTimeStamp());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID playerID = event.getPlayer().getUniqueId();
        PlayerData data = playerDataManager.removePlayerData(playerID);
        ioExecutor.execute(() -> playerDataAccess.update(data));
    }
}
