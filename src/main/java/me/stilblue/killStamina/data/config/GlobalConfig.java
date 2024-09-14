package me.stilblue.killStamina.data.config;

import me.stilblue.killStamina.KillStamina;
import me.stilblue.killStamina.data.PlayerDataManager;
import me.stilblue.killStamina.schedulers.DailyScheduler;
import me.stilblue.killStamina.schedulers.DailyStaminaRefillSchedule;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class GlobalConfig extends AbstractConfig {

    private static GlobalConfig instance;
    private static final DailyScheduler dailyScheduler = new DailyScheduler();

    private String prefix;
    private long autoSaveTime;
    private String refillTime;
    private int maxStamina;

    @Override
    public void save() {
        super.save();
    }

    public void init() {
        super.init("", "config.yml");

        if (getConfig().get("Prefix") == null)
            getConfig().set("Prefix", "&f[&cKill&6Stamina&f] ");
        if (getConfig().get("AutoSaveTime") == null)
            getConfig().set("AutoSaveTime", 1200);
        if (getConfig().get("RefillTime") == null)
            getConfig().set("RefillTime", "00:00");
        if (getConfig().get("MaxStamina") == null)
            getConfig().set("MaxStamina", 10000);

        save();
        reload();
    }
    @Override
    public void reload() {
        loadConfig();

        prefix = getConfig().getString("Prefix");
        autoSaveTime = getConfig().getLong("AutoSaveTime");
        refillTime = getConfig().getString("RefillTime");
        maxStamina = getConfig().getInt("MaxStamina");

        if (refillTime != null) {
            dailyScheduler.stop();
            log("Scheduled DailyStaminaRefill at " + refillTime);
            DailyStaminaRefillSchedule schedule = new DailyStaminaRefillSchedule("DailyStaminaRefill", refillTime);
            dailyScheduler.addSchedule(schedule);
            PlayerDataManager.setDailyTimeStamp(Instant.EPOCH.until(schedule.getNextExecution(), ChronoUnit.MILLIS));
            dailyScheduler.start();
        }

        log("config.yml reloaded");
    }

    public static GlobalConfig getInstance() {
        if (instance == null)
            instance = new GlobalConfig();
        return instance;
    }

    public String getPrefix() {
        return prefix;
    }

    public long getAutoSaveTime() {
        return autoSaveTime;
    }

    public String getRefillTime() {
        return refillTime;
    }

    public int getMaxStamina() {
        return maxStamina;
    }
}