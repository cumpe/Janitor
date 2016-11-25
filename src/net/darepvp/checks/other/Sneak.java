/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package net.darepvp.checks.other;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.packets.events.PacketEntityActionEvent;
import net.darepvp.util.UtilTime;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Sneak
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> sneakTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public Sneak(Janitor janitor) {
        super("Sneak", "Sneak", janitor);
        this.setAutobanTimer(true);
    }

    @EventHandler
    public void EntityAction(PacketEntityActionEvent event) {
        if (event.getAction() != 1) {
            return;
        }
        Player player = event.getPlayer();
        int Count = 0;
        long Time = -1;
        if (this.sneakTicks.containsKey(player.getUniqueId())) {
            Count = this.sneakTicks.get(player.getUniqueId()).getKey();
            Time = this.sneakTicks.get(player.getUniqueId()).getValue();
        }
        ++Count;
        if (this.sneakTicks.containsKey(player.getUniqueId())) {
            if (UtilTime.elapsed(Time, 100)) {
                Count = 0;
                Time = System.currentTimeMillis();
            } else {
                Time = System.currentTimeMillis();
            }
        }
        if (Count > 50) {
            Count = 0;
            this.getJanitor().logCheat(this, player, null, new String[0]);
        }
        this.sneakTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

