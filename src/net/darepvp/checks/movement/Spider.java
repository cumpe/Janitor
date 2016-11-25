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
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package net.darepvp.checks.movement;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import net.darepvp.util.UtilCheat;
import net.darepvp.util.UtilMath;
import net.darepvp.util.UtilPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Spider
extends Check {
    private Map<UUID, Map.Entry<Long, Double>> AscensionTicks = new HashMap<UUID, Map.Entry<Long, Double>>();

    public Spider(Janitor Janitor2) {
        super("Spider", "Spider", Janitor2);
        this.setBannable(false);
        this.setMaxViolations(5);
        this.setViolationsToNotify(1);
        this.setViolationResetTime(60000);
    }

    @EventHandler
    public void CheckSpider(PlayerMoveEvent event) {
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
        boolean ya = false;
        ArrayList<Material> Types = new ArrayList<Material>();
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.SOUTH).getType());
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.NORTH).getType());
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.WEST).getType());
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.EAST).getType());
        for (Material Type : Types) {
            if (!Type.isSolid() || Type == Material.LADDER || Type == Material.VINE) continue;
            ya = true;
            break;
        }
        if (OffsetY > 0.0) {
            TotalBlocks += OffsetY;
        }
        if (!ya || !UtilCheat.blocksNear(player)) {
            TotalBlocks = 0.0;
        }
        if (ya && (event.getFrom().getY() > event.getTo().getY() || UtilPlayer.isOnGround(player))) {
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
        if (ya && TotalBlocks > Limit) {
            if (MS > 500) {
                this.getJanitor().logCheat(this, player, null, "Experimental");
                Time = System.currentTimeMillis();
            }
        } else {
            Time = System.currentTimeMillis();
        }
        this.AscensionTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Double>(Time, TotalBlocks));
    }
}

