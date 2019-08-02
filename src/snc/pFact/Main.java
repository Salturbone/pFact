package snc.pFact;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import snc.pFact.DM.DataIssues;
import snc.pFact.armg.ArmageddonItems;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.obj.cl.B_Player;
import snc.pFact.obj.cl.B_Player.Rank;

public class Main extends JavaPlugin {

    public static JavaPlugin ekl;

    // Player player = getServer().getPlayer(playerName);

    @Override
    public void onEnable() {
        ekl = this;

        DataIssues.create();
        DataIssues.load();
        loadPlayers();
        System.out.println("pFact ba�lat�ld�!");
        PluginManager pm = getServer().getPluginManager();
        ListenerClass lc = new ListenerClass();
        pm.registerEvents(lc, this);

    }

    private void loadPlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            File pFile = new File(DataIssues.playerFile, p.getUniqueId() + ".dp");
            B_Player plyr;
            if (!pFile.exists()) {
                plyr = new B_Player(p.getUniqueId(), null, 0, Rank.Single);
            } else {
                plyr = (B_Player) DataIssues.loadObject(pFile);
            }
            B_Player.players.put(p.getUniqueId(), plyr);
        }
    }

    @Override
    public void onDisable() {
        DataIssues.save();
        savePlayers();
        System.out.println("pFact kapat�ld�!");
    }

    private void savePlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            File pFile = new File(DataIssues.playerFile, p.getUniqueId() + ".dp");
            B_Player plyr = B_Player.players.get(p.getUniqueId());
            if (plyr == null) {
                plyr = new B_Player(p.getUniqueId(), null, 0, Rank.Single);
            }
            DataIssues.saveObject(plyr, pFile);
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
                ArmageddonItems a;
                return true;
            } else { // Faction olu�turma
                B_Player bp = B_Player.players.get(p.getUniqueId());
                if (bp != null && bp.getF() == null) {
                    bp.setRank(Rank.Founder);
                    B_Faction bf = new B_Faction(arg[1], bp);
                    bp.setF(bf);
                    B_Faction.factions.put(bf.getName(), bf);

                    sender.sendMessage("Klan�n ba�ar�yla olu�turuldu!! ::: " + bf.getName());
                    return true;
                } else {
                    p.sendMessage("Bir klana mensup oldu�un i�in klan olu�turamazs�n!");
                    return true;
                }

            }

        }

        if (arg.length == 2) {
            // kurucu yapma komutu
            if (arg[0].equalsIgnoreCase("kurucuyap") && B_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
                Player gp = Bukkit.getPlayer(arg[1]);
                if (B_Player.players.get(gp.getUniqueId()).getF() != B_Player.players.get(p.getUniqueId()).getF()) {
                    sender.sendMessage(ChatColor.RED + "Oyuncu klan�n�n bir mensubu de�il!");
                    return true;
                } else {
                    B_Player.players.get(gp.getUniqueId()).setRank(Rank.Founder);
                    B_Player.players.get(p.getUniqueId()).setRank(Rank.Moderator);
                    B_Player.players.get(p.getUniqueId()).getF().setFounder(B_Player.players.get(gp.getUniqueId()));
                    B_Faction.factions.get(B_Player.players.get(p.getUniqueId()).getF().getName())
                            .setFounder(B_Player.players.get(gp.getUniqueId()));
                    sender.sendMessage(ChatColor.RED + "Art�k kurucu de�ilsin!");
                    gp.sendMessage(ChatColor.GREEN + "Klan�n�n yeni kurucusu sensin!");
                    return true;
                }
            }
        }

        if (arg.length == 1) {
            if (arg[0].equalsIgnoreCase("ayr�l")) {
                if (B_Player.players.get(p.getUniqueId()).getF() != null) {
                    if (B_Player.players.get(p.getUniqueId()).rank() != Rank.Founder) {
                        B_Player.players.get(p.getUniqueId()).setRank(Rank.Single);
                        p.sendMessage(ChatColor.GREEN + "Art�k bir klana mensup de�ilsin!");
                        return true;
                    } else {
                        p.sendMessage(ChatColor.RED + "Kurucusu oldu�un klandan ayr�lamazs�n!!");
                        p.sendMessage(ChatColor.RED + "Klan�n� devretmek i�in:" + ChatColor.RESET
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
            if (B_Player.players.get(p.getUniqueId()).rank() != Rank.Moderator
                    || B_Player.players.get(p.getUniqueId()).rank() != Rank.Founder) {
                sender.sendMessage(ChatColor.DARK_RED + "Bu komutu girmek i�in "
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
            // oyuncu davet etme ve ��karma
            if (arg.length == 3) {
                if (arg[1].equalsIgnoreCase("davet")) {
                    Player gp = Bukkit.getPlayer(arg[2]);
                    if (gp == null || B_Player.players.get(gp.getUniqueId()).getF() != null
                            || B_Player.players.get(gp.getUniqueId()).getES()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu bulunamad�. Oyuncu "
                                + "ba�ka bir klana �ye ya da ba�ka bir klan taraf�ndan davet edilmi� olabilir:"
                                + ChatColor.RESET + arg[2]);
                        return true;
                    } else {
                        B_Player.players.get(gp.getUniqueId()).setES(true);
                        B_Player.players.get(gp.getUniqueId()).setEF(B_Player.players.get(p.getUniqueId()).getF());
                        sender.sendMessage(ChatColor.GREEN + "Oyuncu davet edildi! "
                                + "Teklifini kabul ederse klan�n�n bir �yesi olacak!:" + ChatColor.RESET + arg[2]);
                        gp.sendMessage(ChatColor.RED + B_Player.players.get(gp.getUniqueId()).getF().getName()
                                + ChatColor.GREEN + " Klan� taraf�ndan davet edildin!");
                        gp.sendMessage(ChatColor.GREEN + "Kabul etmek i�in: /klan kabul");
                        gp.sendMessage(ChatColor.GREEN + "Reddetmek i�in: /klan ret");
                        return true;
                    }
                }
                if (arg[1].equalsIgnoreCase("��kar")) {
                    if (B_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
                        if (Bukkit.getPlayer(arg[2]).isOnline()) {
                            Player gp = Bukkit.getPlayer(arg[2]);
                            if (B_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder || B_Player.players
                                    .get(gp.getUniqueId()).getF() != B_Player.players.get(p.getUniqueId()).getF()) {
                                sender.sendMessage(
                                        ChatColor.RED + "Bu oyuncu klan�na dahil de�il ya da klan�nda y�netici!");
                                return true;
                            }
                            B_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
                            B_Player.players.get(gp.getUniqueId()).setF(null);
                            B_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klan�ndan at�ld�!");
                            return true;
                        } else {
                            Player gp = (Player) Bukkit.getOfflinePlayer(arg[2]);
                            if (B_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder || B_Player.players
                                    .get(gp.getUniqueId()).getF() != B_Player.players.get(p.getUniqueId()).getF()) {
                                sender.sendMessage(
                                        ChatColor.RED + "Bu oyuncu klan�na dahil de�il ya da klan�nda y�netici!");
                                return true;
                            }
                            B_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
                            B_Player.players.get(gp.getUniqueId()).setF(null);
                            B_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klan�ndan at�ld�!");
                            return true;
                        }
                    }
                    if (B_Player.players.get(p.getUniqueId()).rank() == Rank.Moderator) {
                        if (Bukkit.getPlayer(arg[2]).isOnline()) {
                            Player gp = Bukkit.getPlayer(arg[2]);
                            if (B_Player.players.get(gp.getUniqueId()).rank() == Rank.Moderator
                                    || B_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder
                                    || B_Player.players.get(gp.getUniqueId()).getF() != B_Player.players
                                            .get(p.getUniqueId()).getF()) {
                                sender.sendMessage(
                                        ChatColor.RED + "Bu oyuncu klan�na dahil de�il ya da klan�nda y�netici!");
                                return true;
                            }
                            B_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
                            B_Player.players.get(gp.getUniqueId()).setF(null);
                            B_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klan�ndan at�ld�!");
                            return true;
                        } else {
                            Player gp = (Player) Bukkit.getOfflinePlayer(arg[2]);
                            if (B_Player.players.get(gp.getUniqueId()).rank() == Rank.Moderator
                                    || B_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder
                                    || B_Player.players.get(gp.getUniqueId()).getF() != B_Player.players
                                            .get(p.getUniqueId()).getF()) {
                                sender.sendMessage(
                                        ChatColor.RED + "Bu oyuncu klan�na dahil de�il ya da klan�nda y�netici!");
                                return true;
                            }
                            B_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
                            B_Player.players.get(gp.getUniqueId()).setF(null);
                            B_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klan�ndan at�ld�!");
                            return true;
                        }
                    }
                }
            }
        }

        // Davet Kabul ve Reddi
        if (arg[0].equalsIgnoreCase("kabul")) {
            if (!B_Player.players.get(p.getUniqueId()).getES()) {
                sender.sendMessage(ChatColor.RED + "Bir davet almad�n ya da halihaz�rda bir klana mensupsun!");
            }
            B_Player.players.get(p.getUniqueId()).setF(B_Player.players.get(p.getUniqueId()).getEF());
            B_Faction.factions.get(B_Player.players.get(p.getUniqueId()).getEF().getName()).addMember(p.getUniqueId(),
                    B_Player.players.get(p.getUniqueId()));
            B_Player.players.get(p.getUniqueId()).setEF(null);
            B_Player.players.get(p.getUniqueId()).setES(false);
            B_Player.players.get(p.getUniqueId()).setRank(Rank.Player);
            sender.sendMessage(ChatColor.GREEN + B_Player.players.get(p.getUniqueId()).getF().getName()
                    + ChatColor.RESET + " Klan�na kat�ld�n!");
            return true;
        }
        if (arg[0].equalsIgnoreCase("ret")) {
            if (!B_Player.players.get(p.getUniqueId()).getES()) {
                sender.sendMessage(ChatColor.RED + "Bir davet almad�n ya da halihaz�rda bir klana mensupsun!");
            }
            B_Player.players.get(p.getUniqueId()).setEF(null);
            B_Player.players.get(p.getUniqueId()).setES(false);
            sender.sendMessage(ChatColor.GREEN + B_Player.players.get(p.getUniqueId()).getF().getName()
                    + ChatColor.RESET + " Klan�n�n daveti reddedildi!");
            return true;
        }

        if (arg.length >= 3) {
            if (arg[0].equalsIgnoreCase("yetki") && B_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
                Player gp = Bukkit.getPlayer(arg[2]);
                if (B_Player.players.get(gp.getUniqueId()).getF() != B_Player.players.get(p.getUniqueId()).getF()) {
                    sender.sendMessage(ChatColor.RED + "Oyuncu klan�n�n bir mensubu de�il!");
                    return true;
                } else {
                    if (arg[1].equalsIgnoreCase("ver")) {
                        B_Player.players.get(gp.getUniqueId()).setRank(Rank.Moderator);
                        sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                + " adl� oyuncuya y�netici yetkisi verildi!");
                        gp.sendMessage(ChatColor.GREEN + "Art�k klan�nda bir y�neticisin!");
                        return true;
                    }
                    if (arg[1].equalsIgnoreCase("al")) {
                        B_Player.players.get(gp.getUniqueId()).setRank(Rank.Player);
                        sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                + " adl� oyuncunun yetkisi geri al�nd�!");
                        gp.sendMessage(ChatColor.GREEN + "Art�k klan�nda bir y�netici de�ilsin!");
                        return true;
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Bir Klana mensup de�ilsin ya da kurucusu de�ilsin!");
                return true;
            }
        }

        if (arg[0].equalsIgnoreCase("clearfiles")) {
            for (File f : DataIssues.factionFile.listFiles()) {
                f.delete();
            }
            for (File f : DataIssues.playerFile.listFiles()) {
                f.delete();
            }
            B_Faction.factions.clear();
            for (B_Player bp : B_Player.players.values()) {
                bp.setF(null);
            }
            Bukkit.broadcastMessage(ChatColor.AQUA + "Cleared Faction&Player files and Factions.");
            return true;
        }
        return false;
    }

}
