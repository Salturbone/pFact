package snc.pFact.Claim;

import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.utils.Location2D;
import snc.pFact.utils.SerLocation;
import snc.pFact.utils.Square3D;
import snc.pFact.utils.ZSIGN;

public abstract class Claim implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    private String faction;
    private SerLocation centerBlock;

    public Claim(int length, ItemStack claimBlock, ItemStack shard) {
        ClaimData cd = new ClaimData();
        cd.setObject("length", length);
        cd.setItemStack("claimBlock", claimBlock);
        cd.setItemStack("shard", shard);
    }

    public void setup(String faction, Location center) {
        this.faction = faction;
        this.centerBlock = new SerLocation(center);
    }

    public Location getCenterBlock() {
        return centerBlock.getLocation();
    }

    public void setCenterBlock(Location loc) {
        this.centerBlock = new SerLocation(loc);
    }

    public Square3D getSquare() {
        return new Square3D(Location2D.fromLocation(centerBlock.getLocation()), claimData().getInt("length"));
    }

    public B_Faction getFaction() {
        return DataIssues.factions.get(faction);
    }

    public final ItemStack getClaimItem(B_Faction fact) {
        return ZSIGN.imzalaZ("claim", getName(),
                ZSIGN.imzalaZ("claimFact", fact.getName(), claimData().getItemStack("claimBlock")));
    }

    public abstract boolean canBreak(Player p, Location loc);

    public abstract boolean canPlace(Player p, Location loc);

    public abstract boolean canInteract(Player p, Location loc);

    public abstract void update();

    public abstract String getName();

    public ItemStack getShard() {
        return ZSIGN.imzalaZ("shard", getName(), claimData().getItemStack("shard"));
    }

    public abstract int getLevel();

    public void canCraft(HashMap<Integer, ItemStack> recipe) {

    }

    @Override
    protected Claim clone() {
        Claim cl;
        try {
            cl = (Claim) super.clone();
            return cl;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;

    }

    public ClaimData claimData() {
        return ClaimFactory.claimDatas.get(getName());
    }
}