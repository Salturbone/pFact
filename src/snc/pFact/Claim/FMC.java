package snc.pFact.Claim;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;

public class FMC implements Claim, Listener {

    private ItemStack FMC_Item = null;
    private Location FMC_center;
    int FMC_R = 20;
    int FMC_r = 4;
    private int[][] as;

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getItem().equals(FMC_Item) && e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (!ClaimCollision(DataIssues.players.get(e.getPlayer().getUniqueId()).getF(),  e.getClickedBlock().getLocation())) {
                e.getPlayer().getInventory().removeItem(e.getItem());
                e.getClickedBlock().getLocation(FMC_center);
            } else {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.DARK_RED + "Seçtiğin nokta başka bir klanın arazisiyle çakışıyor!");
            }
            
        }
    }
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Location lc = e.getBlock().getLocation();
        for (B_Faction fct : DataIssues.factions.values()) {
            int[][] a = fct.GetClaim().CalculateClaimArea();
            int x2 = a[0][0];
            int z2 = a[0][1];
            int w2 = a[1][0] - x2;
            int d2 = a[1][1] - z2;
            if (lc.getBlockX() >= x2 &&
                lc.getBlockZ() >= z2 &&
                lc.getBlockX() <= x2 + w2 &&
                lc.getBlockZ() <= z2 + d2 &&
                DataIssues.players.get(e.getPlayer().getUniqueId()).getF() != fct) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.DARK_RED + "Bu bloğu kırdığın bölge bir klana ait!");
                return;
            }
        }
    }



    private boolean ClaimCollision(B_Faction b, Location lc) {
        int x1,z1,w1,d1;
        x1 = lc.getBlockX() - FMC_R;
        z1 = lc.getBlockZ() - FMC_R;
        w1 = (lc.getBlockX() + FMC_R) - x1;
        d1 = (lc.getBlockZ() + FMC_R) - z1;
        for (B_Faction fct : DataIssues.factions.values()) {
            if (b != fct) {
                int[][] a = fct.GetClaim().CalculateClaimArea();
                int x2 = a[0][0];
                int z2 = a[0][1];
                int w2 = a[1][0] - x2;
                int d2 = a[1][1] - z2;
                if(x1 < x2 + w2 &&
                    x1 + w1 > x2 &&
                    z1 < z2 + d2 &&
                    z1 + d1 > z2) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void GiveClaimItem(Player p) {
        p.getInventory().addItem(FMC_Item);
    }

	@Override
	public void CreateClaimItem() {
		FMC_Item = new ItemStack(Material.FEATHER, 1);
        ItemMeta im = FMC_Item.getItemMeta();

        im.setDisplayName(ChatColor.GREEN + "Klan Ana Alanı Seçme Aracı");

        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.YELLOW + "Bu eşya ile sağ tıkladığınızda");
        lore.add(ChatColor.YELLOW + "Klan evinizi kuracağınız alanı");
        lore.add(ChatColor.YELLOW + "belirlersiniz. Bu alan 41x41 olarak");
        lore.add(ChatColor.YELLOW + "tanımlanmıştır ve bir defa yapıldığında");
        lore.add(ChatColor.YELLOW + "geri alınamaz! Dikkatli seç!");
        
        im.setLore(lore);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        FMC_Item.setItemMeta(im);
	}

    @Override
    public int[][] CalculateClaimArea() {
        as[0][0] =  FMC_center.getBlockX() - FMC_R;
        as[0][1] =  FMC_center.getBlockZ() - FMC_R;
        as[1][0] =  FMC_center.getBlockX() + FMC_R;
        as[1][1] =  FMC_center.getBlockZ() + FMC_R;
        return as;

    }

    public int[][] CalculateMainClaimArea() {
        as[0][0] =  FMC_center.getBlockX() - FMC_r;
        as[0][1] =  FMC_center.getBlockZ() - FMC_r;
        as[1][0] =  FMC_center.getBlockX() + FMC_r;
        as[1][1] =  FMC_center.getBlockZ() + FMC_r;
        return as;
    }


 }