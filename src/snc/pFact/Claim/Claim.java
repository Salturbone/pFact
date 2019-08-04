package snc.pFact.Claim;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.utils.Location2D;
import snc.pFact.utils.SerLocation;
import snc.pFact.utils.Square3D;
import snc.pFact.utils.ZSIGN;

public abstract class Claim implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    private Square3D square;
    private String faction;
    private SerLocation centerBlock;

    public Claim(Location center, int length, String faction) {
        this.centerBlock = new SerLocation(center);
        this.square = new Square3D(Location2D.fromLocation(center), length);
    }

    public void setup(String faction, Location center) {
        this.faction = faction;
        square.setCenter(Location2D.fromLocation(center));
    }

    public Location getCenterBlock() {
        return centerBlock.getLocation();
    }

    public void setCenterBlock(Location loc) {
        this.centerBlock = new SerLocation(loc);
    }

    public Square3D getSquare() {
        return square;
    }

    public void setSquare(Square3D square) {
        this.square = square;
    }

    public B_Faction getFaction() {
        return DataIssues.factions.get(faction);
    }

    public final ItemStack getClaimItem(B_Faction fact) {
        return ZSIGN.imzalaZ("claim", getName(), ZSIGN.imzalaZ("claimFact", fact.getName(), doGetClaimItem(fact)));
    }

    protected abstract ItemStack doGetClaimItem(B_Faction fact);

    public abstract boolean canBreak(Player p, Location loc);

    public abstract boolean canPlace(Player p, Location loc);

    public abstract boolean canInteract(Player p, Location loc);

    public abstract void update();

    public abstract String getName();

    public abstract Recipe getRecipe();

    @Override
    protected Claim clone() {
        Claim cl;
        try {
            cl = (Claim) super.clone();
            cl.square = square.clone();
            cl.centerBlock = centerBlock.clone();
            return cl;
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
}