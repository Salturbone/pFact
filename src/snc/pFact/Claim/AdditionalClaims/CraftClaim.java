package snc.pFact.Claim.AdditionalClaims;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.ZChestLibV6.ChestNode;
import snc.pFact.Claim.ClaimFactory;
import snc.pFact.Claim.Upgrade.GainMultiplierUpgrade;
import snc.pFact.GUIs.GoToCraftingButton;
import snc.pFact.utils.SerItem;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

/**
 * CraftClaim
 */
public class CraftClaim extends UpgAddClaim implements ICraftingClaim {

    private static final long serialVersionUID = 1L;

    private SerItem[] shards;
    private SerItem levelItem;
    private long untilEnd;
    private boolean crafting, ended;

    public CraftClaim(int length, ItemStack claimBlock, ItemStack shard, Color color, long craftTime, int health) {
        super(length, claimBlock, shard, color, craftTime, health);
    }

    @Override
    public void update() {
        if (untilEnd >= 0) {
            untilEnd -= ((double) ClaimFactory.interval / 20) * 1000L * getMultipliers();

        }
        check();
    }

    @Override
    public void setup(String faction, Location center) {
        super.setup(faction, center);
        this.shards = new SerItem[4];
        this.ended = false;
        this.untilEnd = 0L;
        this.levelItem = null;
    }

    @Override
    public String getName() {
        return "craftClaim";
    }

    @Override
    public int getMaxToHave() {
        int fctLevel = getFaction().getLevel();
        List<Integer> ints = new ArrayList<Integer>();
        ints.addAll(Arrays.asList(10, 20, 30));
        int max = 0;
        for (int i = 0; i < ints.size(); i++) {
            if (fctLevel < ints.get(i)) {
                max = i;
            }
        }
        return max;
    }

    @Override
    protected CraftClaim clone() {
        CraftClaim cc = (CraftClaim) super.clone();
        cc.shards = new SerItem[4];
        cc.levelItem = null;
        cc.ended = false;
        return cc;
    }

    @Override
    public List<ChestNode> getConfigurableList(ChestGUI arg0) {
        List<ChestNode> list = super.getConfigurableList(arg0);
        list.add(1, new GoToCraftingButton(this));
        return list;
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public SerItem[] getShards() {
        return shards;
    }

    @Override
    public SerItem getLevelItem() {
        return this.levelItem;
    }

    @Override
    public void setLevelItem(SerItem is) {
        this.levelItem = is;
    }

    @Override
    public long untilEnd() {
        return untilEnd;
    }

    @Override
    public void setUntilEnd(long end) {
        untilEnd = end;
    }

    @Override
    public boolean isCrafting() {
        return crafting;
    }

    @Override
    public void setCrafting(boolean bool) {
        crafting = bool;
    }

    @Override
    public boolean didEnd() {
        return ended;
    }

    @Override
    public void setEnded(boolean bool) {
        this.ended = bool;
    }

    @Override
    public double getMultipliers() {
        double multiplier = 1;
        for (GainMultiplierUpgrade upg : getUpgradesByType(GainMultiplierUpgrade.class)) {
            multiplier *= upg.getMultiplier();
        }
        return multiplier * Math.floor(Math.pow(1.25, Math.pow(getFaction().getVIPCount(), 3 / 4)));
    }

}