package snc.pFact;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import snc.pFact.DM.DataIssues;
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
        System.out.println("pFact başlatıldı!");
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
        System.out.println("pFact kapatıldı!");
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
        B_Player bp = B_Player.players.get(p.getUniqueId());
        if (arg[0].equalsIgnoreCase("oluştur")) {
            if (arg.length == 1) {
                sender.sendMessage(ChatColor.DARK_RED + "Bir isim gir!");
                return true;
            } else { // Faction olu�turma
                if (bp.getF() == null) {
                    Pattern p = Pattern.compile("[\\w\\-+|<>şçıüö,&*?/\\\\#]+");
                    if (arg[1].length() < 4 || arg[1].length() > 20 || arg[1].matches(p)) {
                        sender.sendMessage(ChatColor.DARK_RED + "Girdiğin klan ismi uyumsuz!");
                        sender.sendMessage(ChatColor.DARK_RED + "Klan isimleri yalnızca harf ve sayı içerebilir, özel karakterleri içeremez!");
                        return true;
                    }
                    bp.setRank(Rank.Founder);
                    B_Faction bf = new B_Faction(arg[1], bp);
                    bp.setF(bf);
                    B_Faction.factions.put(bf.getName(), bf);

                    sender.sendMessage(ChatColor.GREEN + "Klanın başarıyla oluşturuldu!! ::: " + bf.getName());
                    return true;
                } else {
                    p.sendMessage("Bir klana mensup olduğun için klan oluşturamazsın!");
                    return true;
                }

            }

        }

        if (arg.length == 2) {
            // kurucu yapma komutu
            if (arg[0].equalsIgnoreCase("kurucuyap") && B_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
                if (Bukkit.getPlayer(arg[1]).isOnline()) {
                    Player gp = Bukkit.getPlayer(arg[1]);
                    if (B_Player.players.get(gp.getUniqueId()).getF() != B_Player.players.get(p.getUniqueId()).getF()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                        return true;
                    } else {
                        B_Player.players.get(gp.getUniqueId()).setRank(Rank.Founder);
                        B_Player.players.get(p.getUniqueId()).setRank(Rank.Moderator);
                        B_Player.players.get(p.getUniqueId()).getF().setFounder(B_Player.players.get(gp.getUniqueId()));
                        B_Faction.factions.get(B_Player.players.get(p.getUniqueId()).getF().getName())
                            .setFounder(B_Player.players.get(gp.getUniqueId()));
                        sender.sendMessage(ChatColor.RED + "Artık kurucu değilsin!");
                        gp.sendMessage(ChatColor.GREEN + "Klanının yeni kurucusu sensin!");
                        return true;
                    }
                } else {
                    Player gp = (Player) Bukkit.getOfflinePlayer(arg[1]);
                    if (B_Player.players.get(gp.getUniqueId()).getF() != B_Player.players.get(p.getUniqueId()).getF()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                        return true;
                    } else {
                        B_Player.players.get(gp.getUniqueId()).setRank(Rank.Founder);
                        B_Player.players.get(p.getUniqueId()).setRank(Rank.Moderator);
                        B_Player.players.get(p.getUniqueId()).getF().setFounder(B_Player.players.get(gp.getUniqueId()));
                        B_Faction.factions.get(B_Player.players.get(p.getUniqueId()).getF().getName())
                            .setFounder(B_Player.players.get(gp.getUniqueId()));
                        sender.sendMessage(ChatColor.RED + "Artık kurucu değilsin!");
                        gp.sendMessage(ChatColor.GREEN + "Klanının yeni kurucusu sensin!");
                        return true;
                    }
                }
                
            }
        }

        if (arg.length == 1) {
            if (arg[0].equalsIgnoreCase("ayrıl")) {
                if (B_Player.players.get(p.getUniqueId()).getF() != null) {
                    if (B_Player.players.get(p.getUniqueId()).rank() != Rank.Founder) {
                        B_Player.players.get(p.getUniqueId()).setRank(Rank.Single);
                        p.sendMessage(ChatColor.GREEN + "Artık bir klana mensup değilsin!");
                        return true;
                    } else {
                        if (B_Player.players.get(p.getUniqueId()).getF().players.size() != 1) {
                            p.sendMessage(ChatColor.RED + "Kurucusu olduğun klandan ayrılamazsın!!");
                            p.sendMessage(ChatColor.RED + "Klanını devretmek için:" + ChatColor.RESET
                                + " /klan kurucuyap <oyuncu_ismi>");
                            return true;
                        } else {
                            B_Player.players.get(p.getUniqueId()).setRank(Rank.Single);
                            File aaf = new File(DataIssues.factionFile, B_Player.players.get(p.getUniqueId()).getF().getName() + ".df");
                            aaf.delete();
                            B_Faction.factions.remove(B_Player.players.get(p.getUniqueId()).getF().getName());
                            B_Player.players.get(p.getUniqueId()).setF(null);
                            p.sendMessage(ChatColor.GREEN + "Artık bir klana mensup değilsin!");
                            return true;
                        }
                        
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Bir klana mensup de�ilsin!");
                    return true;
                }
            }
        }

        if (arg[0].equalsIgnoreCase("oyuncu")) {
            if (B_Player.players.get(p.getUniqueId()).rank() != Rank.Moderator
                    && B_Player.players.get(p.getUniqueId()).rank() != Rank.Founder) {
                sender.sendMessage(ChatColor.DARK_RED + "Bu komutu girmek için "
                        + "gerekli yetkiye sahip değilsin ya da bir klana dahil değil.");
                return true;
            }
            if (arg.length == 1) {
                sender.sendMessage(ChatColor.RED + "Bir işlev gir!");
                return true;
            }
            if (arg.length == 2) {
                if (arg[1].equalsIgnoreCase("davet") || arg[1].equalsIgnoreCase("çıkar")) {
                    sender.sendMessage(ChatColor.RED + "Bir oyuncunun adını gir!");
                    return true;
                }
            }
            // oyuncu davet etme ve ��karma
            if (arg.length == 3) {
                if (arg[1].equalsIgnoreCase("davet")) {
                    Player gp = Bukkit.getPlayer(arg[2]);
                    if (gp == null) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu bulunamadı. Oyuncu "
                                + "başka bir klana üye ya da başka bir klan tarafından" + "davet edilmiş olabilir: "
                                + ChatColor.RESET + arg[2]);
                        return true;
                    }
                    B_Player bgp = B_Player.players.get(gp.getUniqueId());
                    if (bgp.getF() != null || bgp.getES()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu bulunamadı. Oyuncu "
                                + "başka bir klana üye ya da başka bir klan tarafından" + "davet edilmiş olabilir: "
                                + ChatColor.RESET + arg[2]);
                        return true;
                    }
                    bgp.setES(true);
                    bgp.setEF(bp.getF());
                    sender.sendMessage(ChatColor.GREEN + "Oyuncu davet edildi! "
                            + "Teklifini kabul ederse klanının bir üyesi olacak!:" + ChatColor.RESET + arg[2]);
                    gp.sendMessage(
                            ChatColor.RED + bp.getF().getName() + ChatColor.GREEN + " Klanı tarafından davet edildin!");
                    gp.sendMessage(ChatColor.GREEN + "Kabul etmek için: /klan kabul");
                    gp.sendMessage(ChatColor.GREEN + "Reddetmek için: /klan ret");
                    return true;
                }
                if (arg[1].equalsIgnoreCase("at")) {
                    if (B_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
                        if (Bukkit.getPlayer(arg[2]).isOnline()) {
                            Player gp = Bukkit.getPlayer(arg[2]);
                            if (B_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder || B_Player.players
                                    .get(gp.getUniqueId()).getF() != B_Player.players.get(p.getUniqueId()).getF()) {
                                sender.sendMessage(
                                        ChatColor.RED + "Bu oyuncu klanına dahil değil ya da klanında yönetici!");
                                return true;
                            }
                            B_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
                            B_Player.players.get(gp.getUniqueId()).setF(null);
                            B_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            return true;
                        } else {
                            @SuppressWarnings("deprecation")
                            Player gp = (Player) Bukkit.getOfflinePlayer(arg[2]);
                            if (B_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder || B_Player.players
                                    .get(gp.getUniqueId()).getF() != B_Player.players.get(p.getUniqueId()).getF()) {
                                sender.sendMessage(
                                        ChatColor.RED + "Bu oyuncu klanına dahil değil ya da klanında yönetici!");
                                return true;
                            }
                            B_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
                            B_Player.players.get(gp.getUniqueId()).setF(null);
                            B_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
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
                                        ChatColor.RED + "Bu oyuncu klanına dahil değil ya da klanında yönetici!");
                                return true;
                            }
                            B_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
                            B_Player.players.get(gp.getUniqueId()).setF(null);
                            B_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            return true;
                        } else {
                            @SuppressWarnings("deprecation")
                            Player gp = (Player) Bukkit.getOfflinePlayer(arg[2]);
                            if (B_Player.players.get(gp.getUniqueId()).rank() == Rank.Moderator
                                    || B_Player.players.get(gp.getUniqueId()).rank() == Rank.Founder
                                    || B_Player.players.get(gp.getUniqueId()).getF() != B_Player.players
                                            .get(p.getUniqueId()).getF()) {
                                sender.sendMessage(
                                        ChatColor.RED + "Bu oyuncu klanına dahil değil ya da klanında yönetici!");
                                return true;
                            }
                            B_Player.players.get(gp.getUniqueId()).getF().players.remove(gp.getUniqueId());
                            B_Player.players.get(gp.getUniqueId()).setF(null);
                            B_Player.players.get(gp.getUniqueId()).setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            return true;
                        }
                    }
                }
            }
        }

        // Davet Kabul ve Reddi
        if (arg[0].equalsIgnoreCase("kabul")) {
            if (!B_Player.players.get(p.getUniqueId()).getES()) {
                sender.sendMessage(ChatColor.RED + "Bir davet almadın ya da halihazırda bir klana mensupsun!");
            }
            B_Player.players.get(p.getUniqueId()).setF(B_Player.players.get(p.getUniqueId()).getEF());
            B_Faction.factions.get(B_Player.players.get(p.getUniqueId()).getEF().getName()).addMember(p.getUniqueId(),
                    B_Player.players.get(p.getUniqueId()));
            B_Player.players.get(p.getUniqueId()).setEF(null);
            B_Player.players.get(p.getUniqueId()).setES(false);
            B_Player.players.get(p.getUniqueId()).setRank(Rank.Player);
            sender.sendMessage(ChatColor.GREEN + B_Player.players.get(p.getUniqueId()).getF().getName()
                    + ChatColor.RESET + " Klanına katıldın!");
            for ( HashMap.Entry<UUID, B_Player> entry : B_Player.players.get(p.getUniqueId()).getF().players.entrySet()) {
                    Bukkit.getPlayer(
                        entry.getKey()).sendMessage(ChatColor.BOLD 
                        + ""
                        + ChatColor.GREEN 
                        + p.getDisplayName() 
                        + ChatColor.RESET 
                        + " Adlı oyuncu klana katıldı!");
            }
            return true;
        }
        if (arg[0].equalsIgnoreCase("ret")) {
            if (!B_Player.players.get(p.getUniqueId()).getES()) {
                sender.sendMessage(ChatColor.RED + "Bir davet almadın ya da halihazırda bir klana mensupsun!");
            }
            sender.sendMessage(ChatColor.GREEN + B_Player.players.get(p.getUniqueId()).getEF().getName()
                    + ChatColor.RESET + " Klanının daveti reddedildi!");
            B_Player.players.get(p.getUniqueId()).setEF(null);
            B_Player.players.get(p.getUniqueId()).setES(false);
            return true;
        }

        if (arg.length >= 3) {
            if (arg[0].equalsIgnoreCase("yetki") && B_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
                Player gp = Bukkit.getPlayer(arg[2]);
                if (B_Player.players.get(gp.getUniqueId()).getF() != B_Player.players.get(p.getUniqueId()).getF()) {
                    sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                    return true;
                } else {
                    if (arg[1].equalsIgnoreCase("ver")) {
                        B_Player.players.get(gp.getUniqueId()).setRank(Rank.Moderator);
                        sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                + " adlı oyuncuya yönetici yetkisi verildi!");
                        gp.sendMessage(ChatColor.GREEN + "Artık klanında bir yöneticisin!");
                        return true;
                    }
                    if (arg[1].equalsIgnoreCase("al")) {
                        B_Player.players.get(gp.getUniqueId()).setRank(Rank.Player);
                        sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                + " adlı oyuncunun yetkisi geri alındı!");
                        gp.sendMessage(ChatColor.GREEN + "Artık klanında bir yönetici değilsin!");
                        return true;
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Bir Klana mensup değilsin ya da kurucusu değilsin!");
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
            for (B_Player bpp : B_Player.players.values()) {
                bpp.setF(null);
            }
            Bukkit.broadcastMessage(ChatColor.AQUA + "Cleared Faction & Player files and Factions.");
            return true;
        }
        return false;
    }

}
