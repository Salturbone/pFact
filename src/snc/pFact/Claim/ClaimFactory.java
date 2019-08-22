package snc.pFact.Claim;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.Zindev.utils.Itemizer.Itemizer;
import me.Zindev.utils.text.Cutty;
import me.Zindev.utils.text.SoShorten;
import snc.pFact.Main;
import snc.pFact.Claim.AdditionalClaims.CraftClaim;
import snc.pFact.Claim.AdditionalClaims.XPClaim;
import snc.pFact.Claim.Upgrade.ClaimUpgrade;
import snc.pFact.Claim.Upgrade.GainMultiplierUpgrade;
import snc.pFact.Claim.Upgrade.HealthMultiplierUpgrade;
import snc.pFact.Claim.Upgrade.UpgradeCommand;
import snc.pFact.Claim.Upgrade.UpgradeCompleter;
import snc.pFact.Claim.Upgrade.UpgradeData;
import snc.pFact.DM.DataIssues;
import snc.pFact.DM.HashMapManager;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.utils.Location2D;
import snc.pFact.utils.SerItem;
import snc.pFact.utils.ZSIGN;
import snc.pFact.utils.GlowingMagmaAPI.GlowingMagmaProtocols.Color;

public class ClaimFactory {

    public static File claimFolder;
    public static File noUpgradeItemFile, breakClaimFile;

    public static HashMap<String, Claim> standartClaims;
    public static HashMapManager<String, ClaimData> claimDatas;
    public static HashMapManager<Integer, SerItem> craftLevelIS;
    public static HashMap<String, ClaimUpgrade> standartUpgrades;
    public static HashMapManager<String, UpgradeData> upgradeDatas;
    public static SerItem noUpgradeItem, breakClaim;
    public static int task;
    public static int interval;

