package snc.pFact;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.Zindev.utils.ZChestLibV6.ChestManager;
import snc.pFact.Claim.ClaimFactory;
import snc.pFact.DM.DataIssues;
import snc.pFact.obj.VIPPlayer;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.obj.cl.B_FactionMember;
import snc.pFact.obj.cl.B_Player;
import snc.pFact.obj.cl.Rank;
import snc.pFact.utils.Gerekli;
import snc.pFact.utils.Location2D;
import snc.pFact.utils.Msgs;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaFactory;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols112;

public class Main extends JavaPlugin {

    public static JavaPlugin ekl;
    public static ChestManager cm;
    public static int task;
    public static GlowingMagmaFactory gmf;
    public static File languages;
    public static FileConfiguration languagesyml;
    public static long taskRepeating;

    @Override
    public void onEnable() {
        ekl = this;
        gmf = new GlowingMagmaFactory(this, new GlowingMagmaProtocols112(), 300000);
        gmf.doInitialize();
        cm = new ChestManager();
        cm.initialize(this);
        ClaimFactory.initialize(5);
        DataIssues.initalize();
        DataIssues.load();
        System.out.println("pFact başlatıldı!");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ListenerClass(), this);

        BukkitScheduler scheduler = getServer().getScheduler();
        taskRepeating = 5L;
        task = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (B_Faction fct : DataIssues.factions.values()) {
                    fct.update();
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (DataIssues.players.containsKey(p.getUniqueId()))
                        DataIssues.players.get(p.getUniqueId()).update();
                    else {
                        DataIssues.players.put(p.getUniqueId(), new B_Player(p.getUniqueId(), null, 0));
                    }
                }
                for (VIPPlayer vip : DataIssues.vips.values()) {
                    vip.refresh();
                }
                DataIssues.vips.entrySet().removeIf(entry -> {
                    if (!entry.getValue().isGarbage())
                        return false;
                    entry.getValue().destroy();
                    return true;
                });
            }
        }, 5L, taskRepeating);
        languages = new File(getDataFolder(), "languages.yml");
        languagesyml = new YamlConfiguration();
        if (!languages.exists()) {
            languages.getParentFile().mkdirs();
            try {
                languages.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadYamls();
            for (Msgs m : Msgs.values()) {
                languagesyml.set(m.id, m.sub.replaceAll("§", "&"));
            }
            saveYamls();
        }
        loadYamls();
        for (Msgs m : Msgs.values()) {
            if (languagesyml.contains(m.id))
                m.sub = languagesyml.getString(m.id);
            else
                languagesyml.set(m.id, m.sub.replaceAll("§", "&"));
            m.sub = Gerekli.cevc(m.sub);
        }
        saveYamls();

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

    public void saveYamls() {
        try {
            languagesyml.save(languages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadYamls() {
        try {
            languagesyml.load(languages);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
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
                    p.sendMessage(Msgs.VALUE_ERROR.sub);
                    return false;
                }
                if (bp.getCoin() >= bagis) {
                    bp.addCoin((-1) * bagis);
                    bp.getF().addBankAmount(bagis);
                    p.sendMessage(Msgs.DONATED_TO_FACTION.sub.replaceAll("<amount>", bagis + ""));
                } else {
                    p.sendMessage(Msgs.NOT_ENOUGH_MONEY.sub);
                }
            } else {
                p.sendMessage(Msgs.DONT_HAVE_A_CLAN.sub);
            }
        }
        if (args[0].equalsIgnoreCase("bilgi")) {
            if (bp.getF() != null) {
                List<B_FactionMember> list = bf.getByRank(Rank.Moderator);
                String yetkililer = "";
                if (!list.isEmpty()) {
                    for (B_FactionMember l : list) {
                        yetkililer += l.getName() + ",";
                    }
                    yetkililer = yetkililer.substring(0, yetkililer.length() - 1);
                } else {
                    yetkililer = "-";
                }
                String oyuncular = "";
                List<B_FactionMember> list0 = bf.getByRank(Rank.Player);
                if (!list0.isEmpty()) {
                    for (B_FactionMember l : list0) {
                        oyuncular += l.getName() + ",";
                    }
                    oyuncular = oyuncular.substring(0, oyuncular.length() - 1);
                } else {
                    oyuncular = "-";
                }
                sendMessage(p, Msgs.INFO, "<founder>%%" + bf.getFounder().getName(), "<admins>%%" + yetkililer,
                        "<players>%%" + oyuncular, "<level>%%" + bf.getLevel(),
                        "<xp>%%" + Math.floor(bf.getXP() * 10) / 10,
                        "<neededxp>%%" + Math.ceil(bf.getLevelUpXp() * 10) / 10, "<prestige>%%" + bf.getPrestige(),
                        "<money>%%" + bf.getBank());
                return true;
            } else {
                p.sendMessage(Msgs.DONT_HAVE_A_CLAN.sub);
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("kur")) {
            if (args.length == 1) {
                sender.sendMessage(Msgs.ENTER_A_NAME.sub);
                return true;
            } else { // Faction kurma
                if (bf == null) {
                    Pattern pt = Pattern.compile("[\\w\\-+|<>şçıüö,&*?/\\\\#]+");
                    if (args[1].length() < 4 || args[1].length() > 20 || !pt.matcher(args[1]).matches()
                            || DataIssues.factions.containsKey(args[1])) {
                        sendMessage(p, Msgs.FACTION_NAME_ERROR);
                        return true;
                    }
                    bf = new B_Faction(args[1], p.getUniqueId());
                    bp.setF(args[1]);
                    DataIssues.factions.put(bf.getName(), bf);

                    sender.sendMessage(Msgs.FACTION_FOUNDED.sub.replaceAll("<faction>", bf.getName()));
                    sender.sendMessage(Msgs.GIVEN_MAIN_CLAIM.sub);
                    ItemStack is = ClaimFactory.getStandartMainClaim().getClaimItem(bf.getUUID());
                    p.getInventory().addItem(is);
                    return true;
                } else {
                    p.sendMessage(Msgs.ALREADY_HAS_FACTION.sub);
                    return true;
                }

            }

        }

        if (args[0].equalsIgnoreCase("kurucuyap")) {
            if (args.length < 2) {
                sender.sendMessage(Msgs.HELP_KURUCUYAP.sub);
                return true;
            }

            if (bf == null) {
                sender.sendMessage(Msgs.DONT_HAVE_A_CLAN.sub);
                return true;
            }
            B_FactionMember bfm = bf.getPlayer(bp.uuid());
            if (bfm.rank() != Rank.Founder) {
                sender.sendMessage(Msgs.NOT_ENOUGH_PERMISSION.sub);
                return true;
            }
            // kurucu yapma komutu

            if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
                Player gp = Bukkit.getPlayer(args[1]);
                B_Player ggp = DataIssues.players.get(gp.getUniqueId());
                B_Faction gf = ggp.getF();
                if (gf != bf) {
                    sender.sendMessage(Msgs.NOT_A_MEMBER_OF_CLAN.sub);
                    return true;
                } else {
                    gf.changeFounder(ggp.uuid());
                    sender.sendMessage(Msgs.NOT_FOUNDER_ANYMORE.sub);
                    gp.sendMessage(Msgs.FOUNDER_NOW.sub);
                    return true;
                }
            } else {
                OfflinePlayer gp = Bukkit.getOfflinePlayer(args[1]);
                if (bp.getF().getPlayer(gp.getUniqueId()) == null) {
                    sender.sendMessage(Msgs.NOT_A_MEMBER_OF_CLAN.sub);
                    return true;
                } else {
                    bf.changeFounder(gp.getUniqueId());
                    sender.sendMessage(Msgs.NOT_FOUNDER_ANYMORE.sub);
                    return true;
                }
            }
        }

        if (args[0].equalsIgnoreCase("ayrıl")) {
            if (bf == null) {
                sender.sendMessage(Msgs.DONT_HAVE_A_CLAN.sub);
                return true;
            }
            if (bf.getFounder().uuid() != bp.uuid()) {

                bf.getFactionMembers().remove(bp.uuid());
                bp.setF(null);
                p.sendMessage(Msgs.NOT_A_MEMBER_OF_CLAN_ANYMORE.sub);
                return true;
            } else {
                if (bf.getFactionMembers().size() != 1) {
                    sendMessage(p, Msgs.CANT_LEAVE_FACTION_OWNER);
                    return true;
                } else {
                    bf.disband();
                    DataIssues.factions.remove(bf.getName());
                    bp.setF(null);
                    p.sendMessage(Msgs.NOT_A_MEMBER_OF_CLAN_ANYMORE.sub);
                    return true;
                }

            }
        }

        if (args[0].equalsIgnoreCase("oyuncu")) {
            if (bf == null) {
                sender.sendMessage(Msgs.DONT_HAVE_A_CLAN.sub);
                return true;
            }
            if (bf.getPlayer(bp.uuid()).rank() == Rank.Player) {
                sender.sendMessage(Msgs.NOT_ENOUGH_PERMISSION.sub);
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage(Msgs.ENTER_AN_ARGUMENT.sub);
                return true;
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("davet") || args[1].equalsIgnoreCase("çıkar")) {
                    sender.sendMessage(Msgs.ENTER_A_PLAYER_NAME.sub);
                    return true;
                }
            }
            // oyuncu davet etme ve çıkarma
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("davet")) {
                    Player gp = Bukkit.getPlayer(args[2]);
                    if (gp == null || !gp.isOnline()) {
                        sender.sendMessage(Msgs.COULDNT_FIND_PLAYER.sub.replaceAll("<player>", args[2]));
                        return true;
                    }
                    B_Player bgp = DataIssues.players.get(gp.getUniqueId());

                    if (bgp.getF() == bf) {
                        sender.sendMessage(Msgs.PLAYER_IS_IN_CLAN_ALREADY.sub.replaceAll("<player>", args[2]));
                        return true;
                    }
                    if (bgp.hasFaction()) {
                        sender.sendMessage(Msgs.PLAYER_IS_IN_ANOTHER_CLAN.sub.replaceAll("<player>", args[2]));
                        return true;
                    }
                    if (bgp.getES()) {
                        sender.sendMessage(Msgs.PLAYER_IS_INVITED_BY_ANOTHER_CLAN.sub.replaceAll("<player>", args[2]));
                        return true;
                    }
                    bgp.setES(true);
                    bgp.setEF(bp.getF().getName());
                    sender.sendMessage(Msgs.PLAYER_IS_INVITED_BY_ANOTHER_CLAN.sub.replaceAll("<player>", args[2]));
                    sendMessage(p, Msgs.INVITED_TO_CLAN, "<faction>%%" + bf.getName());
                    return true;
                }
                if (args[1].equalsIgnoreCase("at")) {
                    Player gp = Bukkit.getPlayer(args[2]);
                    if (gp != null && gp.isOnline()) {
                        B_Player bgp = DataIssues.players.get(gp.getUniqueId());
                        B_Faction gf = bgp.getF();
                        if (bgp.getF() != bp.getF()) {
                            p.sendMessage(Msgs.NOT_A_MEMBER_OF_CLAN.sub);
                            return true;
                        }
                        B_FactionMember bfm = bf.getPlayer(bp.uuid());
                        B_FactionMember gfm = gf.getPlayer(bgp.uuid());
                        if (gfm.rank() == Rank.Founder) {
                            sender.sendMessage(Msgs.NOT_ENOUGH_PERMISSION.sub);
                            return true;
                        }
                        if (bfm.rank() == Rank.Founder) {
                            bf.getFactionMembers().remove(gp.getUniqueId());
                            bgp.setF(null);
                            sendMessage(p, Msgs.KICKER_MESSAGE, "<player>%%" + args[2]);
                            gp.sendMessage(Msgs.KICKED_FROM_FACTION.sub);
                            return true;
                        }
                        if (bfm.rank() == Rank.Moderator && gfm.rank() == Rank.Player) {

                            bf.getFactionMembers().remove(gp.getUniqueId());
                            bgp.setF(null);
                            sendMessage(p, Msgs.KICKER_MESSAGE, "<player>%%" + args[2]);
                            gp.sendMessage(Msgs.KICKED_FROM_FACTION.sub);
                            return true;
                        }
                        if (gfm.rank() == Rank.Moderator) {
                            sender.sendMessage(Msgs.NOT_ENOUGH_PERMISSION.sub);
                            return true;
                        }
                        return true;
                    } else {
                        OfflinePlayer ogp = Bukkit.getOfflinePlayer(args[2]);
                        if (ogp == null) {
                            sendMessage(p, Msgs.COULDNT_FIND_PLAYER, "<player>%%" + args[2]);
                            return true;
                        }

                        B_FactionMember gfm = bf.getPlayer(ogp.getUniqueId());
                        if (gfm == null) {
                            p.sendMessage(Msgs.NOT_A_MEMBER_OF_CLAN.sub);
                            return true;
                        }

                        B_FactionMember bfm = bf.getPlayer(bp.uuid());
                        if (gfm.rank() == Rank.Founder) {
                            sender.sendMessage(Msgs.NOT_ENOUGH_PERMISSION.sub);
                            return true;
                        }
                        if (bfm.rank() == Rank.Founder) {
                            bf.getFactionMembers().remove(gp.getUniqueId());
                            sendMessage(p, Msgs.KICKER_MESSAGE, "<player>%%" + args[2]);
                            return true;
                        }
                        if (bfm.rank() == Rank.Moderator && gfm.rank() == Rank.Player) {
                            bf.getFactionMembers().remove(gp.getUniqueId());
                            sendMessage(p, Msgs.KICKER_MESSAGE, "<player>%%" + args[2]);
                            return true;
                        }
                        if (gfm.rank() == Rank.Moderator) {
                            sender.sendMessage(Msgs.NOT_ENOUGH_PERMISSION.sub);
                            return true;
                        }
                        return true;
                    }
                }
            }
        }

        // Davet Kabul ve Reddi
        if (args[0].equalsIgnoreCase("kabul")) {
            if (!bp.getES() || bp.hasFaction()) {
                sender.sendMessage(Msgs.NO_INVITE.sub);
                return true;
            }
            bp.setF(bp.getEF().getName());
            bp.getEF().addMember(bp.uuid());
            bp.setEF(null);
            bp.setES(false);
            sendMessage(p, Msgs.JOINED_FACTION, "<faction>%%" + bp.getF().getName());
            for (Player pl : bp.getF().getOnlinePlayers()) {
                if (!pl.getUniqueId().equals(p.getUniqueId()))
                    sendMessage(pl, Msgs.JOINED_FACTION_BROADCAST, "<player>%%" + p.getName());
            }

            return true;
        }
        if (args[0].equalsIgnoreCase("ret")) {
            if (!bp.getES() || bp.hasFaction()) {
                sender.sendMessage(Msgs.NO_INVITE.sub);
                return true;
            }
            sendMessage(p, Msgs.REJECTED_INVITE, "<faction>%%" + bp.getEF().getName());
            bp.setEF(null);
            bp.setES(false);
            return true;
        }

        if (args[0].equalsIgnoreCase("yetki")) {
            if (bf == null) {
                sender.sendMessage(Msgs.NOT_A_MEMBER_OF_CLAN.sub);
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(Msgs.HELP_YETKI.sub);
                return true;
            }
            B_FactionMember bfm = bf.getPlayer(p.getUniqueId());
            if (bfm.rank() == Rank.Founder) {
                Player gp = Bukkit.getPlayer(args[2]);
                if (gp != null && gp.isOnline()) {
                    B_FactionMember gfm = bf.getPlayer(gp.getUniqueId());
                    if (gfm == null) {
                        sender.sendMessage(Msgs.NOT_A_MEMBER_OF_YOUR_FACTION.sub);
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("ver")) {
                        gfm.setRank(Rank.Moderator);
                        sendMessage(p, Msgs.GIVEN_ADMIN, "<player>%%" + args[2]);
                        gp.sendMessage(Msgs.ADMIN_NOW.sub);
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("al")) {
                        gfm.setRank(Rank.Player);
                        sendMessage(p, Msgs.TAKEN_ADMIN, "<player>%%" + args[2]);
                        gp.sendMessage(Msgs.NOT_ADMIN_NOW.sub);
                        return true;
                    }
                } else {
                    Player ggp = (Player) Bukkit.getOfflinePlayer(args[2]);
                    if (ggp == null) {
                        sendMessage(p, Msgs.COULDNT_FIND_PLAYER, "<player>%%" + args[2]);
                        return true;
                    }
                    B_FactionMember gfm = bf.getPlayer(ggp.getUniqueId());
                    if (gfm == null) {
                        sender.sendMessage(Msgs.NOT_A_MEMBER_OF_YOUR_FACTION.sub);
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("ver")) {
                        gfm.setRank(Rank.Moderator);
                        sendMessage(p, Msgs.GIVEN_ADMIN, "<player>%%" + args[2]);
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("al")) {
                        gfm.setRank(Rank.Player);
                        sendMessage(p, Msgs.TAKEN_ADMIN, "<player>%%" + args[2]);
                        return true;
                    }

                }

            } else {
                sender.sendMessage(Msgs.NOT_ENOUGH_PERMISSION.sub);
                return true;
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("evyap")) {
                if (bp.hasFaction()) {
                    B_FactionMember bfm = bf.getFactionMembers().get(bp.uuid());
                    if (bfm.rank() == Rank.Founder) {
                        if (bf.GetMainClaim() == null
                                || !bf.GetMainClaim().getSquare().isInside(Location2D.fromLocation(p.getLocation()))) {
                            p.sendMessage(Msgs.CANT_CHANGE_HOME.sub);
                            return true;
                        }
                        bf.setHome(p.getLocation());
                        p.sendMessage(Msgs.CHANGED_HOME.sub);
                        return true;
                    } else {
                        p.sendMessage(Msgs.NOT_ENOUGH_PERMISSION.sub);
                        return true;
                    }
                } else {
                    p.sendMessage(Msgs.DONT_HAVE_A_CLAN.sub);
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("ev")) {
                if (bp.hasFaction()) {
                    bf.tpPlayerToHome(bp);
                    return true;
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Bir klana mensup değilsin!");
                    return true;
                }

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
                    sender.sendMessage("Geçersiz üye");
                    return true;
                }
                B_FactionMember abfm = bff.getPlayer(ap.getUniqueId());
                if (abfm.rank() == Rank.Founder) {
                    sender.sendMessage("Kurucuyu klandan atamazsın");
                    return true;
                }
                B_Player abp = DataIssues.players.get(p.getUniqueId());
                abp.setF(null);
                ap.sendMessage(Msgs.KICKED_FROM_FACTION.sub);
                sender.sendMessage("oyuncu klandan atıldı");
                bff.getFactionMembers().remove(p.getUniqueId());
                return true;
            }
            if (args[2].equalsIgnoreCase("katıl")) {
                if (bf != null) {
                    sender.sendMessage("Zaten bir klandasın");
                    return true;
                }
                bp.setF(bff.getName());
                bff.addMember(p.getUniqueId());
                sender.sendMessage("Klana katıldın");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("vip")) {
            if (bp.isVIP()) {
                return true;
            } else {
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
        if (args[0].equalsIgnoreCase("makevip") && p.isOp()) {
            if (args.length < 3) {
                p.sendMessage("/klan makevip <oyuncu> <gün sayısı>");
                return true;
            }
            Player ap = Bukkit.getPlayer(args[1]);
            if (ap == null || !ap.isOnline()) {
                p.sendMessage("/klan makevip <oyuncu> <gün sayısı>");
                return true;
            }
            try {
                int i = Integer.parseInt(args[2]);
                VIPPlayer vip = new VIPPlayer(ap.getUniqueId(), i * 24 * 60 * 60);
                DataIssues.vips.put(ap.getUniqueId(), vip);
                p.sendMessage("Oyuncu artık bir vip.");
                sendMessage(ap, Msgs.VIP_FOR, "<time>%%" + Gerekli.getRemainingTime(vip.getRemainingTime()));
                return true;
            } catch (NumberFormatException e) {
                p.sendMessage("/klan makevip <oyuncu> <gün sayısı>");
                return true;
            }
        }
        return false;
    }

    public void sendHelp(Player p) {
        for (Msgs m : Msgs.values()) {
            if (m.name().startsWith("HELP_"))
                p.sendMessage(m.sub);
        }
    }

    public static void sendMessage(Player p, Msgs message, String... replacements) {
        if (message.sub.contains("%%")) {
            for (String s : message.sub.split("%%")) {
                String str = s;
                for (String rep : replacements) {
                    str = str.replaceAll(rep.split("%%")[0], rep.split("%%")[1]);
                }
                p.sendMessage(str);
            }
        } else {
            String str = message.sub;
            for (String rep : replacements) {
                str = str.replaceAll(rep.split("%%")[0], rep.split("%%")[1]);
            }
            p.sendMessage(str);
        }
    }

}
