/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
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
import net.darepvp.util.UtilPlayer;
import net.darepvp.util.UtilTime;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Speed
extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    private Map<UUID, Map.Entry<Integer, Long>> tooFastTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public Speed(Janitor Janitor2) {
        super("Speed", "Speed", Janitor2);
        this.setAutobanTimer(true);
    }

    public boolean isOnIce(Player player) {
        Location a = player.getLocation();
        a.setY(a.getY() - 1.0);
        if (a.getBlock().getType().equals((Object)Material.ICE)) {
            return true;
        }
        a.setY(a.getY() - 1.0);
        if (a.getBlock().getType().equals((Object)Material.ICE)) {
            return true;
        }
        return false;
    }

    @Override
    public void onEnable() {
    }

    @EventHandler
    public void CheckSpeed(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getY() == event.getTo().getY() && event.getFrom().getZ() == event.getFrom().getZ()) {
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
        int Count = 0;
        long Time = UtilTime.nowlong();
        if (this.speedTicks.containsKey(player.getUniqueId())) {
            Count = this.speedTicks.get(player.getUniqueId()).getKey();
            Time = this.speedTicks.get(player.getUniqueId()).getValue();
        }
        int TooFastCount = 0;
        if (this.tooFastTicks.containsKey(player.getUniqueId())) {
            float speed;
            double OffsetXZ = UtilMath.offset(UtilMath.getHorizontalVector(event.getFrom().toVector()), UtilMath.getHorizontalVector(event.getTo().toVector()));
            double LimitXZ = 0.0;
            LimitXZ = UtilPlayer.isOnGround(player) && player.getVehicle() == null ? 0.33 : 0.4;
            if (UtilCheat.slabsNear(player.getLocation())) {
                LimitXZ += 0.05;
            }
            Location b = UtilPlayer.getEyeLocation(player);
            b.add(0.0, 1.0, 0.0);
            if (b.getBlock().getType() != Material.AIR && !UtilCheat.canStandWithin(b.getBlock())) {
                LimitXZ = 0.69;
            }
            if (this.isOnIce(player)) {
                LimitXZ = b.getBlock().getType() != Material.AIR && !UtilCheat.canStandWithin(b.getBlock()) ? 1.0 : 0.75;
            }
            LimitXZ += (double)((speed = player.getWalkSpeed()) > 0.2f ? speed * 10.0f * 0.33f : 0.0f);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (!effect.getType().equals((Object)PotionEffectType.SPEED)) continue;
                if (player.isOnGround()) {
                    LimitXZ += 0.06 * (double)(effect.getAmplifier() + 1);
                    continue;
                }
                LimitXZ += 0.02 * (double)(effect.getAmplifier() + 1);
            }
            this.dumplog(player, "Speed XZ: " + OffsetXZ);
            if (OffsetXZ > LimitXZ && !UtilTime.elapsed(this.tooFastTicks.get(player.getUniqueId()).getValue(), 150)) {
                TooFastCount = this.tooFastTicks.get(player.getUniqueId()).getKey() + 1;
                this.dumplog(player, "New TooFastCount: " + TooFastCount);
            } else {
                TooFastCount = 0;
                this.dumplog(player, "TooFastCount Reset");
            }
        }
        if (TooFastCount > 6) {
            TooFastCount = 0;
            this.dumplog(player, "New Count: " + ++Count);
        }
        if (this.speedTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 60000)) {
            this.dumplog(player, "Count Reset");
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 3) {
            this.dumplog(player, "Logged for Speed. Count: " + Count);
            Count = 0;
            this.getJanitor().logCheat(this, player, null, new String[0]);
        }
        this.tooFastTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(TooFastCount, System.currentTimeMillis()));
        this.speedTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}

