/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package net.darepvp.checks.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.packets.events.PacketPlayerEvent;
import net.darepvp.util.UtilMath;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Timer
extends Check {
    private Map<UUID, Long> lastTimer = new HashMap<UUID, Long>();
    private Map<UUID, List<Long>> MS = new HashMap<UUID, List<Long>>();
    private Map<UUID, Integer> timerTicks = new HashMap<UUID, Integer>();

    public Timer(Janitor janitor) {
        super("Timer", "Timer", janitor);
        this.setAutobanTimer(true);
    }

    @EventHandler
    public void PacketPlayer(PacketPlayerEvent event) {
        Player player = event.getPlayer();
        if (!this.getJanitor().isEnabled()) {
            return;
        }
        int Count = 0;
        if (this.timerTicks.containsKey(player.getUniqueId())) {
            Count = this.timerTicks.get(player.getUniqueId());
        }
        if (this.lastTimer.containsKey(player.getUniqueId())) {
            long MS = System.currentTimeMillis() - this.lastTimer.get(player.getUniqueId());
            List List2 = new ArrayList<Long>();
            if (this.MS.containsKey(player.getUniqueId())) {
                List2 = this.MS.get(player.getUniqueId());
            }
            List2.add(MS);
            if (List2.size() == 20) {
                boolean doeet = true;
                for (Long ListMS : List2) {
                    if (ListMS >= 1) continue;
                    doeet = false;
                }
                Long average = UtilMath.averageLong(List2);
                this.dumplog(player, "Average MS for 20 ticks: " + average);
                if (average < 48 && doeet) {
                    this.dumplog(player, "New Count: " + ++Count);
                } else {
                    Count = 0;
                }
                this.MS.remove(player.getUniqueId());
            } else {
                this.MS.put(player.getUniqueId(), List2);
            }
        }
        if (Count > 4) {
            this.dumplog(player, "Logged for timer. Count: " + Count);
            Count = 0;
            this.getJanitor().logCheat(this, player, null, new String[0]);
        }
        this.lastTimer.put(player.getUniqueId(), System.currentTimeMillis());
        this.timerTicks.put(player.getUniqueId(), Count);
    }
}

