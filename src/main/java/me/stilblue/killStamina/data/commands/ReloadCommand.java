package me.stilblue.killStamina.data.commands;

import me.stilblue.killStamina.data.config.AbstractConfig;
import me.stilblue.killStamina.data.config.GlobalConfig;
import me.stilblue.killStamina.data.config.MessageConfig;
import me.stilblue.killStamina.data.config.StaminaConfig;
import me.stilblue.killStamina.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0 && strings[0].equalsIgnoreCase("reload")) {
            GlobalConfig.getInstance().init();
            MessageConfig.getInstance().init();
            StaminaConfig.getInstance().init();
            commandSender.sendMessage(Utils.hex(GlobalConfig.getInstance().getPrefix()) + "Reloaded KillStamina");

            return true;
        }

        return false;
    }
}
