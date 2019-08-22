package snc.pFact.Claim.AdditionalClaims;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.ZChestLibV6.ChestNode;
import snc.pFact.Claim.AdditionalClaim;
import snc.pFact.Claim.Upgrade.ClaimUpgrade;
import snc.pFact.Claim.Upgrade.HealthMultiplierUpgrade;
import snc.pFact.GUIs.BreakClaimButton;
import snc.pFact.GUIs.ShowBordersButton;
import snc.pFact.GUIs.UpgradesButton;
import snc.pFact.utils.Gerekli;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

/**
 * UpgAddClaim
 */
public abstract class UpgAddClaim extends AdditionalClaim implements UpgradeableClaim {

    private static final long serialVersionUID = 5817805506610848515L;
    private List<ClaimUpgrade> upgrades;

    public UpgAddClaim(int length, ItemStack claimBlock, ItemStack shard, Color color, long craftTime,
            double shardDropChance, double shardWoUpgChance, int health) {
        super(length, claimBlock, shard, color, craftTime, shardDropChance, health);
        upgrades = new ArrayList<>();
        claimData().setObject("shardDropWithoutUpgradeChance", shardWoUpgChance);
    }

    @Override
    public void setup(String faction, Location center) {
        super.setup(faction, center);
        this.upgrades = new ArrayList<>();
    }

    @Override
    protected UpgAddClaim clone() {
        UpgAddClaim uac = (UpgAddClaim) super.clone();
        uac.upgrades = new ArrayList<>();
        return uac;
    }

    @Override
    public List<ClaimUpgrade> getUpgrades() {
        return upgrades;
    }

    @Override
    public double getHealthMultiplier() {
        double multiplier = 1;

        if (this instanceof UpgradeableClaim)
            for (HealthMultiplierUpgrade upg : ((UpgradeableClaim) this)
                    .getUpgradesByType(HealthMultiplierUpgrade.class)) {
                multiplier *= upg.getMultiplier();
            }
        return multiplier;
    }

    @Override
    public List<ItemStack> getDrops(boolean naturally) {
        List<ItemStack> items = super.getDrops(naturally);
        for (ClaimUpgrade upg : upgrades) {
            if (naturally) {
                if (Gerekli.chanceOf(upg.getUpgradeDropChance()))
                    items.add(upg.getUpgradeItem());
            } else {
                items.add(upg.getUpgradeItem());
            }
        }
        return items;
    }

    @Override
    public List<ChestNode> getConfigurableList(ChestGUI arg0) {
        List<ChestNode> nodes = new ArrayList<>();
        // egg
        nodes.add(getSingularItem());
        // break
        nodes.add(new BreakClaimButton());
        // upgrade
        nodes.add(new UpgradesButton(this));
        // show
        nodes.add(new ShowBordersButton(new ItemStack(Material.PAINTING)));
        return nodes;

    }

    public double getShardDropWithoutUpgradeChance() {
        return claimData().getDouble("shardDropWithoutUpgradeChance");
    }

    @Override
    public double shardDropChance() {
        return upgrades.isEmpty() ? getShardDropWithoutUpgradeChance() : shardDropChance();
    }
}