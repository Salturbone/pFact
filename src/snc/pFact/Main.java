package snc.pFact;


import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import snc.pFact.obj.cl.b_Faction;
import snc.pFact.obj.cl.b_Player;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin{
	
	
	
	@Override
	public void onEnable() 
	{
		System.out.println("pFact baþlatýldý!");
		PluginManager pm = getServer().getPluginManager();
		ListenerClass lc = new ListenerClass(this);
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
					} else {
						HashMap<UUID, b_Player> p = new HashMap<UUID, b_Player>();
						p.put(plyr.getUniqueId(), ListenerClass.players.get(plyr.getUniqueId()));
						b_Faction bf = new b_Faction(arg[1], 1, 1, 0.0, 0.0, ListenerClass.players);
						p.get(plyr.getUniqueId()).setF(bf);
						sender.sendMessage("Klanýn baþarýyla oluþturuldu!! ::: " + arg[1]);
						return true;
				}
			}
			}
		}
		return false;
	}
	
}
