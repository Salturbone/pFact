package snc.pFact.DM;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import snc.pFact.Main;
import snc.pFact.obj.cl.b_Faction;

public class dataIssues {
	
	public static void initFactions(File factionFile, HashMap<String, b_Faction> factions) {
		factionFile = new File(Main.ekl.getDataFolder(), "Factions/");
        factionFile.mkdirs();
        File[] ff = factionFile.listFiles();
        for (File file : ff) {
            
            try {
                FileInputStream fi = new FileInputStream(
                        file);
                ObjectInputStream oi = new ObjectInputStream(fi);
                b_Faction fact = (b_Faction) oi.readObject();
                factions.put(fact.getName(), fact);
                oi.close();
                fi.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
	}
	
}
