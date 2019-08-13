package snc.pFact.GUIs;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import snc.pFact.Claim.Claim;
import snc.pFact.Claim.ClaimFactory;
import snc.pFact.Claim.AdditionalClaims.ICraftingClaim;
import snc.pFact.utils.SerItem;

/**
 * ShardButton
 */
public class ShardButton extends PlaceableButtonNode {

    private ICraftingClaim icl;
    private int slot;

    public ShardButton(ICraftingClaim icl, int slot) {
        super();
        this.icl = icl;
        this.slot = slot;
    }

    @Override
    public ItemStack noItem() {
        return new ItemStack(Material.ITEM_FRAME);
    }

    @Override
    public ItemStack getItem() {
        return icl.getShards()[slot].getItemStack();
    }

    @Override
    public ItemStack giveItem() {
        return icl.getShards()[slot].getItemStack();
    }

    @Override
    public boolean canPlace(ItemStack is) {
        Claim cl = ClaimFactory.getClaimFromShard(is);
        return cl != null;
    }

    @Override
    public void doPlace(ItemStack is) {
        is.setAmount(1);
        icl.getShards()[slot] = new SerItem(is);
    }

    @Override
    public boolean canTake() {
        return !icl.isCrafting() && !icl.didEnd();
    }

    @Override
    public void doTake() {
        icl.getShards()[slot] = null;
    }

    @Override
    public boolean hasItem() {
        return icl.getShards()[slot] != null;
    }
}