package snc.pFact.obj;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Zindev.utils.common.Disposable;

/**
 * VIPPlayer
 */
public class VIPPlayer implements Disposable, Serializable {

    private static final long serialVersionUID = 1L;

    private UUID uid;
    private int remainingTime;

    public VIPPlayer(UUID uid, int time) {
        this.uid = uid;
        this.remainingTime = time;
    }

    public void refresh() {
        this.remainingTime--;
    }

    public void addTime(int time) {
        this.remainingTime += time;
    }

    @Override
    public void destroy() {
        Player p = Bukkit.getPlayer(uid);
        if (p == null || !p.isOnline())
            return;
        p.sendMessage("Your vip has expired.");
    }

    @Override
    public boolean isGarbage() {
        return remainingTime <= 0;
    }

}