package snc.pFact.Claim.Upgrade;

import org.bukkit.inventory.ItemStack;

/**
 * MultiplierUpgrade
 */
public class HealthMultiplierUpgrade extends ClaimUpgrade {

    private static final long serialVersionUID = 1L;

    public HealthMultiplierUpgrade(ItemStack item, double multiplier) {
        super(item);
        upgradeData().setObject("multiplier", multiplier);
    }

    public double getMultiplier() {
        return upgradeData().getDouble("multiplier");
    }

    @Override
    public String getName() {
        return "healthMultiplierUpgrade";
    }

    @Override
    public void refresh() {

    }

}