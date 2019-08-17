package snc.pFact.utils;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SerLocation implements Serializable, Cloneable {
    
    private static final long serialVersionUID = 3455127761672450463L;
    private String world;
    private float yaw, pitch;
    private double x, y, z;

    public SerLocation(Location loc) {
        setLocation(loc);
    }

    public Location getLocation() {

        return new Location(Bukkit.getWorld(world) != null ? Bukkit.getWorld(world) : Bukkit.getWorlds().get(0), x, y,
                z, yaw, pitch);
    }

    private String strValueOfDouble(double d) {
        DecimalFormat dm = new DecimalFormat("#.##");
        return dm.format(d);
    }

    @Override
    public String toString() {
        Location lc = getLocation();
        return "x:" + strValueOfDouble(lc.getX()) + ",y:" + strValueOfDouble(lc.getY()) + ",z:"
                + strValueOfDouble(lc.getZ()) + ",w:" + lc.getWorld().getName();
    }

    public void setLocation(Location loc) {
        world = loc.getWorld().getName();
        yaw = loc.getYaw();
        pitch = loc.getPitch();
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();
    }

    @Override
    public SerLocation clone() {
        try {
            return (SerLocation) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SerLocation) {
            SerLocation s = (SerLocation) o;
            return (world.equals(s.world) && yaw == s.yaw && pitch == s.pitch && x == s.x && y == s.y && z == s.z);
        }
        return super.equals(o);
    }

}
