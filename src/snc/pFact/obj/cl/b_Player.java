package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

enum Auth
{ 
    OWNER,MODERATOR; 
} 

public class b_Player implements Serializable {

	private static final long serialVersionUID = 1L;

	public static HashMap<UUID, b_Player> players = new HashMap<UUID, b_Player>();
	
	public enum Rank {
		Single,Player, Moderator, Founder;
	}
	
	private UUID id;
	private b_Faction fct = null;
	private double coin;
	private Rank rank = Rank.Single;
	
	
	public b_Player(UUID id, b_Faction fct, double coin, Rank rank) {
		this.id = id;
		if (fct != null) {
			if (fct.getPlayer(id).id == id) {
				this.fct = fct;
			} else {
				this.fct = null;
			}
		}
		this.rank = rank;
		this.coin = coin;
	}

	// Faction
	public void setF(b_Faction fct) {
		this.fct = fct;
	}

	public b_Faction getF() {
		return fct;
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
	
}
