package snc.pFact.GUIs;

import org.bukkit.entity.Player;

import me.Zindev.utils.ZChestLibV6.ChestGUI;
import snc.pFact.Main;
import snc.pFact.Claim.Claim;

/**
 * CraftClaimGUI
 */
public class CraftClaimGUI extends ChestGUI {

    private Claim cl;

    public CraftClaimGUI(Player p, Claim cl) {
        initialize(p, newChestGUIMeta().setManager(Main.cm).setRowSize(6));
        this.cl = cl;
    }

    @Override
    protected void doRefresh() {

    }

    @Override
    protected void firstOpen() {

    }

    public Claim getClaim() {
        return cl;
    }

}