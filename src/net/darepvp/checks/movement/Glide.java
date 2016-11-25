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
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class Glide
extends Check {
    private Map<UUID, Long> flyTicks = new HashMap<UUID, Long>();

    public Glide(Janitor janitor) {
        super("Glide", "Glide", janitor);
        this.setAutobanTimer(true);
    }

    @Override
    public void onEnable() {
    }

    @EventHandler
    public void CheckGlide(PlayerMoveEvent event) {
        if (!this.getJanitor().isEnabled()) {
            return;
        }
        Player player = event.getPlayer();
        if (player.getAllowFlight()) {
            return;
        }
        if (UtilCheat.isInWeb(player)) {
            return;
        }
        if (player.getVehicle() != null) {
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
        double OffsetY = event.getFrom().getY() - event.getTo().getY();
        if (OffsetY <= 0.0 || OffsetY > 0.16) {
            if (this.flyTicks.containsKey(player.getUniqueId())) {
                this.flyTicks.remove(player.getUniqueId());
            }
            return;
        }
        this.dumplog(player, "OffsetY: " + OffsetY);
        long Time = System.currentTimeMillis();
        if (this.flyTicks.containsKey(player.getUniqueId())) {
            Time = this.flyTicks.get(player.getUniqueId());
        }
        long MS = System.currentTimeMillis() - Time;
        this.dumplog(player, "MS: " + MS);
        if (MS > 1000) {
            this.dumplog(player, "Logged. MS: " + MS);
            this.flyTicks.remove(player.getUniqueId());
            this.getJanitor().logCheat(this, player, "Fall Speed", new String[0]);
            return;
        }
        this.flyTicks.put(player.getUniqueId(), Time);
    }
}

