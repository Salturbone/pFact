package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class B_Player implements Serializable {

	private static final long serialVersionUID = 1L;

	public static HashMap<UUID, B_Player> players = new HashMap<UUID, B_Player>();
	
	public enum Rank {
		Single, Player, Moderator, Founder;
	}
	
	private UUID id;
	private B_Faction fct = null;
	private B_Faction e_fct = null;
	private boolean e_state = false;
	private double coin;
	private Rank rank = Rank.Single;
	
	
	public B_Player(UUID id, B_Faction fct, double coin, Rank rank) {
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
	public void setF(B_Faction fct) {
		this.fct = fct;
	}

	public B_Faction getF() {
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
	
	public B_Faction getEF() {
		return e_fct;
	}
	
	public void setEF(B_Faction fff) {
		e_fct = fff;
	}
	
	public boolean getES() {
		return e_state;
	}
	public void setES(boolean s) {
		e_state = s;
	}

	public boolean hasFaction() {
		if (rank == Rank.Single || fct == null) {
			rank = Rank.Single;
			fct = null;
			return false;
		}
		return true;
	}
	
}
