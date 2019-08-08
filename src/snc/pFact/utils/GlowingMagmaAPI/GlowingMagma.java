package snc.pFact.utils.GlowingMagmaAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

public class GlowingMagma {
    private ArrayList<UUID> players;
    private HashMap<UUID, Integer> playerTimers;
    private Location loc;
    private HashSet<UUID> createdSet;
    private boolean garbage;
    private boolean firstSpawn;
    private int entityid;
    private UUID uid;
    private GlowingMagmaProtocols pr;
    private Color color;
    private final int size;
    private boolean isWhiteListed;

    public GlowingMagma(GlowingMagmaProtocols pr, int entityid, UUID uid, Location loc, int size, Color color,
            boolean isWhiteListed) {
        this.createdSet = new HashSet<UUID>();
        this.uid = uid;
        this.pr = pr;
        this.entityid = entityid;
        this.setPlayers(new ArrayList<UUID>());
        this.loc = loc;
        this.color = color;
        this.size = size;
        this.isWhiteListed = isWhiteListed;
        playerTimers = new HashMap<>();
    }

    public GlowingMagma spawn() {
        if (!this.firstSpawn) {
            this.firstSpawn = true;
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if ((this.isWhiteListed() && this.getPlayers().contains(p.getUniqueId())) || !this.isWhiteListed()) {
                this.spawn(p);
            }
        }
        return this;
    }

    public void remove() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if ((this.isWhiteListed() && this.getPlayers().contains(p.getUniqueId())) || !this.isWhiteListed()) {
                this.remove(p);
            }
        }
    }

    protected void spawn(Player p) {
        this.createdSet.add(p.getUniqueId());
        this.pr.spawnGlowingMagma(p, this);
        updateColor(p);
    }

    public void updateColor() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if ((this.isWhiteListed() && this.getPlayers().contains(p.getUniqueId())) || !this.isWhiteListed()) {
                updateColor(p);
            }
        }
    }

    public void updateColor(Player p) {
        this.pr.createTeam(p, color, uid);
        this.pr.addToTeam(p, color, uid);
    }

    protected void remove(Player p) {
        if (p != null) {
            playerTimers.remove(p.getUniqueId());
            this.createdSet.remove(p.getUniqueId());
            this.pr.removeFromTeam(p, color, uid);
            this.pr.removeGlowingMagma(p, this.entityid);
        }
    }

    public void refresh() {
        if (!this.firstSpawn) {
            return;
        }
        List<Player> pls = this.createdSet.stream().map(o -> Bukkit.getPlayer(o))
                .filter(o -> o == null || !o.isOnline() || o.isDead()).collect(Collectors.toList());
        pls.forEach(o -> remove(o));
        checkTimers();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isDead()) {
                continue;
            }
            if ((!this.isWhiteListed() || (!this.getPlayers().contains(p.getUniqueId())) && this.isWhiteListed())) {
                continue;
            }
            Location ploc = p.getLocation();
            if (!this.loc.getWorld().getName().equals(ploc.getWorld().getName())) {
                this.createdSet.removeIf(o -> o.equals(p.getUniqueId()));
            } else {
                if (this.loc.distance(ploc) > 50.0 && this.createdSet.contains(p.getUniqueId())) {
                    this.remove(p);
                }
                if (this.loc.distance(ploc) > 50.0 || this.createdSet.contains(p.getUniqueId())) {
                    continue;
                }
                this.spawn(p);
            }
        }
    }

    public void checkTimers() {
        List<Player> pls = this.playerTimers.keySet().stream().map(o -> Bukkit.getPlayer(o))
                .filter(o -> o == null || !o.isOnline() || o.isDead() || !players.contains(o.getUniqueId()))
                .collect(Collectors.toList());
        pls.forEach(o -> playerTimers.remove(o.getUniqueId()));
        HashMap<UUID, Integer> nmap = new HashMap<>();
        for (Entry<UUID, Integer> en : playerTimers.entrySet()) {
            nmap.put(en.getKey(), en.getValue() - 1);
        }
        for (Entry<UUID, Integer> en : nmap.entrySet()) {
            if (en.getValue() <= -1) {
                remove(Bukkit.getPlayer(en.getKey()));
                getPlayers().remove(en.getKey());
                playerTimers.remove(en.getKey());
            } else {
                playerTimers.put(en.getKey(), en.getValue());
            }
        }
    }

    public boolean isGarbage() {
        return this.garbage;
    }

    public void destroy() {
        this.garbage = true;
    }

    public void initializeProtocols(GlowingMagmaProtocols protocols) {
        this.pr = protocols;
    }

    private void setPlayers(ArrayList<UUID> players) {
        this.players = players;
    }

    public HashMap<UUID, Integer> getTimedPlayers() {
        return playerTimers;
    }

    public void addTimedPlayer(UUID p, int time) {
        Bukkit.broadcastMessage("faaaaaaaaaaaaaaafafafa");
        getTimedPlayers().put(p, time);
    }

    public boolean isWhiteListed() {
        return isWhiteListed;
    }

    public GlowingMagma setWhiteListed(List<Player> players) {
        this.remove();
        this.getPlayers().clear();
        players.forEach(o -> this.getPlayers().add(o.getUniqueId()));
        return this;
    }

    public ArrayList<UUID> getPlayers() {
        return this.players;
    }

    public int getEntityID() {
        return this.entityid;
    }

    public UUID getUUID() {
        return uid;
    }

    public Location getLocation() {
        return this.loc;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        updateColor();
    }

    public int getSize() {
        return size;
    }

}
