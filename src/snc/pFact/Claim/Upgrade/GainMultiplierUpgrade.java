package snc.pFact.Claim.Upgrade;

import org.bukkit.inventory.ItemStack;

/**
 * GainMultiplierUpgrade
 */
public class GainMultiplierUpgrade extends ClaimUpgrade {

    private static final long serialVersionUID = 1L;

    public GainMultiplierUpgrade(ItemStack item, double multiplier) {
        super(item);
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