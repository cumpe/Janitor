/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Difficulty
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent$RegainReason
 */
package net.darepvp.checks.combat;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.util.UtilTime;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class Regen
extends Check {
    private Map<UUID, Long> LastHeal = new HashMap<UUID, Long>();
    private Map<UUID, Map.Entry<Integer, Long>> FastHealTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public Regen(Janitor janitor) {
        super("Regen", "Regen", janitor);
        this.setAutobanTimer(true);
        this.setViolationsToNotify(3);
        this.setMaxViolations(12);
        this.setViolationResetTime(60000);
    }

    public boolean checkFastHeal(Player player) {
        if (this.LastHeal.containsKey(player.getUniqueId())) {
            long l = this.LastHeal.get(player.getUniqueId());
            this.LastHeal.remove(player.getUniqueId());
            if (System.currentTimeMillis() - l < 3000) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(ignoreCancelled=1, priority=EventPriority.MONITOR)
    public void onHeal(EntityRegainHealthEvent event) {
        if (!event.getRegainReason().equals((Object)EntityRegainHealthEvent.RegainReason.SATIATED)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getEntity();
        if (player.getWorld().getDifficulty().equals((Object)Difficulty.PEACEFUL)) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.FastHealTicks.containsKey(player.getUniqueId())) {
            Count = this.FastHealTicks.get(player.getUniqueId()).getKey();
            Time = this.FastHealTicks.get(player.getUniqueId()).getValue();
        }
        if (this.checkFastHeal(player)) {
            this.getJanitor().logCheat(this, player, null, new String[0]);
        }
        if (this.FastHealTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 60000)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        this.LastHeal.put(player.getUniqueId(), System.currentTimeMillis());
        this.FastHealTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

