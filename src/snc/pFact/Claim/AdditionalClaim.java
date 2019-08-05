package snc.pFact.Claim;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import snc.pFact.Claim.Upgrade.ClaimUpgrade;
import snc.pFact.Claim.Upgrade.GainMultiplierUpgrade;

public abstract class AdditionalClaim extends Claim {

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        curHealth = claimData().getInt("health");
    }

    private static final long serialVersionUID = 1L;
    private transient int curHealth;
    private List<ClaimUpgrade> upgrades;

    public AdditionalClaim(int length, ItemStack claimBlock, ItemStack shard, int health) {
        super(length, claimBlock, shard);
        claimData().setObject("health", health);
    }

    @Override
    public void setup(String faction, Location center) {
        super.setup(faction, center);
        upgrades = new ArrayList<ClaimUpgrade>();
    }

    public int getMaxHealth() {
        return (int) Math.floor(getHealthMultiplier()) * claimData().getInt("health");
    }

    public int curHealth() {
        return curHealth;
    }

    public void setCurHealth(int curHealth) {
        this.curHealth = curHealth;
    }

    public double getHealthMultiplier() {
        double multiplier = 1;
        for (GainMultiplierUpgrade upg : getUpgradesByType(GainMultiplierUpgrade.class)) {
            multiplier *= upg.getMultiplier();
        }
        return multiplier;
    }

    @SuppressWarnings("unchecked")
    public <T extends ClaimUpgrade> List<T> getUpgradesByType(Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        for (ClaimUpgrade cu : upgrades) {
            if (clazz.isAssignableFrom(cu.getClass()))
                list.add((T) cu);
        }
        return list;
    }
}