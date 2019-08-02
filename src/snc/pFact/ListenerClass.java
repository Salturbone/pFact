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

import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Player;
import snc.pFact.obj.cl.B_Player.Rank;

public class ListenerClass implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		File fpath = new File(DataIssues.playerFile, player.getUniqueId() + ".dp");
		if (!fpath.exists()) {
			B_Player plyr = new B_Player(player.getUniqueId(), null, 0, Rank.Single);
			B_Player.players.put(player.getUniqueId(), plyr);
		} else {
			B_Player.players.put(player.getUniqueId(), (B_Player) DataIssues.loadObject(fpath));
		}

	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev) {
		B_Player plyr = B_Player.players.get(ev.getPlayer().getUniqueId());
		if (plyr == null)
			return;
		File f = new File(DataIssues.playerFile, ev.getPlayer().getUniqueId() + ".dp");
		DataIssues.saveObject(plyr, f);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent ev) {
		ev.setCancelled(true);
		B_Player plyr = B_Player.players.get(ev.getPlayer().getUniqueId());
		if (!plyr.hasFaction()) {
			Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GRAY + "AYLAK " + ChatColor.RESET
					+ ChatColor.DARK_AQUA + ev.getPlayer().getDisplayName() + ": " + ChatColor.RESET + ev.getMessage());
		} else {
			Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GRAY
					+ B_Player.players.get(ev.getPlayer().getUniqueId()).getF().getName() + " " + ChatColor.RESET
					+ ChatColor.DARK_AQUA + ev.getPlayer().getDisplayName() + ": " + ChatColor.RESET + ev.getMessage());
		}

	}
}
