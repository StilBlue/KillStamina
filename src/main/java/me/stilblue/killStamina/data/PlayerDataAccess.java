package me.stilblue.killStamina.data;

import me.stilblue.killStamina.utils.GsonUtils;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class PlayerDataAccess {
    private Plugin plugin;

    public PlayerDataAccess(Plugin plugin) {
        this.plugin = plugin;
    }

    public void update(PlayerData playerData) {
        GsonUtils.write(plugin, playerData.getOwnerID().toString(), playerData);
    }

    public PlayerData read(UUID uuid) {
        return GsonUtils.read(plugin, uuid.toString(), PlayerData.class);
    }

    public void delete(UUID uuid) {
        GsonUtils.delete(plugin, uuid.toString());
    }

}
