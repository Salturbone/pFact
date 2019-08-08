package snc.pFact.GUIs;

import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.ZChestLibV6.ButtonNode;
import me.Zindev.utils.ZChestLibV6.ChestGUI;
import snc.pFact.Claim.Claim;

/**
 * ShowBordersButton
 */
public class ShowBordersButton extends ButtonNode {

    public ShowBordersButton(ItemStack is) {
        super(is);
    }

    @Override
    public void onClick(ChestGUI arg0, ChestGUIClickEvent arg1) {
        Claim cl = ((ClaimMenuGUI) arg0).getClaim();
        if (arg1.isShiftClick()) {
            cl.showCorners(10 * 20, arg0.getUser());
        } else {
            cl.showEgg(10 * 20, arg0.getUser());
        }
    }

}