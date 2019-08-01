package snc.pFact;

import java.io.File;
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

public class Main extends JavaPlugin {

	public static JavaPlugin ekl;

	@Override
	public void onEnable() {
		ekl = this;

		dataIssues.create();
		dataIssues.load();
		loadPlayers();
		System.out.println("pFact ba�lat�ld�!");
		PluginManager pm = getServer().getPluginManager();
		ListenerClass lc = new ListenerClass();
		pm.registerEvents(lc, this);

	}

	private void loadPlayers() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			File pFile = new File(dataIssues.playerFile, p.getUniqueId() + ".dp");
			b_Player plyr;
			if (!pFile.exists()) {
				plyr = new b_Player(p.getUniqueId(), null, 0);
			} else {
				plyr = (b_Player) dataIssues.loadObject(pFile);
			}
			b_Player.players.put(p.getUniqueId(), plyr);
		}
	}

	@Override
	public void onDisable() {
		dataIssues.save();
		savePlayers();
		System.out.println("pFact kapat�ld�!");
	}

	private void savePlayers() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			File pFile = new File(dataIssues.playerFile, p.getUniqueId() + ".dp");
			b_Player plyr = b_Player.players.get(p.getUniqueId());
			if (plyr == null) {
				plyr = new b_Player(p.getUniqueId(), null, 0);
			}
			dataIssues.saveObject(plyr, pFile);
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}
		if (!label.equalsIgnoreCase("klan"))
			return false;
		if (arg.length == 0)
			return false;
		if (arg[0].equalsIgnoreCase("olu�tur")) {
			if (arg.length == 1) {
				sender.sendMessage("Bir isim gir!");
				return true;
			} else { // Faction olu�turma
				b_Player bp = b_Player.players.get(p.getUniqueId());
				if (bp != null && bp.getF() == null) {

					HashMap<UUID, b_Player> players = new HashMap<UUID, b_Player>();
					players.put(p.getUniqueId(), bp);
					b_Faction bf = new b_Faction(arg[1], bp);
					bp.setF(bf);
					b_Faction.factions.put(bf.getName(), bf);

					sender.sendMessage("Klan�n ba�ar�yla olu�turuldu!! ::: " + bf.getName());
					return true;
				} else {
					p.sendMessage("Bir klana mensup oldu�un i�in klan olu�turamazs�n!");
					return true;
				}

			}

		}
		if(arg[0].equalsIgnoreCase("clearfiles")) {
			for(File f : dataIssues.factionFile.listFiles()) {
				f.delete();
			}
			for(File f : dataIssues.playerFile.listFiles()) {
				f.delete();
			}
			b_Faction.factions.clear();
			for(b_Player bp : b_Player.players.values()) {
				bp.setF(null);
			}
			Bukkit.broadcastMessage("cleared faction & player files and factions");
			return true;
		}
		return false;
	}

}
