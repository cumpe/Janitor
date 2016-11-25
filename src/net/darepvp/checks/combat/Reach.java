/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package net.darepvp.checks.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.lag.LagCore;
import net.darepvp.util.UtilMath;
import net.darepvp.util.UtilPlayer;
import net.darepvp.util.UtilTime;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Reach
extends Check {
    private Map<UUID, ReachEntry> ReachTicks = new HashMap<UUID, ReachEntry>();

    public Reach(Janitor janitor) {
        super("Reach", "Reach", janitor);
        this.setEnabled(false);
        this.setAutobanTimer(true);
    }

    @EventHandler(ignoreCancelled=1, priority=EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent e) {
        double Difference;
        if (!e.getCause().equals((Object)EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player)e.getDamager();
        Player player = (Player)e.getEntity();
        if (damager.getAllowFlight()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        long Time = System.currentTimeMillis();
        List Reachs = new ArrayList();
        if (this.ReachTicks.containsKey(damager.getUniqueId())) {
            Time = this.ReachTicks.get(damager.getUniqueId()).getLastTime();
            Reachs = new ArrayList<Double>(this.ReachTicks.get(damager.getUniqueId()).getReachs());
        }
        double MaxReach = 4.0;
        if (damager.hasPotionEffect(PotionEffectType.SPEED)) {
            int Level = 0;
            for (PotionEffect Effect : damager.getActivePotionEffects()) {
                if (!Effect.getType().equals((Object)PotionEffectType.SPEED)) continue;
                Level = Effect.getAmplifier();
                break;
            }
            switch (Level) {
                case 0: {
                    MaxReach = 4.1;
                    break;
                }
                case 1: {
                    MaxReach = 4.2;
                    break;
                }
                case 2: {
                    MaxReach = 4.3;
                    break;
                }
                default: {
                    return;
                }
            }
        }
        if (player.getVelocity().length() > 0.08 || this.getJanitor().LastVelocity.containsKey(player.getUniqueId())) {
            return;
        }
        double Reach2 = UtilPlayer.getEyeLocation(damager).distance(player.getLocation());
        int Ping = this.getJanitor().getLag().getPing(damager);
        if (Ping >= 100 && Ping < 200) {
            MaxReach += 0.2;
        } else if (Ping >= 200 && Ping < 250) {
            MaxReach += 0.4;
        } else if (Ping >= 250 && Ping < 300) {
            MaxReach += 0.8;
        } else if (Ping >= 300 && Ping < 350) {
            MaxReach += 1.2;
        } else if (Ping >= 350 && Ping < 400) {
            MaxReach += 1.5;
        } else if (Ping > 400) {
            return;
        }
        this.dumplog(damager, "Reach: " + Reach2 + ", MaxReach: " + MaxReach + ", Damager Velocity: " + damager.getVelocity().length() + ", " + "Player Velocity: " + player.getVelocity().length());
        if (damager.getLocation().getY() > player.getLocation().getY()) {
            Difference = damager.getLocation().getY() - player.getLocation().getY();
            MaxReach += Difference / 4.0;
        } else if (player.getLocation().getY() > damager.getLocation().getY()) {
            Difference = player.getLocation().getY() - damager.getLocation().getY();
            MaxReach += Difference / 4.0;
        }
        if (Reach2 > MaxReach) {
            Reachs.add(Reach2);
            Time = System.currentTimeMillis();
        }
        if (this.ReachTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 25000)) {
            Reachs.clear();
            Time = System.currentTimeMillis();
        }
        this.dumplog(damager, "Reach Count: " + Reachs.size());
        if (Reachs.size() > 3) {
            Double B;
            Double AverageReach = UtilMath.averageDouble(Reachs);
            Double A = 6.0 - MaxReach;
            if (A < 0.0) {
                A = 0.0;
            }
            if ((B = Double.valueOf(AverageReach - MaxReach)) < 0.0) {
                B = 0.0;
            }
            int Level = (int)Math.round(B / A * 100.0);
            Reachs.clear();
            this.dumplog(damager, "Logged for Reach. Average Reach: " + AverageReach + ", Level: " + Level + "%, Max Reach: " + MaxReach);
            this.getJanitor().logCheat(this, damager, null, String.valueOf(Level) + "%");
        }
        this.ReachTicks.put(damager.getUniqueId(), new ReachEntry(Time, Reachs));
    }

    public class ReachEntry {
        public Long LastTime;
        public List<Double> Reachs;

        public ReachEntry(Long LastTime, List<Double> Reachs) {
            this.Reachs = new ArrayList<Double>();
            this.LastTime = LastTime;
            this.Reachs = Reachs;
        }

        public Long getLastTime() {
            return this.LastTime;
        }

        public List<Double> getReachs() {
            return this.Reachs;
        }

        public void setLastTime(Long LastTime) {
            this.LastTime = LastTime;
        }

        public void setReachs(List<Double> Reachs) {
            this.Reachs = Reachs;
        }

        public void addReach(Double Reach2) {
            this.Reachs.add(Reach2);
        }
    }

}

