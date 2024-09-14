package me.stilblue.killStamina.papi;


import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.stilblue.killStamina.KillStamina;
import me.stilblue.killStamina.data.PlayerData;
import me.stilblue.killStamina.data.PlayerDataManager;
import me.stilblue.killStamina.data.StaminaCost;
import me.stilblue.killStamina.data.StaminaCostManager;
import me.stilblue.killStamina.data.config.AbstractConfig;
import me.stilblue.killStamina.data.config.GlobalConfig;
import me.stilblue.killStamina.data.config.StaminaConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaminaExpansion extends PlaceholderExpansion {
    private final KillStamina plugin;

    public StaminaExpansion(KillStamina plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "killstamina";
    }

    @Override
    public @NotNull String getAuthor() {
        return "StilBlue";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        Pattern mobPattern = Pattern.compile("\\^mob_");
        Matcher mobMatcher = mobPattern.matcher(params);

        if (mobMatcher.find()) {
            String internalMobID = params.substring(params.indexOf("_") + 1);

            StaminaCost cost = plugin.getStaminaManager().getStaminaCost(internalMobID);
            if (cost == null) {
                Bukkit.getLogger().warning(AbstractConfig.getLogName() + internalMobID + " does not appear in stamina.yml");
                return null;
            }

            return Integer.toString(cost.getCost());
        }
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (data == null)
            return null;

        return switch (params.toLowerCase()) {
            case "current" -> Integer.toString(data.getStamina());
            case "max" -> Integer.toString(GlobalConfig.getInstance().getMaxStamina());
            case "time" -> Long.toString(data.getRefreshTime());
            default -> null;
        };
    }
}
