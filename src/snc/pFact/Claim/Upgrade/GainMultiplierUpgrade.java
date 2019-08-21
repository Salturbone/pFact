package snc.pFact.Claim.Upgrade;

import org.bukkit.inventory.ItemStack;

/**
 * GainMultiplierUpgrade
 */
public class GainMultiplierUpgrade extends ClaimUpgrade {

    private static final long serialVersionUID = -3031814342822208572L;

    public GainMultiplierUpgrade(ItemStack item, double dropChance, double multiplier) {
        super(item, dropChance);
        upgradeData().setObject("multiplier", multiplier);
    }

    public double getMultiplier() {
        return upgradeData().getDouble("multiplier");
    }

    @Override
    public String getName() {
        return "gainMultiplierUpgrade";
    }

    @Override
    public void refresh() {

    }

}