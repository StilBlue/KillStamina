package me.stilblue.killStamina.listeners;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.stilblue.killStamina.data.PlayerData;
import me.stilblue.killStamina.data.PlayerDataManager;
import me.stilblue.killStamina.data.StaminaCost;
import me.stilblue.killStamina.data.StaminaCostManager;
import me.stilblue.killStamina.data.config.FormatArg;
import me.stilblue.killStamina.data.config.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;

public class PlayerListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final StaminaCostManager staminaCostManager;
    public static final String PERMISSION_BYPASS_KILL = "KillStamina.bypass.kill";
    public static final String PERMISSION_BYPASS_HIT = "KillStamina.bypass.hit";

    public PlayerListener(StaminaCostManager staminaCostManager, PlayerDataManager playerDataManager1) {
        this.staminaCostManager = staminaCostManager;
        this.playerDataManager = playerDataManager1;
    }

    @EventHandler
    public void onKill(MythicMobDeathEvent event) {
        String diedID = event.getMob().getType().getInternalName();

        StaminaCost staminaCost = staminaCostManager.getStaminaCost(diedID);
        if (staminaCost == null)
            return;

        // If killer is op or has bypass permission
        if (event.getKiller() instanceof Player p && (p.hasPermission(PERMISSION_BYPASS_KILL) || p.isOp()))
            return;

        PlayerData playerData = playerDataManager.getPlayerData(event.getKiller().getUniqueId());
        if (playerData == null)
            return;

        playerData.setStamina(playerData.getStamina() - staminaCost.getCost());
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player))
            return;

        // If killer is op or has bypass permission
        if (player.hasPermission(PERMISSION_BYPASS_HIT) || player.isOp())
            return;

        Entity bukkitEntity = event.getEntity();

        Optional<ActiveMob> optActiveMob = MythicBukkit.inst().getMobManager().getActiveMob(bukkitEntity.getUniqueId());
        optActiveMob.ifPresent(activeMob -> {

            String internalID = activeMob.getType().getInternalName();
            StaminaCost cost = staminaCostManager.getStaminaCost(internalID);
            if (cost == null)
                return;

            PlayerData data = playerDataManager.getPlayerData(player.getUniqueId());
            if (data == null)
                return;

            // If the cost is greater than the stamina that killer currently have
            // then do not allow them to hit
            if (cost.getCost() > data.getStamina()) {
                event.setCancelled(true);
                Message.NOT_ENOUGH_STAMINA.sendMessageFormated(
                        player,
                        new FormatArg("%current%", Integer.toString(data.getStamina())),
                        new FormatArg("%need%", Integer.toString(cost.getCost()))
                );
            }
        });
    }
}
