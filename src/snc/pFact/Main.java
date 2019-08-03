package snc.pFact;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

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
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
        // ./klan kur <isim>
        // ./klan oyuncu davet <oyuncu_ismi>
        // ./klan oyuncu at <oyuncu_ismi>
        // ./klan yetki ver <oyuncu_ismi>
        // ./klan yetki al <oyuncu_ismi>
        // ./klan kurucuyap <oyuncu_ismi>
        // ./klan kabul
        // ./klan ret
        // ./klan ayrıl
        // ./klan bilgi

        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        if (!label.equalsIgnoreCase("klan"))
            return false;
        if (arg.length == 0)
            return false;
        B_Player bp = B_Player.players.get(p.getUniqueId());

        if (arg[0].equalsIgnoreCase("bilgi")) {
            if (bp.getF() != null) {
                B_Faction fac = B_Faction.factions.get(bp.getF().getName());
                p.sendMessage(ChatColor.GREEN + fac.getName());
                p.sendMessage(ChatColor.GREEN + " Kurucu: " + ChatColor.RESET
                        + Bukkit.getPlayer(fac.getFounder()).getDisplayName());
                List<B_Player> list = fac.getByRank(Rank.Moderator);
                String yetkililer = "";
                if (!list.isEmpty()) {
                    for (B_Player l : list) {
                        yetkililer += Bukkit.getPlayer(l.uuid()).getDisplayName() + ",";
                    }
                    yetkililer = yetkililer.substring(0, yetkililer.length() - 1);
                    p.sendMessage(ChatColor.GREEN + " Yetkililer: " + ChatColor.RESET + yetkililer);
                } else {
                    p.sendMessage(ChatColor.GREEN + " Yetkililer: -");
                }
                String oyuncular = "";
                List<B_Player> list0 = fac.getByRank(Rank.Player);
                if (!list0.isEmpty()) {
                    for (B_Player l : list0) {
                        oyuncular += Bukkit.getPlayer(l.uuid()).getDisplayName() + ",";
                    }
                    oyuncular = oyuncular.substring(0, oyuncular.length() - 1);
                    p.sendMessage(ChatColor.GREEN + " Diğer Üyeler: " + ChatColor.RESET + oyuncular);
                } else {
                    p.sendMessage(ChatColor.GREEN + " Diğer Üyeler: -");
                }
                p.sendMessage(ChatColor.GREEN + " Seviye: " + fac.getLevel());
                p.sendMessage(ChatColor.GREEN + " Deneyim: " + fac.getXP());
                p.sendMessage(ChatColor.GREEN + " Prestij: " + fac.getPrst());
                return true;
            } else {
                p.sendMessage(ChatColor.DARK_RED + "Bir klana mensup olmadığın için bu komuta erişemezsin!");
                return true;
            }
        }

        if (arg[0].equalsIgnoreCase("kur")) {
            if (arg.length == 1) {
                sender.sendMessage(ChatColor.DARK_RED + "Bir isim gir!");
                return true;
            } else { // Faction kurma
                if (bp.getF() == null) {
                    Pattern pt = Pattern.compile("[\\w\\-+|<>şçıüö,&*?/\\\\#]+");
                    if (arg[1].length() < 4 || arg[1].length() > 20 || !pt.matcher(arg[1]).matches()
                            || B_Faction.factions.containsKey(arg[1])) {
                        sender.sendMessage(ChatColor.DARK_RED + "Girdiğin klan ismi uyumsuz!");
                        sender.sendMessage(ChatColor.DARK_RED
                                + "Klan isimleri yalnızca harf ve sayı içerebilir, özel karakterleri içeremez!");
                        sender.sendMessage(ChatColor.DARK_RED + "Bu isimde başka bir klan oluşturulmuş olabilir.");
                        return true;
                    }
                    bp.setRank(Rank.Founder);
                    B_Faction bf = new B_Faction(arg[1], bp);
                    bp.setF(arg[1]);
                    B_Faction.factions.put(bf.getName(), bf);

                    sender.sendMessage(
                            ChatColor.GREEN + "Klanın başarıyla oluşturuldu!! ::: " + ChatColor.RESET + bf.getName());
                    return true;
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Bir klana mensup olduğun için klan oluşturamazsın!");
                    return true;
                }

            }

        }

        if (arg.length == 2) {
            // kurucu yapma komutu
            if (arg[0].equalsIgnoreCase("kurucuyap") && B_Player.players.get(p.getUniqueId()).rank() == Rank.Founder) {
                if (Bukkit.getPlayer(arg[1]).isOnline()) {
                    Player gp = Bukkit.getPlayer(arg[1]);
                    B_Player ggp = B_Player.players.get(gp.getUniqueId());
                    B_Faction fcc = B_Faction.factions.get(bp.getF().getName());
                    if (ggp.getF() != bp.getF()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                        return true;
                    } else {
                        ggp.setRank(Rank.Founder);
                        bp.setRank(Rank.Moderator);
                        fcc.setFounder(ggp);
                        sender.sendMessage(ChatColor.RED + "Artık kurucu değilsin!");
                        gp.sendMessage(ChatColor.GREEN + "Klanının yeni kurucusu sensin!");
                        return true;
                    }
                } else {
                    Player gp = (Player) Bukkit.getOfflinePlayer(arg[1]);
                    B_Player ggp = B_Player.players.get(gp.getUniqueId());
                    B_Faction fcc = B_Faction.factions.get(bp.getF().getName());
                    if (ggp.getF() != bp.getF()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                        return true;
                    } else {
                        ggp.setRank(Rank.Founder);
                        bp.setRank(Rank.Moderator);
                        fcc.setFounder(ggp);
                        sender.sendMessage(ChatColor.RED + "Artık kurucu değilsin!");
                        return true;
                    }
                }

            }
        }

        if (arg.length == 1) {
            if (arg[0].equalsIgnoreCase("ayrıl")) {
                if (bp.getF() != null) {
                    if (bp.rank() != Rank.Founder) {
                        bp.setRank(Rank.Single);
                        p.sendMessage(ChatColor.GREEN + "Artık bir klana mensup değilsin!");
                        return true;
                    } else {
                        B_Faction fcc = B_Faction.factions.get(bp.getF().getName());
                        if (fcc.players.size() != 1) {
                            p.sendMessage(ChatColor.RED + "Kurucusu olduğun klandan ayrılamazsın!!");
                            p.sendMessage(ChatColor.RED + "Klanını devretmek için:" + ChatColor.RESET
                                    + " /klan kurucuyap <oyuncu_ismi>");
                            return true;
                        } else {
                            bp.setRank(Rank.Single);
                            File aaf = new File(DataIssues.factionFile, fcc.getName() + ".df");
                            aaf.delete();
                            B_Faction.factions.remove(fcc.getName());
                            bp.setF(null);
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
            if (bp.rank() != Rank.Moderator && bp.rank() != Rank.Founder) {
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
                    bgp.setEF(bp.getF().getName());
                    sender.sendMessage(ChatColor.GREEN + "Oyuncu davet edildi! "
                            + "Teklifini kabul ederse klanının bir üyesi olacak!:" + ChatColor.RESET + arg[2]);
                    gp.sendMessage(
                            ChatColor.RED + bp.getF().getName() + ChatColor.GREEN + " Klanı tarafından davet edildin!");
                    gp.sendMessage(ChatColor.GREEN + "Kabul etmek için: /klan kabul");
                    gp.sendMessage(ChatColor.GREEN + "Reddetmek için: /klan ret");
                    return true;
                }
                if (arg[1].equalsIgnoreCase("at")) {
                    if (bp.rank() == Rank.Founder) {
                        if (Bukkit.getPlayer(arg[2]).isOnline()) {
                            Player gp = Bukkit.getPlayer(arg[2]);
                            B_Player ggp = B_Player.players.get(gp.getUniqueId());
                            if (ggp.rank() == Rank.Founder
                                    || B_Player.players.get(gp.getUniqueId()).getF() != bp.getF()) {
                                sender.sendMessage(
                                        ChatColor.RED + "Bu oyuncu klanına dahil değil ya da klanında yönetici!");
                                return true;
                            }
                            ggp.getF().players.remove(gp.getUniqueId());
                            ggp.setF(null);
                            ggp.setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            gp.sendMessage(ChatColor.DARK_RED + "Bulunduğun klandan atıldın!");
                            return true;
                        } else {
                            Player gp = (Player) Bukkit.getOfflinePlayer(arg[2]);
                            B_Player ggp = B_Player.players.get(gp.getUniqueId());
                            if (ggp.rank() == Rank.Founder || ggp.getF() != bp.getF()) {
                                sender.sendMessage(
                                        ChatColor.RED + "Bu oyuncu klanına dahil değil ya da klanında yönetici!");
                                return true;
                            }
                            ggp.getF().players.remove(gp.getUniqueId());
                            ggp.setF(null);
                            ggp.setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            return true;
                        }
                    }
                    if (bp.rank() == Rank.Moderator) {
                        if (Bukkit.getPlayer(arg[2]).isOnline()) {
                            Player gp = Bukkit.getPlayer(arg[2]);
                            B_Player ggp = B_Player.players.get(gp.getUniqueId());
                            if (ggp.rank() == Rank.Moderator || ggp.rank() == Rank.Founder || ggp.getF() != bp.getF()) {
                                sender.sendMessage(
                                        ChatColor.RED + "Bu oyuncu klanına dahil değil ya da klanında yönetici!");
                                return true;
                            }
                            ggp.getF().players.remove(gp.getUniqueId());
                            ggp.setF(null);
                            ggp.setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            gp.sendMessage(ChatColor.DARK_RED + "Bulunduğun klandan atıldın!");
                            return true;
                        } else {
                            Player gp = (Player) Bukkit.getOfflinePlayer(arg[2]);
                            B_Player ggp = B_Player.players.get(gp.getUniqueId());
                            if (ggp.rank() == Rank.Moderator || ggp.rank() == Rank.Founder || ggp.getF() != bp.getF()) {
                                sender.sendMessage(
                                        ChatColor.RED + "Bu oyuncu klanına dahil değil ya da klanında kurucu!");
                                return true;
                            }
                            ggp.getF().players.remove(gp.getUniqueId());
                            ggp.setF(null);
                            ggp.setRank(Rank.Single);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            return true;
                        }
                    }
                }
            }
        }

        // Davet Kabul ve Reddi
        if (arg[0].equalsIgnoreCase("kabul")) {
            if (!bp.getES()) {
                sender.sendMessage(ChatColor.RED + "Bir davet almadın ya da halihazırda bir klana mensupsun!");
            }
            bp.setF(bp.getEF().getName());
            B_Faction.factions.get(bp.getEF().getName()).addMember(p.getUniqueId(), bp);
            bp.setEF(null);
            bp.setES(false);
            bp.setRank(Rank.Player);
            sender.sendMessage(ChatColor.GREEN + bp.getF().getName() + ChatColor.RESET + " Klanına katıldın!");
            for (HashMap.Entry<UUID, B_Player> entry : bp.getF().players.entrySet()) {
                Bukkit.getPlayer(entry.getKey()).sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + p.getDisplayName()
                        + ChatColor.RESET + " Adlı oyuncu klana katıldı!");
            }
            return true;
        }
        if (arg[0].equalsIgnoreCase("ret")) {
            if (!bp.getES()) {
                sender.sendMessage(ChatColor.RED + "Bir davet almadın ya da halihazırda bir klana mensupsun!");
            }
            sender.sendMessage(
                    ChatColor.GREEN + bp.getEF().getName() + ChatColor.RESET + " Klanının daveti reddedildi!");
            bp.setEF(null);
            bp.setES(false);
            return true;
        }

        if (arg.length >= 3) {
            if (arg[0].equalsIgnoreCase("yetki") && bp.rank() == Rank.Founder) {
                if (Bukkit.getPlayer(arg[2]).isOnline()) {
                    Player gp = Bukkit.getPlayer(arg[2]);
                    B_Player ggp = B_Player.players.get(gp.getUniqueId());
                    if (ggp.getF() != bp.getF()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                        return true;
                    } else {
                        if (arg[1].equalsIgnoreCase("ver")) {
                            ggp.setRank(Rank.Moderator);
                            sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                    + " adlı oyuncuya yönetici yetkisi verildi!");
                            gp.sendMessage(ChatColor.GREEN + "Artık klanında bir yöneticisin!");
                            return true;
                        }
                        if (arg[1].equalsIgnoreCase("al")) {
                            ggp.setRank(Rank.Player);
                            sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                    + " adlı oyuncunun yetkisi geri alındı!");
                            gp.sendMessage(ChatColor.GREEN + "Artık klanında bir yönetici değilsin!");
                            return true;
                        }
                    }
                } else {
                    Player gp = (Player) Bukkit.getOfflinePlayer(arg[2]);
                    B_Player ggp = B_Player.players.get(gp.getUniqueId());
                    if (ggp.getF() != bp.getF()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                        return true;
                    } else {
                        if (arg[1].equalsIgnoreCase("ver")) {
                            ggp.setRank(Rank.Moderator);
                            sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                    + " adlı oyuncuya yönetici yetkisi verildi!");
                            return true;
                        }
                        if (arg[1].equalsIgnoreCase("al")) {
                            ggp.setRank(Rank.Player);
                            sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                    + " adlı oyuncunun yetkisi geri alındı!");
                            return true;
                        }
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
            HashMap<UUID, B_Player> temp = new HashMap<>();
            for (UUID idd : B_Player.players.keySet()) {
                B_Player ps = new B_Player(idd, null, 0, Rank.Single);
                temp.put(idd, ps);
            }
            B_Player.players = temp;
            Bukkit.broadcastMessage(ChatColor.AQUA + "Cleared Faction & Player files and Factions.");
            return true;
        }
        return false;
    }

}
