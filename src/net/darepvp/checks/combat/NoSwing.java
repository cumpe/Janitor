/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.plugin.Plugin
 */
package net.darepvp.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.lag.LagCore;
import net.darepvp.packets.events.PacketSwingArmEvent;
import net.darepvp.util.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class NoSwing
extends Check {
    private Map<UUID, Long> LastArmSwing = new HashMap<UUID, Long>();

    public NoSwing(Janitor janitor) {
        super("NoSwing", "NoSwing", janitor);
        this.setAutobanTimer(true);
    }

    @EventHandler(ignoreCancelled=1, priority=EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent e) {
        Player player;
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!e.getCause().equals((Object)EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }
        if (this.getJanitor().getLag().getTPS() < 17.0) {
            return;
        }
        final Player fplayer = player = (Player)e.getDamager();
        if (this.getJanitor().isEnabled()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.getJanitor(), new Runnable(){

                @Override
                public void run() {
                    if (!NoSwing.this.hasSwung(fplayer, 1500)) {
                        NoSwing.this.getJanitor().logCheat(NoSwing.this, fplayer, null, new String[0]);
                    }
                }
            }, 10);
        }
    }

    public boolean hasSwung(Player player, Long time) {
        if (!this.LastArmSwing.containsKey(player.getUniqueId())) {
            return false;
        }
        if (UtilTime.nowlong() < this.LastArmSwing.get(player.getUniqueId()) + time) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void ArmSwing(PacketSwingArmEvent event) {
        this.LastArmSwing.put(event.getPlayer().getUniqueId(), UtilTime.nowlong());
    }

}

