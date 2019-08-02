package snc.pFact;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import snc.pFact.DM.dataIssues;
import snc.pFact.obj.cl.b_Faction;
import snc.pFact.obj.cl.b_Player;
import snc.pFact.obj.cl.b_Player.Rank;

public class Main extends JavaPlugin {

	public static JavaPlugin ekl;

	//Player player = getServer().getPlayer(playerName);
	
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
				plyr = new b_Player(p.getUniqueId(), null, 0, Rank.Single);
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
				plyr = new b_Player(p.getUniqueId(), null, 0, Rank.Single);
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
					bp.setRank(Rank.Founder);
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
		
		if (arg.length == 2) {
			//kurucu yapma komutu
			if (arg[0].equalsIgnoreCase("kurucuyap") && b_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
				Player gp = Bukkit.getPlayer(arg[1]);
				if (b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
					sender.sendMessage(ChatColor.RED + "Oyuncu klan�n�n bir mensubu de�il!");
					return true;
				} else {
					b_Player.players.get(gp.getUniqueId()).setRank(Rank.Founder);
					b_Player.players.get(p.getUniqueId()).setRank(Rank.Moderator);
					b_Player.players.get(p.getUniqueId()).getF().setFounder(b_Player.players.get(gp.getUniqueId()));
					b_Faction.factions.get(b_Player.players.get(p.getUniqueId()).getF().getName()).setFounder(b_Player.players.get(gp.getUniqueId()));
					sender.sendMessage(ChatColor.RED + "Art�k kurucu de�ilsin!");
					gp.sendMessage(ChatColor.GREEN+ "Klan�n�n yeni kurucusu sensin!");
					return true;
				}
			}
		}
		
