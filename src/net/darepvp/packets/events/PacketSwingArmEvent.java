/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.darepvp.packets.events;

import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketSwingArmEvent
extends Event {
    public Player Player;
    public PacketEvent Event;
    private static final HandlerList handlers = new HandlerList();

    public PacketSwingArmEvent(PacketEvent Event2, Player Player2) {
        this.Player = Player2;
        this.Event = Event2;
    }

    public PacketEvent getPacketEvent() {
        return this.Event;
    }

    public Player getPlayer() {
        return this.Player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

