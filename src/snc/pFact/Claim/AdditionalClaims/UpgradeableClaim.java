package snc.pFact.Claim.AdditionalClaims;

import java.util.ArrayList;
import java.util.List;

import snc.pFact.Claim.Upgrade.ClaimUpgrade;

/**
 * UpgradeableClaim
 */
public interface UpgradeableClaim {

    public List<ClaimUpgrade> getUpgrades();

    @SuppressWarnings("unchecked")
    public default <T extends ClaimUpgrade> List<T> getUpgradesByType(Class<T> clazz) {
        List<T> upgrades = new ArrayList<T>();
        for (ClaimUpgrade cu : getUpgrades()) {
            if (clazz.isAssignableFrom(cu.getClass()))
                upgrades.add((T) cu);
        }
        return upgrades;

    }
}