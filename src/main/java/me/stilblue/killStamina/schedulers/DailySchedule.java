package me.stilblue.killStamina.schedulers;

import org.bukkit.Bukkit;

import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class DailySchedule {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private final String id;
    private LocalTime timeToRun;

    public DailySchedule(String id, String time) {
        this.id = id;
        try {
            this.timeToRun = LocalTime.parse(time, TIME_FORMAT);
        } catch (final DateTimeParseException e) {
            Bukkit.getLogger().warning("Unable to parse time '" + time + "': " + e.getMessage());
            e.printStackTrace();
        }
    }

    public abstract void run();

    public String getId() {
        return id;
    }
    public LocalTime getTimeToRun() {
        return timeToRun;
    }

    public Instant getNextExecution(final Instant startTime) {
        final OffsetDateTime now = OffsetDateTime.ofInstant(startTime, ZoneId.systemDefault());
        OffsetDateTime targetTime = getTimeToRun().atOffset(now.getOffset()).atDate(now.toLocalDate());
        if (targetTime.isBefore(now)) {
            targetTime = targetTime.plusDays(1);
        }
        return targetTime.toInstant();
    }
    public Instant getNextExecution() {
        return getNextExecution(Instant.now());
    }
}
