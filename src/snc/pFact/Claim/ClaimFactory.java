package snc.pFact.Claim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import snc.pFact.Main;
import snc.pFact.Claim.AdditionalClaims.XPClaim;
import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.utils.Location2D;
import snc.pFact.utils.ZSIGN;

public class ClaimFactory {

    public static HashMap<String, Claim> standartClaims = new HashMap<String, Claim>();
    public static int task;

    public static void initialize() {
        Bukkit.getPluginManager().registerEvents(new ClaimListener(), Main.ekl);
        Location defLoc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        addStandartClaim(new MainClaim(defLoc, 4, null));
        addStandartClaim(new XPClaim(defLoc, 4, null, 30));
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.ekl, new Runnable() {

            @Override
            public void run() {
                getAllClaims().forEach(c -> c.update());
            }
        }, 1L, 20L);
    }

    public static void deInitialize() {
        standartClaims.clear();
        Bukkit.getScheduler().cancelTask(task);
    }

    public static void addStandartClaim(Claim claim) {
        standartClaims.put(claim.getName(), claim);
    }

    public static boolean isClaimStack(ItemStack is) {
        return is == null || is.getType() == Material.AIR || ZSIGN.sorImzaZ(is, "claim");
    }

    public static Claim getClaimFromStack(ItemStack is) {
        if (isClaimStack(is)) {
            String claimS = ZSIGN.alImzaZ(is, "claim");
            return standartClaims.get(claimS);
        }
        return null;
    }

    // use getShape method to get object of type shape
    public static Claim createClaim(String name, String faction, Location center) {
        Claim original = standartClaims.get(name);
        if (original == null)
            return null;
        Claim newClaim = original.clone();
        newClaim.setup(faction, center);

        return newClaim;
    }

    public static List<Claim> getAllClaims() {
        List<Claim> claims = new ArrayList<Claim>();
        for (B_Faction bf : DataIssues.factions.values()) {
            claims.add(bf.GetMainClaim());
            claims.addAll(bf.getAdditionalClaims());
        }
        return claims;
    }

    public static Claim getStandartMainClaim() {
        return standartClaims.get("mainClaim");
    }

    public static Claim getClaim(Location loc) {
        Location2D loc2d = Location2D.fromLocation(loc);
        for (B_Faction bf : DataIssues.factions.values()) {
            if (!bf.getMaxClaimArea().isInside(loc2d))
                continue;
            MainClaim mc = bf.GetMainClaim();
            if (mc.getSquare().isInside(loc2d))
                return mc;
            for (Claim cl : bf.getAdditionalClaims())
                if (cl.getSquare().isInside(loc2d))
                    return cl;
        }
        return null;
    }

    public static boolean canPlaceMainClaim(Location center) {
        return true;
    }

}