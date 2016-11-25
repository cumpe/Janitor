/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.darepvp.packets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketEntityActionEvent
extends Event {
    public int Action;
    public Player Player;
    private static final HandlerList handlers = new HandlerList();

    public PacketEntityActionEvent(Player Player2, int Action) {
        this.Player = Player2;
        this.Action = Action;
    }

    public Player getPlayer() {
        return this.Player;
    }

    public int getAction() {
        return this.Action;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public class PlayerAction {
    }

}

