
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

/**
 * PlaceableButtonNode
 */
public abstract class PlaceableButtonNode extends ButtonNode {

    public PlaceableButtonNode() {
        super(null);
    }

    public abstract boolean hasItem();

    public abstract ItemStack noItem();

    public abstract ItemStack getItem();

    public abstract ItemStack giveItem();

    public abstract boolean canPlace(ItemStack is);

    public abstract void doPlace(ItemStack is);

    public abstract boolean canTake();

    public abstract void doTake();

    @Override
    public ItemStack getStack(ChestGUI gui) {
        if (!hasItem())
            return noItem();
        return getItem();
    }

    @Override
    public void onClick(ChestGUI gui, ChestGUIClickEvent e) {
        Player p = gui.getUser();
        if (!hasItem()) {
            ItemStack is = e.getCursor();
            if (is == null || is.getType() == Material.AIR || !canPlace(is)) {
                new SoundData(1f, 1f, Sound.BLOCK_ANVIL_LAND).play(p);
                return;
            }
            doPlace(is.clone());
            new SoundData(1f, 1f, Sound.ENTITY_EXPERIENCE_ORB_PICKUP).play(p);
            is.setAmount(is.getAmount() - 1);
            setCursor(e, is);

            return;
        } else {
            if (!canTake()) {
                new SoundData(1f, 1f, Sound.BLOCK_ANVIL_LAND).play(p);
                return;
            }
            p.getInventory().addItem(giveItem()).values().forEach(is -> {
                p.getWorld().dropItemNaturally(p.getLocation(), is);
            });
            new SoundData(1f, 1f, Sound.ENTITY_EXPERIENCE_ORB_PICKUP).play(p);
            doTake();
            return;
        }
    }

    @SuppressWarnings("deprecation")
    private void setCursor(ChestGUIClickEvent e, ItemStack is) {
        try {
            Field evField = e.getClass().getDeclaredField("e");
            evField.setAccessible(true);
            InventoryClickEvent ev = (InventoryClickEvent) evField.get(e);
            ev.setCursor(is);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }
}