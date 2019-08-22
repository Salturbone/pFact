package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import snc.pFact.Main;
import snc.pFact.DM.DataIssues;
import snc.pFact.utils.Msgs;

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
    private transient Location warpingStart, toWarp;
    private transient int warpingTime;

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
        if (timer == (20 / Main.taskRepeating) * 60) {
            addCoin(isVIP() ? 2 : 1);
            timer = 0;
        }

        if (warpingTime > 0) {
            warpingTime--;
            if (warpingTime <= 0) {
                getPlayer().teleport(toWarp);
                warpingTime = -1;
                toWarp = null;
                warpingStart = null;
            } else {
                Player p = getPlayer();
                Location loc = p.getLocation();
                if (!loc.getWorld().equals(warpingStart.getWorld())
                        || warpingStart.distance(getPlayer().getLocation()) >= 0.2) {
                    getPlayer().sendMessage(Msgs.WARP_CANCELLED.sub);
                    warpingTime = -1;
                    toWarp = null;
                    warpingStart = null;
                }
            }
        }
    }

    public void warp(Location warping) {
        this.toWarp = warping;
        this.warpingStart = getPlayer().getLocation();
        warpingTime = (int) ((20 / Main.taskRepeating) * 3);
    }

    public boolean isWarping() {
        return warpingTime > 0;
    }

    public boolean isVIP() {
        return DataIssues.vips.containsKey(uuid());
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(id);
    }

}
