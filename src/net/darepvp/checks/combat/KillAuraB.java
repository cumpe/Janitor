/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.util.Vector
 */
package net.darepvp.checks.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.lag.LagCore;
import net.darepvp.packets.events.PacketUseEntityEvent;
import net.darepvp.util.UtilCheat;
import net.darepvp.util.UtilTime;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

public class KillAuraB
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> AuraTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public KillAuraB(Janitor janitor) {
        super("KillAuraB", "Kill Aura (Type B)", janitor);
        this.setBannable(false);
        this.setMaxViolations(200);
        this.setViolationsToNotify(25);
    }

    @Override
    public void onEnable() {
    }

    @EventHandler
    public void UseEntity(PacketUseEntityEvent e) {
        int Ping;
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }
        if (!(e.getAttacked() instanceof Player)) {
            return;
        }
        Player damager = e.getAttacker();
        Player player = (Player)e.getAttacked();
        if (damager.getAllowFlight()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.AuraTicks.containsKey(damager.getUniqueId())) {
            Count = this.AuraTicks.get(damager.getUniqueId()).getKey();
            Time = this.AuraTicks.get(damager.getUniqueId()).getValue();
        }
        double OffsetXZ = UtilCheat.getAimbotoffset(damager.getLocation(), damager.getEyeHeight(), (LivingEntity)player);
        double LimitOffset = 200.0;
        if (damager.getVelocity().length() > 0.08 || this.getJanitor().LastVelocity.containsKey(damager.getUniqueId())) {
            LimitOffset += 200.0;
        }
        if ((Ping = this.getJanitor().getLag().getPing(damager)) >= 100 && Ping < 200) {
            LimitOffset += 50.0;
        } else if (Ping >= 200 && Ping < 250) {
            LimitOffset += 75.0;
        } else if (Ping >= 250 && Ping < 300) {
            LimitOffset += 150.0;
        } else if (Ping >= 300 && Ping < 350) {
            LimitOffset += 300.0;
        } else if (Ping >= 350 && Ping < 400) {
            LimitOffset += 400.0;
        } else if (Ping > 400) {
            return;
        }
        this.dumplog(damager, "Offset: " + OffsetXZ + ", Ping: " + Ping + ", Max Offset: " + LimitOffset);
        if (OffsetXZ > LimitOffset * 4.0) {
            Count += 12;
        } else if (OffsetXZ > LimitOffset * 3.0) {
            Count += 10;
        } else if (OffsetXZ > LimitOffset * 2.0) {
            Count += 8;
        } else if (OffsetXZ > LimitOffset) {
            Count += 4;
        }
        if (this.AuraTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 60000)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 16) {
            this.dumplog(damager, "Logged. Count: " + Count + ", Ping: " + Ping);
            Count = 0;
            this.getJanitor().logCheat(this, damager, "Hit Miss Ratio", "Experimental");
        }
        this.AuraTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

