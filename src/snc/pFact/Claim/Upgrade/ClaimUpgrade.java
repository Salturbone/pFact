package snc.pFact.Claim.Upgrade;

import org.bukkit.inventory.ItemStack;

/**
 * ClaimUpgrade
 */
public abstract class ClaimUpgrade {

    public enum ClaimUpgType {
        Multiplier, Protection;
    }

    public abstract String getName();

    public ItemStack getUpgradeItem() {
        return null;
    }

}