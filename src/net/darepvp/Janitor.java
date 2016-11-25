package net.darepvp;

import java.io.PrintStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.darepvp.checks.Check;
import net.darepvp.checks.combat.AttackSpeed;
import net.darepvp.checks.combat.Crits;
import net.darepvp.checks.combat.DoubleClick;
import net.darepvp.checks.combat.FastBow;
import net.darepvp.checks.combat.KillAuraA;
import net.darepvp.checks.combat.KillAuraB;
import net.darepvp.checks.combat.KillAuraC;
import net.darepvp.checks.combat.KillAuraE;
import net.darepvp.checks.combat.NoSwing;
import net.darepvp.checks.combat.Reach;
import net.darepvp.checks.combat.Regen;
import net.darepvp.checks.combat.TriggerbotA;
import net.darepvp.checks.combat.Twitch;
import net.darepvp.checks.combat.VelocityVertical;
import net.darepvp.checks.movement.Ascension;
import net.darepvp.checks.movement.BedLeave;
import net.darepvp.checks.movement.Fly;
import net.darepvp.checks.movement.Glide;
import net.darepvp.checks.movement.Jesus;
import net.darepvp.checks.movement.NewAscension;
import net.darepvp.checks.movement.NoFall;
import net.darepvp.checks.movement.Phase;
import net.darepvp.checks.movement.Speed;
import net.darepvp.checks.movement.Spider;
import net.darepvp.checks.movement.Step;
import net.darepvp.checks.movement.VClip;
import net.darepvp.checks.other.Crash;
import net.darepvp.checks.other.MorePackets;
import net.darepvp.checks.other.Sneak;
import net.darepvp.checks.other.TabComplete;
import net.darepvp.checks.other.Timer;
import net.darepvp.commands.AlertsCommand;
import net.darepvp.commands.AutobanCommand;
import net.darepvp.commands.JanitorCommand;
import net.darepvp.lag.LagCore;
import net.darepvp.packets.PacketCore;
import net.darepvp.update.UpdateType;
import net.darepvp.update.Updater;
import net.darepvp.update.events.UpdateEvent;
import net.darepvp.util.C;
import net.darepvp.util.Config;
import net.darepvp.util.TxtFile;
import net.darepvp.util.UtilActionMessage;
import net.darepvp.util.UtilServer;
import net.darepvp.xray.XrayCatcher;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Janitor
extends JavaPlugin
implements Listener {
    public static Janitor Instance;
    public static String PREFIX;
    public Updater updater;
    public PacketCore packet;
    public LagCore lag;
    public XrayCatcher xray;
    public List<Check> Checks = new ArrayList<Check>();
    public Map<UUID, Map<Check, Integer>> Violations = new HashMap<UUID, Map<Check, Integer>>();
    public Map<UUID, Map<Check, Long>> ViolationReset = new HashMap<UUID, Map<Check, Long>>();
    public List<Player> AlertsOn = new ArrayList<Player>();
    public Map<Player, Map.Entry<Check, Long>> AutoBan = new HashMap<Player, Map.Entry<Check, Long>>();
    public Map<String, Check> NamesBanned = new HashMap<String, Check>();
    Random rand = new Random();
    public Config mainConfig;
    public TxtFile autobanMessages;
    public Map<UUID, Map.Entry<Long, Vector>> LastVelocity = new HashMap<UUID, Map.Entry<Long, Vector>>();

    static {
        PREFIX = String.valueOf(C.Gray) + "[" + C.Red + C.Bold + "!" + C.Gray + "]: ";
    }

    public void onEnable() {
        Instance = this;
        this.updater = new Updater(this);
        this.packet = new PacketCore(this);
        this.lag = new LagCore(this);
        this.xray = new XrayCatcher(this);
        this.Checks.add(new Spider(this));
        this.Checks.add(new Jesus(this));
        this.Checks.add(new Ascension(this));
        this.Checks.add(new NewAscension(this));
        this.Checks.add(new Speed(this));
        this.Checks.add(new Glide(this));
        this.Checks.add(new Fly(this));
        this.Checks.add(new Regen(this));
        this.Checks.add(new BedLeave(this));
        this.Checks.add(new NoFall(this));
        this.Checks.add(new Step(this));
        this.Checks.add(new VClip(this));
        this.Checks.add(new Phase(this));
        this.Checks.add(new DoubleClick(this));
        this.Checks.add(new KillAuraA(this));
        this.Checks.add(new KillAuraB(this));
        this.Checks.add(new KillAuraC(this));
        this.Checks.add(new KillAuraE(this));
        this.Checks.add(new AttackSpeed(this));
        this.Checks.add(new NoSwing(this));
        this.Checks.add(new FastBow(this));
        this.Checks.add(new Twitch(this));
        this.Checks.add(new VelocityVertical(this));
        this.Checks.add(new Crits(this));
        this.Checks.add(new Reach(this));
        this.Checks.add(new TriggerbotA(this));
        this.Checks.add(new MorePackets(this));
        this.Checks.add(new Timer(this));
        this.Checks.add(new Sneak(this));
        this.Checks.add(new TabComplete(this));
        this.Checks.add(new Crash(this));
        for (Check check : this.Checks) {
            if (!check.isEnabled()) continue;
            this.RegisterListener(check);
        }
        this.RegisterListener(this);
        this.getCommand("alerts").setExecutor((CommandExecutor)new AlertsCommand(this));
        this.getCommand("autoban").setExecutor((CommandExecutor)new AutobanCommand(this));
        this.getCommand("janitor").setExecutor((CommandExecutor)new JanitorCommand(this));
        this.mainConfig = new Config(this, "", "config");
        this.mainConfig.setDefault("silentban", true);
        this.mainConfig.setDefault("hcfmode", false);
        this.mainConfig.setDefault("bans", 0);
        this.mainConfig.setDefault("xrayduration", 30);
        if (this.getMainConfig().getConfig().getBoolean("hcfmode")) {
            for (Check Check2 : this.Checks) {
                if (!Check2.isBannable() || Check2.hasBanTimer() || Check2 instanceof Crash) continue;
                Check2.setAutobanTimer(true);
            }
        }
        this.autobanMessages = new TxtFile(this, "", "autobanmessages");
    }

    public List<Check> getChecks() {
        return new ArrayList<Check>(this.Checks);
    }

    public Map<String, Check> getNamesBanned() {
        return new HashMap<String, Check>(this.NamesBanned);
    }

    public List<Player> getAutobanQueue() {
        return new ArrayList<Player>(this.AutoBan.keySet());
    }

    public void removeFromAutobanQueue(Player player) {
        this.AutoBan.remove((Object)player);
    }

    public void removeViolations(Player player) {
        this.Violations.remove(player.getUniqueId());
    }

    public boolean hasAlertsOn(Player player) {
        return this.AlertsOn.contains((Object)player);
    }

    public void toggleAlerts(Player player) {
        if (this.hasAlertsOn(player)) {
            this.AlertsOn.remove((Object)player);
        } else {
            this.AlertsOn.add(player);
        }
    }

    public Config getMainConfig() {
        return this.mainConfig;
    }

    public LagCore getLag() {
        return this.lag;
    }

    @EventHandler
    public void Join(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission("janitor.staff")) {
            return;
        }
        this.AlertsOn.add(e.getPlayer());
    }

    @EventHandler
    public void autobanupdate(UpdateEvent event) {
        if (!event.getType().equals((Object)UpdateType.SEC)) {
            return;
        }
        HashMap<Player, Map.Entry<Check, Long>> AutoBan = new HashMap<Player, Map.Entry<Check, Long>>(this.AutoBan);
        for (Player player : AutoBan.keySet()) {
            if (player == null || !player.isOnline()) {
                this.AutoBan.remove((Object)player);
                continue;
            }
            Long time = AutoBan.get((Object)player).getValue();
            if (System.currentTimeMillis() < time) continue;
            this.autobanOver(player);
        }
        HashMap<UUID, Map<Check, Long>> ViolationResets = new HashMap<UUID, Map<Check, Long>>(this.ViolationReset);
        for (UUID uid : ViolationResets.keySet()) {
            if (!this.Violations.containsKey(uid)) continue;
            HashMap<Check, Long> Checks = new HashMap<Check, Long>(ViolationResets.get(uid));
            for (Check check : Checks.keySet()) {
                Long time = Checks.get(check);
                if (System.currentTimeMillis() < time) continue;
                this.ViolationReset.get(uid).remove(check);
                this.Violations.get(uid).remove(check);
            }
        }
    }

    public Integer getViolations(Player player, Check check) {
        if (this.Violations.containsKey(player.getUniqueId())) {
            return this.Violations.get(player.getUniqueId()).get(check);
        }
        return 0;
    }

    public Map<Check, Integer> getViolations(Player player) {
        if (this.Violations.containsKey(player.getUniqueId())) {
            return new HashMap<Check, Integer>(this.Violations.get(player.getUniqueId()));
        }
        return null;
    }

    public void addViolation(Player player, Check check) {
        Map map = new HashMap<Check, Integer>();
        if (this.Violations.containsKey(player.getUniqueId())) {
            map = this.Violations.get(player.getUniqueId());
        }
        if (!map.containsKey(check)) {
            map.put((Check)check, 1);
        } else {
            map.put((Check)check, (Integer)map.get(check) + 1);
        }
        this.Violations.put(player.getUniqueId(), map);
    }

    public void removeViolations(Player player, Check check) {
        if (this.Violations.containsKey(player.getUniqueId())) {
            this.Violations.get(player.getUniqueId()).remove(check);
        }
    }

    public void setViolationResetTime(Player player, Check check, long time) {
        Map map = new HashMap<Check, Long>();
        if (this.ViolationReset.containsKey(player.getUniqueId())) {
            map = this.ViolationReset.get(player.getUniqueId());
        }
        map.put(check, time);
        this.ViolationReset.put(player.getUniqueId(), map);
    }

    public void autobanOver(Player player) {
        HashMap<Player, Map.Entry<Check, Long>> AutoBan = new HashMap<Player, Map.Entry<Check, Long>>(this.AutoBan);
        if (AutoBan.containsKey((Object)player)) {
            this.banPlayer(player, AutoBan.get((Object)player).getKey());
            this.AutoBan.remove((Object)player);
        }
    }

    public void autoban(Check check, Player player) {
        if (this.lag.getTPS() < 17.0) {
            return;
        }
        if (check.hasBanTimer()) {
            if (this.AutoBan.containsKey((Object)player)) {
                return;
            }
            this.AutoBan.put(player, new AbstractMap.SimpleEntry<Check, Long>(check, System.currentTimeMillis() + 15000));
            System.out.println("[" + player.getUniqueId().toString() + "] " + player.getName() + " will be banned in 15s for " + check.getName() + ".");
            UtilActionMessage msg = new UtilActionMessage();
            msg.addText(PREFIX);
            msg.addText(String.valueOf(C.Reset) + player.getName()).addHoverText(String.valueOf(C.Yellow) + "Click to teleport to " + C.Aqua + player.getName() + C.Yellow + ".").setClickEvent(UtilActionMessage.ClickableType.RunCommand, "/tp " + player.getName());
            msg.addText(String.valueOf(C.Gray) + " will be");
            msg.addText(String.valueOf(C.Red) + " banned");
            msg.addText(String.valueOf(C.Gray) + " for");
            msg.addText(String.valueOf(C.Blue) + " " + check.getName());
            msg.addText(String.valueOf(C.Gray) + " in 15s. ");
            msg.addText(String.valueOf(C.DGreen) + C.Bold + "[F]").addHoverText(String.valueOf(C.Yellow) + "Click to freeze " + C.Aqua + player.getName() + C.Yellow + ".").setClickEvent(UtilActionMessage.ClickableType.RunCommand, "/freeze " + player.getName());
            msg.addText(" ");
            msg.addText(String.valueOf(C.Red) + C.Bold + "[C]").addHoverText(String.valueOf(C.Yellow) + "Click to cancel " + C.Aqua + player.getName() + C.Yellow + "'s autoban.").setClickEvent(UtilActionMessage.ClickableType.RunCommand, "/autoban cancel " + player.getName());
            msg.addText(" ");
            msg.addText(String.valueOf(C.DRed) + C.Bold + "[B]").addHoverText(String.valueOf(C.Yellow) + "Click to ban " + C.Aqua + player.getName() + C.Yellow + ".").setClickEvent(UtilActionMessage.ClickableType.RunCommand, "/autoban ban " + player.getName());
            Player[] arrplayer = UtilServer.getPlayers();
            int n = arrplayer.length;
            int n2 = 0;
            while (n2 < n) {
                Player playerplayer = arrplayer[n2];
                if (playerplayer.hasPermission("janitor.staff")) {
                    msg.sendToPlayer(playerplayer);
                }
                ++n2;
            }
        } else {
            Bukkit.getServer().broadcastMessage("hi 3");
            this.banPlayer(player, check);
        }
    }

    public void banPlayer(final Player player, Check check) {
        this.NamesBanned.put(player.getName(), check);
        final boolean silentban = this.mainConfig.getConfig().getBoolean("silentban");
        this.Violations.remove(player.getUniqueId());
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this, new Runnable(){

            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "ban " + (silentban ? "-s " : "") + player.getName() + " [Janitor] Cheating");
            }
        }, 10);
        this.mainConfig.getConfig().set("bans", (Object)(this.mainConfig.getConfig().getInt("bans") + 1));
        this.mainConfig.save();
        ArrayList<String> a = new ArrayList<String>();
        this.autobanMessages.readTxtFile();
        Iterator<String> iterator = this.autobanMessages.getLines().iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            line = line.replaceAll("%player%", player.getName());
            line = line.replaceAll("&", C.Split);
            a.add(line);
        }
        if (a.size() > 0) {
            Bukkit.getServer().broadcastMessage((String)a.get(this.rand.nextInt(a.size())));
        }
    }

    public void alert(String message) {
        for (Player playerplayer : this.AlertsOn) {
            playerplayer.sendMessage(String.valueOf(PREFIX) + message);
        }
    }

    public /* varargs */ void logCheat(Check check, Player player, String hoverabletext, String ... identifiers) {
        String a = "";
        if (identifiers != null) {
            String[] arrstring = identifiers;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String b = arrstring[n2];
                a = String.valueOf(a) + " (" + b + ")";
                ++n2;
            }
        }
        this.addViolation(player, check);
        Integer violations = this.getViolations(player, check);
        UtilActionMessage msg = new UtilActionMessage();
        msg.addText(PREFIX);
        msg.addText(String.valueOf(C.Reset) + player.getName()).addHoverText(String.valueOf(C.Yellow) + "Click to teleport to " + C.Aqua + player.getName() + C.Yellow + ".").setClickEvent(UtilActionMessage.ClickableType.RunCommand, "/tp " + player.getName());
        msg.addText(String.valueOf(C.Gray) + " failed " + (check.isJudgmentDay() ? "JD check " : ""));
        UtilActionMessage.AMText CheckText = msg.addText(String.valueOf(C.Blue) + check.getName());
        if (hoverabletext != null) {
            CheckText.addHoverText(hoverabletext);
        }
        msg.addText(String.valueOf(C.Gray) + a + C.Gray + " ");
        msg.addText(String.valueOf(C.Gray) + "[" + C.Red + violations + C.Gray + " VL]");
        if (violations % check.getViolationsToNotify() == 0) {
            for (Player playerplayer : this.AlertsOn) {
                if (check.isJudgmentDay() && !playerplayer.hasPermission("janitor.admin")) continue;
                msg.sendToPlayer(playerplayer);
            }
        }
        System.out.println("[" + player.getUniqueId().toString() + "] " + player.getName() + " failed " + (check.isJudgmentDay() ? "JD check " : "") + check.getName() + a + " [" + violations + " VL]");
        if (check.isJudgmentDay()) {
            return;
        }
        this.setViolationResetTime(player, check, System.currentTimeMillis() + check.getViolationResetTime());
        if (violations > check.getMaxViolations() && check.isBannable()) {
            this.autoban(check, player);
        }
    }

    public void onDisable() {
        this.updater.Disable();
    }

    public void RegisterListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, (Plugin)this);
    }

    public Map<UUID, Map.Entry<Long, Vector>> getLastVelocity() {
        return new HashMap<UUID, Map.Entry<Long, Vector>>(this.LastVelocity);
    }

    @EventHandler
    public void Velocity(PlayerVelocityEvent event) {
        this.LastVelocity.put(event.getPlayer().getUniqueId(), new AbstractMap.SimpleEntry<Long, Vector>(System.currentTimeMillis(), event.getVelocity()));
    }

    @EventHandler
    public void Update(UpdateEvent event) {
        if (!event.getType().equals((Object)UpdateType.TICK)) {
            return;
        }
        for (UUID uid : this.getLastVelocity().keySet()) {
            Player player = this.getServer().getPlayer(uid);
            if (player == null || !player.isOnline()) {
                this.LastVelocity.remove(uid);
                continue;
            }
            Vector velocity = this.getLastVelocity().get(uid).getValue();
            Long time = this.getLastVelocity().get(uid).getKey();
            if (time + 500 > System.currentTimeMillis()) continue;
            double velY = velocity.getY() * velocity.getY();
            double Y = player.getVelocity().getY() * player.getVelocity().getY();
            if (Y < 0.02) {
                this.LastVelocity.remove(uid);
                continue;
            }
            if (Y <= velY * 3.0) continue;
            this.LastVelocity.remove(uid);
        }
    }

    @EventHandler
    public void Kick(PlayerKickEvent event) {
        if (event.getReason().equals("Flying is not enabled on this server")) {
            this.alert(String.valueOf(C.Gray) + event.getPlayer().getName() + " was kicked for flying");
        }
    }

}

