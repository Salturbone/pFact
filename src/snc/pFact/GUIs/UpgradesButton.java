package snc.pFact.GUIs;

import java.lang.reflect.Field;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.ZChestLibV6.ButtonNode;
import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.data.SoundData;
import snc.pFact.Claim.ClaimFactory;
import snc.pFact.Claim.AdditionalClaims.UpgradeableClaim;
import snc.pFact.Claim.Upgrade.ClaimUpgrade;

/**
 * UpgradesButton
 */
public class UpgradesButton extends ButtonNode {
    public UpgradesButton() {
        super(null);
    }

    @Override
    public ItemStack getStack(ChestGUI gui) {
        UpgradeableClaim cl = (UpgradeableClaim) ((ClaimMenuGUI) gui).getClaim();
        if (cl.getUpgrades().size() == 1) {
            ClaimUpgrade cu = cl.getUpgrades().get(0);
            return cu.getUpgradeItem();
        } else {
            return ClaimFactory.noUpgradeItem.getItemStack();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(ChestGUI gui, ChestGUIClickEvent e) {
        UpgradeableClaim cl = (UpgradeableClaim) ((ClaimMenuGUI) gui).getClaim();
        Player p = gui.getUser();
        if (cl.getUpgrades().size() == 1) {
            ClaimUpgrade cu = cl.getUpgrades().get(0);
            p.getInventory().addItem(cu.getUpgradeItem()).values().forEach(is -> {
                p.getWorld().dropItemNaturally(p.getLocation(), is);
            });
            cl.getUpgrades().clear();
            new SoundData(1f, 1f, Sound.ENTITY_EXPERIENCE_ORB_PICKUP).play(p);
            return;
        }
        ItemStack is = e.getCursor();
        if (is != null && is.getType() != Material.AIR) {
            ClaimUpgrade cu = ClaimFactory.getUpgradeFromItemStack(is);
            if (cu == null) {
                new SoundData(1f, 1f, Sound.BLOCK_ANVIL_LAND).play(p);
                return;
            }
            cl.getUpgrades().add(cu);
            new SoundData(1f, 1f, Sound.ENTITY_EXPERIENCE_ORB_PICKUP).play(p);
            try {

                Field evField = e.getClass().getDeclaredField("e");
                evField.setAccessible(true);
                InventoryClickEvent ev = (InventoryClickEvent) evField.get(e);
                is.setAmount(is.getAmount() - 1);
                ev.setCursor(is);

            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
                e1.printStackTrace();
            }

            return;
        }

        new SoundData(1f, 1f, Sound.BLOCK_ANVIL_LAND).play(p);
        return;
    }
}