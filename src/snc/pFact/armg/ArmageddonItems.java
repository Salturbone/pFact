package snc.pFact.armg;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArmageddonItems {

    // E�ya1
    public static ItemStack Item1() {
        ItemStack item = new ItemStack(Material.COMPASS, 1);
        ItemMeta im = item.getItemMeta();

        im.setDisplayName(ChatColor.GREEN + "Eşya 1");

        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.YELLOW + "\"Bu...\"");
        lore.add(ChatColor.DARK_GRAY + "Ne olduğunu öğrenmek için diğer 3 eşyayı da topla.");

        im.setLore(lore);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(im);

        return item;
    }

    // E�ya 2
    public static ItemStack Item2() {
        ItemStack item = new ItemStack(Material.COMPASS, 1);
        ItemMeta im = item.getItemMeta();

        im.setDisplayName(ChatColor.GREEN + "Eşya 2");

        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.YELLOW + "\"...eşya...\"");
        lore.add(ChatColor.DARK_GRAY + "Ne olduğunu öğrenmek için diğer 3 eşyayı da topla.");

        im.setLore(lore);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(im);

        return item;
    }

    // E�ya 3
    public static ItemStack Item3() {
        ItemStack item = new ItemStack(Material.COMPASS, 1);
        ItemMeta im = item.getItemMeta();

        im.setDisplayName(ChatColor.GREEN + "Eşya 3");

        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.YELLOW + "\"...kıyameti...\"");
        lore.add(ChatColor.DARK_GRAY + "Ne olduğunu öğrenmek için diğer 3 eşyayı da topla.");

        im.setLore(lore);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(im);

        return item;
    }

    // E�ya 4
    public static ItemStack Item4() {
        ItemStack item = new ItemStack(Material.COMPASS, 1);
        ItemMeta im = item.getItemMeta();

        im.setDisplayName(ChatColor.GREEN + "Eşya 4");

        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.YELLOW + "\"...getirebilir.\"");
        lore.add(ChatColor.DARK_GRAY + "Ne olduğunu öğrenmek için diğer 3 eşyayı da topla.");

        im.setLore(lore);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(im);

        return item;
    }

}
