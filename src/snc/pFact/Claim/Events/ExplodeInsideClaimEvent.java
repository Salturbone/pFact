package snc.pFact.Claim.Events;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import snc.pFact.Claim.Claim;

/**
 * ExplodeInsideClaimEvent
 */
public class ExplodeInsideClaimEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancel = false;

    private Claim cl;
    private Location loc;

    public ExplodeInsideClaimEvent(Claim cl, Location loc) {
        this.cl = cl;
        this.loc = loc;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Claim getClaim() {
        return cl;
    }

    public Location getLocation() {
        return loc;
    }

}