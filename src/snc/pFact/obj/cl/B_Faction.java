package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

import snc.pFact.Claim.AdditionalClaim;
import snc.pFact.Claim.Claim;
import snc.pFact.Claim.MainClaim;
import snc.pFact.obj.cl.B_Player.Rank;
import snc.pFact.utils.Square3D;

public class B_Faction implements Serializable {

    private static final long serialVersionUID = 1L;
    public static int maxMaxClaimLength = 81;

    private String name;
    private int level;
    private double xp;
    private double prestige;
    public HashMap<UUID, B_FactionMember> players;
    public transient int timer = 0;
    private double bank = 0;
    private transient B_FactionMember founder;
    private MainClaim mainClaim;
    private List<AdditionalClaim> addClaims = new ArrayList<AdditionalClaim>();

    /*
     * public b_Faction(String name, int level, int member_count, double xp, double
     * prestige, HashMap<UUID, b_Player> players) { this.name = name; this.level =
     * level; this.member_count = member_count; this.xp = xp; this.prestige =
     * prestige; this.players = players; }
     */

    public B_Faction(String name, UUID founder) {
        this.name = name;
        this.level = 1;
        this.xp = 0;
        this.prestige = 0;
        players = new HashMap<UUID, B_FactionMember>();
        this.founder = new B_FactionMember(founder);
        this.founder.setRank(Rank.Founder);
        players.put(founder, this.founder);
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
        return players.size();
    }

    public void addMember(UUID idd) {
        players.put(idd, new B_FactionMember(idd));
    }

    public double getXP() {
        return xp;
    }

    public double getLevelBlock() {
        return 1 - Math.floor((double) level / 10) / 10;
    }

    public double toGainXP(boolean careOnline) {
        int on = 0;
        for (UUID idd : players.keySet()) {
            if (Bukkit.getPlayer(idd) != null && Bukkit.getPlayer(idd).isOnline()) {
                on++;
            }
        }
        if (on != 0 || !careOnline) {
            return getLevelBlock() * (100 + (2 * Math.sqrt(on * on * on))) / 100;
        }
        return 0;
    }

    public void addXP(double a) {
        xp += a;
        if (xp >= getLevelUpXp()) {
            xp -= getLevelUpXp();
            level += 1;
            for (B_FactionMember bfm : players.values()) {
                if (bfm.isOnline()) {
                    Bukkit.getPlayer(bfm.uuid()).sendTitle(
                            ChatColor.GREEN + "Yeni Klan Seviyesi: " + ChatColor.DARK_PURPLE + level,
                            ChatColor.DARK_GREEN + "Klanın seviye atladı!", -1, -1, -1);
                }
            }
        }
        xp = Math.floor(xp * 100) / 100;
    }

    public double getLevelUpXp() {
        return (1 / getLevelBlock()) * (35 + 35 * level * level);
    }

    public double getPrestige() {
        return prestige;
    }

    public B_FactionMember getPlayer(UUID id) {
        return players.get(id);
    }

    public B_FactionMember getFounder() {
        if (founder == null)
            founder = getByRank(Rank.Founder).get(0);
        return founder;
    }

    public void changeFounder(UUID id) {
        B_FactionMember nfounder = players.get(id);
        if (nfounder == null)
            return;
        this.founder.setRank(Rank.Moderator);
        this.founder = nfounder;

        this.founder.setRank(Rank.Founder);
    }

    public double getNXP() {
        return (1 / getLevelBlock()) * (35 + 35 * level * level);
    }

    public double getBank() {
        return bank;
    }

    public void addBankAmount(double a) {
        bank += a;
    }

    public List<B_FactionMember> getByRank(Rank rank) {
        List<B_FactionMember> list = new ArrayList<B_FactionMember>();
        for (B_FactionMember bp : players.values()) {
            if (bp.rank() == rank)
                list.add(bp);
        }
        return list;
    }

    public MainClaim GetMainClaim() {
        return mainClaim;
    }

    public void setMainClaim(MainClaim claim) {
        this.mainClaim = claim;
    }

    public List<AdditionalClaim> getAdditionalClaims() {
        return addClaims;
    }

    public Square3D getMaxClaimArea() {
        if (mainClaim == null)
            return null;
        return new Square3D(mainClaim.getSquare().center(), getMaxClaimLength());
    }

    public int getMaxClaimLength() {
        return 22 + (int) Math.min(Math.floor((double) level / 10) * 6, 18);
    }

    public void disband() {
        for (Claim cl : getAllClaims()) {
            Block bl = cl.getCenterBlock().getBlock();
            bl.setType(Material.AIR);

        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Claim> List<T> getClaimsByType(T claim) {
        List<T> claims = new ArrayList<>();
        for (Claim ac : getAllClaims()) {
            if (ac.getName().equals(claim.getName()))
                claims.add((T) ac);
        }
        return claims;
    }

    public int getMaxClaimCount() {
        HashMap<Integer, Integer> ints = new HashMap<>();
        ints.put(0, 1);
        ints.put(3, 2);
        ints.put(5, 4);
        ints.put(7, 6);
        ints.put(9, 8);
        ints.put(10, 10);
        ints.put(12, 13);
        ints.put(14, 16);
        ints.put(16, 19);
        ints.put(18, 22);
        ints.put(20, 25);
        ints.put(21, 29);
        ints.put(22, 33);
        ints.put(23, 37);
        ints.put(24, 41);
        ints.put(25, 45);
        ints.put(26, 49);
        ints.put(27, 53);
        ints.put(28, 57);
        ints.put(29, 60);
        ints.put(30, 63);
        int max = 0;
        for (Integer i : ints.keySet()) {

            if (i > level) {
                break;
            }
            max = ints.get(i);
        }
        return max;
    }

    public List<Claim> getAllClaims() {
        List<Claim> cls = new ArrayList<Claim>(getAdditionalClaims());
        cls.add(mainClaim);
        return cls;
    }

    public void update() {
        timer++;
        // klanların xp kazanma mekaniği
        if (timer >= 60) {
            // sabit 10 üzerinden her aktif üye başına %2 artar
            // sabit 10 üzerinden her 5 seviye başına level_blocker kadar sağlar
            // aktif oyuncu yoksa deneyim kazanılmaz.

            timer = 0;
            // seviye atlama
            addXP(toGainXP(true));

        }

    }

}
