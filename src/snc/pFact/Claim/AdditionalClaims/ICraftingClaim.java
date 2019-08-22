package snc.pFact.Claim.AdditionalClaims;

import java.util.ArrayList;
import java.util.List;

import snc.pFact.Claim.Claim;
import snc.pFact.Claim.ClaimFactory;
import snc.pFact.GUIs.LevelItemButton;
import snc.pFact.GUIs.PlaceableButtonNode;
import snc.pFact.GUIs.ShardButton;
import snc.pFact.utils.SerItem;

/**
 * ICraftingClaim
 */
public interface ICraftingClaim {

    public SerItem[] getShards();

    public SerItem getLevelItem();

    public void setLevelItem(SerItem is);

    public long untilEnd();

    public void setUntilEnd(long end);

    public boolean didEnd();

    public void setEnded(boolean bool);

    public boolean isCrafting();

    public void setCrafting(boolean bool);

    public default void check() {
        if (!isCrafting() || untilEnd() > 0) {
            return;
        }
        setCrafting(false);
        setEnded(true);
    }

    public default void clear() {
        setLevelItem(null);
        setCrafting(false);
        setUntilEnd(-1);
        setEnded(false);
        for (int i = 0; i < 4; i++)
            getShards()[i] = null;
    }

    public default void cancel() {
        setCrafting(false);
        setUntilEnd(-1);
        setEnded(false);
    }

    public default void startCrafting() {
        setCrafting(true);
        setUntilEnd(getCrafting().getCraftTime());
    }

    public default Claim getCrafting() {
        if (getCraftingState() != CraftingState.READY)
            return null;
        return ClaimFactory.getClaimFromShard(getShards()[0].getItemStack());
    }

    public double getMultipliers();

    public default long getMultipliedEnd() {
        return (long) (untilEnd() / getMultipliers());
    }

    public default CraftingTime getCraftingTime() {
        if (didEnd())
            return CraftingTime.CRAFTED;
        if (isCrafting())
            return CraftingTime.CRAFTING;
        return CraftingTime.NO_CRAFT;
    }

    public default CraftingState getCraftingState() {
        if (getLevelItem() == null) {
            return CraftingState.NOT_ENOUGH_ITEMS;
        }

        Claim cl = null;

        for (int i = 0; i < getShards().length; i++) {
            SerItem si = getShards()[i];
            if (si == null)
                return CraftingState.NOT_ENOUGH_ITEMS;

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

    public default List<PlaceableButtonNode> getShardLevelButtons() {
        List<PlaceableButtonNode> list = new ArrayList<>();
        list.add(new LevelItemButton(this));
        for (int i = 0; i < 4; i++)
            list.add(new ShardButton(this, i));

        return list;
    }

    public enum CraftingState {
        NOT_ENOUGH_ITEMS, DIFFERENT_SHARDS, SHARD_LEVEL_DIFF, READY;
    }

    public enum CraftingTime {
        NO_CRAFT, CRAFTED, CRAFTING;
    }

}