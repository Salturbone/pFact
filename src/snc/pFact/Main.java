package snc.pFact;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
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
	@Override
	public void onEnable() 
	{
		ekl = this;
		
		dataIssues.create();
		dataIssues.load();
		loadPlayers();
		System.out.println("pFact baþlatýldý!");
		PluginManager pm = getServer().getPluginManager();
		ListenerClass lc = new ListenerClass();
		pm.registerEvents(lc, this);
		
	}
	
	private void loadPlayers() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			File pFile = new File(dataIssues.playerFile, p.getUniqueId() + ".dp");
			b_Player plyr;
			if(!pFile.exists()) {
				plyr = new b_Player(p.getUniqueId(), null, 0);
			}
			else {
				plyr = (b_Player) dataIssues.loadObject(pFile);
			}
			b_Player.players.put(p.getUniqueId(), plyr);
		}
	}
	
	@Override
	public void onDisable() {
		dataIssues.save();
		savePlayers();
		System.out.println("pFact kapatýldý!");
	}
	
	private void savePlayers() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			File pFile = new File(dataIssues.playerFile, p.getUniqueId() + ".dp");
			b_Player plyr = b_Player.players.get(p.getUniqueId());
			if(plyr == null) {
				plyr = new b_Player(p.getUniqueId(), null, 0);
			}
			dataIssues.saveObject(plyr, pFile);
		}
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
						if (b_Player.players.get(plyr.getUniqueId()).getF() == null) {
							
							HashMap<UUID, b_Player> p = new HashMap<UUID, b_Player>();
							p.put(plyr.getUniqueId(), b_Player.players.get(plyr.getUniqueId()));
							b_Faction bf = new b_Faction(arg[1], 1, 1, 0.0, 0.0, p);
							p.get(plyr.getUniqueId()).setF(bf);
							
							
							File fpath = new File(dataIssues.factionFile, arg[1] + ".df");
							
							b_Faction.factions.put(arg[1], bf);
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
