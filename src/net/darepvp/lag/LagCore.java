/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  org.bukkit.Bukkit
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package net.darepvp.lag;

import net.darepvp.Janitor;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class LagCore
implements Listener {
    public Janitor janitor;
    private double tps;

    public LagCore(Janitor janitor) {
        this.janitor = janitor;
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this.janitor, new Runnable(){
            long sec;
            long currentSec;
            int ticks;

            @Override
            public void run() {
                this.sec = System.currentTimeMillis() / 1000;
                if (this.currentSec == this.sec) {
                    ++this.ticks;
                } else {
                    this.currentSec = this.sec;
                    LagCore.access$1(LagCore.this, LagCore.this.tps == 0.0 ? (double)this.ticks : (LagCore.this.tps + (double)this.ticks) / 2.0);
                    this.ticks = 0;
                }
            }
        }, 0, 1);
        this.janitor.RegisterListener(this);
    }

    public double getTPS() {
        return this.tps + 1.0 > 20.0 ? 20.0 : this.tps + 1.0;
    }

    public int getPing(Player player) {
        CraftPlayer cp = (CraftPlayer)player;
        EntityPlayer ep = cp.getHandle();
        return ep.ping;
    }

    static /* synthetic */ void access$1(LagCore lagCore, double d) {
        lagCore.tps = d;
    }

}

