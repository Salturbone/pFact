package snc.pFact.armg;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArmageddonItems {
	
	//E�ya1
	public static ItemStack Item1() {
		ItemStack item = new ItemStack(Material.COMPASS, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(ChatColor.GREEN + "E�ya 1");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW 
				+ "\"Bu...\"" 
				);
		lore.add(ChatColor.DARK_GRAY 
				+ "Ne oldu�unu ��renmek i�in di�er 3 e�yay� da topla.");
		
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(im);
		
		return item;
	}
	
	//E�ya 2
	public static ItemStack Item2() {
		ItemStack item = new ItemStack(Material.COMPASS, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(ChatColor.GREEN + "E�ya 2");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW 
				+ "\"...e�ya...\"" 
				);
		lore.add(ChatColor.DARK_GRAY 
				+ "Ne oldu�unu ��renmek i�in di�er 3 e�yay� da topla.");
		
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(im);
		
		return item;
	}
	//E�ya 3
	public static ItemStack Item3() {
		ItemStack item = new ItemStack(Material.COMPASS, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(ChatColor.GREEN + "E�ya 3");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW 
				+ "\"...k�yameti...\"" 
				);
		lore.add(ChatColor.DARK_GRAY 
				+ "Ne oldu�unu ��renmek i�in di�er 3 e�yay� da topla.");
		
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(im);
		
		return item;
	}
	//E�ya 4
	public static ItemStack Item4() {
		ItemStack item = new ItemStack(Material.CLOCK, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(ChatColor.GREEN + "E�ya 3");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW 
				+ "\"...getirebilir.\"" 
				);
		lore.add(ChatColor.DARK_GRAY 
				+ "Ne oldu�unu ��renmek i�in di�er 3 e�yay� da topla.");
		
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(im);
		
		
		return item;
	}
	
	
}
