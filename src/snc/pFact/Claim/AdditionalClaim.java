package snc.pFact.Claim;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.bukkit.Location;

public abstract class AdditionalClaim extends Claim {

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        curHealth = health;
    }

    private static final long serialVersionUID = 1L;
    private int t = 0;
    public static final int FMC_R = 4;

    public int health;
    public transient int curHealth;

    public AdditionalClaim(Location center, int length, String faction, int health) {
        super(center, length, faction);
        this.health = health;
        this.curHealth = health;
    }

    public void SetItemType(int a) {
        t = a;
    }

    public int health() {
        return health;
    }

    public int curHealth() {
        return curHealth;
    }

    public void setHealth(int health) {
        this.health = health;
        this.curHealth = health;
    }

    public void setCurHealth(int curHealth) {
        this.curHealth = curHealth;
    }

    public int GetType() {
        return t;
    }
}