/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package net.darepvp.checks.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.packets.events.PacketUseEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class DoubleClick
extends Check {
    private Map<UUID, Long[]> LastMSCPS = new HashMap<UUID, Long[]>();

    public DoubleClick(Janitor janitor) {
        super("DoubleClick", "DoubleClick", janitor);
        this.setBannable(false);
        this.setViolationsToNotify(2);
        this.setMaxViolations(50);
    }

    @Override
    public void onEnable() {
    }

    @EventHandler
    public void UseEntity(PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK) {
            return;
        }
        if (!(e.getAttacked() instanceof Player)) {
            return;
        }
        Player damager = e.getAttacker();
        Long first = 0;
        Long second =  0;
        if (this.LastMSCPS.containsKey(damager.getUniqueId())) {
            first = this.LastMSCPS.get(damager.getUniqueId())[0];
            second = this.LastMSCPS.get(damager.getUniqueId())[1];
        }
        if (first == 0) {
            first = System.currentTimeMillis();
        } else if (second == 0) {
            second = System.currentTimeMillis();
            first = System.currentTimeMillis() - first;
        } else {
            second = System.currentTimeMillis() - second;
            if (first > 50 && second == 0) {
                this.getJanitor().logCheat(this, damager, null, new String[0]);
            }
            first = 0;
            second = 0;
        }
        this.LastMSCPS.put(damager.getUniqueId(), new Long[]{first, second});
    }
}

