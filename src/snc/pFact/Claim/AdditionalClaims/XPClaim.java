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
public class XPClaim extends AdditionalClaim implements MultiplierClaim {

    private static final long serialVersionUID = 1L;

    private double multiplier;

    public XPClaim(Location center, int length, String faction, int health, double multiplier) {
        super(center, length, faction, health);
        this.multiplier = multiplier;
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

    @Override
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public double getMultiplier() {
        return multiplier;
    }
}