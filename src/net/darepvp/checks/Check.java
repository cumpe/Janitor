/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.darepvp.checks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.darepvp.Janitor;
import net.darepvp.util.TxtFile;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Check
implements Listener {
    private String Identifier;
    private String Name;
    private Janitor Janitor;
    private boolean Enabled = true;
    private boolean BanTimer = false;
    private boolean Bannable = true;
    private boolean JudgementDay = false;
    private Integer MaxViolations = 5;
    private Integer ViolationsToNotify = 1;
    private Long ViolationResetTime = (long) 600000;
    public Map<String, List<String>> DumpLogs = new HashMap<String, List<String>>();

    public Check(String Identifier, String Name, Janitor Janitor2) {
        this.Name = Name;
        this.Janitor = Janitor2;
        this.Identifier = Identifier;
    }

    public void dumplog(Player player, String log) {
        if (!this.DumpLogs.containsKey(player.getName())) {
            ArrayList<String> logs = new ArrayList<String>();
            logs.add(log);
            this.DumpLogs.put(player.getName(), logs);
        } else {
            this.DumpLogs.get(player.getName()).add(log);
        }
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public boolean isEnabled() {
        return this.Enabled;
    }

    public boolean isBannable() {
        return this.Bannable;
    }

    public boolean hasBanTimer() {
        return this.BanTimer;
    }

    public boolean isJudgmentDay() {
        return this.JudgementDay;
    }

    public Janitor getJanitor() {
        return this.Janitor;
    }

    public Integer getMaxViolations() {
        return this.MaxViolations;
    }

    public Integer getViolationsToNotify() {
        return this.ViolationsToNotify;
    }

    public Long getViolationResetTime() {
        return this.ViolationResetTime;
    }

    public void setEnabled(boolean Enabled) {
        if (Enabled) {
            if (!this.isEnabled()) {
                this.Janitor.RegisterListener(this);
            }
        } else if (this.isEnabled()) {
            HandlerList.unregisterAll((Listener)this);
        }
        this.Enabled = Enabled;
    }

    public void setBannable(boolean Bannable) {
        this.Bannable = Bannable;
    }

    public void setAutobanTimer(boolean BanTimer) {
        this.BanTimer = BanTimer;
    }

    public void setMaxViolations(int MaxViolations) {
        this.MaxViolations = MaxViolations;
    }

    public void setViolationsToNotify(int ViolationsToNotify) {
        this.ViolationsToNotify = ViolationsToNotify;
    }

    public void setViolationResetTime(long ViolationResetTime) {
        this.ViolationResetTime = ViolationResetTime;
    }

    public void setJudgementDay(boolean JudgementDay) {
        this.JudgementDay = JudgementDay;
    }

    public String getName() {
        return this.Name;
    }

    public String getIdentifier() {
        return this.Identifier;
    }

    public String dump(String player) {
        if (!this.DumpLogs.containsKey(player)) {
            return null;
        }
        TxtFile file = new TxtFile(this.getJanitor(), "/Dumps", String.valueOf(player) + "_" + this.getIdentifier());
        file.clear();
        for (String Line : this.DumpLogs.get(player)) {
            file.addLine(Line);
        }
        file.write();
        return file.getName();
    }
}

