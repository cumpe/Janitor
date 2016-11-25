/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.darepvp.update.events;

import net.darepvp.update.UpdateType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpdateEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private UpdateType Type;

    public UpdateEvent(UpdateType Type) {
        this.Type = Type;
    }

    public UpdateType getType() {
        return this.Type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

