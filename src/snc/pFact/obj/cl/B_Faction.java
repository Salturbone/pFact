package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import snc.pFact.obj.cl.B_Player.Rank;

public class B_Faction implements Serializable {

    private static final long serialVersionUID = 1L;

    public static HashMap<String, B_Faction> factions = new HashMap<String, B_Faction>();

    private String name;
    private int level;
    private int member_count;
    private double xp;
    private double prestige;
    public HashMap<UUID, B_Player> players;
    public int timer = 0;
    UUID founder;

    /*
     * public b_Faction(String name, int level, int member_count, double xp, double
     * prestige, HashMap<UUID, b_Player> players) { this.name = name; this.level =
     * level; this.member_count = member_count; this.xp = xp; this.prestige =
     * prestige; this.players = players; }
     */

    public B_Faction(String name, B_Player founder) {
        this.name = name;
        this.level = 0;
        this.member_count = 0;
        this.xp = 0;
        this.prestige = 0;
        players = new HashMap<UUID, B_Player>();
        players.put(founder.uuid(), founder);
        this.founder = founder.uuid();
    }

    // Level
    public int getLevel() {
        return level;
    }

    public void setLevel(int lvl) {
        level = lvl;
    }

    public void addLevel(int a) {
        level += a;
    }

    // Name
    public String getName() {
        return name;
    }

    // MemberCount
    public int getMemberCount() {
        return member_count;
    }

    public void setMemberCount(int mc) {
        member_count = mc;
    }

    public void addMember(UUID idd, B_Player pp) {
        players.put(idd, pp);
        member_count += 1;
    }

    public double getXP() {
        return xp;
    }

    public void addXP(double a) {
        xp += a;
    }

    public double getPrestige() {
        return prestige;
    }

    public B_Player getPlayer(UUID id) {
        return players.get(id);
    }

    public UUID getFounder() {
        return founder;
    }

    public void setFounder(B_Player pll) {
        founder = pll.uuid();
    }

    public List<B_Player> getByRank(Rank rank) {
        List<B_Player> list = new ArrayList<B_Player>();
        for (B_Player bp : players.values()) {
            if (bp.rank() == rank)
                list.add(bp);
        }
        return list;
    }

    public void update() {
        timer++;
        // klanların xp kazanma mekaniği
        if (timer == 60 * 5) {
            // sabit 10 üzerinden her aktif üye başına %1 artar
            // sabit 10 üzerinden her seviye başına %2 azalır
            int on = 0;
            for (UUID idd : players.keySet()) {
                if (Bukkit.getPlayer(idd).isOnline()) {
                    on += 1;
                }
            }
            addXP(10.0 * ((100 + (on)) / 100) * ((100 - (2 * getLevel())) / 100));
            timer = 0;
        }
    }

}
