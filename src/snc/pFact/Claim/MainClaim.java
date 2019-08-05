package snc.pFact.Claim;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import snc.pFact.obj.cl.B_Faction;
import snc.pFact.obj.cl.B_FactionMember;
import snc.pFact.obj.cl.B_Player;
import snc.pFact.obj.cl.B_Player.Rank;

public class MainClaim extends Claim {

    private static final long serialVersionUID = 1L;

    public MainClaim(int length, ItemStack claimBlock, ItemStack shard) {
        super(length, claimBlock, shard);
    }

    @Override
    public boolean canBreak(Player p, Location loc) {
        return canPlace(p, loc);
    }

    @Override
    public boolean canPlace(Player p, Location loc) {
        B_Faction bf = B_Player.getFactionOfPlayer(p.getUniqueId());
        if (bf == null || bf != getFaction())
            return false;
        B_FactionMember bfm = bf.getPlayer(p.getUniqueId());
        if (bfm == null)
            return false;
        if (bfm.rank() != Rank.Player)
            return true;
        return false;
    }

    @Override
    public void update() {

    }

    @Override
    public String getName() {
        return "mainClaim";
    }

    @Override
    public boolean canInteract(Player p, Location loc) {
        B_Faction bf = B_Player.getFactionOfPlayer(p.getUniqueId());
        if (bf == null || bf != getFaction())
            return false;
        return true;
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
}