/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.Entity
 *  net.minecraft.server.v1_7_R4.World
 *  net.minecraft.server.v1_7_R4.WorldServer
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_7_R4.CraftWorld
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package net.darepvp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Player;

public class UtilServer {
    public static Player[] getPlayers() {
        return Bukkit.getServer().getOnlinePlayers();
    }

    public static List<org.bukkit.entity.Entity> getEntities(World world) {
        ArrayList<org.bukkit.entity.Entity> entities = new ArrayList<org.bukkit.entity.Entity>();
        WorldServer nmsworld = ((CraftWorld)world).getHandle();
        for (Object o : new ArrayList(nmsworld.entityList)) {
            CraftEntity bukkitEntity;
            Entity mcEnt;
            if (!(o instanceof Entity) || (bukkitEntity = (mcEnt = (Entity)o).getBukkitEntity()) == null) continue;
            entities.add((org.bukkit.entity.Entity)bukkitEntity);
        }
        return entities;
    }
}

