package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import snc.pFact.DM.DataIssues;

/**
 * B_FactionMember
 */
public class B_FactionMember implements Serializable {

    private static final long serialVersionUID = 8982698787406379807L;
    private final UUID id;
    private Rank rank;

    public B_FactionMember(UUID id) {
        this.id = id;
        this.rank = Rank.Player;
    }

    public Rank rank() {
        return rank;
    }

    public UUID uuid() {
        return id;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getName() {
        if (isOnline()) {
            return Bukkit.getPlayer(id).getDisplayName();
        }
        return Bukkit.getOfflinePlayer(id).getName();
    }

    public boolean isOnline() {
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getUniqueId().equals(id))
                return true;
        return false;
    }

    public boolean isVIP() {
        return DataIssues.vips.containsKey(uuid());
    }

}