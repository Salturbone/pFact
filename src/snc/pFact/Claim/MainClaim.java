package snc.pFact.Claim;

import org.bukkit.inventory.ItemStack;

public class MainClaim extends Claim {

    private static final long serialVersionUID = 1L;

    public MainClaim(int length, ItemStack claimBlock, ItemStack shard) {
        super(length, claimBlock, shard);
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
}