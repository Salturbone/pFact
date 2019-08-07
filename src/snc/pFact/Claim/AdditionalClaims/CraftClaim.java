package snc.pFact.Claim.AdditionalClaims;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.ZChestLibV6.ChestNode;
import snc.pFact.Claim.AdditionalClaim;

/**
 * CraftClaim
 */
public class CraftClaim extends AdditionalClaim {

    private static final long serialVersionUID = 1L;

    public CraftClaim(int length, ItemStack claimBlock, ItemStack shard, int health) {
        super(length, claimBlock, shard, health);
    }

    @Override
    public List<ChestNode> getConfigurableList(ChestGUI arg0) {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public String getName() {
        return "craftClaim";
    }

    @Override
    public int getMaxToHave() {
        int fctLevel = getFaction().getLevel();
        List<Integer> ints = new ArrayList<Integer>();
        ints.addAll(Arrays.asList(10, 20, 30));
        int max = 0;
        for (int i = 0; i < ints.size(); i++) {
            if (fctLevel < ints.get(i)) {
                max = i;
            }
        }
        return max;
    }

    @Override
    public int getLevel() {
        return 2;
    }

}