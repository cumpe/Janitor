/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package net.darepvp.checks.other;

import com.comphenix.protocol.events.PacketEvent;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.packets.events.PacketBlockPlacementEvent;
import net.darepvp.packets.events.PacketHeldItemChangeEvent;
import net.darepvp.packets.events.PacketSwingArmEvent;
import net.darepvp.util.UtilTime;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Crash
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> faggotTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    private Map<UUID, Map.Entry<Integer, Long>> faggot2Ticks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    private Map<UUID, Map.Entry<Integer, Long>> faggot3Ticks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    public List<UUID> faggots = new ArrayList<UUID>();

    public Crash(Janitor janitor) {
        super("Crash", "Crash", janitor);
        this.setMaxViolations(0);
    }

    @EventHandler
    public void Swing(PacketSwingArmEvent e) {
        Player faggot = e.getPlayer();
        if (this.faggots.contains(faggot.getUniqueId())) {
            e.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.faggotTicks.containsKey(faggot.getUniqueId())) {
            Count = this.faggotTicks.get(faggot.getUniqueId()).getKey();
            Time = this.faggotTicks.get(faggot.getUniqueId()).getValue();
        }
        ++Count;
        if (this.faggotTicks.containsKey(faggot.getUniqueId()) && UtilTime.elapsed(Time, 100)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000) {
            this.getJanitor().logCheat(this, faggot, null, new String[0]);
            this.faggots.add(faggot.getUniqueId());
        }
        this.faggotTicks.put(faggot.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }

    @EventHandler
    public void Switch(PacketHeldItemChangeEvent e) {
        Player faggot = e.getPlayer();
        if (this.faggots.contains(faggot.getUniqueId())) {
            e.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.faggot2Ticks.containsKey(faggot.getUniqueId())) {
            Count = this.faggot2Ticks.get(faggot.getUniqueId()).getKey();
            Time = this.faggot2Ticks.get(faggot.getUniqueId()).getValue();
        }
        ++Count;
        if (this.faggot2Ticks.containsKey(faggot.getUniqueId()) && UtilTime.elapsed(Time, 100)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000) {
            this.getJanitor().logCheat(this, faggot, null, new String[0]);
            this.faggots.add(faggot.getUniqueId());
        }
        this.faggot2Ticks.put(faggot.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }

    @EventHandler
    public void BlockPlace(PacketBlockPlacementEvent e) {
        Player faggot = e.getPlayer();
        if (this.faggots.contains(faggot.getUniqueId())) {
            e.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.faggot3Ticks.containsKey(faggot.getUniqueId())) {
            Count = this.faggot3Ticks.get(faggot.getUniqueId()).getKey();
            Time = this.faggot3Ticks.get(faggot.getUniqueId()).getValue();
        }
        ++Count;
        if (this.faggot3Ticks.containsKey(faggot.getUniqueId()) && UtilTime.elapsed(Time, 100)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000) {
            this.getJanitor().logCheat(this, faggot, null, new String[0]);
            this.faggots.add(faggot.getUniqueId());
        }
        this.faggot3Ticks.put(faggot.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

