package snc.pFact;

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
import snc.pFact.utils.Msgs;

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
                player.sendMessage(Msgs.FACTION_DISBANDED.sub);
            }
            if (bf != null && bf.getPlayer(player.getUniqueId()) == null) {
                bp.setF(null);
                player.sendMessage(Msgs.KICKED_FROM_FACTION.sub);
            }
            if (bp.isVIP()) {
                player.setPlayerListName(Msgs.VIP_TAG.sub + ChatColor.GRAY + player.getDisplayName());
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
        String format = Msgs.CHAT_FORMAT.sub;
        format = format.replace("<player>", "%1$s");
        format = format.replace("<message>", "%2$s");
        String tags = "";
        if (plyr.isVIP()) {
            tags += Msgs.VIP_TAG.sub;
        }
        if (ev.getPlayer().isOp())
            tags += Msgs.ADMIN_TAG.sub;
        format.replace("<tag>", tags);
        if (!plyr.hasFaction()) {
            format = format.replace("<faction>", Msgs.SLACK_TAG.sub);
            format = format.replace("<rank>", "");
        } else {
            B_FactionMember bfm = plyr.getF().getFactionMembers().get(plyr.uuid());
            format = format.replace("<faction>", plyr.getF().getName());
            if (bfm.rank() == Rank.Founder) {
                format = format.replace("<rank>", Msgs.FACTION_OWNER_TAG.sub);
            } else if (bfm.rank() == Rank.Moderator) {
                format = format.replace("<rank>", Msgs.FACTION_MODERATOR_TAG.sub);
            } else {
                format = format.replace("<rank>", "");
            }

        }
        ev.setFormat(format);
    }

}
