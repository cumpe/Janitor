/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package net.darepvp.checks.movement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.util.UtilCheat;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class Jesus
extends Check {
    private Map<UUID, Long> jesusTicks = new HashMap<UUID, Long>();

    public Jesus(Janitor Janitor2) {
        super("Jesus", "Jesus", Janitor2);
        this.setAutobanTimer(true);
        this.setMaxViolations(10);
        this.setViolationsToNotify(2);
    }

    @EventHandler
    public void CheckJesus(PlayerMoveEvent event) {
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }
        long Time = System.currentTimeMillis();
        Player player = event.getPlayer();
        if (player.getAllowFlight()) {
            return;
        }
        if (!player.getNearbyEntities(1.0, 1.0, 1.0).isEmpty()) {
            return;
        }
        if (this.jesusTicks.containsKey(player.getUniqueId())) {
            Time = this.jesusTicks.get(player.getUniqueId());
        }
        long MS = System.currentTimeMillis() - Time;
        if (UtilCheat.cantStandAtWater(player.getWorld().getBlockAt(player.getLocation())) && UtilCheat.isHoveringOverWater(player.getLocation()) && !UtilCheat.isFullyInWater(player.getLocation())) {
            this.dumplog(player, "Been hovering over water for: " + MS + "ms.");
            if (MS > 500) {
                this.dumplog(player, "(LIMIT) Been hovering over water for: " + MS + "ms.");
                this.getJanitor().logCheat(this, player, null, "Experimental");
                Time = System.currentTimeMillis();
            }
        } else {
            Time = System.currentTimeMillis();
        }
        this.jesusTicks.put(player.getUniqueId(), Time);
    }
}

