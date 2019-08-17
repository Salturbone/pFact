package snc.pFact.Claim.AdditionalClaims;

import org.bukkit.inventory.ItemStack;

import snc.pFact.Claim.Upgrade.GainMultiplierUpgrade;
import snc.pFact.utils.Gerekli;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

/**
 * XPClaim
 */
public class XPClaim extends UpgAddClaim {

    private static final long serialVersionUID = -6508729199203042348L;
    private transient int timer, random;

    public XPClaim(int length, ItemStack claimBlock, ItemStack shard, Color color, long craftTime, int health) {
        super(length, claimBlock, shard, color, craftTime, health);
    }

    @Override
    public void update() {
        if (random == 0) {
            random = 30 + Gerekli.random.nextInt(15) * 2;
        }
        if (timer >= random * 4) {
            getFaction().addXP(((double) (random) / 60) * toGainXP());
            timer = 0;
        }
        timer++;
    }

    public double toGainXP() {
        int on = getFaction().getOnlinePlayers().size();
        if (on >= 1) {
            return 3 * getFaction().getLevelBlock() * ((100 + (2 * (double) on)) / 100) * getXPMultiplier();
        } else {
            return getFaction().getLevelBlock() * getXPMultiplier() / 2;
        }
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