package me.stilblue.killStamina.data;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private static long dailyTimeStamp;
    private final Map<UUID, PlayerData> livePlayerData = new ConcurrentHashMap<>();

    public PlayerDataManager() {
        dailyTimeStamp = System.currentTimeMillis();
    }

    public void addPlayerData(PlayerData data) {
        livePlayerData.put(data.getOwnerID(), data);
    }

    public PlayerData getPlayerData(UUID uuid) {
        return livePlayerData.get(uuid);
    }

    public PlayerData removePlayerData(UUID uuid) {
        return livePlayerData.remove(uuid);
    }
    public List<PlayerData> getAllLoadedData() {
        return List.copyOf(livePlayerData.values());
    }

    public void updateTimeStamp() {
        getAllLoadedData().forEach(playerData -> playerData.setRefreshTime(dailyTimeStamp));
    }

    public static void setDailyTimeStamp(long dailyTimeStamp) {
        PlayerDataManager.dailyTimeStamp = dailyTimeStamp;
    }

    public static long getDailyTimeStamp() {
        return dailyTimeStamp;
    }
}
