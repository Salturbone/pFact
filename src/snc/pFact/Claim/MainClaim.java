package snc.pFact.Claim;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.ZChestLibV6.ButtonNode;
import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.ZChestLibV6.ChestNode;
import me.Zindev.utils.ZChestLibV6.ItemNode;
import me.Zindev.utils.data.SoundData;
import snc.pFact.Claim.AdditionalClaims.ICraftingClaim;
import snc.pFact.GUIs.ClaimMenuGUI;
import snc.pFact.GUIs.GoToCraftingButton;
import snc.pFact.GUIs.ShowBordersButton;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.utils.SerItem;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

public class MainClaim extends Claim implements ICraftingClaim {

    private static final long serialVersionUID = -7709034004024030740L;
    private SerItem[] shards;
    private SerItem levelItem;
    private long untilEnd;
    private boolean crafting, ended;

    public MainClaim(int length, ItemStack claimBlock, ItemStack shard, Color color) {
        super(length, claimBlock, shard, color, 0);
    }

    @Override
    public void update() {
        if (untilEnd >= 0) {
            untilEnd -= ((double) ClaimFactory.interval / 20) * 1000L * getMultipliers();
        }
        check();
    }

    @Override
    public void setup(String faction, Location center) {
        super.setup(faction, center);
        this.shards = new SerItem[4];
        this.ended = false;
        this.untilEnd = 0L;
        this.levelItem = null;
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
        MainClaim mc = (MainClaim) super.clone();
        mc.shards = new SerItem[4];
        mc.levelItem = null;
        mc.ended = false;
        return mc;
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
        nodes.add(new GoToCraftingButton(this));
        // show
        nodes.add(new ShowBordersButton(new ItemStack(Material.PAINTING)));
        return nodes;
    }

    @Override
    public SingularItem getSingularItem() {
        return new ItemNode(claimData().getItemStack("displayItem"));
    }

    @Override
    public SerItem[] getShards() {
        return shards;
    }

    @Override
    public SerItem getLevelItem() {
        return this.levelItem;
    }

    @Override
    public void setLevelItem(SerItem is) {
        this.levelItem = is;
    }

    @Override
    public long untilEnd() {
        return untilEnd;
    }

    @Override
    public void setUntilEnd(long end) {
        untilEnd = end;
    }

    @Override
    public boolean didEnd() {
        return ended;
    }

    @Override
    public void setEnded(boolean bool) {
        this.ended = bool;
    }

    @Override
    public boolean isCrafting() {
        return crafting;
    }

    @Override
    public void setCrafting(boolean bool) {
        crafting = bool;
    }

    @Override
    public double getMultipliers() {
        return Math.floor(Math.pow(1.25, Math.pow(getFaction().getVIPCount(), 3 / 4)));
    }

}