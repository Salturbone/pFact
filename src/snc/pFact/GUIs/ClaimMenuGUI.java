package snc.pFact.GUIs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.Itemizer.Itemizer;
import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.ZChestLibV6.ChestNode;
import me.Zindev.utils.ZChestLibV6.ChestNode.ChestGUIClickEvent;
import me.Zindev.utils.ZChestLibV6.GUIArray;
import me.Zindev.utils.ZChestLibV6.ItemNode;
import snc.pFact.Main;
import snc.pFact.Claim.Claim;

/**
 * ClaimMenuGUI
 */
public class ClaimMenuGUI extends ChestGUI {

    private static final ItemStack black_pane = Itemizer.wrap(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15))
            .setDisplayName("").build();

    private Claim cl;
    private GUIArray<ChestNode> nodes;

    public ClaimMenuGUI(Player p, Claim cl) {
        this.cl = cl;
        initialize(p, newChestGUIMeta().setManager(Main.cm).setRowSize(6).setTitle("Claim Menu"));
    }

    @Override
    protected void doRefresh() {
    }

    @Override
    protected boolean selfClick(ChestGUIClickEvent e) {
        return true;
    }

    @Override
    protected void firstOpen() {
        List<ChestNode> cnodes = cl.getConfigurableList(this);
        List<Integer> slots = new ArrayList<Integer>();
        if (cnodes.size() == 3) {
            slots.addAll(Arrays.asList(4, 11, 15));
        }
        if (cnodes.size() == 4) {
            slots.addAll(Arrays.asList(4, 11, 13, 15));
        }
        if (cnodes.size() == 5) {
            slots.addAll(Arrays.asList(4, 10, 12, 14, 16));
        }
        for (int i = 0; i < getRowSize() * 9; i++) {
            if (slots.contains(i))
                content[i] = new ItemNode(black_pane);
        }
        nodes = new GUIArray<ChestNode>(this, slots, cnodes);
        groups.add(nodes);
    }

    public Claim getClaim() {
        return cl;
    }

}