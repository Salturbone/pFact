package snc.pFact.Claim;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.ZChestLibV6.ButtonNode;
import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.ZChestLibV6.ChestNode;
import me.Zindev.utils.ZChestLibV6.ItemNode;
import me.Zindev.utils.data.SoundData;
import snc.pFact.GUIs.ClaimMenuGUI;
import snc.pFact.GUIs.CraftClaimGUI;
import snc.pFact.GUIs.ShowBordersButton;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

public class MainClaim extends Claim {

    private static final long serialVersionUID = 1L;

    public MainClaim(int length, ItemStack claimBlock, ItemStack shard, Color color) {
        super(length, claimBlock, shard, color);
    }

    @Override
    public void update() {

    }

    @Override
    public String getName() {
        return "mainClaim";
    }

    /*
     * @Override protected ItemStack doGetClaimItem(B_Faction fact) { ItemStack item
     * = new ItemStack(Material.FEATHER, 1); ItemMeta im = item.getItemMeta();
     * 
     * im.setDisplayName(ChatColor.GREEN + "Klan Ana Alanı Seçme Aracı");
     * 
     * ArrayList<String> lore = new ArrayList<String>(); lore.add(ChatColor.YELLOW +
     * "Bu eşya ile sağ tıkladığınızda"); lore.add(ChatColor.YELLOW +
     * "Klan evinizi kuracağınız alanı"); lore.add(ChatColor.YELLOW +
     * "belirlersiniz. Bu alan 41x41 olarak"); lore.add(ChatColor.YELLOW +
     * "tanımlanmıştır ve bir defa yapıldığında"); lore.add(ChatColor.YELLOW +
     * "geri alınamaz! Dikkatli seç!"); im.setLore(lore);
     * im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
     * 
     * item.setItemMeta(im); return item; }
     */

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    protected MainClaim clone() {
        return (MainClaim) super.clone();
    }

    @Override
    public List<ChestNode> getConfigurableList(ChestGUI arg0) {
        List<ChestNode> nodes = new ArrayList<>();
        // egg
        nodes.add(getSingularItem());
        // break
        nodes.add(new ButtonNode(ClaimFactory.breakClaim.getItemStack()) {

            @Override
            public void onClick(ChestGUI arg0, ChestGUIClickEvent arg1) {
                MainClaim cl = (MainClaim) ((ClaimMenuGUI) arg0).getClaim();
                if (cl == null)
                    return;
                B_Faction fact = cl.getFaction();
                if (fact.getAdditionalClaims().size() != 0) {
                    return;
                }
                arg0.getUser().getInventory().addItem(cl.getClaimItem(fact.getName()));
                cl.getCenterBlock().getBlock().setType(Material.AIR);
                fact.setMainClaim(null);
                new SoundData(1f, 1f, Sound.BLOCK_GLASS_BREAK).play(arg0.getUser());
                arg0.close(true);
            }
        });
        // goto crafting gui
        nodes.add(new ButtonNode(null) {

            @Override
            public void onClick(ChestGUI arg0, ChestGUIClickEvent arg1) {
                arg0.switchTo(new CraftClaimGUI(arg0.getUser(), ((ClaimMenuGUI) arg0).getClaim()));
            }

            @Override
            public ItemStack getStack(ChestGUI gui) {

                return new ItemStack(Material.WORKBENCH);
            }
        });
        // show
        nodes.add(new ShowBordersButton(new ItemStack(Material.PAINTING)));
        return nodes;
    }

    @Override
    public SingularItem getSingularItem() {
        return new ItemNode(claimData().getItemStack("displayItem"));
    }
}