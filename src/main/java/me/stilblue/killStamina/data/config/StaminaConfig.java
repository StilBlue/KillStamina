package me.stilblue.killStamina.data.config;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.stilblue.killStamina.data.StaminaCost;
import me.stilblue.killStamina.data.StaminaCostManager;
import org.bukkit.configuration.ConfigurationSection;

public class StaminaConfig extends AbstractConfig {

    private static StaminaConfig instance;
    private StaminaCostManager staminaCostManager;

    @Override
    public void save() {
        super.save();
    }

    public void init(StaminaCostManager staminaCostManager) {
        this.staminaCostManager = staminaCostManager;
        init();
    }

    public void init() {
        super.init("", "stamina.yml");

        if (getConfig().getConfigurationSection("MythicMobs") == null) {
            getConfig().set("MythicMobs.MobA", 100);
            getConfig().set("MythicMobs.MobB", 5);
            getConfig().set("MythicMobs.MobC", 23);
        }

        save();
        reload();
    }
    @Override
    public void reload() {
        loadConfig();

        staminaCostManager.clearAll();

        ConfigurationSection section = getConfig().getConfigurationSection("MythicMobs");
        if (section == null)
            return;

        for (String internalMobID : section.getKeys(false)) {
            MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(internalMobID).orElse(null);
            if (mob == null) {
                warn("MythicMob with id " + internalMobID + " is not found, thus ignored!");
                continue;
            }

            StaminaCost cost = new StaminaCost(internalMobID, getConfig().getInt("MythicMobs." + internalMobID));
            staminaCostManager.addStaminaCost(cost);
        }

        log("stamina.yml reloaded");
    }

    public static StaminaConfig getInstance() {
        if (instance == null)
            instance = new StaminaConfig();
        return instance;
    }


}