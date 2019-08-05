package snc.pFact.utils;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SerItem implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    public static transient ItemStack NULLITEM = SerItem.yapEsya(new ItemStack(Material.BARRIER), "#EMPTY", null,
            (short) 0);
    private String itemstack;

    public SerItem(ItemStack is) {
        itemstack = ZISU64.cevIS(is);
    }

    @Override
    public SerItem clone() {
        try {
            SerItem clone = (SerItem) super.clone();
            clone.setItemStack(getItemStack().clone());
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNull(SerItem is) {
        if (is == null)
            return true;
        if (is.getItemStack() == null)
            return true;
        if (is.getItemStack().equals(NULLITEM))
            return true;
        if (is.getItemStack().getType().equals(Material.AIR))
            return true;
        return false;

    }

    public void setItemStack(ItemStack is) {
        itemstack = ZISU64.cevIS(is);
    }

    public ItemStack getItemStack() {
        return ZISU64.yukleIS(itemstack);
    }

    @Override
    public String toString() {
        String a = "&c";
        ItemMeta im = getItemStack().getItemMeta();
        if (im.hasDisplayName()) {
            a = im.getDisplayName();
        } else {
            a = getItemStack().getType().name();
        }
        a += "&c(" + getItemStack().getDurability() + "):" + getItemStack().getAmount();
        return a;
    }

    public static ItemStack yapEsya(ItemStack is, String isim, ArrayList<String> lore, short altid) {
        ItemStack esya = is;
        ItemMeta esyam = esya.getItemMeta();
        if (isim != null) {
            esyam.setDisplayName(ChatColor.translateAlternateColorCodes('&', isim));
        }
        if (lore != null) {
            ArrayList<String> lored = new ArrayList<String>();
            for (String str : lore) {
                lored.add(ChatColor.translateAlternateColorCodes('&', str));
            }

            esyam.setLore(lored);
        }
        if (altid > 0) {
            esya.setDurability(altid);
        }
        esya.setItemMeta(esyam);
        return is;

    }

}
