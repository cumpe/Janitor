/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package net.darepvp.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.lag.LagCore;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FastBow
extends Check {
    public Map<UUID, Long> bowPull = new HashMap<UUID, Long>();

    public FastBow(Janitor janitor) {
        super("FastBow", "FastBow", janitor);
        this.setViolationsToNotify(2);
        this.setMaxViolations(10);
    }

    @EventHandler(ignoreCancelled=1, priority=EventPriority.MONITOR)
    public void Interact(PlayerInteractEvent e) {
        Player Player2 = e.getPlayer();
        if (Player2.getItemInHand() != null && Player2.getItemInHand().getType().equals((Object)Material.BOW)) {
            this.bowPull.put(Player2.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler(ignoreCancelled=1, priority=EventPriority.MONITOR)
    public void onShoot(ProjectileLaunchEvent e) {
        Player player;
        Arrow arrow;
        if (e.getEntity() instanceof Arrow && (arrow = (Arrow)e.getEntity()).getShooter() != null && arrow.getShooter() instanceof Player && this.bowPull.containsKey((player = (Player)arrow.getShooter()).getUniqueId())) {
            Long time = System.currentTimeMillis() - this.bowPull.get(player.getUniqueId());
            double power = arrow.getVelocity().length();
            Long timeLimit = 300;
            int ping = this.getJanitor().lag.getPing(player);
            if (ping > 400) {
                timeLimit = 150;
            }
            if (power > 2.5 && time < timeLimit) {
                this.getJanitor().logCheat(this, player, null, new String[0]);
            }
        }
    }
}

