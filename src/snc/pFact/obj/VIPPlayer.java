package snc.pFact.obj;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Zindev.utils.common.Disposable;
import snc.pFact.Main;
import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Player;
import snc.pFact.utils.Msgs;

/**
 * VIPPlayer
 */
public class VIPPlayer implements Disposable, Serializable {

    private static final long serialVersionUID = 2808327056769688264L;
    private UUID uid;
    private long remainingTime;

    public VIPPlayer(UUID uid, long time) {
        this.uid = uid;
        this.remainingTime = time;
        DataIssues.vips.put(uid, this);
        getBPlayer().tabNameUpdate();
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void refresh() {
        this.remainingTime -= (((double) Main.taskRepeating / 20) * 1000D);
    }

    public void addTime(long time) {
        this.remainingTime += time;
    }

    @Override
    public void destroy() {
        Player p = Bukkit.getPlayer(uid);
        if (p == null || !p.isOnline())
            return;
        getBPlayer().tabNameUpdate();
        p.sendMessage(Msgs.VIP_EXPIRED.sub);
    }

    @Override
    public boolean isGarbage() {
        return remainingTime <= 0;
    }

    public B_Player getBPlayer() {
        return DataIssues.players.getLoaded(uid);
    }

}