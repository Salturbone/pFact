package snc.pFact.Claim.AdditionalClaims;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.ZChestLibV6.ChestNode;
import snc.pFact.Main;
import snc.pFact.Claim.ClaimFactory;
import snc.pFact.Claim.Upgrade.GainMultiplierUpgrade;
import snc.pFact.GUIs.CraftClaimGUI;
import snc.pFact.GUIs.GoToCraftingButton;
import snc.pFact.utils.SerItem;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

/**
 * CraftClaim
 */
public class CraftClaim extends UpgAddClaim implements ICraftingClaim {

    private static final long serialVersionUID = 7764519676332672866L;
    private SerItem[] shards;
    private SerItem levelItem;
    private long untilEnd;
    private boolean crafting, ended;

    public CraftClaim(int length, ItemStack claimBlock, ItemStack shard, Color color, long craftTime,
            double shardDropChance, double shardWoUpgChance, int health) {
        super(length, claimBlock, shard, color, craftTime, shardDropChance, shardWoUpgChance, health);
    }

    @Override
    public void update() {
        if (untilEnd >= 0) {
            untilEnd -= ((double) ClaimFactory.interval / 20) * (double) 1000L * getMultipliers();
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
        list.add(2, new GoToCraftingButton(this));
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
    public List<ItemStack> getDrops(boolean naturally) {
        List<ItemStack> items = super.getDrops(naturally);
        if (!naturally) {
            for (SerItem item : shards) {
                if (item == null)
                    continue;
                items.add(item.getItemStack());
            }
            if (getLevelItem() != null)
                items.add(getLevelItem().getItemStack());
        }
        return items;
    }

    @Override
    public double getMultipliers() {
        double multiplier = 1;
        for (GainMultiplierUpgrade upg : getUpgradesByType(GainMultiplierUpgrade.class)) {
            multiplier *= upg.getMultiplier();
        }
        return multiplier * Math.floor(Math.pow(1.25, Math.pow(getFaction().getVIPCount(), 3 / 4)));
    }

    @Override
    public void closeGUIS() {
        super.closeGUIS();
        for (Entry<UUID, ChestGUI> ent : Main.cm.entrySet()) {
            ChestGUI gui = ent.getValue();
            if (gui instanceof CraftClaimGUI) {
                ICraftingClaim cl = ((CraftClaimGUI) gui).getClaim();
                if (cl == this)
                    gui.close(true);
            }
        }
    }
}