/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerBedLeaveEvent
 */
package net.darepvp.checks.movement;

import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class BedLeave
extends Check {
    public BedLeave(Janitor Janitor2) {
        super("BedLeave", "BedLeave", Janitor2);
        this.setAutobanTimer(true);
    }

    @EventHandler
    public void CheckBedLeave(PlayerBedLeaveEvent event) {
        if (!this.getJanitor().isEnabled()) {
            return;
        }
        Player player = event.getPlayer();
        Location pLoc = player.getLocation();
        int x = pLoc.getBlockX() - 10;
        while (x < pLoc.getBlockX() + 10) {
            int y = pLoc.getBlockY() - 10;
            while (y < pLoc.getBlockY() + 10) {
                int z = pLoc.getBlockZ() - 10;
                while (z < pLoc.getBlockZ() + 10) {
                    Block b = new Location(pLoc.getWorld(), (double)x, (double)y, (double)z).getBlock();
                    if (b.getType().equals((Object)Material.BED) || b.getType().equals((Object)Material.BED_BLOCK)) {
                        return;
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        this.getJanitor().logCheat(this, player, null, new String[0]);
    }
}

