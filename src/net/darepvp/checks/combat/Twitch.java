/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package net.darepvp.checks.combat;

import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.packets.events.PacketPlayerEvent;
import net.darepvp.packets.events.PacketPlayerType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Twitch
extends Check {
    public Twitch(Janitor janitor) {
        super("Twitch", "Twitch", janitor);
        this.setAutobanTimer(true);
    }

    @EventHandler
    public void Player(PacketPlayerEvent e) {
        if (e.getType() != PacketPlayerType.LOOK) {
            return;
        }
        if (e.getPitch() > 90.1f || e.getPitch() < -90.1f) {
            this.getJanitor().logCheat(this, e.getPlayer(), null, new String[0]);
        }
    }
}

