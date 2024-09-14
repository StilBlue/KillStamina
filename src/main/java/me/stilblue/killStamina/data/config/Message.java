package me.stilblue.killStamina.data.config;

import me.stilblue.killStamina.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public enum Message {
    // Player message
    NOT_ENOUGH_STAMINA("You don't have enough stamina to hit this mob, you need &c%current%&7/%need%"),

    // Scheduler Message
    STAMINA_REFRESH("Stamina has been refilled to full &6%amount%"),

    // Placeholder
    TIME_UNTIL("%days% %hours% %minutes% %seconds%"),

    // Universal
    DAYS("days"),
    HOURS("hours"),
    MINUTES("minutes"),
    SECONDS("seconds");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public void reload() {
        if (MessageConfig.getInstance().getMap().containsKey(this.name().toLowerCase())) {
            this.message = MessageConfig.getInstance().getMap().get(this.name().toLowerCase());
        }
    }

    public String getMessage()
    {
        String m = Utils.hex(message);

        m = Utils.applyPlaceholders(null, m);
        return m;
    }

    public String getMessagePAPI(Player p)
    {
        String m = Utils.hex(message);

        m = Utils.applyPlaceholders(p.getUniqueId(), m);
        return m;
    }

    public void sendMessage(Player p) {
        if(message.isEmpty())
            return;
        p.sendMessage(Utils.hex(GlobalConfig.getInstance().getPrefix() + getMessagePAPI(p).replace("\n", "\n" + GlobalConfig.getInstance().getPrefix())));
    }

    public void sendMessage(CommandSender sender) {
        if(message.isEmpty())
            return;
        sender.sendMessage(Utils.hex(GlobalConfig.getInstance().getPrefix() + getMessage().replace("\n", "\n" + GlobalConfig.getInstance().getPrefix())));
    }

    public void broadcast() {
        if(message.isEmpty())
            return;
        Bukkit.broadcastMessage(Utils.hex(GlobalConfig.getInstance().getPrefix() + getMessage().replace("\n", "\n" + GlobalConfig.getInstance().getPrefix())));
    }

    public void broadcastFormatted(FormatArg... args) {
        if(message.isEmpty())
            return;
        String toSend = getMessage();
        for (FormatArg arg : args) {
            toSend = arg.applyToString(toSend);
        }
        Bukkit.broadcastMessage(Utils.hex(GlobalConfig.getInstance().getPrefix() + toSend.replace("\n", "\n" + GlobalConfig.getInstance().getPrefix())).replaceAll("\\s+", " "));
    }

    public void sendMessageFormated(CommandSender sender, FormatArg... args) {
        if(message.isEmpty())
            return;
        String toSend = getMessage();
        for (FormatArg arg : args) {
            toSend = arg.applyToString(toSend);
        }
        sender.sendMessage(Utils.hex(GlobalConfig.getInstance().getPrefix() + toSend.replace("\n", "\n" + GlobalConfig.getInstance().getPrefix())).replaceAll("\\s+", " "));
    }

    public String getMessageFormatted(FormatArg... args) {
        String toSend = getMessage();
        for (FormatArg arg : args) {
            toSend = arg.applyToString(toSend);
        }
        return toSend.replaceAll("\\s+", " ");
    }
}
