package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class B_Player implements Serializable {

    private static final long serialVersionUID = 1L;

    public static HashMap<UUID, B_Player> players = new HashMap<UUID, B_Player>();

    public enum Rank {
        Single, Player, Moderator, Founder;
    }

    private UUID id;
    private String fct = null;
    private String e_fct = null;
    private boolean e_state = false;
    public int timer = 0;
    private double coin;
    private Rank rank = Rank.Single;

    public B_Player(UUID id, String fct, double coin, Rank rank) {
        this.id = id;
        if (fct != null) {
            if (B_Faction.factions.get(fct).getPlayer(id).id == id) {
                this.fct = fct;
            } else {
                this.fct = null;
            }
        }
        this.rank = rank;
        this.coin = coin;
    }

    // Faction
    public void setF(String fct) {
        this.fct = fct;
    }

    public B_Faction getF() {
        return B_Faction.factions.get(fct);
    }

    // UUID
    public UUID uuid() {
        return id;
    }

    public void setCoin(double d) {
        coin = d;
    }

    public double getCoin() {
        return coin;
    }

    public void addCoin(double d) {
        coin += d;
    }

    public Rank rank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public B_Faction getEF() {
        return B_Faction.factions.get(e_fct);
    }

    public void setEF(String fff) {
        e_fct = fff;
    }

    public boolean getES() {
        return e_state;
    }

    public void setES(boolean s) {
        e_state = s;
    }

    public boolean hasFaction() {
        if (rank == Rank.Single || fct == null) {
            rank = Rank.Single;
            fct = null;
            return false;
        }
        return true;
    }

    public void update() {
        timer++;
        if (timer == 60) {
            addCoin(1);
        }
    }

}
