/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package net.darepvp.checks.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.util.UtilCheat;
import net.darepvp.util.UtilPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class Fly
extends Check {
    private Map<UUID, Long> flyTicks = new HashMap<UUID, Long>();

    public Fly(Janitor Janitor2) {
        super("Fly", "Fly", Janitor2);
        this.setAutobanTimer(true);
    }

    @EventHandler
    public void CheckFly(PlayerMoveEvent event) {
        long MS;
        if (!this.getJanitor().isEnabled()) {
            return;
        }
        Player player = event.getPlayer();
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (UtilPlayer.isInWater(player)) {
            return;
        }
        if (UtilCheat.isInWeb(player)) {
            return;
        }
        if (UtilCheat.blocksNear(player)) {
            if (this.flyTicks.containsKey(player.getUniqueId())) {
                this.flyTicks.remove(player.getUniqueId());
            }
            return;
        }
        if (event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()) {
            return;
        }
        if (event.getTo().getY() != event.getFrom().getY()) {
            if (this.flyTicks.containsKey(player.getUniqueId())) {
                this.flyTicks.remove(player.getUniqueId());
            }
            return;
        }
        long Time = System.currentTimeMillis();
        if (this.flyTicks.containsKey(player.getUniqueId())) {
            Time = this.flyTicks.get(player.getUniqueId());
        }
        if ((MS = System.currentTimeMillis() - Time) > 500) {
            this.dumplog(player, "Logged. MS: " + MS);
            this.flyTicks.remove(player.getUniqueId());
            this.getJanitor().logCheat(this, player, "Hover", new String[0]);
            return;
        }
        this.flyTicks.put(player.getUniqueId(), Time);
    }
}

