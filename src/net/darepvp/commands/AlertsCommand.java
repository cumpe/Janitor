/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package net.darepvp.commands;

import net.darepvp.Janitor;
import net.darepvp.util.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertsCommand
implements CommandExecutor {
    private Janitor janitor;

    public AlertsCommand(Janitor janitor) {
        this.janitor = janitor;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player to run this command!");
            return true;
        }
        Player player = (Player)sender;
        if (!player.hasPermission("janitor.staff")) {
            sender.sendMessage(String.valueOf(C.Red) + "No permission.");
            return true;
        }
        if (this.janitor.hasAlertsOn(player)) {
            this.janitor.toggleAlerts(player);
            player.sendMessage(String.valueOf(Janitor.PREFIX) + "Alerts toggled " + C.Red + "OFF");
        } else {
            this.janitor.toggleAlerts(player);
            player.sendMessage(String.valueOf(Janitor.PREFIX) + "Alerts toggled " + C.Green + "ON");
        }
        return true;
    }
}

