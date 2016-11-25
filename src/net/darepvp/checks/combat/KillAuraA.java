/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package net.darepvp.checks.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.packets.events.PacketUseEntityEvent;
import net.darepvp.util.UtilTime;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class KillAuraA
extends Check {
    private Map<UUID, Long> LastMS = new HashMap<UUID, Long>();
    private Map<UUID, List<Long>> Clicks = new HashMap<UUID, List<Long>>();
    private Map<UUID, Map.Entry<Integer, Long>> ClickTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public KillAuraA(Janitor janitor) {
        super("KillAuraA", "Kill Aura (Type A)", janitor);
        this.setBannable(false);
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
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.ClickTicks.containsKey(damager.getUniqueId())) {
            Count = this.ClickTicks.get(damager.getUniqueId()).getKey();
            Time = this.ClickTicks.get(damager.getUniqueId()).getValue();
        }
        if (this.LastMS.containsKey(damager.getUniqueId())) {
            long MS = UtilTime.nowlong() - this.LastMS.get(damager.getUniqueId());
            if (MS > 500 || MS < 5) {
                this.LastMS.put(damager.getUniqueId(), UtilTime.nowlong());
                return;
            }
            if (this.Clicks.containsKey(damager.getUniqueId())) {
                List<Long> Clicks = this.Clicks.get(damager.getUniqueId());
                if (Clicks.size() == 10) {
                    this.Clicks.remove(damager.getUniqueId());
                    Collections.sort(Clicks);
                    long Range = Clicks.get(Clicks.size() - 1) - Clicks.get(0);
                    this.dumplog(damager, "New Range: " + Range);
                    if (Range < 30) {
                        Time = System.currentTimeMillis();
                        this.dumplog(damager, "New Count: " + ++Count);
                    }
                } else {
                    Clicks.add(MS);
                    this.Clicks.put(damager.getUniqueId(), Clicks);
                }
            } else {
                ArrayList<Long> Clicks = new ArrayList<Long>();
                Clicks.add(MS);
                this.Clicks.put(damager.getUniqueId(), Clicks);
            }
        }
        if (this.ClickTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 5000)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 0) {
            this.dumplog(damager, "Logged. Count: " + Count);
            Count = 0;
            this.getJanitor().logCheat(this, damager, "Click Pattern", "Experimental");
        }
        this.LastMS.put(damager.getUniqueId(), UtilTime.nowlong());
        this.ClickTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

