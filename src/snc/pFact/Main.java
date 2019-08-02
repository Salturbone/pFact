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
		System.out.println("pFact baþlatýldý!");
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
		System.out.println("pFact kapatýldý!");
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
		if (arg[0].equalsIgnoreCase("oluþtur")) {
			if (arg.length == 1) {
				sender.sendMessage("Bir isim gir!");
				return true;
			} else { // Faction oluþturma
				b_Player bp = b_Player.players.get(p.getUniqueId());
				if (bp != null && bp.getF() == null) {
					bp.setRank(Rank.Founder);
					b_Faction bf = new b_Faction(arg[1], bp);
					bp.setF(bf);
					b_Faction.factions.put(bf.getName(), bf);

					sender.sendMessage("Klanýn baþarýyla oluþturuldu!! ::: " + bf.getName());
					return true;
				} else {
					p.sendMessage("Bir klana mensup olduðun için klan oluþturamazsýn!");
					return true;
				}

			}

		}
		
		if (arg.length == 2) {
			//kurucu yapma komutu
			if (arg[0].equalsIgnoreCase("kurucuyap") && b_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
				Player gp = Bukkit.getPlayer(arg[1]);
				if (b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
					sender.sendMessage(ChatColor.RED + "Oyuncu klanýnýn bir mensubu deðil!");
					return true;
				} else {
					b_Player.players.get(gp.getUniqueId()).setRank(Rank.Founder);
					b_Player.players.get(p.getUniqueId()).setRank(Rank.Moderator);
					b_Player.players.get(p.getUniqueId()).getF().setFounder(b_Player.players.get(gp.getUniqueId()));
					b_Faction.factions.get(b_Player.players.get(p.getUniqueId()).getF().getName()).setFounder(b_Player.players.get(gp.getUniqueId()));
					sender.sendMessage(ChatColor.RED + "Artýk kurucu deðilsin!");
					gp.sendMessage(ChatColor.GREEN+ "Klanýnýn yeni kurucusu sensin!");
					return true;
				}
			}
		}
		
		if (arg.length == 1) {
			if (arg[0].equalsIgnoreCase("ayrýl")) {
				if (b_Player.players.get(p.getUniqueId()).getF() != null) {
					if (b_Player.players.get(p.getUniqueId()).rank() != Rank.Founder) {
						b_Player.players.get(p.getUniqueId()).setRank(Rank.Single);
						p.sendMessage(ChatColor.GREEN + "Artýk bir klana mensup deðilsin!");
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "Kurucusu olduðun klandan ayrýlamazsýn!!");
						p.sendMessage(ChatColor.RED 
								+ "Klanýný devretmek için:" 
								+ ChatColor.RESET 
								+ " /klan kurucuyap <oyuncu_ismi>");
						return true;
					}
				} else {
					p.sendMessage(ChatColor.RED + "Bir klana mensup deðilsin!");
					return true;
				}
			}
		}
		
		if (arg[0].equalsIgnoreCase("oyuncu")) {
			if (b_Player.players.get(p.getUniqueId()).rank() != Rank.Moderator 
					|| b_Player.players.get(p.getUniqueId()).rank() != Rank.Founder) {
				sender.sendMessage(ChatColor.DARK_RED 
						+ "Bu komutu girmek için "
						+ "gerekli yetkiye sahip deðilsin ya da bir klana dahil deðilsin.");
				return true;
			}
			if (arg.length == 1) {
				sender.sendMessage(ChatColor.RED + "Bir iþlev gir!");
				return true;
			}
			if (arg.length == 2) {
				if (arg[1].equalsIgnoreCase("davet") || arg[1].equalsIgnoreCase("çýkar")) {
					sender.sendMessage(ChatColor.RED + "Bir oyuncunun adýný gir!");
					return true;
				}
			}
			//oyuncu davet etme ve çýkarma
			if (arg.length == 3) {
				if (arg[1].equalsIgnoreCase("davet")) {
					Player gp = Bukkit.getPlayer(arg[2]);
					if (gp == null 
							|| b_Player.players.get(gp.getUniqueId()).getF() != null 
							|| b_Player.players.get(gp.getUniqueId()).getES()) {
						sender.sendMessage(ChatColor.RED + "Oyuncu bulunamadý. Oyuncu "
								+ "baþka bir klana üye ya da baþka bir klan tarafýndan davet edilmiþ olabilir:" 
								+ ChatColor.RESET 
								+ arg[2]);
						return true;
					} else {
						b_Player.players.get(gp.getUniqueId()).setES(true);
						b_Player.players.get(gp.getUniqueId()).setEF(b_Player.players.get(p.getUniqueId()).getF());
						sender.sendMessage(ChatColor.GREEN + "Oyuncu davet edildi! "
								+ "Teklifini kabul ederse klanýnýn bir üyesi olacak!:"+ ChatColor.RESET + arg[2]);
						gp.sendMessage(ChatColor.RED 
								+ b_Player.players.get(gp.getUniqueId()).getF().getName() 
								+ ChatColor.GREEN 
								+ " Klaný tarafýndan davet edildin!");
						gp.sendMessage(ChatColor.GREEN 
								+ "Kabul etmek için: /klan kabul");
						gp.sendMessage(ChatColor.GREEN 
								+ "Reddetmek için: /klan ret");
						return true;
					}
				}
				if (arg[1].equalsIgnoreCase("çýkar")) {
					if (b_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
						if (Bukkit.getPlayer(arg[2]).isOnline()) {
							Player gp = Bukkit.getPlayer(arg[2]);
							if (b_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder 
									|| b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
								sender.sendMessage(ChatColor.RED + "Bu oyuncu klanýna dahil deðil ya da klanýnda yönetici!");
								return true;
							}
							b_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
							b_Player.players.get(gp.getUniqueId()).setF(null);
							b_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
							sender.sendMessage(ChatColor.GREEN + "Oyuncu klanýndan atýldý!");
							return true;
						} else {
							Player gp = (Player) Bukkit.getOfflinePlayer(arg[2]);
							if (b_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder 
									|| b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
								sender.sendMessage(ChatColor.RED + "Bu oyuncu klanýna dahil deðil ya da klanýnda yönetici!");
								return true;
							}
							b_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
							b_Player.players.get(gp.getUniqueId()).setF(null);
							b_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
							sender.sendMessage(ChatColor.GREEN + "Oyuncu klanýndan atýldý!");
							return true;
						}
					}
					if (b_Player.players.get(p.getUniqueId()).rank() == Rank.Moderator) {
						if (Bukkit.getPlayer(arg[2]).isOnline()) {
							Player gp = Bukkit.getPlayer(arg[2]);
							if (b_Player.players.get(gp.getUniqueId()).rank() == Rank.Moderator
									||b_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder 
									|| b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
								sender.sendMessage(ChatColor.RED + "Bu oyuncu klanýna dahil deðil ya da klanýnda yönetici!");
								return true;
							}
							b_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
							b_Player.players.get(gp.getUniqueId()).setF(null);
							b_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
							sender.sendMessage(ChatColor.GREEN + "Oyuncu klanýndan atýldý!");
							return true;
						} else {
							Player gp = (Player) Bukkit.getOfflinePlayer(arg[2]);
							if (b_Player.players.get(gp.getUniqueId()).rank() == Rank.Moderator
									||b_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder 
									|| b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
								sender.sendMessage(ChatColor.RED + "Bu oyuncu klanýna dahil deðil ya da klanýnda yönetici!");
								return true;
							}
							b_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
							b_Player.players.get(gp.getUniqueId()).setF(null);
							b_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
							sender.sendMessage(ChatColor.GREEN + "Oyuncu klanýndan atýldý!");
							return true;
						}
					}
				}
			}
		}
		
		
		//Davet Kabul ve Reddi
		if(arg[0].equalsIgnoreCase("kabul")) {
			if (!b_Player.players.get(p.getUniqueId()).getES()) {
				sender.sendMessage(ChatColor.RED + "Bir davet almadýn ya da halihazýrda bir klana mensupsun!");
			}
			b_Player.players.get(p.getUniqueId()).setF(b_Player.players.get(p.getUniqueId()).getEF());
			b_Faction.factions.get(b_Player.players.get(p.getUniqueId()).getEF().getName()).addMember(p.getUniqueId(), b_Player.players.get(p.getUniqueId()));
			b_Player.players.get(p.getUniqueId()).setEF(null);
			b_Player.players.get(p.getUniqueId()).setES(false);
			b_Player.players.get(p.getUniqueId()).setRank(Rank.Player);
			sender.sendMessage(ChatColor.GREEN 
					+ b_Player.players.get(p.getUniqueId()).getF().getName() 
					+ ChatColor.RESET 
					+ " Klanýna katýldýn!");
			return true;
		}
		if(arg[0].equalsIgnoreCase("ret")) {
			if (!b_Player.players.get(p.getUniqueId()).getES()) {
				sender.sendMessage(ChatColor.RED + "Bir davet almadýn ya da halihazýrda bir klana mensupsun!");
			}
			b_Player.players.get(p.getUniqueId()).setEF(null);
			b_Player.players.get(p.getUniqueId()).setES(false);
			sender.sendMessage(ChatColor.GREEN 
					+ b_Player.players.get(p.getUniqueId()).getF().getName() 
					+ ChatColor.RESET 
					+ " Klanýnýn daveti reddedildi!");
			return true;
		}
		
		if (arg.length >= 3) {
			if (arg[0].equalsIgnoreCase("yetki") && b_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
				Player gp = Bukkit.getPlayer(arg[2]);
				if (b_Player.players.get(gp.getUniqueId()).getF() != b_Player.players.get(p.getUniqueId()).getF()) {
					sender.sendMessage(ChatColor.RED + "Oyuncu klanýnýn bir mensubu deðil!");
					return true;
				} else {
					if (arg[1].equalsIgnoreCase("ver")) {
						b_Player.players.get(gp.getUniqueId()).setRank(Rank.Moderator);
						sender.sendMessage(ChatColor.GREEN 
								+ gp.getDisplayName()
								+ ChatColor.RESET 
								+ " adlý oyuncuya yönetici yetkisi verildi!");
						gp.sendMessage(ChatColor.GREEN 
								+ "Artýk klanýnda bir yöneticisin!");
						return true;
					}
					if (arg[1].equalsIgnoreCase("al")) {
						b_Player.players.get(gp.getUniqueId()).setRank(Rank.Player);
						sender.sendMessage(ChatColor.GREEN 
								+ gp.getDisplayName()
								+ ChatColor.RESET 
								+ " adlý oyuncunun yetkisi geri alýndý!");
						gp.sendMessage(ChatColor.GREEN 
								+ "Artýk klanýnda bir yönetici deðilsin!");
						return true;
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Bir Klana mensup deðilsin ya da kurucusu deðilsin!");
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
