package snc.pFact.Claim.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

import snc.pFact.Claim.Claim;

/**
 * InteractInsideClaimEvent
 */
public class InteractInsideClaimEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancel = false;

    private Claim cl;
    private Player pl;
    private Location loc;
    private PlayerInteractEvent ev;

    public InteractInsideClaimEvent(Claim cl, Player pl, Location loc, PlayerInteractEvent ev) {
        this.cl = cl;
        this.pl = pl;
        this.loc = loc;
        this.ev = ev;
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

    public Player getPlayer() {
        return pl;
    }

    public Location getLocation() {
        return loc;
    }

    public PlayerInteractEvent getInteractEvent() {
        return ev;
    }

}