package snc.pFact.Claim;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.obj.cl.B_FactionMember;

/**
 * ClaimListener
 */
public class ClaimListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent ev) {
        Player p = ev.getPlayer();
        ItemStack is = ev.getItemInHand();
        Claim cl = ClaimFactory.getClaim(ev.getBlock().getLocation());
        if (cl == null) {
            Claim isCl = ClaimFactory.getClaimFromStack(is);
            if (isCl instanceof MainClaim) {
                B_Faction bf = DataIssues.players.getLoaded(p.getUniqueId()).getF();
                B_FactionMember bfm = bf.getFounder();
                if (!bfm.uuid().equals(p.getUniqueId())) {
                    // TODO not founder;
                    ev.setCancelled(true);
                    return;
                }
                if (bf.GetMainClaim() != null) {
                    // TODO already has main claim
                    ev.setCancelled(true);
                    return;
                }

            }
            return;
        }

    }

}