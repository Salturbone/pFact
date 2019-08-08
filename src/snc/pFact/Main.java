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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.Zindev.utils.ZChestLibV6.ChestManager;
import snc.pFact.Claim.ClaimFactory;
import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.obj.cl.B_FactionMember;
import snc.pFact.obj.cl.B_Player;
import snc.pFact.obj.cl.B_Player.Rank;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaFactory;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols112;

public class Main extends JavaPlugin {

    public static JavaPlugin ekl;
    public static ChestManager cm;
    public static int task;
    public static GlowingMagmaFactory gmf;

    @Override
    public void onEnable() {
        ekl = this;
        gmf = new GlowingMagmaFactory(this, new GlowingMagmaProtocols112(), 300000);
        gmf.doInitialize();
        cm = new ChestManager();
        cm.initialize(this);
        DataIssues.initalize();
        DataIssues.load();
        System.out.println("pFact başlatıldı!");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ListenerClass(), this);
        ClaimFactory.initialize();
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
        gmf.doUninitialize();
        DataIssues.save();
        ClaimFactory.deInitialize();
        cm.uninitialize();
        Bukkit.getScheduler().cancelTask(task);
        System.out.println("pFact kapatıldı!");
    }

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
        /*
         * if (label.equalsIgnoreCase("param")) { p.sendMessage(ChatColor.GREEN +
         * "Mevcut Paranız: " + ChatColor.RESET + bp.getCoin()); return true; }
         */
        if (!label.equalsIgnoreCase("klan"))
            return false;
        if (args.length == 0)
            return false;

        if (args[0].equalsIgnoreCase("bağış")) {
            if (args.length == 2 && bp.getF() != null) {
                double bagis;
                try {
                    bagis = Double.parseDouble(args[1]);
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
        if (args[0].equalsIgnoreCase("bilgi")) {
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

        if (args[0].equalsIgnoreCase("kur")) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.DARK_RED + "Bir isim gir!");
                return true;
            } else { // Faction kurma
                if (bf == null) {
                    Pattern pt = Pattern.compile("[\\w\\-+|<>şçıüö,&*?/\\\\#]+");
                    if (args[1].length() < 4 || args[1].length() > 20 || !pt.matcher(args[1]).matches()
                            || DataIssues.factions.containsKey(args[1])) {
                        sender.sendMessage(ChatColor.DARK_RED + "Girdiğin klan ismi uyumsuz!");
                        sender.sendMessage(ChatColor.DARK_RED
                                + "Klan isimleri yalnızca harf ve sayı içerebilir, özel karakterleri içeremez!");
                        sender.sendMessage(ChatColor.DARK_RED + "Bu isimde başka bir klan oluşturulmuş olabilir.");
                        return true;
                    }
                    bf = new B_Faction(args[1], p.getUniqueId());
                    bp.setF(args[1]);
                    DataIssues.factions.put(bf.getName(), bf);

                    sender.sendMessage(
                            ChatColor.GREEN + "Klanın başarıyla oluşturuldu!! ::: " + ChatColor.RESET + bf.getName());
                    ItemStack is = ClaimFactory.getStandartMainClaim().getClaimItem(bf.getName());
                    p.getInventory().addItem(is);
                    return true;
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Bir klana mensup olduğun için klan oluşturamazsın!");
                    return true;
                }

            }

        }

        if (args[0].equalsIgnoreCase("kurucuyap")) {
            if (args.length < 2) {
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

            if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
                Player gp = Bukkit.getPlayer(args[1]);
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
                OfflinePlayer gp = Bukkit.getOfflinePlayer(args[1]);
                if (bp.getF().getPlayer(gp.getUniqueId()) == null) {
                    sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                    return true;
                } else {
                    bf.changeFounder(gp.getUniqueId());
                    sender.sendMessage(ChatColor.RED + "Artık kurucu değilsin!");
                    return true;
                }
            }
        }

        if (args[0].equalsIgnoreCase("ayrıl")) {
            if (bf == null) {
                sender.sendMessage(ChatColor.RED + "Bir klana mensup değilsin.");
                return true;
            }
            if (bf.getFounder().uuid() != bp.uuid()) {

                bf.getFactionMembers().remove(bp.uuid());
                bp.setF(null);
                p.sendMessage(ChatColor.GREEN + "Artık bir klana mensup değilsin!");
                return true;
            } else {
                if (bf.getFactionMembers().size() != 1) {
                    p.sendMessage(ChatColor.RED + "Kurucusu olduğun klandan ayrılamazsın!!");
                    p.sendMessage(ChatColor.RED + "Klanını devretmek için:" + ChatColor.RESET
                            + " /klan kurucuyap <oyuncu_ismi>");
                    return true;
                } else {
                    bf.disband();
                    DataIssues.factions.remove(bf.getName());
                    bp.setF(null);
                    p.sendMessage(ChatColor.GREEN + "Artık bir klana mensup değilsin!");
                    return true;
                }

            }
        }

        if (args[0].equalsIgnoreCase("oyuncu")) {
            if (bf == null) {
                sender.sendMessage(ChatColor.RED + "Bir klana mensup değilsin.");
                return true;
            }
            if (bf.getPlayer(bp.uuid()).rank() == Rank.Player) {
                sender.sendMessage(ChatColor.DARK_RED + "Bu komutu girmek için "
                        + "gerekli yetkiye sahip değilsin ya da bir klana dahil değil.");
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Bir işlev gir!");
                return true;
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("davet") || args[1].equalsIgnoreCase("çıkar")) {
                    sender.sendMessage(ChatColor.RED + "Bir oyuncunun adını gir!");
                    return true;
                }
            }
            // oyuncu davet etme ve çıkarma
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("davet")) {
                    Player gp = Bukkit.getPlayer(args[2]);
                    if (gp == null || !gp.isOnline()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu bulunamadı:" + ChatColor.RESET + args[2]);
                        return true;
                    }
                    B_Player bgp = DataIssues.players.get(gp.getUniqueId());

                    if (bgp.getF() == bf) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu zaten senin klanında: " + ChatColor.RESET + args[2]);
                        return true;
                    }
                    if (bgp.hasFaction()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu başka bir klana üye: " + ChatColor.RESET + args[2]);
                        return true;
                    }
                    if (bgp.getES()) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu başka bir klan tarafından davet edilmiş olabilir: "
                                + ChatColor.RESET + args[2]);
                        return true;
                    }
                    bgp.setES(true);
                    bgp.setEF(bp.getF().getName());
                    sender.sendMessage(ChatColor.GREEN + "Oyuncu davet edildi! "
                            + "Teklifini kabul ederse klanının bir üyesi olacak!:" + ChatColor.RESET + args[2]);
                    gp.sendMessage(
                            ChatColor.RED + bp.getF().getName() + ChatColor.GREEN + " Klanı tarafından davet edildin!");
                    gp.sendMessage(ChatColor.GREEN + "Kabul etmek için: /klan kabul");
                    gp.sendMessage(ChatColor.GREEN + "Reddetmek için: /klan ret");
                    return true;
                }
                if (args[1].equalsIgnoreCase("at")) {
                    Player gp = Bukkit.getPlayer(args[2]);
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
                            bf.getFactionMembers().remove(gp.getUniqueId());
                            bgp.setF(null);
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            gp.sendMessage(ChatColor.DARK_RED + "Bulunduğun klandan atıldın!");
                            return true;
                        }
                        if (bfm.rank() == Rank.Moderator && gfm.rank() == Rank.Player) {

                            bf.getFactionMembers().remove(gp.getUniqueId());
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
                        OfflinePlayer ogp = Bukkit.getOfflinePlayer(args[2]);
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
                            bf.getFactionMembers().remove(gp.getUniqueId());
                            sender.sendMessage(ChatColor.GREEN + "Oyuncu klanından atıldı!");
                            return true;
                        }
                        if (bfm.rank() == Rank.Moderator && gfm.rank() == Rank.Player) {
                            bf.getFactionMembers().remove(gp.getUniqueId());
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
        if (args[0].equalsIgnoreCase("kabul")) {
            if (!bp.getES()) {
                sender.sendMessage(ChatColor.RED + "Bir davet almadın ya da halihazırda bir klana mensupsun!");
            }
            bp.setF(bp.getEF().getName());
            bp.getEF().addMember(bp.uuid());
            bp.setEF(null);
            bp.setES(false);
            sender.sendMessage(ChatColor.GREEN + bp.getF().getName() + ChatColor.RESET + " Klanına katıldın!");
            for (Player pl : bp.getF().getOnlinePlayers()) {
                if (!pl.getUniqueId().equals(p.getUniqueId()))
                    p.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + p.getDisplayName() + ChatColor.RESET
                            + " Adlı oyuncu klana katıldı!");
            }

            return true;
        }
        if (args[0].equalsIgnoreCase("ret")) {
            if (!bp.getES()) {
                sender.sendMessage(ChatColor.RED + "Bir davet almadın ya da halihazırda bir klana mensupsun!");
            }
            sender.sendMessage(
                    ChatColor.GREEN + bp.getEF().getName() + ChatColor.RESET + " Klanının daveti reddedildi!");
            bp.setEF(null);
            bp.setES(false);
            return true;
        }

        if (args[0].equalsIgnoreCase("yetki")) {
            if (bf == null) {
                sender.sendMessage(ChatColor.RED + "Bir klana mensup değilsin.");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "/klan yetki ver/al <isim>");
                return true;
            }
            B_FactionMember bfm = bf.getPlayer(p.getUniqueId());
            if (bfm.rank() == Rank.Founder) {
                Player gp = Bukkit.getPlayer(args[2]);
                if (gp != null && gp.isOnline()) {
                    // B_Player ggp = DataIssues.players.get(gp.getUniqueId());
                    B_FactionMember gfm = bf.getPlayer(gp.getUniqueId());
                    if (gfm == null) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("ver")) {
                        gfm.setRank(Rank.Moderator);
                        sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                + " adlı oyuncuya yönetici yetkisi verildi!");
                        gp.sendMessage(ChatColor.GREEN + "Artık klanında bir yetkilisin!");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("al")) {
                        gfm.setRank(Rank.Player);
                        sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                + " adlı oyuncunun yetkisi geri alındı!");
                        gp.sendMessage(ChatColor.GREEN + "Artık klanında bir yetkili değilsin!");
                        return true;
                    }
                } else {
                    Player ggp = (Player) Bukkit.getOfflinePlayer(args[2]);
                    if (ggp == null) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu bulunamadı.");
                        return true;
                    }
                    B_FactionMember gfm = bf.getPlayer(ggp.getUniqueId());
                    if (gfm == null) {
                        sender.sendMessage(ChatColor.RED + "Oyuncu klanının bir mensubu değil!");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("ver")) {
                        gfm.setRank(Rank.Moderator);
                        sender.sendMessage(ChatColor.GREEN + gp.getDisplayName() + ChatColor.RESET
                                + " adlı oyuncuya yönetici yetkisi verildi!");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("al")) {
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

        if (args[0].equalsIgnoreCase("admin") && p.isOp()) {
            if (args.length < 3) {
                sender.sendMessage("/klan admin <klan ismi> <xp/yık/at/katıl/para> <args> <args>");
                return true;
            }
            B_Faction bff = DataIssues.factions.get(args[1]);
            if (bff == null) {
                sender.sendMessage("klan bulunamadı");
                return true;
            }
            if (args[2].equalsIgnoreCase("xp")) {
                if (args.length < 4) {
                    sender.sendMessage("/klan admin <klan ismi> xp <miktar>");
                    return true;
                }
                try {
                    Double d = Double.parseDouble(args[3]);
                    bff.addXP(d);
                } catch (NumberFormatException e) {
                    sender.sendMessage("/klan admin <klan ismi> xp <miktar>");
                }
                return true;
            }
            if (args[2].equalsIgnoreCase("para")) {
                if (args.length < 5 && !(args[3].equalsIgnoreCase("ver") || args[3].equalsIgnoreCase("al"))) {
                    sender.sendMessage("/klan admin <klan ismi> para <ver/al> <miktar>");
                    return true;
                }

                boolean give = args[3].equalsIgnoreCase("ver");
                try {
                    Double d = Double.parseDouble(args[4]);
                    bff.addBankAmount((give ? -1 : 1) * d);
                } catch (NumberFormatException e) {
                    sender.sendMessage("/klan admin <klan ismi> para <ver/al> <miktar>");
                }
                return true;
            }
            if (args[2].equalsIgnoreCase("yık")) {
                for (Player pl : bff.getOnlinePlayers()) {
                    pl.sendMessage("Klanın dağıldı!");
                    B_Player bpp = DataIssues.players.get(pl.getUniqueId());
                    bpp.setF(null);

                }
                bff.disband();
                DataIssues.factions.remove(bff.getName());
                sender.sendMessage("klan yıkıldı");
                return true;
            }
            if (args[2].equalsIgnoreCase("at")) {
                if (args.length < 4) {
                    sender.sendMessage("/klan admin <klan ismi> at <üye>");
                    return true;
                }
                Player ap = Bukkit.getPlayerExact(args[3]);
                if (ap == null || !ap.isOnline() || bff.getPlayer(ap.getUniqueId()) == null) {
                    sender.sendMessage("geçersiz üye");
                    return true;
                }
                B_FactionMember abfm = bff.getPlayer(ap.getUniqueId());
                if (abfm.rank() == Rank.Founder) {
                    sender.sendMessage("kurucuyu klandan atamazsın");
                    return true;
                }
                B_Player abp = DataIssues.players.get(p.getUniqueId());
                abp.setF(null);
                ap.sendMessage("klanından atıldın");
                sender.sendMessage("oyuncu klandan atıldı");
                bff.getFactionMembers().remove(p.getUniqueId());
                return true;
            }
            if (args[2].equalsIgnoreCase("katıl")) {
                if (bf != null) {
                    sender.sendMessage("zaten bir klandasın");
                    return true;
                }
                bp.setF(bff.getName());
                bff.addMember(p.getUniqueId());
                sender.sendMessage("klana katıldın");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("clearfiles") && p.isOp()) {
            if (args.length >= 2 && args[1].equalsIgnoreCase("true")) {
                DataIssues.factions.clear();
                DataIssues.players.clear();
                Bukkit.broadcastMessage(ChatColor.AQUA + "Cleared Faction & Player files and Factions.");
                return true;
            }
            HashMap<UUID, B_Player> temp = new HashMap<>();
            for (UUID idd : DataIssues.players.keySet()) {
                B_Player ps = new B_Player(idd, null, 0);
                temp.put(idd, ps);
            }
            DataIssues.players.clear();
            DataIssues.players.putAll(temp);
            for (B_Faction bff : DataIssues.factions.values()) {
                bff.disband();
            }
            DataIssues.factions.clear();
            Bukkit.broadcastMessage(ChatColor.AQUA + "Cleared Faction & Player files and Factions.");
            return true;
        }
        return false;
    }

}
