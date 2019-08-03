package snc.pFact.DM;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import snc.pFact.ListenerClass;
import snc.pFact.Main;
import snc.pFact.DM.HashMapManager.KeyConverter;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.obj.cl.B_Player;

public class DataIssues {

    public static File factionFile;
    public static File playerFile;
    public static HashMapManager<UUID, B_Player> players;
    public static HashMapManager<String, B_Faction> factions;

    public static void initalize() {
        create();
        players = new HashMapManager<UUID, B_Player>(playerFile, new KeyConverter<UUID>() {

            @Override
            protected String toFileName(UUID key) {
                return key.toString() + ".dp";
            }

            @Override
            protected UUID toKey(String filename) {
                return UUID.fromString(filename.replaceAll(".dp", ""));
            }
        });
        factions = new HashMapManager<String, B_Faction>(factionFile, new KeyConverter<String>() {

            @Override
            protected String toFileName(String key) {
                return key + ".df";
            }

            @Override
            protected String toKey(String filename) {
                return filename.replaceAll(".df", "");
            }
        });
    }

    // onEnable'da ilk �al��an metod, factionFile'� fln tan�ml�yor, gerekirse
    // olu�turuyor
    public static void create() {

        factionFile = new File(Main.ekl.getDataFolder(), "Factions/");
        playerFile = new File(Main.ekl.getDataFolder(), "Players/");
        if (!factionFile.exists())
            factionFile.mkdirs();
        if (!playerFile.exists())
            playerFile.mkdirs();
    }

    // onEnable'da �al��an 2. metod, y�klemen gereken filelar� okumak i�in
    public static void load() {
        factions.loadAllData(true);
        for (Player p : Bukkit.getOnlinePlayers()) {
            ListenerClass.onJoin(p);
        }
    }

    public static void save() {
        factions.saveAndUnloadAllDatas();
        for (Player p : Bukkit.getOnlinePlayers()) {
            ListenerClass.onQuit(p);
        }
    }

    /*
     * / objeyi file'a yazd�rma metodu public static void saveObject(Object a, File
     * f) { try { FileOutputStream d = new FileOutputStream(f); ObjectOutputStream
     * out = new ObjectOutputStream(d); out.writeObject(a); out.close(); d.close();
     * } catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException
     * e) { e.printStackTrace(); } }
     * 
     * // objeyi file'dan okuma metodu public static Object loadObject(File f) {
     * 
     * if (!f.exists()) { return null; } FileInputStream fileIn; try { fileIn = new
     * FileInputStream(f);
     * 
     * ObjectInputStream in = new ObjectInputStream(fileIn); Object o =
     * in.readObject(); in.close(); fileIn.close(); return o; } catch
     * (FileNotFoundException e1) { e1.printStackTrace();
     * 
     * } catch (ClassNotFoundException e1) { e1.printStackTrace();
     * 
     * } catch (IOException e1) { e1.printStackTrace();
     * 
     * } return null; }
     */
}
