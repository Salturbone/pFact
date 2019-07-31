package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.UUID;

public class b_Player implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private b_Faction fct = null;
	private double coin;

	public b_Player(UUID id, b_Faction fct, double coin) {
		this.id = id;
		if (fct != null) {
			if (fct.players.get(id).id == id) {
				this.fct = fct;
			} else {
				this.fct = null;
			}
		}
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

}