    public static void initialize(int interval) {
        claimFolder = new File(Main.ekl.getDataFolder(), "claim");
        if (!claimFolder.exists())
            claimFolder.mkdirs();
        Bukkit.getPluginManager().registerEvents(new ClaimListener(), Main.ekl);
        ClaimFactory.interval = interval;
        initMaps();
        Main.ekl.getCommand("claim").setExecutor(new ClaimCommand());
        Main.ekl.getCommand("claim").setTabCompleter(new ClaimTabCompleter());
        Main.ekl.getCommand("upgrade").setExecutor(new UpgradeCommand());
        Main.ekl.getCommand("upgrade").setTabCompleter(new UpgradeCompleter());

        initStandartClaims();
        initStandartUpgrades();
        loadObjects();
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.ekl, new Runnable() {

            @Override
            public void run() {
                getAllClaims().forEach(c -> c.update());
            }
        }, 1L, (long) interval);
    }

    public static void deInitialize() {
        saveObjects();
        standartClaims.clear();
        Bukkit.getScheduler().cancelTask(task);
    }

    public static void loadObjects() {
        System.out.println("a");
        if (!claimDatas.getDataFolder().exists()) {
            claimDatas.getDataFolder().mkdirs();
        }
        if (!upgradeDatas.getDataFolder().exists()) {
            upgradeDatas.getDataFolder().mkdirs();
        }
        if (!craftLevelIS.getDataFolder().exists()) {
            craftLevelIS.getDataFolder().mkdirs();
        }
        for (ClaimData cd : claimDatas.values()) {
            System.out.println(cd.getString("displayName") + "   " + cd.getInt("length") + "");
        }
        claimDatas.loadAllData(true);
        for (ClaimData cd : claimDatas.values()) {
            System.out.println(cd.getString("displayName") + "   " + cd.getInt("length") + "");
        }
        craftLevelIS.loadAllData(true);
        upgradeDatas.loadAllData(true);
        noUpgradeItemFile = new File(claimFolder, "noUpgradeItem.si");
        if (noUpgradeItemFile.exists()) {
            noUpgradeItem = (SerItem) DataIssues.loadObject(noUpgradeItemFile);
        }
        breakClaimFile = new File(claimFolder, "breakClaim.si");
        if (breakClaimFile.exists()) {
            breakClaim = (SerItem) DataIssues.loadObject(breakClaimFile);
        }
    }

    public static void saveObjects() {
        claimDatas.saveAndUnloadAllDatas();

        craftLevelIS.saveAndUnloadAllDatas();

        upgradeDatas.saveAndUnloadAllDatas();

        DataIssues.saveObject(noUpgradeItem, noUpgradeItemFile);
        DataIssues.saveObject(breakClaim, breakClaimFile);
    }

    public static void initStandartUpgrades() {
        noUpgradeItem = new SerItem(Itemizer.wrap(new ItemStack(Material.ANVIL)).setDisplayName("No Upgrades")
                .setLore(Cutty.wrap(SoShorten.colorize("Put an upgrade in this slot."), 16).asLines()).build());
        addStandartUpgrade(new GainMultiplierUpgrade(new ItemStack(Material.GOLD_INGOT), 0.2, 1.5));
        addStandartUpgrade(new HealthMultiplierUpgrade(new ItemStack(Material.GOLDEN_APPLE), 0.2, 1.5));
    }

    private static void initMaps() {
        standartClaims = new HashMap<String, Claim>();
        claimDatas = new HashMapManager<>(new File(claimFolder, "Claim Datas"),
                new HashMapManager.KeyConverter<String>() {
                    @Override
                    protected String toFileName(String key) {
                        return key + ".cd";
                    }

                    @Override
                    protected String toKey(String filename) {
                        return filename.replaceAll(".cd", "");
                    }
                });
        upgradeDatas = new HashMapManager<>(new File(claimFolder, "Upgrade Datas"),
                new HashMapManager.KeyConverter<String>() {
                    @Override
                    protected String toFileName(String key) {
                        return key + ".up";
                    }

                    @Override
                    protected String toKey(String filename) {
                        return filename.replaceAll(".up", "");
                    }
                });
        craftLevelIS = new HashMapManager<>(new File(claimFolder, "LevelItems"),
                new HashMapManager.KeyConverter<Integer>() {

                    @Override
                    protected String toFileName(Integer key) {
                        return "level" + key + ".si";
                    }

                    @Override
                    protected Integer toKey(String filename) {
                        return Integer.parseInt(filename.replaceAll("level", "").replaceAll(".si", ""));
                    }

                });
        for (int i = 1; i <= 3; i++) {
            craftLevelIS.put(i, new SerItem(
                    Itemizer.wrap(new ItemStack(Material.NETHER_STAR)).setDisplayName("Craft Level " + i).build()));

        }

        standartUpgrades = new HashMap<>();

    }

    private static void initStandartClaims() {

        breakClaim = new SerItem(Itemizer.wrap(new ItemStack(Material.BARRIER)).setDisplayName("Break Claim")
                .setLore(Cutty.wrap(SoShorten.colorize("Breaks claim & gives its block."), 16).asLines()).build());
        addStandartClaim(
                new MainClaim(4, new ItemStack(Material.DRAGON_EGG), new ItemStack(Material.ANVIL), Color.AQUA));
        // level 1 8 hours
        // level 2 24 hours
        // level 3 72 hours

        addStandartClaim(new XPClaim(4, new ItemStack(Material.DRAGON_EGG), new ItemStack(Material.PRISMARINE_SHARD),
                Color.DARK_GREEN, 8L * 60L * 60L * 1000L, 0.5, 0.6, 30));
        addStandartClaim(new CraftClaim(4, new ItemStack(Material.DRAGON_EGG), new ItemStack(Material.PRISMARINE_SHARD),
                Color.DARK_GREEN, 24L * 60L * 60L * 1000L, 0.5, 0.6, 30));
        for (int i = 1; i <= 3; i++) {
            craftLevelIS.put(i, new SerItem(getItemByLevel(i)));
        }

    }

    public static ItemStack getItemByLevel(int level) {
        return ZSIGN.imzalaZ("levelItem", level + "", craftLevelIS.get(level).getItemStack());
    }

    public static int getLevelFromItem(ItemStack is) {
        if (!ZSIGN.sorImzaZ(is, "levelItem"))
            return -1;
        return Integer.parseInt(ZSIGN.alImzaZ(is, "levelItem"));
    }

    public static Claim getClaimFromShard(ItemStack is) {
        return standartClaims.get(ZSIGN.alImzaZ(is, "shard"));
    }

    public static void addStandartClaim(Claim claim) {
        standartClaims.put(claim.getName(), claim);
    }

    public static void addStandartUpgrade(ClaimUpgrade upg) {
        standartUpgrades.put(upg.getName(), upg);
    }

    public static UUID getClaimStackFaction(ItemStack is) {
        return is == null ? null
                : (ZSIGN.sorImzaZ(is, "claimFaction") ? UUID.fromString(ZSIGN.alImzaZ(is, "claimFaction")) : null);
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
        return is == null ? null : standartUpgrades.get(ZSIGN.alImzaZ(is, "claimUpgrade"));
    }

    @SuppressWarnings("unchecked")
    public static <T extends ClaimUpgrade> T createUpgrade(T upgrade, Claim cl) {
        ClaimUpgrade cu = upgrade.clone();
        cu.setup(cl);
        return (T) cu;
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