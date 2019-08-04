package snc.pFact.utils;

import java.io.Serializable;

/**
 * 3DSquare
 */
public class Square3D implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    private Location2D center;
    private int length;

    public Square3D(Location2D center, int length) {
        this.center = center;
        this.length = length;
    }

    public Location2D center() {
        return center;
    }

    public void setCenter(Location2D center) {
        this.center = center;
    }

    public int length() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isInside(Location2D loc) {
        if (!loc.world().equals(center.world()))
            return false;
        double x = loc.x();
        double z = loc.z();
        double xdiff = Math.abs(x - center.x());
        double zdiff = Math.abs(z - center.z());
        if (xdiff < length && zdiff < length)
            return true;
        return false;
    }

    private boolean doCollide(Square3D square) {
        Location2D sCenter = square.center;
        if (!sCenter.world().equals(center.world()))
            return false;
        return false;
    }

    @Override
    public Square3D clone() {
        Square3D cl;
        try {
            cl = (Square3D) super.clone();
            cl.center = center.clone();
            return cl;
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}