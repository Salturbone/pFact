package snc.pFact.Claim;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;

public class FAC implements Claim, Listener {

    private ItemStack FAC_Item = null;
    private Location FAC_center;
    private int t = 0;
    int FMC_R = 4;

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getItem().equals(FAC_Item) && e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if(!ClaimCollision(DataIssues.players.get(e.getPlayer().getUniqueId()).getF(), e.getClickedBlock().getLocation())) {
                e.getPlayer().getInventory().removeItem(e.getItem());
                e.getClickedBlock().getLocation(FAC_center);
            } else {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.DARK_RED + "Seçtiğin nokta başka bir klanın arazisiyle çakışıyor!");
            }
        }
            
    }

    private boolean ClaimCollision(B_Faction b, Location lc) {
        int x1,z1,w1,d1;
        x1 = lc.getBlockX() - FMC_R;
        z1 = lc.getBlockZ() - FMC_R;
        w1 = (lc.getBlockX() + FMC_R) - x1;
        d1 = (lc.getBlockZ() + FMC_R) - z1;
        int[][] a = ((FMC)b.GetClaim()).CalculateMainClaimArea();
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
        
        return false;
    }

    public void SetItemType(int a) {
        t = a;
    }
    public int GetType() {
        return t;
    }
    @Override
    public void GiveClaimItem(Player p) {
        p.getInventory().addItem(FAC_Item);
    }

    @Override
    public void CreateClaimItem() {
        if (t == 0) {
            FAC_Item = new ItemStack(Material.GOLD_INGOT, 1);
            ItemMeta im = FAC_Item.getItemMeta();

            im.setDisplayName(ChatColor.GREEN + "Klan Geliri Arttırıcı");

            ArrayList<String> lore = new ArrayList<String>();
            lore.add(ChatColor.YELLOW + "Bu eşya ile klan bölgenin içine");
            lore.add(ChatColor.YELLOW + "sağ tıklayarak kuracağın yeri");
            lore.add(ChatColor.YELLOW + "belirlersin. Bu işlem geri");
            lore.add(ChatColor.YELLOW + "alınamaz. Dikkatli seç!");

            im.setLore(lore);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            FAC_Item.setItemMeta(im);
            return;
        }
        if (t == 1) {
            FAC_Item = new ItemStack(Material.STICK, 1);
            ItemMeta im = FAC_Item.getItemMeta();

            im.setDisplayName(ChatColor.GREEN + "Klan Deneyim Kazancı Arttırıcı");

            ArrayList<String> lore = new ArrayList<String>();
            lore.add(ChatColor.YELLOW + "Bu eşya ile klan bölgenin içine");
            lore.add(ChatColor.YELLOW + "sağ tıklayarak kuracağın yeri");
            lore.add(ChatColor.YELLOW + "belirlersin. Bu işlem geri");
            lore.add(ChatColor.YELLOW + "alınamaz. Dikkatli seç!");

            im.setLore(lore);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            FAC_Item.setItemMeta(im);
            return;
        }
    }

    @Override
    public int[][] CalculateClaimArea() {
        return null;
    }

 }