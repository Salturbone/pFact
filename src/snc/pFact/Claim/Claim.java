package snc.pFact.Claim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
        ClaimFactory.claimDatas.put(getName(), cd);
        cd.setObject("length", length);
        cd.setItemStack("block", claimBlock);
        cd.setItemStack("shard", shard);
    }

    public void setup(String faction, Location center) {
        this.faction = faction;
        this.centerBlock = new SerLocation(center.getBlock().getLocation());
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

    public final ItemStack getClaimItem(String fact) {
        return ZSIGN.imzalaZ("claim", getName(),
                ZSIGN.imzalaZ("claimFaction", fact, claimData().getItemStack("block")));
    }

    public boolean canBreak(Player p, Location loc) {
        return canPlace(p, loc);
    }

    public boolean canPlace(Player p, Location loc) {
        Location center = getCenterBlock();
        double xDiff = Math.abs(loc.getX() - center.getX());
        double yDiff = Math.abs(loc.getY() - center.getY());
        double zDiff = Math.abs(loc.getZ() - center.getZ());
        if (xDiff < 2 && yDiff < 2 && zDiff < 2) {
            return false;
        }
        return true;
    }

    public boolean canInteract(Player p, Location loc) {
        return true;
    }

    public abstract void update();

    public abstract String getName();

    public ItemStack getShard() {
        return ZSIGN.imzalaZ("shard", getName(), claimData().getItemStack("shard"));
    }

    public abstract int getLevel();

    public boolean canCreate() {
        int has = getFaction().getClaimsByType(this).size();
        int hasTotal = getFaction().getAllClaims().size();
        if (has >= getMaxToHave())
            return false;
        if (hasTotal >= getFaction().getMaxClaimCount()) {
            return false;
        }
        return true;
    }

    public int getMaxToHave() {
        int fctLevel = getFaction().getLevel();
        List<Integer> ints = new ArrayList<Integer>();
        if (getLevel() == 1) {
            ints.addAll(Arrays.asList(3, 8, 14, 19, 23, 27, 30));
        } else if (getLevel() == 2) {
            ints.addAll(Arrays.asList(5, 11, 18, 24, 30));
        }
        int max = 0;
        for (int i = 0; i < ints.size(); i++) {
            if (fctLevel < ints.get(i)) {
                max = i;
            }
        }
        return max;
    }

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