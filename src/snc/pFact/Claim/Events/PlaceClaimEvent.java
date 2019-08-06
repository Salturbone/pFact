package snc.pFact.Claim.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;

import snc.pFact.Claim.Claim;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.obj.cl.B_FactionMember;

/**
 * PlaceClaimEvent
 */
public class PlaceClaimEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancel = false;

    private Claim cl;
    private Player pl;
    private Location loc;
    private B_Faction bf;
    private B_FactionMember bfm;
    private BlockPlaceEvent ev;

    public PlaceClaimEvent(Claim cl, Player pl, Location loc, B_Faction bf, B_FactionMember bfm, BlockPlaceEvent ev) {
        this.cl = cl;
        this.pl = pl;
        this.loc = loc;
        this.bf = bf;
        this.bfm = bfm;
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

    public B_Faction getFaction() {
        return bf;
    }

    public B_FactionMember getFactionMember() {
        return bfm;
    }

    public BlockPlaceEvent getBlockPlaceEvent() {
        return ev;
    }

}