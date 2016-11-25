/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerChatTabCompleteEvent
 */
package net.darepvp.checks.other;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.darepvp.Janitor;
import net.darepvp.checks.Check;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

public class TabComplete
extends Check {
    public Map<UUID, Long> TabComplete = new HashMap<UUID, Long>();

    public TabComplete(Janitor janitor) {
        super("TabComplete", "TabComplete", janitor);
        this.setBannable(false);
    }

    @EventHandler
    public void TabCompleteEvent(PlayerChatTabCompleteEvent e) {
        String[] Args = e.getChatMessage().split(" ");
        Player Player2 = e.getPlayer();
        if (Args[0].startsWith(".") && Args[0].substring(1, 2).equalsIgnoreCase("/")) {
            return;
        }
        if (Args.length > 1 && (Args[0].startsWith(".") || Args[0].startsWith("-") || Args[0].startsWith("#") || Args[0].startsWith("*"))) {
            if (this.TabComplete.containsKey(Player2.getUniqueId()) && System.currentTimeMillis() < this.TabComplete.get(Player2.getUniqueId()) + 1000) {
                return;
            }
            this.getJanitor().logCheat(this, Player2, null, e.getChatMessage());
            this.TabComplete.put(Player2.getUniqueId(), System.currentTimeMillis());
        }
    }
}

