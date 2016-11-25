/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package net.darepvp.checks.movement;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.util.UtilCheat;
import net.darepvp.util.UtilMath;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Ascension
extends Check {
    private Map<UUID, Map.Entry<Long, Double>> AscensionTicks = new HashMap<UUID, Map.Entry<Long, Double>>();

    public Ascension(Janitor Janitor2) {
        super("Ascension", "Ascension", Janitor2);
        this.setAutobanTimer(true);
    }

    @EventHandler
    public void CheckAscension(PlayerMoveEvent event) {
        Location a;
        Player player = event.getPlayer();
        if (event.getFrom().getY() >= event.getTo().getY()) {
            return;
        }
        if (!this.getJanitor().isEnabled()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }
        if (this.getJanitor().LastVelocity.containsKey(player.getUniqueId())) {
            return;
        }
        long Time = System.currentTimeMillis();
        double TotalBlocks = 0.0;
        if (this.AscensionTicks.containsKey(player.getUniqueId())) {
            Time = this.AscensionTicks.get(player.getUniqueId()).getKey();
            TotalBlocks = this.AscensionTicks.get(player.getUniqueId()).getValue();
        }
        long MS = System.currentTimeMillis() - Time;
        double OffsetY = UtilMath.offset(UtilMath.getVerticalVector(event.getFrom().toVector()), UtilMath.getVerticalVector(event.getTo().toVector()));
        if (OffsetY > 0.0) {
            TotalBlocks += OffsetY;
        }
        if (UtilCheat.blocksNear(player)) {
            TotalBlocks = 0.0;
        }
        if (UtilCheat.blocksNear(a = player.getLocation().subtract(0.0, 1.0, 0.0))) {
            TotalBlocks = 0.0;
        }
        double Limit = 0.5;
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (!effect.getType().equals((Object)PotionEffectType.JUMP)) continue;
                int level = effect.getAmplifier() + 1;
                Limit += Math.pow((double)level + 4.2, 2.0) / 16.0;
                break;
            }
        }
        if (TotalBlocks > Limit) {
            if (MS > 500) {
                this.getJanitor().logCheat(this, player, "Flying Upward " + TotalBlocks + " Blocks", new String[0]);
                Time = System.currentTimeMillis();
            }
        } else {
            Time = System.currentTimeMillis();
        }
        this.AscensionTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Double>(Time, TotalBlocks));
    }
}

