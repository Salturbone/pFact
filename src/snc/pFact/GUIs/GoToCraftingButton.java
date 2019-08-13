
package snc.pFact.GUIs;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.Itemizer.Itemizer;
import me.Zindev.utils.ZChestLibV6.ButtonNode;
import me.Zindev.utils.ZChestLibV6.ChestGUI;
import snc.pFact.Claim.Claim;
import snc.pFact.Claim.AdditionalClaims.ICraftingClaim;
import snc.pFact.Claim.AdditionalClaims.ICraftingClaim.CraftingTime;
import snc.pFact.utils.Gerekli;

/**
 * GoToCraftingButton
 */
public class GoToCraftingButton extends ButtonNode {

    private ICraftingClaim icl;

    public GoToCraftingButton(ICraftingClaim icl) {
        super(null);
        this.icl = icl;
    }

    @Override
    public ItemStack getStack(ChestGUI gui) {

        ArrayList<String> lore = new ArrayList<String>();

        lore.add("Click to access crafting menu.");

        Claim crafting = icl.getCrafting();
        CraftingTime ct = icl.getCraftingTime();
        if (ct == CraftingTime.NO_CRAFT) {
            lore.add("Currently: Empty");
        } else {
            lore.add("Currently: " + crafting.getDisplayName());
            if (ct == CraftingTime.CRAFTED) {
                lore.add("Status: Completed");
            } else {
                lore.add("Status: Crafting");
                lore.add("Remaining Time: " + Gerekli.getRemainingTime(icl.getMultipliedEnd()));
            }
        }
        return Itemizer.wrap(new ItemStack(Material.WORKBENCH)).setDisplayName("Craft Menu").setLore(lore).build();
    }

    @Override
    public void onClick(ChestGUI arg0, ChestGUIClickEvent arg1) {
        ICraftingClaim icl = (ICraftingClaim) ((ClaimMenuGUI) arg0).getClaim();
        arg0.switchTo(new CraftClaimGUI(arg0.getUser(), icl));
    }

}