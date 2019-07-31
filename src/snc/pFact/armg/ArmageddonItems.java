package snc.pFact.armg;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArmageddonItems {
	
	//Eþya1
	public static ItemStack Item1() {
		ItemStack item = new ItemStack(Material.COMPASS, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(ChatColor.GREEN + "Eþya 1");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW 
				+ "\"Bu...\"" 
				);
		lore.add(ChatColor.DARK_GRAY 
				+ "Ne olduðunu öðrenmek için diðer 3 eþyayý da topla.");
		
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(im);
		
		return item;
	}
	
	//Eþya 2
	public static ItemStack Item2() {
		ItemStack item = new ItemStack(Material.COMPASS, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(ChatColor.GREEN + "Eþya 2");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW 
				+ "\"...eþya...\"" 
				);
		lore.add(ChatColor.DARK_GRAY 
				+ "Ne olduðunu öðrenmek için diðer 3 eþyayý da topla.");
		
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(im);
		
		return item;
	}
	//Eþya 3
	public static ItemStack Item3() {
		ItemStack item = new ItemStack(Material.COMPASS, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(ChatColor.GREEN + "Eþya 3");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW 
				+ "\"...kýyameti...\"" 
				);
		lore.add(ChatColor.DARK_GRAY 
				+ "Ne olduðunu öðrenmek için diðer 3 eþyayý da topla.");
		
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(im);
		
		return item;
	}
	//Eþya 4
	public static ItemStack Item4() {
		ItemStack item = new ItemStack(Material.CLOCK, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(ChatColor.GREEN + "Eþya 3");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW 
				+ "\"...getirebilir.\"" 
				);
		lore.add(ChatColor.DARK_GRAY 
				+ "Ne olduðunu öðrenmek için diðer 3 eþyayý da topla.");
		
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(im);
		
		
		return item;
	}
	
	
}
