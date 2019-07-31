package snc.pFact;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import snc.pFact.obj.cl.b_Player;

public class ListenerClass implements Listener{
	
	public static HashMap<UUID, b_Player> players;
	String path;
	
	
	public ListenerClass(Main plugin) {
		
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		try {
			path = new File(".").getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File fpath = new File(path 
				+ "/ProjectFactions/Players/" 
				+ player.getUniqueId() 
				+ ".dp");
		if (!fpath.exists()) {
			b_Player plyr = new b_Player(player.getUniqueId(), null, 0);
			players.put(player.getUniqueId(), plyr);
			// Write objects to file
			try {
				fpath.mkdirs();
				fpath.createNewFile();
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
					new File(path 
							+ "/ProjectFactions/Players/" 
							+ ev.getPlayer().getUniqueId()
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
}
