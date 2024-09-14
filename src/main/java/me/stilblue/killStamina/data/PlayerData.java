package me.stilblue.killStamina.data;

import me.stilblue.killStamina.data.config.FormatArg;
import me.stilblue.killStamina.data.config.GlobalConfig;
import me.stilblue.killStamina.data.config.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final UUID ownerID;
    private int stamina;
    private long refreshTime;

    public PlayerData(UUID ownerID) {
        this.ownerID = ownerID;
    }

    public UUID getOwnerID() {
        return ownerID;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
        stamina = GlobalConfig.getInstance().getMaxStamina();
        Player player = Bukkit.getPlayer(ownerID);
        if (player != null)
            Message.STAMINA_REFRESH.sendMessageFormated(player, new FormatArg("%amount%", Integer.toString(stamina)));
    }
}
