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
import snc.pFact.obj.cl.B_Player;

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
        ev.setCancelled(true);
        B_Player plyr = DataIssues.players.get(ev.getPlayer().getUniqueId());
        if (!plyr.hasFaction()) {
            Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GRAY + "AYLAK " + ChatColor.RESET
                    + ChatColor.DARK_AQUA + ev.getPlayer().getDisplayName() + ": " + ChatColor.RESET + ev.getMessage());
        } else {
            Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GRAY + plyr.getF().getName() + " " + ChatColor.RESET
                    + ChatColor.DARK_AQUA + ev.getPlayer().getDisplayName() + ": " + ChatColor.RESET + ev.getMessage());
        }

    }
}
