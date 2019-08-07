package snc.pFact.Claim.AdditionalClaims;

import org.bukkit.inventory.ItemStack;

import snc.pFact.Claim.AdditionalClaim;
import snc.pFact.Claim.Upgrade.GainMultiplierUpgrade;
import snc.pFact.utils.Gerekli;

/**
 * XPClaim
 */
public class XPClaim extends AdditionalClaim {

    private static final long serialVersionUID = 1L;

    private transient int timer, random;

    public XPClaim(int length, ItemStack claimBlock, ItemStack shard, int health, double multiplier) {
        super(length, claimBlock, shard, health);
        claimData().setObject("multiplier", multiplier);
    }

    @Override
    public void update() {
        if (random == 0) {
            random = 30 + Gerekli.random.nextInt(15) * 2;
        }
        if (timer >= random) {
            getFaction().addXP(((double) timer / random) * getFaction().toGainXP(false) * getXPMultiplier());
            timer = 0;
        }
        timer++;
    }

    @Override
    public String getName() {
        return "xpClaim";
    }

    public double getXPMultiplier() {
        double multiplier = claimData().getDouble("multiplier");
        for (GainMultiplierUpgrade upg : getUpgradesByType(GainMultiplierUpgrade.class)) {
            multiplier *= upg.getMultiplier();
        }
        return multiplier;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}