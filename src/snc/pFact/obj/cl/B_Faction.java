package snc.pFact.obj.cl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import snc.pFact.Main;
import snc.pFact.Claim.AdditionalClaim;
import snc.pFact.Claim.Claim;
import snc.pFact.Claim.MainClaim;
import snc.pFact.utils.Msgs;
import snc.pFact.utils.SerLocation;
import snc.pFact.utils.Square3D;

public class B_Faction implements Serializable {

    private static final long serialVersionUID = -5192871401095680611L;
    public static int maxMaxClaimLength = 81;

    private void readObject(ObjectInputStream in) {
        try {
            in.defaultReadObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        raidState = RaidState.NO_RAID;
    }

    private String name;
    private UUID uid;
    private int level;
    private double xp;
    private double prestige;
    private HashMap<UUID, B_FactionMember> players;
    public transient int timer = 0;
    private double bank = 0;
    private transient B_FactionMember founder;
    private MainClaim mainClaim;
    private List<AdditionalClaim> addClaims = new ArrayList<AdditionalClaim>();
    private transient RaidState raidState = RaidState.NO_RAID;
    private List<RaidHistory> rh = new ArrayList<RaidHistory>();
    private SerLocation home = null;

    /*
     * public b_Faction(String name, int level, int member_count, double xp, double
     * prestige, HashMap<UUID, b_Player> players) { this.name = name; this.level =
     * level; this.member_count = member_count; this.xp = xp; this.prestige =
     * prestige; this.players = players; }
     */

    public B_Faction(String name, UUID founder) {
        uid = UUID.randomUUID();
        this.name = name;
        this.level = 1;
        this.xp = 0;
        this.prestige = 0;
        players = new HashMap<UUID, B_FactionMember>();
        this.founder = new B_FactionMember(founder);
        this.founder.setRank(Rank.Founder);
        players.put(founder, this.founder);
        this.raidState = RaidState.NO_RAID;
        rh = new ArrayList<RaidHistory>();
    }

    public UUID getUUID() {
        return uid;
    }

    public SerLocation getHomeSerLocation() {
        return home;
    }

    public void setSerHome(SerLocation loc) {
        home = loc;
    }

    public Location getHomeLocation() {
        return home.getLocation();
    }

    public void setHome(Location loc) {
        home = new SerLocation(loc);
    }

    public void tpPlayerToHome(B_Player bp) {
        if (home != null) {
            bp.warp(getHomeLocation());
        } else {
            bp.getPlayer().sendMessage(Msgs.NO_HOME.sub);
        }
    }

    public void addRaidHistory(RaidHistory h) {
        rh.add(h);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int lvl) {
        level = lvl;
    }

    public RaidState getRaidState() {
        return raidState;
    }

    public void setRaidState(RaidState state) {
        raidState = state;
    }

    public void addLevel(int a) {
        level += a;
    }

    public HashMap<UUID, B_FactionMember> getFactionMembers() {
        return players;
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
        int on = getOnlinePlayers().size();
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
            for (Player p : getOnlinePlayers()) {
                p.sendTitle(Msgs.FACTION_LEVEL_UP_TITLE.sub,
                        Msgs.FACTION_LEVEL_UP_SUBTITLE.sub.replaceAll("<level>", level + ""), -1, -1, -1);
            }
        }
    }

    public List<Player> getOnlinePlayers() {
        List<Player> list = new ArrayList<>();
        for (B_FactionMember bfm : players.values()) {
            if (bfm.isOnline()) {
                list.add(Bukkit.getPlayer(bfm.uuid()));
            }
        }
        return list;
    }

    public int getVIPCount() {
        int i = 0;
        for (B_FactionMember bfm : players.values())
            if (bfm.isVIP())
                i++;
        return i;
    }

    public double getLevelUpXp() {
        return (1 / getLevelBlock()) * (5 * level * level * level + 15 * level * level + 20 * level + 55);
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
        getFounder().setRank(Rank.Moderator);
        this.founder = nfounder;

        this.founder.setRank(Rank.Founder);
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
        if (mainClaim != null)
            cls.add(mainClaim);
        return cls;
    }

    public void update() {
        timer++;
        // klanların xp kazanma mekaniği
        if (timer >= (20 / Main.taskRepeating) * 60) {
            // sabit 10 üzerinden her aktif üye başına %2 artar
            // sabit 10 üzerinden her 5 seviye başına level_blocker kadar sağlar
            // aktif oyuncu yoksa deneyim kazanılmaz.

            timer = 0;
            // seviye atlama
            addXP(toGainXP(true));

        }
    }
}
