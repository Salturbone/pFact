package snc.pFact.Claim.Upgrade;

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;

import snc.pFact.Claim.Claim;
import snc.pFact.Claim.ClaimFactory;
import snc.pFact.utils.ZSIGN;

/**
 * ClaimUpgrade
 */
public abstract class ClaimUpgrade implements Serializable, Cloneable {

    private static final long serialVersionUID = -4826842068790315783L;

    public abstract String getName();

    public ClaimUpgrade(ItemStack item, double dropChance) {
        UpgradeData upData = new UpgradeData();
        ClaimFactory.upgradeDatas.put(getName(), upData);
        upgradeData().setItemStack("upgradeItem", item);
        upgradeData().setObject("dropChance", dropChance);
    }

    public void setup(Claim cl) {

    }

    public abstract void refresh();

    public ItemStack getUpgradeItem() {
        return ZSIGN.imzalaZ("claimUpgrade", getName(), upgradeData().getItemStack("upgradeItem"));
    }

    public UpgradeData upgradeData() {
        return ClaimFactory.upgradeDatas.get(getName());
    }

    @Override
    public ClaimUpgrade clone() {
        try {
            return (ClaimUpgrade) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getUpgradeDropChance() {
        return upgradeData().getDouble("dropChance");
    }
}