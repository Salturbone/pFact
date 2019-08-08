package snc.pFact.utils.GlowingMagmaAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

public class GlowingMagmaFactory {
    private HashMap<Integer, GlowingMagma> map;
    private ArrayList<Integer> emptyList;
    private int entityNum;
    private final int startNum;
    private final GlowingMagmaProtocols protocols;
    private JavaPlugin pl;
    private int task;

    public GlowingMagmaFactory(JavaPlugin pl, GlowingMagmaProtocols protocols, int startNum) {
        this.entityNum = startNum;
        this.startNum = startNum;
        this.protocols = protocols;
        this.pl = pl;
    }

    public void doInitialize() {
        this.setMap(new HashMap<Integer, GlowingMagma>());
        this.setEmptyList(new ArrayList<Integer>());
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        }, 20L, 1L);
    }

    public void doUninitialize() {
        Bukkit.getScheduler().cancelTask(task);
        this.getMap().values().forEach(fs -> fs.remove());
    }

    public void refresh() {
        if (this.getEntityNum() - this.getStartNum() >= 100000) {
            this.setEntityNum(this.getStartNum());
        }
        this.getMap().values().forEach(fs -> fs.refresh());
        for (Map.Entry<Integer, GlowingMagma> en : this.getMap().entrySet()) {
            GlowingMagma fs2 = en.getValue();
            if (fs2.isGarbage()) {
                this.getEmptyList().add(fs2.getEntityID());
                fs2.remove();
            }
        }
        this.getMap().entrySet().removeIf(t -> t.getValue().isGarbage());
    }

    public GlowingMagma requestGlowingMagma(Location loc, Color color, int size, boolean isWhiteListed) {
        int a = -1;
        a = this.requestEntityID();
        UUID uid = UUID.randomUUID();
        GlowingMagma ret = new GlowingMagma(this.getProtocols(), a, uid, loc, size, color, isWhiteListed);
        this.getMap().put(a, ret);
        return ret;
    }

    private Integer requestEntityID() {
        if (!this.getEmptyList().isEmpty()) {
            return this.getEmptyList().remove(0);
        }
        return this.entityNum++;
    }

    public GlowingMagmaProtocols getProtocols() {
        return this.protocols;
    }

    HashMap<Integer, GlowingMagma> getMap() {
        return this.map;
    }

    private void setMap(HashMap<Integer, GlowingMagma> map) {
        this.map = map;
    }

    private ArrayList<Integer> getEmptyList() {
        return this.emptyList;
    }

    private void setEmptyList(ArrayList<Integer> emptyList) {
        this.emptyList = emptyList;
    }

    private int getEntityNum() {
        return this.entityNum;
    }

    private void setEntityNum(int entityNum) {
        this.entityNum = entityNum;
    }

    int getStartNum() {
        return this.startNum;
    }

    protected ProtocolManager getProtocolManager() {
        return ProtocolLibrary.getProtocolManager();
    }
}
