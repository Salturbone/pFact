package snc.pFact.GUIs;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.Itemizer.Itemizer;
import me.Zindev.utils.ZChestLibV6.ButtonNode;
import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.ZChestLibV6.ChestNode.ChestGUIClickEvent;
import me.Zindev.utils.ZChestLibV6.GUIArray;
import me.Zindev.utils.ZChestLibV6.ItemNode;
import me.Zindev.utils.data.SoundData;
import snc.pFact.Main;
import snc.pFact.Claim.Claim;
import snc.pFact.Claim.AdditionalClaims.ICraftingClaim;
import snc.pFact.Claim.AdditionalClaims.ICraftingClaim.CraftingState;
import snc.pFact.Claim.AdditionalClaims.ICraftingClaim.CraftingTime;
import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.utils.Gerekli;
import snc.pFact.utils.ZSIGN;

/**
 * CraftClaimGUI
 */
public class CraftClaimGUI extends ChestGUI {
    private static final ItemStack black_pane = Itemizer.wrap(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15))
            .setDisplayName("").build();

    private ICraftingClaim icl;

    private GUIArray<PlaceableButtonNode> placeables;

    public CraftClaimGUI(Player p, ICraftingClaim icl) {
        initialize(p, newChestGUIMeta().setManager(Main.cm).setRowSize(6));
        this.icl = icl;

    }

    @Override
    protected void doRefresh() {

    }

    @Override
    protected void firstOpen() {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        ints.addAll(Arrays.asList(21, 12, 20, 22, 30));
        placeables = new GUIArray<>(this, ints, icl.getShardLevelButtons());
        for (int i = 0; i < getRowSize() * 9; i++) {
            if (i == 0) {
                content[i] = getBackButton();
            } else if (i == 14) {
                content[i] = getInfoSign();
            } else if (i == 33) {
                content[i] = getClaimButton();
            } else {
                content[i] = new ItemNode(black_pane);
            }
        }
        groups.add(placeables);
    }

    public ICraftingClaim getClaim() {
        return icl;
    }

    @Override
    protected boolean selfClick(ChestGUIClickEvent e) {
        ItemStack is = e.getCurrentItem();
        if (is == null || is.getType() == Material.AIR)
            return e.getCursor() == null || e.getCursor().getType() == Material.AIR;
        return !ZSIGN.sorImzaZ(is, "levelItem") && !ZSIGN.sorImzaZ(is, "shard");
    }

    ItemNode getInfoSign() {
        return new ItemNode(new ItemStack(Material.PAINTING)) {
            @Override
            public ItemStack getStack(ChestGUI gui) {
                ArrayList<String> lore = new ArrayList<String>();

                Claim crafting = icl.getCrafting();
                CraftingTime ct = icl.getCraftingTime();
                if (ct == CraftingTime.NO_CRAFT) {
                    lore.add("Currently: Empty");
                    if (crafting != null) {
                        lore.add("Time to craft: "
                                + Gerekli.getRemainingTime(crafting.getCraftTime() / (long) icl.getMultipliers()));
                    }

                } else {
                    lore.add("Currently: " + crafting.getDisplayName());
                    if (ct == CraftingTime.CRAFTED) {
                        lore.add("Status: Completed");
                    } else {
                        lore.add("Status: Crafting");
                        lore.add("Remaining Time: " + Gerekli.getRemainingTime(icl.getMultipliedEnd()));
                    }
                }
                return Itemizer.wrap(new ItemStack(Material.SIGN)).setDisplayName("Craft Info").setLore(lore).build();

            }
        };
    }

    ButtonNode getClaimButton() {
        return new ButtonNode(null) {

            @Override
            public ItemStack getStack(ChestGUI gui) {
                ArrayList<String> lore = new ArrayList<String>();
                Claim cl = icl.getCrafting();
                CraftingTime ct = icl.getCraftingTime();

                if (ct == CraftingTime.NO_CRAFT) {
                    CraftingState cs = icl.getCraftingState();
                    if (cs == CraftingState.READY) {
                        lore.add("Click to start craft");
                        return Itemizer.wrap(cl.getDisplayItem()).getLore().addAll(lore).getBack().build();
                    }
                    if (cs == CraftingState.NOT_ENOUGH_ITEMS) {
                        lore.add("need to put shards and a core");
                    } else if (cs == CraftingState.DIFFERENT_SHARDS) {
                        lore.add("shards doesn't match");
                    } else if (cs == CraftingState.SHARD_LEVEL_DIFF) {
                        lore.add("shards and level doesn't match");
                    }

                    return Itemizer.wrap(new ItemStack(Material.BARRIER)).setDisplayName("To Craft").setLore(lore)
                            .build();

                } else if (ct == CraftingTime.CRAFTING) {
                    lore.add("Shift+ Right click to cancel");
                    return Itemizer.wrap(cl.getDisplayItem()).getLore().addAll(lore).getBack().build();
                } else {
                    lore.add("Click to get claim");
                    return Itemizer.wrap(cl.getDisplayItem()).getLore().addAll(lore).getBack().build();
                }
            }

            @Override
            public void onClick(ChestGUI chest, ChestGUIClickEvent e) {
                Player p = chest.getUser();
                B_Faction bf = DataIssues.players.get(p.getUniqueId()).getF();
                if (bf == null)
                    return;
                CraftingTime ct = icl.getCraftingTime();
                Claim cl = icl.getCrafting();
                if (ct == CraftingTime.NO_CRAFT) {
                    if (cl == null) {
                        new SoundData(1f, 1f, Sound.BLOCK_ANVIL_FALL).play(p);
                        return;
                    }
                    icl.startCrafting();
                    new SoundData(1f, 1f, Sound.ENTITY_PLAYER_LEVELUP).play(p);
                    return;
                } else if (ct == CraftingTime.CRAFTING) {
                    if (e.getClick() == ClickType.SHIFT_RIGHT) {
                        icl.cancel();
                        new SoundData(1f, 1f, Sound.BLOCK_FIRE_EXTINGUISH).play(p);
                        return;
                    }
                } else if (ct == CraftingTime.CRAFTED) {
                    ItemStack is = cl.getClaimItem(bf.getName());
                    p.getInventory().addItem(is).values()
                            .forEach(iss -> p.getWorld().dropItemNaturally(p.getLocation(), iss));
                    icl.clear();
                    new SoundData(1f, 1f, Sound.ENTITY_PLAYER_LEVELUP).play(p);
                    return;
                }
                new SoundData(1f, 1f, Sound.BLOCK_ANVIL_FALL).play(p);
            }

        };
    }

    ButtonNode getBackButton() {
        ItemStack is = new ItemStack(Material.REDSTONE_BLOCK);
        is = Itemizer.wrap(is).setDisplayName("Get Back").build();
        return new ButtonNode(is) {

            @Override
            public void onClick(ChestGUI arg0, ChestGUIClickEvent arg1) {
                switchTo(getBackGui());
            }
        };
    }

}