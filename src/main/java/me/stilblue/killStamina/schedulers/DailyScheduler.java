package me.stilblue.killStamina.schedulers;

import me.stilblue.killStamina.data.config.AbstractConfig;
import org.bukkit.Bukkit;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DailyScheduler {
    public static final int TERMINATION_TIMEOUT_MS = 5;
    private final Map<String, DailySchedule> schedules = new HashMap<>();
    private ScheduledExecutorService executor;
    private boolean isRunning;

    public void addSchedule(DailySchedule dailySchedule) {
        schedules.put(dailySchedule.getId(), dailySchedule);
    }

    public void schedule(DailySchedule dailySchedule) {
        executor.schedule(() -> {
            Bukkit.getLogger().info(AbstractConfig.getLogName() + "Running task for schedule " + dailySchedule.getId());
            dailySchedule.run();
            schedule(dailySchedule);
        }, Instant.now().until(dailySchedule.getNextExecution(), ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);
    }

    public void start() {
        isRunning = true;
        executor = Executors.newSingleThreadScheduledExecutor();
        schedules.values().forEach(this::schedule);
    }

    public void stop() {
        if (isRunning()) {
            Bukkit.getLogger().info(AbstractConfig.getLogName() + "Stopping " + getClass().getSimpleName().toLowerCase(Locale.ROOT).replace("scheduler", "")
                    + " scheduler.");
            executor.shutdownNow();
            try {
                terminateExecutor();
                Bukkit.getLogger().info(AbstractConfig.getLogName() + "Successfully shut down executor service.");
            } catch (final InterruptedException | TimeoutException e) {
                Bukkit.getLogger().warning(AbstractConfig.getLogName() + "Error while stopping scheduler");
                e.printStackTrace();
            }
            isRunning = false;
            schedules.clear();
            Bukkit.getLogger().info(AbstractConfig.getLogName() + "Stop complete.");
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    private void terminateExecutor() throws InterruptedException, TimeoutException {
        if (!executor.awaitTermination(TERMINATION_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
            throw new TimeoutException("Not all schedules could be terminated within time constraints");
        }
    }
}
