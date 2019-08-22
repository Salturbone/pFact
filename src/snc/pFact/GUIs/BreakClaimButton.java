package snc.pFact.GUIs;

import org.bukkit.Sound;

import me.Zindev.utils.ZChestLibV6.ButtonNode;
import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.data.SoundData;
import snc.pFact.Claim.AdditionalClaim;
import snc.pFact.Claim.ClaimFactory;

/**
 * BreakClaimButton
 */
public class BreakClaimButton extends ButtonNode {

    public BreakClaimButton() {
        super(ClaimFactory.breakClaim.getItemStack());
    }

    @Override
    public void onClick(ChestGUI arg0, ChestGUIClickEvent arg1) {
        AdditionalClaim cl = (AdditionalClaim) ((ClaimMenuGUI) arg0).getClaim();
        if (cl == null)
            return;
        cl.breakClaim(false);
        new SoundData(1f, 1f, Sound.BLOCK_GLASS_BREAK).play(arg0.getUser());
        arg0.close(true);
    }
}