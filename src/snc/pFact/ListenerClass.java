package snc.pFact;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import snc.pFact.DM.dataIssues;
import snc.pFact.obj.cl.b_Player;
import snc.pFact.obj.cl.b_Player.Rank;

public class ListenerClass implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		File fpath = new File(dataIssues.playerFile, player.getUniqueId() + ".dp");
		if (!fpath.exists()) {
			b_Player plyr = new b_Player(player.getUniqueId(), null, 0, Rank.Single);
			b_Player.players.put(player.getUniqueId(), plyr);
		} else {
			b_Player.players.put(player.getUniqueId(), (b_Player) dataIssues.loadObject(fpath));
		}

	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev) {
		b_Player plyr = b_Player.players.get(ev.getPlayer().getUniqueId());
		if (plyr == null)
			return;
		File f = new File(dataIssues.playerFile, ev.getPlayer().getUniqueId() + ".dp");
		dataIssues.saveObject(plyr, f);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent ev) {
		ev.setCancelled(true);
		b_Player plyr = b_Player.players.get(ev.getPlayer().getUniqueId());
		if (plyr == null || plyr.getF() == null) {
			Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GRAY + "AYLAK " + ChatColor.RESET
					+ ChatColor.DARK_AQUA + ev.getPlayer().getDisplayName() + ": " + ChatColor.RESET + ev.getMessage());
		} else {
			Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GRAY
					+ b_Player.players.get(ev.getPlayer().getUniqueId()).getF().getName() + " "+ ChatColor.RESET
					+ ChatColor.DARK_AQUA + ev.getPlayer().getDisplayName() + ": " + ChatColor.RESET + ev.getMessage());
		}

	}
}
