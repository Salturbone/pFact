package snc.pFact.Claim.AdditionalClaims;

import org.bukkit.inventory.ItemStack;

import snc.pFact.Claim.Upgrade.GainMultiplierUpgrade;
import snc.pFact.utils.Gerekli;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

/**
 * XPClaim
 */
public class XPClaim extends UpgAddClaim {

    private static final long serialVersionUID = 1L;

    private transient int timer, random;

    public XPClaim(int length, ItemStack claimBlock, ItemStack shard, Color color, int health) {
        super(length, claimBlock, shard, color, health);
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
        double multiplier = 1;
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