package me.stilblue.killStamina.data.config;

import org.bukkit.Bukkit;

import java.util.HashMap;

public class MessageConfig extends AbstractConfig {

    public static MessageConfig instance;

    private final HashMap<String, String> map = new HashMap<>();


    public static MessageConfig getInstance() {

        if (instance == null)
            instance = new MessageConfig();

        return instance;
    }

    public void init() {
        super.init("", "messages.yml");

        for (Message lang : Message.values()) {
            if (getConfig().get(lang.name().toLowerCase()) == null)
                getConfig().set(lang.name().toLowerCase(), lang.getMessage());
        }

        save();
        reload();
    }

    @Override
    public void save() {
        super.save();
    }

    @Override
    public void reload() {

        loadConfig();

        map.clear();

        for (Message lang : Message.values()) {
            if (getConfig().get(lang.name().toLowerCase()) != null)
                map.put(lang.name().toLowerCase(), getConfig().getString(lang.name().toLowerCase()));

            lang.reload();
        }

        Bukkit.getLogger().info(getLogName() + "Message file reloaded.");
    }

    public HashMap<String, String> getMap() {
        return map;
    }
}