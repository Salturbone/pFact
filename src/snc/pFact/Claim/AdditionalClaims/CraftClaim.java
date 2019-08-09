package snc.pFact.Claim.AdditionalClaims;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import snc.pFact.Claim.ClaimFactory;
import snc.pFact.Claim.Upgrade.GainMultiplierUpgrade;
import snc.pFact.utils.SerItem;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

/**
 * CraftClaim
 */
public class CraftClaim extends UpgAddClaim implements ICraftingClaim {

    private static final long serialVersionUID = 1L;

    private List<SerItem> shards;
    private SerItem levelItem;
    private Date end;
    private long untilEnd;

    public CraftClaim(int length, ItemStack claimBlock, ItemStack shard, Color color, int health) {
        super(length, claimBlock, shard, color, health);
    }

    @Override
    public void update() {
        if (untilEnd >= 0) {
            untilEnd -= ((double) ClaimFactory.interval / 20) * 1000L * getTimeMultiplier();
        }
    }

    @Override
    public void setup(String faction, Location center) {
        super.setup(faction, center);
        this.shards = new ArrayList<SerItem>();
        this.end = null;
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
        cc.shards = new ArrayList<SerItem>();
        cc.levelItem = null;
        cc.end = null;
        return cc;
    }

    public double getTimeMultiplier() {
        double multiplier = 1;
        for (GainMultiplierUpgrade upg : getUpgradesByType(GainMultiplierUpgrade.class)) {
            multiplier *= upg.getMultiplier();
        }
        return multiplier;
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public List<SerItem> getShards() {
        return shards;
    }

    @Override
    public SerItem getLevelItem() {
        return levelItem;
    }

    @Override
    public long untilEnd() {
        return untilEnd;
    }

    @Override
    public Date getEndDate() {
        return end == null ? new Date(System.currentTimeMillis() + untilEnd) : end;
    }

    @Override
    public void startCrafting() {
        untilEnd = getEndLongForClaim(getCrafting());
    }

}