package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.UUID;

import snc.pFact.DM.DataIssues;

public class B_Player implements Serializable {

    private static final long serialVersionUID = 8584153525493905698L;

    public static final B_Faction getFactionOfPlayer(UUID id) {
        B_Player bp = DataIssues.players.get(id);
        B_Faction bf = bp.getF();
        return bf;
    }

    private UUID id;
    private String fct = null;
    private String e_fct = null;
    private boolean e_state = false;
    public int timer = 0;
    private double coin;

    public B_Player(UUID id, String fct, double coin) {
        this.id = id;
        this.fct = fct;
        this.coin = coin;
    }

    // Faction
    public void setF(String fct) {
        this.fct = fct;
    }

    public B_Faction getF() {
        return fct == null ? null : DataIssues.factions.get(fct);
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

    public B_Faction getEF() {
        return DataIssues.factions.get(e_fct);
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
        return fct != null;
    }

    public void update() {
        timer++;
        if (timer == 60) {
            addCoin(1);
            timer = 0;
        }
    }

    public boolean isVIP() {
        return DataIssues.vips.containsKey(uuid());
    }

}
