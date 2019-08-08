package snc.pFact.Claim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.ZChestLibV6.ChestGUI;
import me.Zindev.utils.ZChestLibV6.ChestNode;
import me.Zindev.utils.ZChestLibV6.ChestNode.ProcessItem;
import me.Zindev.utils.ZChestLibV6.GUIReadLine.GUIConfigurable;
import snc.pFact.Main;
import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.utils.Location2D;
import snc.pFact.utils.SerLocation;
import snc.pFact.utils.Square3D;
import snc.pFact.utils.ZSIGN;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagma;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaFactory;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

public abstract class Claim implements Cloneable, Serializable, GUIConfigurable, ProcessItem {
    private static final long serialVersionUID = 1L;

    private String faction;
    private SerLocation centerBlock;
    private transient List<GlowingMagma> cgms;
    private transient GlowingMagma egm;

    public Claim(int length, ItemStack claimBlock, ItemStack shard, Color color) {
        ClaimData cd = new ClaimData();
        ClaimFactory.claimDatas.put(getName(), cd);
        cd.setObject("length", length);
        cd.setItemStack("block", claimBlock);
        cd.setItemStack("shard", shard);
        cd.setObject("displayName", getName());
        cd.setItemStack("displayItem", getClaimItem("null"));
        cd.setObject("eggColor", color.name());
        cd.setObject("cornerColor", Color.WHITE.name());
    }

    public void setup(String faction, Location center) {
        this.faction = faction;
        this.centerBlock = new SerLocation(center.getBlock().getLocation());
    }

    public Location getCenterBlock() {
        return centerBlock.getLocation();
    }

    public void setCenterBlock(Location loc) {
        this.centerBlock = new SerLocation(loc);
    }

    public Square3D getSquare() {
        return new Square3D(Location2D.fromLocation(centerBlock.getLocation()), claimData().getInt("length"));
    }

    public B_Faction getFaction() {
        return DataIssues.factions.get(faction);
    }

    public final ItemStack getClaimItem(String fact) {
        return ZSIGN.imzalaZ("claim", getName(),
                ZSIGN.imzalaZ("claimFaction", fact, claimData().getItemStack("block")));
    }

    public boolean canBreak(Location loc) {
        return canPlace(loc);
    }

    public boolean canPlace(Location loc) {
        Location center = getCenterBlock();
        double xDiff = Math.abs(loc.getX() - center.getX());
        double yDiff = loc.getY() - center.getY();
        double zDiff = Math.abs(loc.getZ() - center.getZ());
        if (xDiff <= 1 && yDiff >= 0 && zDiff <= 1) {
            return false;
        }
        return true;
    }

    public boolean canInteract(Location loc) {
        return true;
    }

    public abstract void update();

    public abstract String getName();

    public ItemStack getShard() {
        return ZSIGN.imzalaZ("shard", getName(), claimData().getItemStack("shard"));
    }

    public abstract int getLevel();

    public boolean canCreate() {
        int has = getFaction().getClaimsByType(this).size();
        int hasTotal = getFaction().getAllClaims().size();
        if (has >= getMaxToHave())
            return false;
        if (hasTotal >= getFaction().getMaxClaimCount()) {
            return false;
        }
        return true;
    }

    public int getMaxToHave() {
        int fctLevel = getFaction().getLevel();
        List<Integer> ints = new ArrayList<Integer>();
        if (getLevel() == 1) {
            ints.addAll(Arrays.asList(3, 8, 14, 19, 23, 27, 30));
        } else if (getLevel() == 2) {
            ints.addAll(Arrays.asList(5, 11, 18, 24, 30));
        }
        int max = 0;
        for (int i = 0; i < ints.size(); i++) {
            if (fctLevel < ints.get(i)) {
                max = i;
            }
        }
        return max;
    }

    public void canCraft(HashMap<Integer, ItemStack> recipe) {

    }

    @Override
    protected Claim clone() {
        Claim cl;
        try {
            cl = (Claim) super.clone();
            return cl;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;

    }

    public ClaimData claimData() {
        return ClaimFactory.claimDatas.get(getName());
    }

    @Override
    public abstract List<ChestNode> getConfigurableList(ChestGUI arg0);

    public void showCorners(int time, Player... pls) {
        createCornerGMS(false);
        for (GlowingMagma gm : cgms) {
            for (Player p : pls) {
                gm.getPlayers().add(p.getUniqueId());
                if (time != -1)
                    gm.addTimedPlayer(p.getUniqueId(), time);
            }

        }
    }

    public void showEgg(int time, Player... pls) {
        createEggGM(false);
        for (Player p : pls) {
            egm.getPlayers().add(p.getUniqueId());
            if (time != -1)
                egm.addTimedPlayer(p.getUniqueId(), time);
        }
    }

    private void createCornerGMS(boolean recreate) {
        if (cgms == null || cgms.isEmpty() || recreate) {
            GlowingMagmaFactory gmf = Main.gmf;
            cgms = new ArrayList<>();
            for (Location2D loc2d : getSquare().getHollowSquare()) {
                Location loc = loc2d.toLocation(getCenterBlock().getY()).add(0.5, -1, 0.5);
                GlowingMagma gm = gmf.requestGlowingMagma(loc, getCornerColor(), 2, true);
                gm.spawn();
                cgms.add(gm);
            }
        }

    }

    private void createEggGM(boolean recreate) {
        if (egm == null || egm.isGarbage() || recreate) {
            GlowingMagmaFactory gmf = Main.gmf;
            egm = gmf.requestGlowingMagma(getCenterBlock().add(0.5, -1, 0.5), getEggColor(), 2, true);
            egm.spawn();
        }
    }

    public GlowingMagma getEggGM() {
        createEggGM(false);
        return egm;
    }

    public List<GlowingMagma> getGMS() {
        createCornerGMS(false);
        return cgms;
    }

    public Color getCornerColor() {
        return Color.valueOf(claimData().getString("cornerColor"));
    }

    public Color getEggColor() {
        return Color.valueOf(claimData().getString("eggColor"));
    }

}