package snc.pFact.obj;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Zindev.utils.common.Disposable;
import snc.pFact.Main;
import snc.pFact.utils.Msgs;

/**
 * VIPPlayer
 */
public class VIPPlayer implements Disposable, Serializable {

    private static final long serialVersionUID = 2808327056769688264L;
    private UUID uid;
    private long remainingTime;

    public VIPPlayer(UUID uid, int time) {
        this.uid = uid;
        this.remainingTime = time;
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
        p.sendMessage(Msgs.VIP_EXPIRED.sub);
    }

    @Override
    public boolean isGarbage() {
        return remainingTime <= 0;
    }

}