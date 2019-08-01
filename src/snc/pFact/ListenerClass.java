	package snc.pFact;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import snc.pFact.obj.cl.b_Player;

public class ListenerClass implements Listener{
	
	public static HashMap<UUID, b_Player> players = new HashMap<UUID, b_Player>();
	public static File playerFile = new File(Main.ekl.getDataFolder(), "Players/");
	
	public ListenerClass() {
		playerFile.mkdirs();
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		File fpath = new File(playerFile, player.getUniqueId() 
				+ ".dp");
		if (!fpath.exists()) {
			b_Player plyr = new b_Player(player.getUniqueId(), null, 0);
			players.put(player.getUniqueId(), plyr);
			// Write objects to file
			try {	
				FileOutputStream f = new FileOutputStream(
						fpath);
				
				ObjectOutputStream o = new ObjectOutputStream(f);
				o.writeObject(plyr);
				o.close();
				f.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			try {
				FileInputStream fi = new FileInputStream(
						fpath);
				ObjectInputStream oi = new ObjectInputStream(fi);
				players.put(player.getUniqueId(), (b_Player) oi.readObject());
				oi.close();
				fi.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent ev){
		try {
			b_Player plyr = players.get(ev.getPlayer().getUniqueId());
			FileOutputStream f = new FileOutputStream(
					new File(playerFile, ev.getPlayer().getUniqueId()
							+ ".dp"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(plyr);
			o.close();
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent ev) {
		ev.setCancelled(true);
		b_Player plyr = players.get(ev.getPlayer().getUniqueId());
		if(plyr != null && plyr.getF() == null) {
			Bukkit.broadcastMessage(
					 ChatColor.BOLD
					+ ""
					+ ChatColor.GRAY
					+ "AYLAK "
					+ ChatColor.RESET
					+ ChatColor.DARK_AQUA 
					+ ev.getPlayer().getDisplayName() 
					+ ": " 
					+ ChatColor.RESET 
					+ ev.getMessage());
		} else {
			Bukkit.broadcastMessage(
					ChatColor.BOLD
					+ ""
					+ ChatColor.GRAY 
					+ players.get(ev.getPlayer().getUniqueId()).getF().getName() 
					+ ChatColor.RESET
					+ ChatColor.DARK_AQUA 
					+ ev.getPlayer().getDisplayName() 
					+ ": " 
					+ ChatColor.RESET
					+ ev.getMessage());
		}
		
	}
}
