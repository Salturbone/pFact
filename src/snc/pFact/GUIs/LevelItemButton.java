package snc.pFact.GUIs;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import snc.pFact.Claim.ClaimFactory;
import snc.pFact.Claim.AdditionalClaims.ICraftingClaim;
import snc.pFact.utils.SerItem;

/**
 * LevelItemButton
 */
public class LevelItemButton extends PlaceableButtonNode {

    private ICraftingClaim icl;

    public LevelItemButton(ICraftingClaim icl) {
        super();
        this.icl = icl;
    }

    @Override
    public ItemStack noItem() {
        return new ItemStack(Material.ITEM_FRAME);
    }

    @Override
    public ItemStack getItem() {
        return icl.getLevelItem().getItemStack();
    }

    @Override
    public ItemStack giveItem() {
        return icl.getLevelItem().getItemStack();
    }

    @Override
    public boolean canPlace(ItemStack is) {
        int level = ClaimFactory.getLevelFromItem(is);
        return level != -1;
    }

    @Override
    public void doPlace(ItemStack is) {
        is.setAmount(1);
        icl.setLevelItem(new SerItem(is));
    }

    @Override
    public boolean canTake() {
        return !icl.isCrafting() && !icl.didEnd();
    }

    @Override
    public void doTake() {
        icl.setLevelItem(null);
    }

    @Override
    public boolean hasItem() {
        return icl.getLevelItem() != null;
    }

}