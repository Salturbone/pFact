package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import snc.pFact.obj.cl.b_Player.Rank;

public class b_Faction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static HashMap<String, b_Faction> factions = new HashMap<String, b_Faction>();
	
	private String name;
	private int level;
	private int member_count;
	private double xp;
	private double prestige;
	private HashMap<UUID, b_Player> players;
	private b_Player founder;
	
	/*public b_Faction(String name, int level, int member_count, double xp, double prestige, HashMap<UUID, b_Player> players) {
		this.name = name;
		this.level = level;
		this.member_count = member_count;
		this.xp = xp;
		this.prestige = prestige;
		this.players = players;
	}*/
	
	public b_Faction(String name, b_Player founder) {
        this.name = name;
        this.level = 0;
        this.member_count = 0;
        this.xp = 0;
        this.prestige = 0;
        players = new HashMap<UUID,b_Player>();
        players.put(founder.uuid(), founder);
        this.founder = founder;
    }
	
	//Level
	public int getLevel() {
		return level;
	}
	public void setLevel(int lvl) {
		level = lvl;
	}
	public void addLevel(int a) {
		level += a;
	}
	
	//Name
	public String getName() {
		return name;
	}
	
	//MemberCount
	public int getMemberCount() {
		return member_count;
	}
	public void setMemberCount(int mc) {
		member_count = mc;
	}
	public void addMember(UUID idd, b_Player pp) {
		players.put(idd, pp);
		member_count += 1;
	}
	
	public double getXP() {
		return xp;
	}
	
	public double getPrst( ) {
		return prestige;
	}
	
	public b_Player getPlayer(UUID id) {
		return players.get(id);
	}
	
	public b_Player getFounder() {
		return founder;
	}
	
	public List<b_Player> getByRank(Rank rank) {
		List<b_Player> list = new ArrayList<b_Player>();
		for(b_Player bp : players.values()) {
			if(bp.rank() == rank)
				list.add(bp);
		}
		return list;
	}
	
}
