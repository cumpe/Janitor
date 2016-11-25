package net.darepvp.xray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.util.C;
import net.darepvp.util.Config;
import net.darepvp.util.UtilTime;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class XrayCatcher
implements Listener,
CommandExecutor {
    public Janitor janitor;
    public List<Player> xrayCatcher = new ArrayList<Player>();
    public Map<UUID, Long> lastPatch = new HashMap<UUID, Long>();

    public XrayCatcher(Janitor janitor) {
        this.janitor = janitor;
        this.janitor.getCommand("xray").setExecutor((CommandExecutor)this);
        this.janitor.RegisterListener(this);
    }

    @EventHandler
    public void breakblock(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getBlock().getType() != Material.DIAMOND_ORE) {
            return;
        }
        if (event.getBlock().hasMetadata("AlreadyDone")) {
            return;
        }
        Player player = event.getPlayer();
        if (this.lastPatch.containsKey(player.getUniqueId())) {
            Long time = this.lastPatch.get(player.getUniqueId());
            Long maxtime = (long)this.janitor.getMainConfig().getConfig().getInt("xrayduration") * 1000;
            if (UtilTime.nowlong() - time < maxtime) {
                for (Player staff : this.xrayCatcher) {
                    staff.sendMessage(String.valueOf(Janitor.PREFIX) + C.Reset + player.getName() + C.Gray + " might be using " + C.Blue + "Xray");
                }
            }
        }
        int x = -7;
        while (x < 7) {
            int y = -7;
            while (y < 7) {
                int z = -7;
                while (z < 7) {
                    Location location = event.getBlock().getLocation().add((double)x, (double)y, (double)z);
                    if (location.getBlock().getType() == Material.DIAMOND_ORE) {
                        location.getBlock().setMetadata("AlreadyDone", (MetadataValue)new FixedMetadataValue((Plugin)this.janitor, (Object)true));
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        this.lastPatch.put(player.getUniqueId(), UtilTime.nowlong());
    }

    @EventHandler
    public void blockplace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getBlock().getType() != Material.DIAMOND_ORE) {
            return;
        }
        event.getBlock().setMetadata("AlreadyDone", (MetadataValue)new FixedMetadataValue((Plugin)this.janitor, (Object)true));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String cmdname, String[] args) {
        if (!sender.hasPermission("janitor.staff")) {
            sender.sendMessage(String.valueOf(C.Red) + "No permission.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command!");
            return true;
        }
        Player player = (Player)sender;
        if (this.xrayCatcher.contains((Object)player)) {
            sender.sendMessage(String.valueOf(Janitor.PREFIX) + C.Gray + "Xray alerts " + C.Red + "OFF");
            this.xrayCatcher.remove((Object)player);
        } else {
            sender.sendMessage(String.valueOf(Janitor.PREFIX) + C.Gray + "Xray alerts " + C.Green + "ON");
            this.xrayCatcher.add(player);
        }
        return true;
    }
}

