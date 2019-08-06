package snc.pFact.Claim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import snc.pFact.Main;
import snc.pFact.Claim.AdditionalClaims.XPClaim;
import snc.pFact.Claim.Upgrade.ClaimUpgrade;
import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.utils.Location2D;
import snc.pFact.utils.ZSIGN;

public class ClaimFactory {

    public static HashMap<String, Claim> standartClaims = new HashMap<String, Claim>();
    public static HashMap<String, ClaimData> claimDatas = new HashMap<String, ClaimData>();
    public static HashMap<Integer, ItemStack> craftLevelIS = new HashMap<Integer, ItemStack>();
    public static HashMap<String, ClaimUpgrade> upgrades = new HashMap<String, ClaimUpgrade>();
    public static int task;

    public static void initialize() {
        Main.ekl.getCommand("claim").setExecutor(new ClaimCommand());
        Main.ekl.getCommand("claim").setTabCompleter(new ClaimTabCompleter());

        loadObjects();
        Bukkit.getPluginManager().registerEvents(new ClaimListener(), Main.ekl);
        addStandartClaim(new MainClaim(4, new ItemStack(Material.DRAGON_EGG), new ItemStack(Material.ANVIL)));
        addStandartClaim(
                new XPClaim(4, new ItemStack(Material.DRAGON_EGG), new ItemStack(Material.PRISMARINE_SHARD), 30, 2));
        for (int i = 1; i <= 3; i++) {
            craftLevelIS.put(i, getItemByLevel(i));
        }
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.ekl, new Runnable() {

            @Override
            public void run() {
                getAllClaims().forEach(c -> c.update());
            }
        }, 1L, 20L);
    }

    public static ItemStack getItemByLevel(int level) {
        ItemStack is = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName("Claim Craft Item LV 1");
        List<String> lore = new ArrayList<String>();
        lore.add("Used in crafting");
        lore.add("Level " + level + " claim blocks");
        meta.setLore(lore);
        is.setItemMeta(meta);
        return is;
    }

    public static void loadObjects() {

    }

    public static void saveObjects() {

    }

    public static void deInitialize() {
        saveObjects();
        standartClaims.clear();
        Bukkit.getScheduler().cancelTask(task);
    }

    public static void addStandartClaim(Claim claim) {
        standartClaims.put(claim.getName(), claim);
    }

    public static String getClaimStackFaction(ItemStack is) {
        return is == null ? null : ZSIGN.alImzaZ(is, "claimFaction");
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

    public static ClaimUpgrade getUpgradeFromItemStack(ItemStack is) {
        return is == null ? null : upgrades.get(ZSIGN.alImzaZ(is, "claimUpgrade"));
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

    @SuppressWarnings("unchecked")
    public static <T extends Claim> T createClaim(T claim, String faction, Location center) {
        T newClaim = (T) claim.clone();
        newClaim.setup(faction, center);
        return newClaim;
    }

    public static List<Claim> getAllClaims() {
        List<Claim> claims = new ArrayList<Claim>();
        for (B_Faction bf : DataIssues.factions.values()) {
            claims.addAll(bf.getAllClaims());
        }
        return claims;
    }

    public static Claim getStandartMainClaim() {
        return standartClaims.get("mainClaim");
    }

    public static Claim getClaim(Location loc) {
        Location2D loc2d = Location2D.fromLocation(loc);
        for (B_Faction bf : DataIssues.factions.values()) {
            if (bf.GetMainClaim() == null)
                continue;
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
        boolean suits = false;
        for (B_Faction fct : DataIssues.factions.values()) {
            Claim mc = fct.GetMainClaim();
            if (mc == null)
                continue;
            Location mcloc = mc.getCenterBlock();
            if (!mcloc.getWorld().getName().equals(center.getWorld().getName()))
                continue;
            double xdiff = Math.abs(center.getX() - mcloc.getX());
            double zdiff = Math.abs(center.getZ() - mcloc.getZ());
            if (xdiff < B_Faction.maxMaxClaimLength * 3 || zdiff < B_Faction.maxMaxClaimLength * 3)
                return false;
            if (xdiff > B_Faction.maxMaxClaimLength * 6 || zdiff < B_Faction.maxMaxClaimLength * 6)
                suits = true;
        }
        return suits;
    }

}