/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package net.darepvp.checks.combat;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.packets.events.PacketPlayerEvent;
import net.darepvp.update.UpdateType;
import net.darepvp.update.events.UpdateEvent;
import net.darepvp.util.UtilCheat;
import net.darepvp.util.UtilTime;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class VelocityVertical
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> VelocityTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    private Map<Player, Map.Entry<Double, Long>> velocity = new HashMap<Player, Map.Entry<Double, Long>>();
    private Map<Player, Long> LastUpdate = new HashMap<Player, Long>();

    public VelocityVertical(Janitor janitor) {
        super("VelocityVertical", "Velocity (Vertical)", janitor);
        this.setBannable(false);
        this.setEnabled(false);
    }

    @EventHandler(ignoreCancelled=1, priority=EventPriority.MONITOR)
    public void Knockback(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        Player player = (Player)event.getEntity();
        if (this.velocity.containsKey((Object)player)) {
            return;
        }
        Location pL = player.getLocation().clone();
        pL.add(0.0, player.getEyeHeight() + 1.0, 0.0);
        if (UtilCheat.blocksNear(pL)) {
            return;
        }
        this.velocity.put(player, new AbstractMap.SimpleEntry<Double, Long>(player.getLocation().getY(), System.currentTimeMillis()));
    }

    @EventHandler
    public void Death(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (this.velocity.containsKey((Object)player)) {
            this.velocity.remove((Object)player);
        }
    }

    @EventHandler
    public void Move(PlayerMoveEvent event) {
        Map.Entry<Double, Long> ye;
        Player player = event.getPlayer();
        double Y = event.getTo().getY();
        if (this.velocity.containsKey((Object)player) && Y > (ye = this.velocity.get((Object)player)).getKey()) {
            this.velocity.remove((Object)player);
        }
    }

    @EventHandler
    public void Packet(PacketPlayerEvent event) {
        this.LastUpdate.put(event.getPlayer(), System.currentTimeMillis());
    }

    @EventHandler
    public void Update(UpdateEvent event) {
        if (!event.getType().equals((Object)UpdateType.TICK)) {
            return;
        }
        for (Player player : this.velocity.keySet()) {
            if (!this.LastUpdate.containsKey((Object)player) || this.LastUpdate.get((Object)player) == null) continue;
            int Count = 0;
            long Time = System.currentTimeMillis();
            if (this.VelocityTicks.containsKey(player.getUniqueId())) {
                Count = this.VelocityTicks.get(player.getUniqueId()).getKey();
                Time = this.VelocityTicks.get(player.getUniqueId()).getValue();
                if (UtilTime.elapsed(Time, 5000)) {
                    Count = 0;
                    Time = System.currentTimeMillis();
                }
            }
            Map.Entry<Double, Long> ye = this.velocity.get((Object)player);
            if (System.currentTimeMillis() >= ye.getValue() + 1000) {
                this.velocity.remove((Object)player);
                if (System.currentTimeMillis() - this.LastUpdate.get((Object)player) < 60) {
                    ++Count;
                    Time = System.currentTimeMillis();
                } else {
                    Count = 0;
                }
            }
            if (Count > 3) {
                Count = 0;
                this.getJanitor().logCheat(this, player, "Knockback Modifier", "Experimental");
            }
            this.VelocityTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
        }
    }
}

