/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package net.darepvp.checks.movement;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.util.UtilPlayer;
import net.darepvp.util.UtilTime;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class NoFall
extends Check {
    private Map<UUID, Map.Entry<Long, Integer>> NoFallTicks = new HashMap<UUID, Map.Entry<Long, Integer>>();
    private Map<UUID, Double> FallDistance = new HashMap<UUID, Double>();

    public NoFall(Janitor janitor) {
        super("NoFall", "NoFall", janitor);
        this.setBannable(false);
        this.setAutobanTimer(true);
        this.setMaxViolations(10);
    }

    @EventHandler
    public void Move(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (player.getHealth() <= 0.0) {
            return;
        }
        if (UtilPlayer.isOnClimbable(player)) {
            return;
        }
        if (UtilPlayer.isInWater(player)) {
            return;
        }
        double Falling = 0.0;
        if (!UtilPlayer.isOnGround(player) && e.getFrom().getY() > e.getTo().getY()) {
            if (this.FallDistance.containsKey(player.getUniqueId())) {
                Falling = this.FallDistance.get(player.getUniqueId());
            }
            Falling += e.getFrom().getY() - e.getTo().getY();
        }
        this.FallDistance.put(player.getUniqueId(), Falling);
        if (Falling < 3.0) {
            return;
        }
        long Time = System.currentTimeMillis();
        int Count = 0;
        if (this.NoFallTicks.containsKey(player.getUniqueId())) {
            Time = this.NoFallTicks.get(player.getUniqueId()).getKey();
            Count = this.NoFallTicks.get(player.getUniqueId()).getValue();
        }
        if (player.isOnGround() || player.getFallDistance() == 0.0f) {
            this.dumplog(player, "NoFall. Real Fall Distance: " + Falling);
            ++Count;
        } else {
            this.dumplog(player, "Count Reset");
            Count = 0;
        }
        if (this.NoFallTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 10000)) {
            this.dumplog(player, "Count Reset");
            Count = 0;
            Time = System.currentTimeMillis();
        }
        if (Count >= 3) {
            this.dumplog(player, "Logged. Count: " + Count);
            Count = 0;
            this.FallDistance.put(player.getUniqueId(), 0.0);
            this.getJanitor().logCheat(this, player, null, new String[0]);
        }
        this.NoFallTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Integer>(Time, Count));
    }
}

