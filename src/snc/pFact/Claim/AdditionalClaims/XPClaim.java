package snc.pFact.Claim.AdditionalClaims;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import snc.pFact.Claim.AdditionalClaim;
import snc.pFact.obj.cl.B_Faction;

/**
 * XPClaim
 */
public class XPClaim extends AdditionalClaim {

    private static final long serialVersionUID = 1L;

    public XPClaim(Location center, int length, String faction, int health) {
        super(center, length, faction, health);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void update() {

    }

    @Override
    public String getName() {
        return "xpClaim";
    }

    @Override
    public boolean canBreak(Player p, Location loc) {
        return false;
    }

    @Override
    public boolean canPlace(Player p, Location loc) {
        return false;
    }

    @Override
    public boolean canInteract(Player p, Location loc) {
        return false;
    }

    @Override
    public Recipe getRecipe() {
        return null;
    }

    @Override
    protected ItemStack doGetClaimItem(B_Faction fact) {
        return null;
    }
}