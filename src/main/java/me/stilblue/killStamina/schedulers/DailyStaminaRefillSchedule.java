package me.stilblue.killStamina.schedulers;

import me.stilblue.killStamina.KillStamina;
import me.stilblue.killStamina.data.PlayerData;
import me.stilblue.killStamina.data.PlayerDataManager;
import me.stilblue.killStamina.data.config.AbstractConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DailyStaminaRefillSchedule extends DailySchedule {


    public DailyStaminaRefillSchedule(String id, String time) {
        super(id, time);
    }

    @Override
    public void run() {
        long timeStamp = Instant.EPOCH.until(getNextExecution(), ChronoUnit.MILLIS);
        PlayerDataManager.setDailyTimeStamp(timeStamp);
        KillStamina.getInstance().getPlayerDataManager().updateTimeStamp();
        Bukkit.getLogger().info(AbstractConfig.getLogName() + "Refilled stamina for everyone!");
    }
//
//    public Instant getLastExecution() {
//        return getLastExecution(Instant.now());
//    }
//
//    public Instant getLastExecution(final Instant startTime) {
//        final OffsetDateTime now = OffsetDateTime.ofInstant(startTime, ZoneId.systemDefault());
//        OffsetDateTime targetTime = getTimeToRun().minusHours(24).atOffset(now.getOffset()).atDate(now.toLocalDate());
//        if (targetTime.isBefore(now)) {
//            targetTime = targetTime.plusDays(1);
//        }
//        return targetTime.toInstant();
//    }
}
