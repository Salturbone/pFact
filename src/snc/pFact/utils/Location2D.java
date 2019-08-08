package snc.pFact.utils;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Location2D
 */
public class Location2D implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private double x, z;
    private String world;

    public Location2D(double x, double z, String world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    public Location toLocation(double y) {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public Location toLocation() {
        return toLocation(0);
    }

    public double x() {
        return x;
    }

    public double z() {
        return z;
    }

    public String world() {
        return world;
    }

    public Location2D add(Location2D loc2d) {
        return add(loc2d.x, loc2d.z);
    }

    public Location2D add(double x, double z) {
        this.x += x;
        this.z += z;
        return this;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void minimize(Location2D maximize) {
        double tempx = x;
        double tempz = z;
        x = Math.min(x, maximize.x);
        z = Math.min(z, maximize.z);
        maximize.x = Math.max(tempx, maximize.x);
        maximize.z = Math.max(tempz, maximize.z);
    }

    public static Location2D fromLocation(Location loc) {
        return new Location2D(loc.getX(), loc.getZ(), loc.getWorld().getName());
    }

    public static Location2D defaultLocation2D() {
        return new Location2D(0, 0, Bukkit.getWorlds().get(0).getName());
    }

    @Override
    public Location2D clone() {
        try {
            return (Location2D) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}