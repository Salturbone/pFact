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
        if (xdiff <= length && zdiff <= length)
            return true;
        return false;
    }

    public boolean doCollide(Square3D square) {
        Location2D sCenter = square.center;
        if (!sCenter.world().equals(center.world()))
            return false;
        for (Location2D loc2d : getCorners()) {
            if (square.isInside(loc2d))
                return true;
        }

        return false;
    }

    public boolean IsInsideof(Square3D large) {
        if (!large.center().world().equals(center.world()))
            return false;

        for (Location2D loc2d : getCorners()) {
            if (!large.isInside(loc2d))
                return false;
        }
        /*
         * if (center.x() - length >= large.center.x() && center.x() - length <=
         * large.center.x() + large.length && center.z() - length >= large.center.z() &&
         * center.z() - length <= large.center.z() + large.length && center.x() + length
         * >= large.center.x() && center.x() + length <= large.center.x() + large.length
         * && center.z() + length >= large.center.z() && center.z() + length <=
         * large.center.z() + large.length) { return true; }
         */
        return true;
    }

    public Location2D[] getCorners() {
        Location2D[] corners = new Location2D[4];
        int t = 0;
        for (int i = -1; i <= 1; i += 2) {
            for (int a = -1; a <= 1; a += 2) {
                Location2D nloc2d = new Location2D(center.x() + i * length, center.z() + a * length, center.world());
                corners[t] = nloc2d;
                t++;
            }
        }
        return corners;
    }

    public Location2D[] getHollowSquare() {
        Location2D[] corners = getCorners();
        Location2D zplus = corners[0];
        Location2D zminus = corners[3];
        Location2D xplus = corners[1];
        Location2D xminus = corners[2];
        Location2D[] hsquare = new Location2D[length * 8];
        for (int d = 0; d < 2 * length; d++) {
            hsquare[0 + d] = xplus.clone().add(d, 0);
            hsquare[length * 2 + d] = zplus.clone().add(0, d);
            hsquare[length * 4 + d] = xminus.clone().add(-d, 0);
            hsquare[length * 6 + d] = zminus.clone().add(0, -d);
        }
        return hsquare;
    }

    @Override
    public Square3D clone() {
        Square3D cl;
        try {
            cl = (Square3D) super.clone();
            cl.center = center.clone();
            return cl;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
