/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package net.darepvp.checks.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.packets.events.PacketUseEntityEvent;
import net.darepvp.util.UtilTime;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class KillAuraC
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> AimbotTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    private Map<UUID, Double> Differences = new HashMap<UUID, Double>();
    private Map<UUID, Location> LastLocation = new HashMap<UUID, Location>();

    public KillAuraC(Janitor janitor) {
        super("KillAuraC", "Kill Aura (Type C)", janitor);
        this.setAutobanTimer(true);
    }

    @Override
    public void onEnable() {
    }

    @EventHandler
    public void UseEntity(PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }
        if (!(e.getAttacked() instanceof Player)) {
            return;
        }
        Player damager = e.getAttacker();
        if (damager.getAllowFlight()) {
            return;
        }
        Location from = null;
        Location to = damager.getLocation();
        if (this.LastLocation.containsKey(damager.getUniqueId())) {
            from = this.LastLocation.get(damager.getUniqueId());
        }
        this.LastLocation.put(damager.getUniqueId(), damager.getLocation());
        int Count = 0;
        long Time = System.currentTimeMillis();
        double LastDifference = -111111.0;
        if (this.Differences.containsKey(damager.getUniqueId())) {
            LastDifference = this.Differences.get(damager.getUniqueId());
        }
        if (this.AimbotTicks.containsKey(damager.getUniqueId())) {
            Count = this.AimbotTicks.get(damager.getUniqueId()).getKey();
            Time = this.AimbotTicks.get(damager.getUniqueId()).getValue();
        }
        if (from == null || to.getX() == from.getX() && to.getZ() == from.getZ()) {
            return;
        }
        double Difference = Math.abs(to.getYaw() - from.getYaw());
        if (Difference == 0.0) {
            return;
        }
        if (Difference > 2.0) {
            this.dumplog(damager, "Difference: " + Difference);
            double diff = Math.abs(LastDifference - Difference);
            if (diff < 3.0) {
                this.dumplog(damager, "New Count: " + ++Count);
            }
        }
        this.Differences.put(damager.getUniqueId(), Difference);
        if (this.AimbotTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 5000)) {
            this.dumplog(damager, "Count Reset");
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 10) {
            Count = 0;
            this.dumplog(damager, "Logged. Last Difference: " + Math.abs(to.getYaw() - from.getYaw()) + ", Count: " + Count);
            this.getJanitor().logCheat(this, damager, "Aimbot", new String[0]);
        }
        this.AimbotTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

