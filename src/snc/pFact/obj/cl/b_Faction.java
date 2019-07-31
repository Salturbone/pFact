package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class b_Faction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int level;
	private int member_count;
	private double xp;
	private double prestige;
	HashMap<UUID, b_Player> players;
	
	public b_Faction(String name, int level, int member_count, double xp, double prestige, HashMap<UUID, b_Player> players) {
		this.name = name;
		this.level = level;
		this.member_count = member_count;
		this.xp = xp;
		this.prestige = prestige;
		this.players = players;
	}
	
}
