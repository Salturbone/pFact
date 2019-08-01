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
import snc.pFact.obj.cl.b_Faction;

public class dataIssues {

	public static File factionFile;

	//onEnable'da ilk çalýþan metod, factionFile'ý fln tanýmlýyor, gerekirse oluþturuyor
	public static void create() {
		
		factionFile = new File(Main.ekl.getDataFolder(), "Factions/");
		if (!factionFile.exists())
			factionFile.mkdirs();
	}
	//onEnable'da çalýþan 2. metod, yüklemen gereken filelarý okumak için
	public static void load() {
		File[] ff = factionFile.listFiles();
		for (File file : ff) {
			b_Faction fact = (b_Faction) loadObject(file);
			Main.factions.put(fact.getName(), fact);
		}
	}

	public static void save() {
		//Eðer bir faction silinirse main'deki hashmapdan da silinecek ve kaydedilmeyecek ama dosyasý kalacaðýndan
		//sonraki load çalýþtýðýnda sanki hala varmýþ gibi onu da okuyacak, o yüzden aktif olmayan factionlarý silmek gerek
		//kaydedilecek yani varolan factionlarýn dosya isimlerini bir listeye at
		List<String> names = new ArrayList<String>();
		//aktif factionlarý loopla
		for(b_Faction fact : Main.factions.values()) {
			File file = new File(factionFile, fact.getName() + ".df");
			//kaydet iþte dosyasýna
			saveObject(fact, file);
			//aktif olduðu için aktifler listesine ekle
			names.add(fact.getName() + ".df");
		}
		//bütün faction file'larýný dolaþ
		for(File file : factionFile.listFiles()) {
			//eðer aktif faction listesinde yoksa sil
			if(!names.contains(file.getName())) {
				file.delete();
			}
		}
	}

	//objeyi file'a yazdýrma metodu
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
