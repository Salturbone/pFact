package snc.pFact;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import snc.pFact.DM.dataIssues;
import snc.pFact.obj.cl.b_Faction;
import snc.pFact.obj.cl.b_Player;

public class Main extends JavaPlugin{
	
	public static JavaPlugin ekl;
	public static HashMap<String, b_Faction> factions = null;
	
	//Player player = getServer().getPlayer(playerName);
	//FilenameUtils.removeExtension(fileNameWithExt);
	@Override
	public void onEnable() 
	{
		ekl = this;
		factions = new HashMap<String, b_Faction>();
		dataIssues.create();
		dataIssues.load();
		
		System.out.println("pFact baþlatýldý!");
		PluginManager pm = getServer().getPluginManager();
		ListenerClass lc = new ListenerClass();
		pm.registerEvents(lc, this);
		
	}
	
	@Override
	public void onDisable() {
		dataIssues.save();
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
							
							
							File fpath = new File(dataIssues.factionFile, arg[1] + ".df");
							
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
