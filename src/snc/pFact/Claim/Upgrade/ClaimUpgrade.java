package snc.pFact.Claim.Upgrade;

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;

import snc.pFact.utils.SerItem;
import snc.pFact.utils.ZSIGN;

/**
 * ClaimUpgrade
 */
public abstract class ClaimUpgrade implements Serializable {

    private static final long serialVersionUID = 1L;
    private SerItem item;

    public abstract String getName();

    public void setItem(ItemStack is) {
        this.item = new SerItem(is);
    }

    public ItemStack getUpgradeItem() {
        return ZSIGN.imzalaZ("claimUpgrade", getName(), item.getItemStack());
    }

}