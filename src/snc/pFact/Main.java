package snc.pFact;


import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import snc.pFact.obj.cl.b_Faction;
import snc.pFact.obj.cl.b_Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class Main extends JavaPlugin{
	
	public static JavaPlugin ekl;
	public static File factionFile = new File(Main.ekl.getDataFolder(), "Factions/");
	public static HashMap<String, b_Faction> factions = new HashMap<String, b_Faction>();
	ArrayList<String> fNames = new ArrayList<String>();
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
		}
		
		ekl = this;
		System.out.println("pFact ba�lat�ld�!");
		PluginManager pm = getServer().getPluginManager();
		ListenerClass lc = new ListenerClass();
		pm.registerEvents(lc, this);
		
	}
	
	@Override
	public void onDisable() {
		System.out.println("pFact kapat�ld�!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		Player plyr = null;
		if (sender instanceof Player) {
			plyr = (Player) sender;
		}
		if (label.equalsIgnoreCase("klan")) {
			if (arg.length >= 1) {
				if (arg[0].equalsIgnoreCase("olu�tur")) {
					if (arg.length == 1) {
						sender.sendMessage("Bir isim gir!");
						return true;
					} else {  // Faction olu�turma
						File fpath = new File(factionFile, arg[1] + ".df");
						
						
						
						
						
						
						HashMap<UUID, b_Player> p = new HashMap<UUID, b_Player>();
						p.put(plyr.getUniqueId(), ListenerClass.players.get(plyr.getUniqueId()));
						b_Faction bf = new b_Faction(arg[1], 1, 1, 0.0, 0.0, ListenerClass.players);
						p.get(plyr.getUniqueId()).setF(bf);
						
						
						
						
						
						sender.sendMessage("Klan�n ba�ar�yla olu�turuldu!! ::: " + arg[1]);
						return true;
				}
			}
			}
		}
		return false;
	}
	
}
