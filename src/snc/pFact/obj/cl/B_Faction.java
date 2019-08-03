package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

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
    private double level_block = 1;
    private double bank = 0;
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

    public double getNXP() {
        return (1/level_block) * (50 + 25*level*level);
    }

    public double getBank() {
        return bank;
    }
    public void addBankAmount(double a) {
        bank += a;
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
            // sabit 10 üzerinden her aktif üye başına %2 artar
            // sabit 10 üzerinden her 5 seviye başına level_blocker kadar sağlar
            // aktif oyuncu yoksa deneyim kazanılmaz.
            int on = 0;
            for (UUID idd : players.keySet()) {
                if (Bukkit.getPlayer(idd).isOnline()) {
                    on += 1;
                }
            }
            if (on != 0) {
                addXP(10.0 
                * ((100 + (2 * (on))) / 100) 
                * level_block);
            }
            timer = 0;
            //seviye atlama
            if (xp >= (1/level_block) * (50 + 25*level*level)) {
                xp -= (1/level_block) * (50 + 25*level*level);
                level += 1;
                for (UUID idd : players.keySet()) {
                    if (Bukkit.getPlayer(idd).isOnline()) {
                        Bukkit.getPlayer(idd).sendMessage(ChatColor.GREEN + "Klanın seviye atladı!");
                        Bukkit.getPlayer(idd).sendMessage(ChatColor.GREEN + "Yeni Klan Seviyesi: " + ChatColor.RESET + level);
                    }
                }
            }
            if (level == 10) {
                level_block = 0.9;
            }
            if (level == 20) {
                level_block = 0.8;
            }
            if (level == 30) {
                level_block = 0.7;
            }
        }
    }

}
