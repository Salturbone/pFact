package snc.pFact.Claim;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Zindev.utils.ZChestLibV6.ItemNode;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

public abstract class AdditionalClaim extends Claim {

    private static final long serialVersionUID = -5229148213223165619L;

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        curHealth = claimData().getInt("health");
    }

    
    private transient int curHealth;

    public AdditionalClaim(int length, ItemStack claimBlock, ItemStack shard, Color color, long craftTime, int health) {
        super(length, claimBlock, shard, color, craftTime);
        claimData().setObject("health", health);
    }

    @Override
    public void setup(String faction, Location center) {
        super.setup(faction, center);
        curHealth = getMaxHealth();
    }

    @Override
    protected AdditionalClaim clone() {
        return (AdditionalClaim) super.clone();
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
        return multiplier;
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