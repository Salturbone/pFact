package snc.pFact.Claim;

import org.bukkit.entity.Player;

public interface Claim {
    void GiveClaimItem(Player p);
    void CreateClaimItem();
    int[][] CalculateClaimArea();
}