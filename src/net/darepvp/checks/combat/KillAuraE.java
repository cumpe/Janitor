/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package net.darepvp.checks.combat;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class KillAuraE
extends Check {
    private Map<Player, Map.Entry<Integer, Long>> lastAttack = new HashMap<Player, Map.Entry<Integer, Long>>();

    public KillAuraE(Janitor janitor) {
        super("KillAuraE", "Kill Aura (Type E)", janitor);
        this.setBannable(false);
    }

    @Override
    public void onEnable() {
    }

    @EventHandler(ignoreCancelled=1, priority=EventPriority.MONITOR)
    public void Damage(EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)e.getDamager();
        if (this.lastAttack.containsKey((Object)player)) {
            Integer entityid = this.lastAttack.get((Object)player).getKey();
            Long time = this.lastAttack.get((Object)player).getValue();
            if (entityid.intValue() != e.getEntity().getEntityId() && System.currentTimeMillis() - time < 5) {
                this.getJanitor().logCheat(this, player, "MultiAura", new String[0]);
            }
            this.lastAttack.remove((Object)player);
        } else {
            this.lastAttack.put(player, new AbstractMap.SimpleEntry<Integer, Long>(e.getEntity().getEntityId(), System.currentTimeMillis()));
        }
    }
}

