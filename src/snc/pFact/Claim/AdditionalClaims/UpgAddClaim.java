package snc.pFact.Claim.AdditionalClaims;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.ZChestLibV6.ButtonNode;
import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.ZChestLibV6.ChestNode;
import me.Zindev.utils.data.SoundData;
import snc.pFact.Claim.AdditionalClaim;
import snc.pFact.Claim.ClaimFactory;
import snc.pFact.Claim.Upgrade.ClaimUpgrade;
import snc.pFact.Claim.Upgrade.HealthMultiplierUpgrade;
import snc.pFact.GUIs.ClaimMenuGUI;
import snc.pFact.GUIs.ShowBordersButton;
import snc.pFact.GUIs.UpgradesButton;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

/**
 * UpgAddClaim
 */
public abstract class UpgAddClaim extends AdditionalClaim implements UpgradeableClaim {

    private static final long serialVersionUID = 1L;

    private List<ClaimUpgrade> upgrades;

    public UpgAddClaim(int length, ItemStack claimBlock, ItemStack shard, Color color, int health) {
        super(length, claimBlock, shard, color, health);
        upgrades = new ArrayList<>();
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
    public List<ChestNode> getConfigurableList(ChestGUI arg0) {
        List<ChestNode> nodes = new ArrayList<>();
        // egg
        nodes.add(getSingularItem());
        // break
        nodes.add(new ButtonNode(ClaimFactory.breakClaim.getItemStack()) {

            @Override
            public void onClick(ChestGUI arg0, ChestGUIClickEvent arg1) {
                AdditionalClaim cl = (AdditionalClaim) ((ClaimMenuGUI) arg0).getClaim();
                if (cl == null)
                    return;
                B_Faction fact = cl.getFaction();
                arg0.getUser().getInventory().addItem(cl.getClaimItem(fact.getName()));
                cl.getCenterBlock().getBlock().setType(Material.AIR);
                fact.getAdditionalClaims().remove(cl);
                new SoundData(1f, 1f, Sound.BLOCK_GLASS_BREAK).play(arg0.getUser());
                arg0.close(true);
            }
        });
        // upgrade
        nodes.add(new UpgradesButton(this));
        // show
        nodes.add(new ShowBordersButton(new ItemStack(Material.PAINTING)));
        return nodes;

    }
}