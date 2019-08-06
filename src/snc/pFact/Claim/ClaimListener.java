package snc.pFact.Claim;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import snc.pFact.Claim.Events.BreakInsideClaimEvent;
import snc.pFact.Claim.Events.InteractClaimEvent;
import snc.pFact.Claim.Events.InteractInsideClaimEvent;
import snc.pFact.Claim.Events.PlaceClaimEvent;
import snc.pFact.Claim.Events.PlaceInsideClaimEvent;
import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.obj.cl.B_FactionMember;
import snc.pFact.obj.cl.B_Player;
import snc.pFact.obj.cl.B_Player.Rank;
import snc.pFact.utils.Square3D;

/**
 * ClaimListener
 */
public class ClaimListener implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent ev) {
        Player p = ev.getPlayer();
        Location loc = ev.getBlock().getLocation();
        ItemStack is = ev.getItemInHand();
        Claim cl = ClaimFactory.getClaim(ev.getBlock().getLocation());
        // placing claim
        Claim isCl = ClaimFactory.getClaimFromStack(is);
        if (isCl != null) {
            if (cl == null) {
                B_Faction bf = DataIssues.players.getLoaded(p.getUniqueId()).getF();
                if (bf == null) {
                    p.sendMessage("you need a faction to claim a area");
                    ev.setCancelled(true);
                    return;
                }

                B_FactionMember bfm = bf.getPlayer(p.getUniqueId());
                String isFact = ClaimFactory.getClaimStackFaction(is);
                if (isFact.equalsIgnoreCase("null") && !bf.getName().equals(isFact)) {
                    p.sendMessage("this claim item is not created in your faction");
                    ev.setCancelled(true);
                    return;
                }
                Claim createdClaim;
                if (isCl instanceof MainClaim) {
                    if (bfm.rank() != Rank.Founder) {
                        p.sendMessage("you need to be founder");
                        ev.setCancelled(true);
                        return;
                    }
                    if (bf.GetMainClaim() != null) {
                        p.sendMessage("your faction already has main claim");
                        ev.setCancelled(true);
                        return;
                    }
                    if (!ClaimFactory.canPlaceMainClaim(loc) && !p.isOp()) {
                        p.sendMessage("too close or too far away from other factions");
                        ev.setCancelled(true);
                        return;
                    }
                    createdClaim = ClaimFactory.createClaim(isCl, bf.getName(), loc);
                }
                // PLacing other claims
                else {
                    if (bfm.rank() == Rank.Player) {
                        p.sendMessage("you don't have enough permission");
                        ev.setCancelled(true);
                        return;
                    }
                    createdClaim = ClaimFactory.createClaim(isCl, bf.getName(), loc);
                    Square3D sq = bf.getMaxClaimArea();
                    if (!createdClaim.canCreate()) {
                        p.sendMessage("already has too many claims");
                        ev.setCancelled(true);
                        return;
                    }
                    if (!createdClaim.getSquare().IsInsideof(sq)) {
                        p.sendMessage("not inside max claim area");
                        ev.setCancelled(true);
                        return;
                    }
                    for (Claim cla : bf.getAllClaims()) {
                        if (cla.getSquare().doCollide(createdClaim.getSquare())) {
                            p.sendMessage("too close to another claim");
                            ev.setCancelled(true);
                            return;
                        }
                    }
                }
                if (createdClaim == null) {
                    ev.setCancelled(true);
                    return;
                }
                PlaceClaimEvent pce = new PlaceClaimEvent(isCl, p, loc, bf, bfm, ev);
                Bukkit.getPluginManager().callEvent(pce);
                ev.setCancelled(pce.isCancelled());
                if (!pce.isCancelled()) {
                    for (double x = -2; x <= 2; x++) {
                        for (int z = -2; z <= 2; z++) {

                            for (int y = -2; y <= 2; y++) {
                                if (x == 0 && z == 0 && y <= 0)
                                    continue;
                                Location loc2 = loc.clone().add(x, y, z);
                                if (!loc2.getBlock().breakNaturally())
                                    loc2.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                    if (createdClaim instanceof MainClaim) {
                        bf.setMainClaim((MainClaim) createdClaim);
                        p.sendMessage("placed main claim " + createdClaim.getName());
                    } else {
                        bf.getAdditionalClaims().add((AdditionalClaim) createdClaim);
                        p.sendMessage("placed add claim " + createdClaim.getName());
                    }

                }
                return;
            } else {
                p.sendMessage("can't put another claim too close to another");
                ev.setCancelled(true);
                return;
            }
        }
        if (cl == null)
            return;
        PlaceInsideClaimEvent pice = new PlaceInsideClaimEvent(cl, p, loc, ev);
        Bukkit.getPluginManager().callEvent(pice);
        ev.setCancelled(pice.isCancelled());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlaceInsideClaim(PlaceInsideClaimEvent ev) {
        Player p = ev.getPlayer();
        B_Player bp = DataIssues.players.get(p.getUniqueId());
        B_Faction bf = bp.getF();
        Claim cl = ev.getClaim();
        if (bf != null && cl.getFaction().equals(bf)) {
            B_FactionMember bfm = bf.getPlayer(bp.uuid());
            if (cl instanceof MainClaim) {
                if (bfm.rank() == Rank.Player) {
                    p.sendMessage("you don't have enough permission");
                    ev.setCancelled(true);
                    return;
                }

            }
            if (!cl.canPlace(p, ev.getLocation())) {
                p.sendMessage("can't place block too close to a claim block");
                ev.setCancelled(true);
                return;
            }

            return;
        }
        if (p.isOp())
            return;
        p.sendMessage("can't place block on a faction's claim");

        ev.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent ev) {
        Player p = ev.getPlayer();
        Location loc = ev.getBlock().getLocation();
        Claim cl = ClaimFactory.getClaim(ev.getBlock().getLocation());
        if (cl == null)
            return;
        if (cl.getCenterBlock().equals(loc)) {
            ev.setCancelled(true);
            return;
        }
        BreakInsideClaimEvent bice = new BreakInsideClaimEvent(cl, p, loc, ev);
        Bukkit.getPluginManager().callEvent(bice);
        ev.setCancelled(bice.isCancelled());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreakInsideClaim(BreakInsideClaimEvent ev) {
        Player p = ev.getPlayer();
        B_Player bp = DataIssues.players.get(p.getUniqueId());
        B_Faction bf = bp.getF();
        Claim cl = ev.getClaim();
        if (bf != null && bf.equals(cl.getFaction())) {
            if (cl instanceof MainClaim) {
                B_FactionMember bfm = bf.getPlayer(bp.uuid());
                if (bfm.rank() == Rank.Player) {
                    p.sendMessage("you don't have enough permission");
                    ev.setCancelled(true);
                    return;
                }

            }
            if (!cl.canBreak(p, ev.getLocation())) {
                p.sendMessage("trying to break too close to claim block");
                ev.setCancelled(true);
                return;
            }
            return;
        }
        if (p.isOp())
            return;
        p.sendMessage("can't break block on a faction's claim");
        ev.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent ev) {
        Block bl = ev.getClickedBlock();
        Player p = ev.getPlayer();
        Location loc = bl.getLocation();
        Claim cl = ClaimFactory.getClaim(loc);
        if (cl == null)
            return;
        if (cl.getCenterBlock().equals(loc)) {
            Bukkit.broadcastMessage("interact claim");
            InteractClaimEvent ice = new InteractClaimEvent(cl, p, loc, ev);
            ev.setCancelled(true);
            Bukkit.getPluginManager().callEvent(ice);
            return;
        }
        Bukkit.broadcastMessage("interact inside claim");
        InteractInsideClaimEvent iice = new InteractInsideClaimEvent(cl, p, loc, ev);
        Bukkit.getPluginManager().callEvent(iice);
        ev.setCancelled(iice.isCancelled());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onClaimInteract(InteractClaimEvent ev) {
        Player p = ev.getPlayer();
        B_Player bp = DataIssues.players.get(p.getUniqueId());
        B_Faction bf = bp.getF();
        Claim cl = ev.getClaim();
        if (bf != null && cl.getFaction().equals(bf)) {
            B_FactionMember bfm = bf.getPlayer(bp.uuid());
            if (bfm.rank() == Rank.Player || (bfm.rank() == Rank.Moderator && cl instanceof MainClaim)) {
                p.sendMessage("you don't have enough permission");
                return;
            }
            if (!cl.canInteract(p, ev.getLocation())) {
                p.sendMessage("can't interact");
                return;
            }
            return;
        }
        p.sendMessage("can't interact with others claim blocks outside raids");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteractInsideClaim(InteractInsideClaimEvent ev) {
        Player p = ev.getPlayer();
        B_Player bp = DataIssues.players.get(p.getUniqueId());
        B_Faction bf = bp.getF();
        Claim cl = ev.getClaim();
        if (bf != null && cl.getFaction().equals(bf)) {
            return;
        }
        if (p.isOp())
            return;
        p.sendMessage("can't interact with blocks in claims");
        ev.setCancelled(true);
    }

}