package snc.pFact.Claim.AdditionalClaims;

import java.util.Date;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import snc.pFact.Claim.Claim;
import snc.pFact.Claim.ClaimFactory;
import snc.pFact.utils.SerItem;

/**
 * ICraftingClaim
 */
public interface ICraftingClaim {

    public List<SerItem> getShards();

    public SerItem getLevelItem();

    public SerItem setLevelItem(ItemStack is);

    public default Date getEndDateForClaim(Claim cl) {

        return new Date(System.currentTimeMillis() + getEndLongForClaim(cl));
    }

    public default long getEndLongForClaim(Claim cl) {
        long untilEnd = 0;
        if (cl.getLevel() == 1) {
            untilEnd = 8L * 60L * 60L * 1000L;
        }
        if (cl.getLevel() == 2) {
            untilEnd = 24L * 60L * 60L * 1000L;
        }
        if (cl.getLevel() == 3) {
            untilEnd = 3L * 24L * 60L * 60L * 1000L;
        }
        return untilEnd;
    }

    public long untilEnd();

    public Date getEndDate();

    public void startCrafting();

    public default Claim getCrafting() {
        if (getCraftingState() != CraftingState.READY)
            return null;
        return ClaimFactory.getClaimFromShard(getShards().get(0).getItemStack());
    }

    public default boolean isCraftCompleted() {
        return getEndDate() != null && getEndDate().before(new Date());
    }

    public default CraftingState getCraftingState() {
        if (getShards().size() < 4 || getLevelItem() == null) {
            return CraftingState.NOT_ENOUGH_ITEMS;
        }

        Claim cl = null;

        for (SerItem si : getShards()) {
            Claim siCl = ClaimFactory.getClaimFromShard(si.getItemStack());
            if (cl == null) {
                cl = siCl;
                continue;
            } else {
                if (siCl.getName().equals(cl.getName())) {
                    continue;
                }
                return CraftingState.DIFFERENT_SHARDS;
            }
        }
        if (cl.getLevel() != ClaimFactory.getLevelFromItem(getLevelItem().getItemStack())) {
            return CraftingState.SHARD_LEVEL_DIFF;
        }

        return CraftingState.READY;
    }

    public enum CraftingState {
        NOT_ENOUGH_ITEMS, DIFFERENT_SHARDS, SHARD_LEVEL_DIFF, READY;
    }

}