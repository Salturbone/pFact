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
    private int member_count;
    private double xp;
    private double prestige;
    public HashMap<UUID, B_FactionMember> players;
    public int timer = 0;
    private double level_block = 1;
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
        this.level = 0;
        this.member_count = 0;
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
        return member_count;
    }

    public void setMemberCount(int mc) {
        member_count = mc;
    }

    public void addMember(UUID idd) {
        players.put(idd, new B_FactionMember(idd));
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
        return (1 / level_block) * (25 + 25 * level * level);
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

    public List<AdditionalClaim> getAdditionalClaims() {
        return addClaims;
    }

    public Square3D getMaxClaimArea() {
        if (mainClaim == null)
            return null;
        return new Square3D(mainClaim.getSquare().center(), getMaxClaimLength());
    }

    public int getMaxClaimLength() {
        return 22 + (int) Math.min(Math.floor((double) level / 10) * 12, 12);
    }

    public void disband() {
        List<Claim> cls = new ArrayList<Claim>(getAdditionalClaims());
        cls.add(mainClaim);
        for (Claim cl : cls) {
            Block bl = cl.getCenterBlock().getBlock();
            bl.setType(Material.AIR);
        }
    }

    public void update() {
        timer++;
        // klanların xp kazanma mekaniği
        if (timer == 60) {
            // sabit 10 üzerinden her aktif üye başına %2 artar
            // sabit 10 üzerinden her 5 seviye başına level_blocker kadar sağlar
            // aktif oyuncu yoksa deneyim kazanılmaz.
            int on = 0;
            for (UUID idd : players.keySet()) {
                if (Bukkit.getPlayer(idd) != null && Bukkit.getPlayer(idd).isOnline()) {
                    on++;
                }
            }
            if (on != 0) {
                addXP(1.0 * (100 + (2 * Math.sqrt(on * on * on))) / 100 * level_block);
            }
            timer = 0;
            // seviye atlama
            if (xp >= (1 / level_block) * (25 + 25 * level * level)) {
                xp -= (1 / level_block) * (25 + 25 * level * level);
                level += 1;
                for (B_FactionMember bfm : players.values()) {
                    if (bfm.isOnline()) {
                        Bukkit.getPlayer(bfm.uuid()).sendMessage(ChatColor.GREEN + "Klanın seviye atladı!");
                        Bukkit.getPlayer(bfm.uuid())
                                .sendMessage(ChatColor.GREEN + "Yeni Klan Seviyesi: " + ChatColor.RESET + level);
                    }
                }
            }
            xp = Math.floor(xp * 1000) / 1000;
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
