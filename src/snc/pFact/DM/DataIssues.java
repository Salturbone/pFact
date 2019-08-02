package snc.pFact.DM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import snc.pFact.Main;
import snc.pFact.obj.cl.B_Faction;

public class DataIssues {

	public static File factionFile;
	public static File playerFile;
	
	//onEnable'da ilk �al��an metod, factionFile'� fln tan�ml�yor, gerekirse olu�turuyor
	public static void create() {
		
		factionFile = new File(Main.ekl.getDataFolder(), "Factions/");
		playerFile = new File(Main.ekl.getDataFolder(), "Players/");
		if (!factionFile.exists())
			factionFile.mkdirs();
		if(!playerFile.exists())
			playerFile.mkdirs();
	}
	//onEnable'da �al��an 2. metod, y�klemen gereken filelar� okumak i�in
	public static void load() {
		File[] ff = factionFile.listFiles();
		for (File file : ff) {
			B_Faction fact = (B_Faction) loadObject(file);
			B_Faction.factions.put(fact.getName(), fact);
		}
	}

	public static void save() {
		//E�er bir faction silinirse main'deki hashmapdan da silinecek ve kaydedilmeyecek ama dosyas� kalaca��ndan
		//sonraki load �al��t���nda sanki hala varm�� gibi onu da okuyacak, o y�zden aktif olmayan factionlar� silmek gerek
		//kaydedilecek yani varolan factionlar�n dosya isimlerini bir listeye at
		List<String> names = new ArrayList<String>();
		//aktif factionlar� loopla
		for(B_Faction fact : B_Faction.factions.values()) {
			File file = new File(factionFile, fact.getName() + ".df");
			//kaydet i�te dosyas�na
			saveObject(fact, file);
			//aktif oldu�u i�in aktifler listesine ekle
			names.add(fact.getName() + ".df");
		}
		//b�t�n faction file'lar�n� dola�
		for(File file : factionFile.listFiles()) {
			//e�er aktif faction listesinde yoksa sil
			if(!names.contains(file.getName())) {
				file.delete();
			}
		}
	}

	//objeyi file'a yazd�rma metodu
	public static void saveObject(Object a, File f) {
		try {
			FileOutputStream d = new FileOutputStream(f);
			ObjectOutputStream out = new ObjectOutputStream(d);
			out.writeObject(a);
			out.close();
			d.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//objeyi file'dan okuma metodu
	public static Object loadObject(File f) {

		if (!f.exists()) {
			return null;
		}
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(f);

			ObjectInputStream in = new ObjectInputStream(fileIn);
			Object o = in.readObject();
			in.close();
			fileIn.close();
			return o;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();

		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();

		} catch (IOException e1) {
			e1.printStackTrace();

		}
		return null;
	}

}