		if (arg.length == 1) {
			if (arg[0].equalsIgnoreCase("ayr�l")) {
				if (b_Player.players.get(p.getUniqueId()).getF() != null) {
					if (b_Player.players.get(p.getUniqueId()).rank() != Rank.Founder) {
						b_Player.players.get(p.getUniqueId()).setRank(Rank.Single);
						p.sendMessage(ChatColor.GREEN + "Art�k bir klana mensup de�ilsin!");
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "Kurucusu oldu�un klandan ayr�lamazs�n!!");
						p.sendMessage(ChatColor.RED 
								+ "Klan�n� devretmek i�in:" 
								+ ChatColor.RESET 
								+ " /klan kurucuyap <oyuncu_ismi>");
						return true;
					}
				} else {
					p.sendMessage(ChatColor.RED + "Bir klana mensup de�ilsin!");
					return true;
				}
			}
		}
		
		if (arg[0].equalsIgnoreCase("oyuncu")) {
			if (b_Player.players.get(p.getUniqueId()).rank() != Rank.Moderator 
					|| b_Player.players.get(p.getUniqueId()).rank() != Rank.Founder) {
				sender.sendMessage(ChatColor.DARK_RED 
						+ "Bu komutu girmek i�in "
						+ "gerekli yetkiye sahip de�ilsin ya da bir klana dahil de�ilsin.");
				return true;
			}
			if (arg.length == 1) {
				sender.sendMessage(ChatColor.RED + "Bir i�lev gir!");
				return true;
			}
			if (arg.length == 2) {
				if (arg[1].equalsIgnoreCase("davet") || arg[1].equalsIgnoreCase("��kar")) {
					sender.sendMessage(ChatColor.RED + "Bir oyuncunun ad�n� gir!");
					return true;
				}
			}
			//oyuncu davet etme ve ��karma
			if (arg.length == 3) {
				if (arg[1].equalsIgnoreCase("davet")) {
					Player gp = Bukkit.getPlayer(arg[2]);
					if (gp == null 
							|| b_Player.players.get(gp.getUniqueId()).getF() != null 
							|| b_Player.players.get(gp.getUniqueId()).getES()) {
						sender.sendMessage(ChatColor.RED + "Oyuncu bulunamad�. Oyuncu "
								+ "ba�ka bir klana �ye ya da ba�ka bir klan taraf�ndan davet edilmi� olabilir:" 
								+ ChatColor.RESET 
								+ arg[2]);
						return true;
					} else {
						b_Player.players.get(gp.getUniqueId()).setES(true);
						b_Player.players.get(gp.getUniqueId()).setEF(b_Player.players.get(p.getUniqueId()).getF());
						sender.sendMessage(ChatColor.GREEN + "Oyuncu davet edildi! "
								+ "Teklifini kabul ederse klan�n�n bir �yesi olacak!:"+ ChatColor.RESET + arg[2]);
						gp.sendMessage(ChatColor.RED 
								+ b_Player.players.get(gp.getUniqueId()).getF().getName() 
								+ ChatColor.GREEN 
								+ " Klan� taraf�ndan davet edildin!");
						gp.sendMessage(ChatColor.GREEN 
								+ "Kabul etmek i�in: /klan kabul");
						gp.sendMessage(ChatColor.GREEN 
								+ "Reddetmek i�in: /klan ret");
						return true;
					}
				}
				if (arg[1].equalsIgnoreCase("��kar")) {
					if (b_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
						if (Bukkit.getPlayer(arg[2]).isOnline()) {
							Player gp = Bukkit.getPlayer(arg[2]);
							if (b_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder 
									|| b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
								sender.sendMessage(ChatColor.RED + "Bu oyuncu klan�na dahil de�il ya da klan�nda y�netici!");
								return true;
							}
							b_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
							b_Player.players.get(gp.getUniqueId()).setF(null);
							b_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
							sender.sendMessage(ChatColor.GREEN + "Oyuncu klan�ndan at�ld�!");
							return true;
						} else {
							Player gp = (Player) Bukkit.getOfflinePlayer(arg[2]);
							if (b_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder 
									|| b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
								sender.sendMessage(ChatColor.RED + "Bu oyuncu klan�na dahil de�il ya da klan�nda y�netici!");
								return true;
							}
							b_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
							b_Player.players.get(gp.getUniqueId()).setF(null);
							b_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
							sender.sendMessage(ChatColor.GREEN + "Oyuncu klan�ndan at�ld�!");
							return true;
						}
					}
					if (b_Player.players.get(p.getUniqueId()).rank() == Rank.Moderator) {
						if (Bukkit.getPlayer(arg[2]).isOnline()) {
							Player gp = Bukkit.getPlayer(arg[2]);
							if (b_Player.players.get(gp.getUniqueId()).rank() == Rank.Moderator
									||b_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder 
									|| b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
								sender.sendMessage(ChatColor.RED + "Bu oyuncu klan�na dahil de�il ya da klan�nda y�netici!");
								return true;
							}
							b_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
							b_Player.players.get(gp.getUniqueId()).setF(null);
							b_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
							sender.sendMessage(ChatColor.GREEN + "Oyuncu klan�ndan at�ld�!");
							return true;
						} else {
							Player gp = (Player) Bukkit.getOfflinePlayer(arg[2]);
							if (b_Player.players.get(gp.getUniqueId()).rank() == Rank.Moderator
									||b_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder 
									|| b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
								sender.sendMessage(ChatColor.RED + "Bu oyuncu klan�na dahil de�il ya da klan�nda y�netici!");
								return true;
							}
							b_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
							b_Player.players.get(gp.getUniqueId()).setF(null);
							b_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
							sender.sendMessage(ChatColor.GREEN + "Oyuncu klan�ndan at�ld�!");
							return true;
						}
					}
				}
			}
		}
		
		
		//Davet Kabul ve Reddi
		if(arg[0].equalsIgnoreCase("kabul")) {
			if (!b_Player.players.get(p.getUniqueId()).getES()) {
				sender.sendMessage(ChatColor.RED + "Bir davet almad�n ya da halihaz�rda bir klana mensupsun!");
			}
			b_Player.players.get(p.getUniqueId()).setF(b_Player.players.get(p.getUniqueId()).getEF());
			b_Faction.factions.get(b_Player.players.get(p.getUniqueId()).getEF().getName()).addMember(p.getUniqueId(), b_Player.players.get(p.getUniqueId()));
			b_Player.players.get(p.getUniqueId()).setEF(null);
			b_Player.players.get(p.getUniqueId()).setES(false);
			b_Player.players.get(p.getUniqueId()).setRank(Rank.Player);
			sender.sendMessage(ChatColor.GREEN 
					+ b_Player.players.get(p.getUniqueId()).getF().getName() 
					+ ChatColor.RESET 
					+ " Klan�na kat�ld�n!");
			return true;
		}
		if(arg[0].equalsIgnoreCase("ret")) {
			if (!b_Player.players.get(p.getUniqueId()).getES()) {
				sender.sendMessage(ChatColor.RED + "Bir davet almad�n ya da halihaz�rda bir klana mensupsun!");
			}
			b_Player.players.get(p.getUniqueId()).setEF(null);
			b_Player.players.get(p.getUniqueId()).setES(false);
			sender.sendMessage(ChatColor.GREEN 
					+ b_Player.players.get(p.getUniqueId()).getF().getName() 
					+ ChatColor.RESET 
					+ " Klan�n�n daveti reddedildi!");
			return true;
		}
		
		if (arg.length >= 3) {
			if (arg[0].equalsIgnoreCase("yetki") && b_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
				Player gp = Bukkit.getPlayer(arg[2]);
				if (b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
					sender.sendMessage(ChatColor.RED + "Oyuncu klan�n�n bir mensubu de�il!");
					return true;
				} else {
					if (arg[1].equalsIgnoreCase("ver")) {
						b_Player.players.get(gp.getUniqueId()).setRank(Rank.Moderator);
						sender.sendMessage(ChatColor.GREEN 
								+ gp.getDisplayName()
								+ ChatColor.RESET 
								+ " adl� oyuncuya y�netici yetkisi verildi!");
						gp.sendMessage(ChatColor.GREEN 
								+ "Art�k klan�nda bir y�neticisin!");
						return true;
					}
					if (arg[1].equalsIgnoreCase("al")) {
						b_Player.players.get(gp.getUniqueId()).setRank(Rank.Player);
						sender.sendMessage(ChatColor.GREEN 
								+ gp.getDisplayName()
								+ ChatColor.RESET 
								+ " adl� oyuncunun yetkisi geri al�nd�!");
						gp.sendMessage(ChatColor.GREEN 
								+ "Art�k klan�nda bir y�netici de�ilsin!");
						return true;
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Bir Klana mensup de�ilsin ya da kurucusu de�ilsin!");
				return true;
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
			Bukkit.broadcastMessage(ChatColor.AQUA + "Cleared Faction&Player files and Factions.");
			return true;
		}
		return false;
	}

}
