package snc.pFact.Claim;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Zindev.utils.ZChestLibV6.ButtonNode;
import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.ZChestLibV6.ChestNode;
import me.Zindev.utils.ZChestLibV6.ItemNode;
import me.Zindev.utils.data.SoundData;
import snc.pFact.Claim.Upgrade.ClaimUpgrade;
import snc.pFact.Claim.Upgrade.GainMultiplierUpgrade;
import snc.pFact.GUIs.ClaimMenuGUI;
import snc.pFact.GUIs.ShowBordersButton;
import snc.pFact.obj.cl.B_Faction;

public abstract class AdditionalClaim extends Claim {

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        curHealth = claimData().getInt("health");
    }

    private static final long serialVersionUID = 1L;
    private transient int curHealth;
    private List<ClaimUpgrade> upgrades;

    public AdditionalClaim(int length, ItemStack claimBlock, ItemStack shard, int health) {
        super(length, claimBlock, shard);
        claimData().setObject("health", health);
    }

    @Override
    public void setup(String faction, Location center) {
        super.setup(faction, center);
        upgrades = new ArrayList<ClaimUpgrade>();
    }

    public int getMaxHealth() {
        return (int) Math.floor(getHealthMultiplier()) * claimData().getInt("health");
    }

    public int curHealth() {
        return curHealth;
    }

    public void setCurHealth(int curHealth) {
        this.curHealth = curHealth;
    }

    public double getHealthMultiplier() {
        double multiplier = 1;
        for (GainMultiplierUpgrade upg : getUpgradesByType(GainMultiplierUpgrade.class)) {
            multiplier *= upg.getMultiplier();
        }
        return multiplier;
    }

    @SuppressWarnings("unchecked")
    public <T extends ClaimUpgrade> List<T> getUpgradesByType(Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        for (ClaimUpgrade cu : upgrades) {
            if (clazz.isAssignableFrom(cu.getClass()))
                list.add((T) cu);
        }
        return list;
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
        nodes.add(new ButtonNode(null) {

            @Override
            public ItemStack getStack(ChestGUI gui) {
                if (upgrades.size() == 1) {
                    ClaimUpgrade cu = upgrades.get(0);
                    return cu.getUpgradeItem();
                } else {
                    return ClaimFactory.noUpgradeItem.getItemStack();
                }
            }

            @Override
            public void onClick(ChestGUI arg0, ChestGUIClickEvent arg1) {
                Player p = arg0.getUser();
                if (upgrades.size() == 1) {
                    ClaimUpgrade cu = upgrades.get(0);
                    p.getInventory().addItem(cu.getUpgradeItem()).values().forEach(is -> {
                        p.getWorld().dropItemNaturally(p.getLocation(), is);
                    });
                    upgrades.clear();
                    new SoundData(1f, 1f, Sound.ENTITY_EXPERIENCE_ORB_PICKUP).play(p);
                    return;
                }
                ItemStack is = arg1.getCurrentItem();
                if (is != null && is.getType() != Material.AIR) {
                    ClaimUpgrade cu = ClaimFactory.getUpgradeFromItemStack(is);
                    if (cu == null) {
                        new SoundData(1f, 1f, Sound.BLOCK_ANVIL_LAND).play(p);
                        return;
                    }
                    upgrades.add(cu);
                    new SoundData(1f, 1f, Sound.ENTITY_EXPERIENCE_ORB_PICKUP).play(p);
                }
                new SoundData(1f, 1f, Sound.BLOCK_ANVIL_LAND).play(p);
                return;
            }
        });
        // show
        nodes.add(new ShowBordersButton(null));
        return nodes;
    }

    @Override
    public SingularItem getSingularItem() {
        ItemStack is = claimData().getItemStack("displayItem");
        ItemMeta im = is.getItemMeta();
        if (im.hasLore())
            for (String st : im.getLore()) {
                st.replaceAll("<has>", getFaction().getClaimsByType(this).size() + "");
                st.replaceAll("<max>", getMaxToHave() + "");
                st.replaceAll("<level>", getLevel() + "");
            }
        is.setItemMeta(im);
        return new ItemNode(is);
    }
}