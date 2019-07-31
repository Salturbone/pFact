package snc.pFact;


import org.apache.commons.io.FilenameUtils;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import snc.pFact.obj.cl.b_Faction;
import snc.pFact.obj.cl.b_Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin{
	
	public static JavaPlugin ekl;
	public static File factionFile = new File(Main.ekl.getDataFolder(), "Factions/");
	public static HashMap<String, b_Faction> factions = new HashMap<String, b_Faction>();
	ArrayList<String> fNames = new ArrayList<String>();
	//Player player = getServer().getPlayer(playerName);
	//FilenameUtils.removeExtension(fileNameWithExt);
	@Override
	public void onEnable() 
	{
		factionFile.mkdirs();
		File[] ff = factionFile.listFiles();
		
		for (int i = 0; i < ff.length; i++) {
			  if (ff[i].isFile()) {
				  fNames.add(ff[i].getName());
			  }
		}
		for (String a : fNames) {
			
			File fpath = new File(factionFile, a);
			try {
				FileInputStream fi = new FileInputStream(
						fpath);
				ObjectInputStream oi = new ObjectInputStream(fi);
				factions.put(FilenameUtils.removeExtension(a), (b_Faction) oi.readObject());
				oi.close();
				fi.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		ekl = this;
		System.out.println("pFact baþlatýldý!");
		PluginManager pm = getServer().getPluginManager();
		ListenerClass lc = new ListenerClass();
		pm.registerEvents(lc, this);
		
	}
	
	@Override
	public void onDisable() {
		System.out.println("pFact kapatýldý!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		Player plyr = null;
		if (sender instanceof Player) {
			plyr = (Player) sender;
		}
		if (label.equalsIgnoreCase("klan")) {
			if (arg.length >= 1) {
				if (arg[0].equalsIgnoreCase("oluþtur")) {
					if (arg.length == 1) {
						sender.sendMessage("Bir isim gir!");
						return true;
					} else {  // Faction oluþturma
						if (ListenerClass.players.get(plyr.getUniqueId()).getF() == null) {
							
							HashMap<UUID, b_Player> p = new HashMap<UUID, b_Player>();
							p.put(plyr.getUniqueId(), ListenerClass.players.get(plyr.getUniqueId()));
							b_Faction bf = new b_Faction(arg[1], 1, 1, 0.0, 0.0, p);
							p.get(plyr.getUniqueId()).setF(bf);
							
							
							File fpath = new File(factionFile, arg[1] + ".df");
							
							factions.put(arg[1], bf);
							// Write objects to file
							try {	
								FileOutputStream f = new FileOutputStream(
										fpath);
								
								ObjectOutputStream o = new ObjectOutputStream(f);
								o.writeObject(bf);
								o.close();
								f.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							sender.sendMessage("Klanýn baþarýyla oluþturuldu!! ::: " + arg[1]);
							return true;
						} else {
							plyr.sendMessage("Bir klana mensup olduðun için klan oluþturamazsýn!");
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
}
