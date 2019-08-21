package snc.pFact;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.obj.cl.B_FactionMember;
import snc.pFact.obj.cl.B_Player;
import snc.pFact.obj.cl.Rank;

public class ListenerClass implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        onJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent ev) {
        onQuit(ev.getPlayer());
    }

    public static void onJoin(Player player) {
        if (!DataIssues.players.containsKey(player.getUniqueId())) {
            B_Player plyr = new B_Player(player.getUniqueId(), null, 0);
            DataIssues.players.put(player.getUniqueId(), plyr);
        } else {
            B_Player bp = DataIssues.players.loadData(player.getUniqueId());
            B_Faction bf = bp.getF();
            if (bf == null && bp.hasFaction()) {
                bp.setF(null);
                player.sendMessage("Klanın dağıldı.");
            }
            if (bf != null && bf.getPlayer(player.getUniqueId()) == null) {
                bp.setF(null);
                player.sendMessage("Klanından atıldın.");
            }
        }
    }

    public static void onQuit(Player player) {
        B_Player plyr = DataIssues.players.get(player.getUniqueId());
        if (plyr == null) {
            plyr = new B_Player(player.getUniqueId(), null, 0);
            DataIssues.players.put(player.getUniqueId(), plyr);
        }
        DataIssues.players.saveAndUnloadData(plyr.uuid());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent ev) {
        B_Player plyr = DataIssues.players.get(ev.getPlayer().getUniqueId());
        String format = "<faction> <rank> " + ChatColor.DARK_AQUA + "<player> >>" + ChatColor.RESET + " <message>";
        format = format.replace("<player>", "%1$s");
        format = format.replace("<message>", "%2$s");
        if (!plyr.hasFaction()) {
            format = format.replace("<faction>", ChatColor.BOLD + "" + ChatColor.GRAY + "AYLAK");
            format = format.replace("<rank>", "");
        } else {
            B_FactionMember bfm = plyr.getF().getFactionMembers().get(plyr.uuid());
            // Bukkit.broadcastMessage(bfm + " " + plyr);
            format = format.replace("<faction>", ChatColor.GOLD + plyr.getF().getName());
            if (bfm.rank() == Rank.Founder) {
                format = format.replace("<rank>", ChatColor.GRAY + "-K-");
            } else if (bfm.rank() == Rank.Moderator) {
                format = format.replace("<rank>", ChatColor.GRAY + "-Y-");
            } else {
                format = format.replace("<rank>", "");
            }

        }
        ev.setFormat(format);
    }

}
