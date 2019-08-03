package snc.pFact;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.obj.cl.B_FactionMember;
import snc.pFact.obj.cl.B_Player;
import snc.pFact.obj.cl.B_Player.Rank;

public class Main extends JavaPlugin {

    public static JavaPlugin ekl;
    public static int task;

    @Override
    public void onEnable() {
        ekl = this;

        DataIssues.initalize();
        DataIssues.load();
        System.out.println("pFact başlatıldı!");
        PluginManager pm = getServer().getPluginManager();
        ListenerClass lc = new ListenerClass();
        pm.registerEvents(lc, this);

        BukkitScheduler scheduler = getServer().getScheduler();
        task = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (B_Faction fct : DataIssues.factions.values()) {
                    fct.update();
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (DataIssues.players.containsKey(p.getUniqueId()))
                        DataIssues.players.get(p.getUniqueId()).update();
                }
            }
        }, 0L, 20L);

    }

    @Override
    public void onDisable() {
        DataIssues.save();
        Bukkit.getScheduler().cancelTask(task);
        System.out.println("pFact kapatıldı!");
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

        // ./param

        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        B_Player bp = DataIssues.players.get(p.getUniqueId());
        B_Faction bf = bp.getF();
        if (label.equalsIgnoreCase("param")) {
            p.sendMessage(ChatColor.GREEN + "Mevcut Paranız: " + ChatColor.RESET + bp.getCoin());
            return true;
        }
        if (!label.equalsIgnoreCase("klan"))
            return false;
        if (arg.length == 0)
            return false;

        if (arg[0].equalsIgnoreCase("bağış")) {
            if (arg.length == 2 && bp.getF() != null) {
                double bagis;
                try {
                    bagis = Double.parseDouble(arg[1]);
                } catch (NumberFormatException e) {
                    p.sendMessage(ChatColor.DARK_RED + "Geçerli bir değer gir!");
                    return false;
                }
                if (bp.getCoin() >= bagis) {
                    bp.addCoin((-1) * bagis);
                    bp.getF().addBankAmount(bagis);
                    p.sendMessage("Klanına yaptığın bağış miktarı: " + ChatColor.GREEN + bagis);
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Sahip olduğundan fazla bağış yapamazsın!");
                }
            } else {
                p.sendMessage(ChatColor.DARK_RED + "Bir klana mensup değilsin!");
            }
        }
        if (arg[0].equalsIgnoreCase("bilgi")) {
            if (bp.getF() != null) {
                B_Faction fac = DataIssues.factions.get(bp.getF().getName());
                p.sendMessage(ChatColor.GREEN + fac.getName());
                p.sendMessage(ChatColor.GREEN + " Kurucu: " + ChatColor.RESET + fac.getFounder().getName());
                List<B_FactionMember> list = fac.getByRank(Rank.Moderator);
                String yetkililer = "";
                if (!list.isEmpty()) {
                    for (B_FactionMember l : list) {
                        yetkililer += l.getName() + ",";
                    }
                    yetkililer = yetkililer.substring(0, yetkililer.length() - 1);
                    p.sendMessage(ChatColor.GREEN + " Yetkililer: " + ChatColor.RESET + yetkililer);
                } else {
                    p.sendMessage(ChatColor.GREEN + " Yetkililer: -");
                }
                String oyuncular = "";
                List<B_FactionMember> list0 = fac.getByRank(Rank.Player);
                if (!list0.isEmpty()) {
                    for (B_FactionMember l : list0) {
                        oyuncular += l.getName() + ",";
                    }
                    oyuncular = oyuncular.substring(0, oyuncular.length() - 1);
                    p.sendMessage(ChatColor.GREEN + " Diğer Üyeler: " + ChatColor.RESET + oyuncular);
                } else {
                    p.sendMessage(ChatColor.GREEN + " Diğer Üyeler: -");
                }
                p.sendMessage(ChatColor.GREEN + " Seviye: " + fac.getLevel());
                p.sendMessage(ChatColor.GREEN + " Deneyim: " + fac.getXP() + "/" + fac.getNXP());
                p.sendMessage(ChatColor.GREEN + " Prestij: " + fac.getPrestige());
                p.sendMessage(ChatColor.GREEN + " Banka: " + fac.getBank());
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
                if (bf == null) {
                    Pattern pt = Pattern.compile("[\\w\\-+|<>şçıüö,&*?/\\\\#]+");
                    if (arg[1].length() < 4 || arg[1].length() > 20 || !pt.matcher(arg[1]).matches()
                            || DataIssues.factions.containsKey(arg[1])) {
                        sender.sendMessage(ChatColor.DARK_RED + "Girdiğin klan ismi uyumsuz!");
                        sender.sendMessage(ChatColor.DARK_RED
                                + "Klan isimleri yalnızca harf ve sayı içerebilir, özel karakterleri içeremez!");
                        sender.sendMessage(ChatColor.DARK_RED + "Bu isimde başka bir klan oluşturulmuş olabilir.");
                        return true;
                    }
                    bf = new B_Faction(arg[1], p.getUniqueId());
                    bp.setF(arg[1]);
                    DataIssues.factions.put(bf.getName(), bf);

                    sender.sendMessage(
                            ChatColor.GREEN + "Klanın başarıyla oluşturuldu!! ::: " + ChatColor.RESET + bf.getName());
                    return true;
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Bir klana mensup olduğun için klan oluşturamazsın!");
                    return true;
                }

            }

        }

        if (arg[0].equalsIgnoreCase("kurucuyap")) {
            if (arg.length < 2) {
                sender.sendMessage(ChatColor.RED + "/klan kurucuyap <isim>");
                return true;
            }

            if (bf == null) {
                sender.sendMessage(ChatColor.RED + "Bir klana mensup değilsin.");
                return true;
            }
            B_FactionMember bfm = bf.getPlayer(bp.uuid());
            if (bfm.rank() != Rank.Founder) {
                sender.sendMessage(ChatColor.RED + "Klanında kurucu değilsin.");
                return true;
            }
            // kurucu yapma komutu

            if (Bukkit.getPlayer(arg[1]) != null && Bukkit.getPlayer(arg[1]).isOnline()) {
                Player gp = Bukkit.getPlayer(arg[1]);
                B_Player ggp = DataIssues.players.get(gp.getUniqueId());
                B_Faction gf = ggp.getF();
                if (gf != bf) {
                    sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                    return true;
                } else {
                    gf.changeFounder(ggp.uuid());
                    sender.sendMessage(ChatColor.RED + "Artık kurucu değilsin!");
                    gp.sendMessage(ChatColor.GREEN + "Klanının yeni kurucusu sensin!");
                    return true;
                }
            } else {
                Player gp = (Player) Bukkit.getOfflinePlayer(arg[1]);
                B_Player ggp = DataIssues.players.get(gp.getUniqueId());
                B_Faction gf = bp.getF();
                if (ggp.getF() != bp.getF()) {
                    sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                    return true;
                } else {
                    gf.changeFounder(ggp.uuid());
                    sender.sendMessage(ChatColor.RED + "Artık kurucu değilsin!");
                    return true;
                }
            }
        }

        if (arg[0].equalsIgnoreCase("ayrıl")) {
            if (bf == null) {
                sender.sendMessage(ChatColor.RED + "Bir klana mensup değilsin.");
                return true;
            }
            if (bf.getFounder().uuid() != bp.uuid()) {

                bf.players.remove(bp.uuid());
                bp.setF(null);
                p.sendMessage(ChatColor.GREEN + "Artık bir klana mensup değilsin!");
                return true;
            } else {
                if (bf.players.size() != 1) {
                    p.sendMessage(ChatColor.RED + "Kurucusu olduğun klandan ayrılamazsın!!");
                    p.sendMessage(ChatColor.RED + "Klanını devretmek için:" + ChatColor.RESET
                            + " /klan kurucuyap <oyuncu_ismi>");
                    return true;
                } else {
                    DataIssues.factions.remove(bf.getName());
                    bp.setF(null);
                    p.sendMessage(ChatColor.GREEN + "Artık bir klana mensup değilsin!");
                    return true;
                }

            }
        }

        if (arg[0].equalsIgnoreCase("oyuncu")) {
            if (bf == null) {
                sender.sendMessage(ChatColor.RED + "Bir klana mensup değilsin.");
                return true;
            }
            if (bf.getPlayer(bp.uuid()).rank() == Rank.Player) {
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
            // oyuncu davet etme ve çıkarma
            if (arg.length == 3) {
                if (arg[1].equalsIgnoreCase("davet")) {
                    Player gp = Bukkit.getPlayer(arg[2]);
                    if (gp == null || !gp.isOnline()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu bulunamadı:" + ChatColor.RESET + arg[2]);
                        return true;
                    }
                    B_Player bgp = DataIssues.players.get(gp.getUniqueId());

                    if (bgp.getF() == bf) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu zaten senin klanında: " + ChatColor.RESET + arg[2]);
                        return true;
                    }
                    if (bgp.hasFaction()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu başka bir klana üye: " + ChatColor.RESET + arg[2]);
                        return true;
                    }
                    if (bgp.getES()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu başka bir klan tarafından davet edilmiş olabilir: "
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
                    Player gp = Bukkit.getPlayer(arg[2]);
                    if (gp != null && gp.isOnline()) {
                        B_Player bgp = DataIssues.players.get(gp.getUniqueId());
                        B_Faction gf = bgp.getF();
                        if (bgp.getF() != bp.getF()) {
                            sender.sendMessage(ChatColor.RED + "Bu oyuncu klanına dahil değil");
                            return true;
                        }
                        B_FactionMember bfm = bf.getPlayer(bp.uuid());
                        B_FactionMember gfm = gf.getPlayer(bgp.uuid());
                        if (gfm.rank() == Rank.Founder) {
                            sender.sendMessage(ChatColor.RED + "Bu oyuncu klanında yönetici!");
                            return true;
                        }
                        if (bfm.rank() == Rank.Founder) {
                            bf.players.remove(gp.getUniqueId());
                            bgp.setF(null);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            gp.sendMessage(ChatColor.DARK_RED + "Bulunduğun klandan atıldın!");
                            return true;
                        }
                        if (bfm.rank() == Rank.Moderator && gfm.rank() == Rank.Player) {

                            bf.players.remove(gp.getUniqueId());
                            bgp.setF(null);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            gp.sendMessage(ChatColor.DARK_RED + "Bulunduğun klandan atıldın!");
                            return true;
                        }
                        if (gfm.rank() == Rank.Moderator) {
                            sender.sendMessage(ChatColor.RED + "Oyuncu klanında bir yetkili.");
                            return true;
                        }
                        return true;
                    } else {
                        OfflinePlayer ogp = Bukkit.getOfflinePlayer(arg[2]);
                        if (ogp == null) {
                            sender.sendMessage(ChatColor.RED + "Oyuncu bulunamadı!");
                            return true;
                        }

                        B_FactionMember gfm = bf.getPlayer(ogp.getUniqueId());
                        if (gfm == null) {
                            sender.sendMessage(ChatColor.RED + "Bu oyuncu klanına dahil değil");
                            return true;
                        }

                        B_FactionMember bfm = bf.getPlayer(bp.uuid());
                        if (gfm.rank() == Rank.Founder) {
                            sender.sendMessage(ChatColor.RED + "Bu oyuncu klanında yönetici!");
                            return true;
                        }
                        if (bfm.rank() == Rank.Founder) {
                            bf.players.remove(gp.getUniqueId());
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            return true;
                        }
                        if (bfm.rank() == Rank.Moderator && gfm.rank() == Rank.Player) {
                            bf.players.remove(gp.getUniqueId());
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            return true;
                        }
                        if (gfm.rank() == Rank.Moderator) {
                            sender.sendMessage(ChatColor.RED + "Oyuncu klanında bir yetkili.");
                            return true;
                        }
                        return true;
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
            bp.getEF().addMember(bp.uuid());
            bp.setEF(null);
            bp.setES(false);
            sender.sendMessage(ChatColor.GREEN + bp.getF().getName() + ChatColor.RESET + " Klanına katıldın!");
            for (B_FactionMember bfm : bp.getF().players.values()) {
                if (bfm.isOnline() && bfm.uuid() != bp.uuid())
                    Bukkit.getPlayer(bfm.uuid()).sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + p.getDisplayName()
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

        if (arg[0].equalsIgnoreCase("yetki")) {
            if (bf == null) {
                sender.sendMessage(ChatColor.RED + "Bir klana mensup değilsin.");
                return true;
            }
            if (arg.length < 3) {
                sender.sendMessage(ChatColor.RED + "/klan yetki ver/al <isim>");
                return true;
            }
            B_FactionMember bfm = bf.getPlayer(p.getUniqueId());
            if (bfm.rank() == Rank.Founder) {
                Player gp = Bukkit.getPlayer(arg[2]);
                if (gp != null && gp.isOnline()) {
                    // B_Player ggp = DataIssues.players.get(gp.getUniqueId());
                    B_FactionMember gfm = bf.getPlayer(gp.getUniqueId());
                    if (gfm == null) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                        return true;
                    }
                    if (arg[1].equalsIgnoreCase("ver")) {
                        gfm.setRank(Rank.Moderator);
                        sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                + " adlı oyuncuya yönetici yetkisi verildi!");
                        gp.sendMessage(ChatColor.GREEN + "Artık klanında bir yetkilisin!");
                        return true;
                    }
                    if (arg[1].equalsIgnoreCase("al")) {
                        gfm.setRank(Rank.Player);
                        sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                + " adlı oyuncunun yetkisi geri alındı!");
                        gp.sendMessage(ChatColor.GREEN + "Artık klanında bir yetkili değilsin!");
                        return true;
                    }
                } else {
                    Player ggp = (Player) Bukkit.getOfflinePlayer(arg[2]);
                    if (ggp == null) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu bulunamadı.");
                        return true;
                    }
                    B_FactionMember gfm = bf.getPlayer(ggp.getUniqueId());
                    if (gfm == null) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                        return true;
                    }
                    if (arg[1].equalsIgnoreCase("ver")) {
                        gfm.setRank(Rank.Moderator);
                        sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                + " adlı oyuncuya yönetici yetkisi verildi!");
                        return true;
                    }
                    if (arg[1].equalsIgnoreCase("al")) {
                        gfm.setRank(Rank.Player);
                        sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                + " adlı oyuncunun yetkisi geri alındı!");
                        return true;
                    }

                }

            } else {
                sender.sendMessage(ChatColor.RED + "Klanının kurucusu değilsin!");
                return true;
            }
        }

        if (arg[0].equalsIgnoreCase("clearfiles")) {
            DataIssues.factions.clear();
            HashMap<UUID, B_Player> temp = new HashMap<>();
            for (UUID idd : DataIssues.players.keySet()) {
                B_Player ps = new B_Player(idd, null, 0);
                temp.put(idd, ps);
            }
            DataIssues.players.clear();
            DataIssues.players.putAll(temp);
            Bukkit.broadcastMessage(ChatColor.AQUA + "Cleared Faction & Player files and Factions.");
            return true;
        }
        return false;
    }

}
