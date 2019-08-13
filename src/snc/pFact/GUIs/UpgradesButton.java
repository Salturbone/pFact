package snc.pFact.GUIs;

import org.bukkit.inventory.ItemStack;

import snc.pFact.Claim.ClaimFactory;
import snc.pFact.Claim.AdditionalClaims.UpgAddClaim;
import snc.pFact.Claim.Upgrade.ClaimUpgrade;

/**
 * UpgradesButton
 */
public class UpgradesButton extends PlaceableButtonNode {

    UpgAddClaim cl;

    public UpgradesButton(UpgAddClaim cl) {
        super();
        this.cl = cl;
    }

    @Override
    public ItemStack noItem() {
        return ClaimFactory.noUpgradeItem.getItemStack();
    }

    @Override
    public ItemStack getItem() {
        ClaimUpgrade cu = cl.getUpgrades().get(0);
        return cu.getUpgradeItem();
    }

    @Override
    public ItemStack giveItem() {
        ClaimUpgrade cu = cl.getUpgrades().get(0);
        return cu.getUpgradeItem();
    }

    @Override
    public boolean canPlace(ItemStack is) {
        ClaimUpgrade cu = ClaimFactory.getUpgradeFromItemStack(is);
        if (cu == null) {
            return false;
        }
        return true;
    }

    @Override
    public void doPlace(ItemStack is) {
        ClaimUpgrade cu = ClaimFactory.getUpgradeFromItemStack(is);
        cl.getUpgrades().add(cu);
    }

    @Override
    public boolean canTake() {
        return true;
    }

    @Override
    public void doTake() {
        cl.getUpgrades().remove(0);
    }

    @Override
    public boolean hasItem() {
        return !cl.getUpgrades().isEmpty();
    }
}