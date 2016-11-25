/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package net.darepvp.checks.movement;

import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.util.UtilCheat;
import net.darepvp.util.UtilPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Step
extends Check {
    public Step(Janitor janitor) {
        super("Step", "Step", janitor);
        this.setBannable(false);
    }

    public boolean isOnGround(Player player) {
        if (UtilPlayer.isOnClimbable(player)) {
            return false;
        }
        if (player.getVehicle() != null) {
            return false;
        }
        Material type = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        Location a = player.getLocation().clone();
        a.setY(a.getY() - 0.5);
        type = a.getBlock().getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        a = player.getLocation().clone();
        a.setY(a.getY() + 0.5);
        type = a.getBlock().getRelative(BlockFace.DOWN).getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        if (UtilCheat.isBlock(player.getLocation().getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER})) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!this.isOnGround(player)) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        if (UtilCheat.slabsNear(player.getLocation())) {
            return;
        }
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            return;
        }
        if (this.getJanitor().LastVelocity.containsKey(player.getUniqueId())) {
            return;
        }
        double yDist = event.getTo().getY() - event.getFrom().getY();
        this.dumplog(player, "Height: " + yDist);
        if (yDist > 0.9) {
            this.dumplog(player, "Height (Logged): " + yDist);
            this.getJanitor().logCheat(this, player, null, String.valueOf(Math.round(yDist)));
        }
    }
}

