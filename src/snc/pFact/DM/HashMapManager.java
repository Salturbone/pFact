package snc.pFact.DM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.apache.commons.lang.Validate;

public class HashMapManager<K extends Serializable, V extends Serializable> {
    public static abstract class KeyConverter<K> {
        protected abstract String toFileName(K key);

        protected abstract K toKey(String filename);
    }

    private final File dataFolder;
    private HashMap<K, V> map;
    private KeyConverter<K> converter;

    public HashMapManager(File dataFolder, KeyConverter<K> converter) {
        this.dataFolder = dataFolder;
        this.converter = converter;
        map = new HashMap<K, V>();
    }

    /**
     * Put a key,value to the hashmap
     */
    public void put(K key, V value) {
        Validate.notNull(key, "Key cannot be null !");
        Validate.notNull(value, "Value cannot be null !");
        map.put(key, value);
    }

    /**
     * @returns get or load and get the value if exsits <b>null</b> otherwise.
     */
    public V get(K key) {
        V v = map.get(key);
        if (v == null && containsKey(key)) {
            v = loadData(key);
        }
        return v;
    }

    /**
     * @returns loaded value if exsits <b>null</b> otherwise.
     */
    public V getLoaded(K key) {
        return map.get(key);
    }

    /**
     * loads all of the file data to the hashmap.
     * 
     * @param override Should override the existing entries ?
     */
    @SuppressWarnings("unchecked")
    public void loadAllData(boolean override) {
        for (File f : dataFolder.listFiles()) {
            if (f.isFile()) {
                K t = converter.toKey(f.getName());
                if (map.containsKey(t) && !override)
                    continue;
                V value = (V) loadObject(f);
                map.put(t, value);
            }
        }
    }

    /**
     * Load a data from file and put it into the hashmap.
     * 
     * @param key
     * @returns loaded data if the key exists <b>null</b> otherwise.
     */
    @SuppressWarnings("unchecked")
    public V loadData(K key) {
        Validate.notNull(key, "Key cannot be null !");
        for (File f : dataFolder.listFiles()) {
            if (f.isFile()) {
                K t = converter.toKey(f.getName());
                if (key.equals(t)) {
                    V value = (V) loadObject(f);
                    if (value != null)
                        map.put(key, value);
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * Save a data to file from hashmap if the value is not null.
     * 
     * @returns value != null
     * 
     */
    public boolean saveData(K key) {
        Validate.notNull(key, "Key cannot be null !");
        String fileName = converter.toFileName(key);
        V v = map.get(key);
        if (v != null)
            saveObject(v, new File(dataFolder, "/" + fileName));
        return v != null;
    }

    /**
     * Save a data to file from hashmap if the value is not null. <br>
     * Also it will remove it from the hashmap.
     * 
     * @returns value != null
     * 
     */
    public boolean saveAndUnloadData(K key) {
        Validate.notNull(key, "Key cannot be null !");
        String fileName = converter.toFileName(key);
        V v = map.remove(key);
        if (v != null)
            saveObject(v, new File(dataFolder, "/" + fileName));
        return v != null;
    }

    /**
     * Save all non-null data to files from hashmap.
     */
    public void saveAllDatas() {
        map.keySet().forEach(k -> saveData(k));
    }

    /**
     * Save all non-null data to files from hashmap. <br>
     * Also it will clear the hashmap.
     */
    public void saveAndUnloadAllDatas() {
        new ArrayList<K>(map.keySet()).forEach(k -> saveAndUnloadData(k));
    }

    /**
     * Check that the hashmap or the files contains the key.
     * 
     * @return true if contains.
     */
    public boolean containsKey(K key) {
        Validate.notNull(key, "Key cannot be null !");
        boolean ret = map.containsKey(key);
        if (ret == false) {
            for (File f : dataFolder.listFiles()) {
                if (f.isFile()) {
                    K t = converter.toKey(f.getName());
                    if (key.equals(t))
                        return true;
                }
            }
        }
        return ret;

    }

    /**
     * Clear all the data from hashmap and the file.
     */
    public void clear() {
        map.clear();
        for (File f : dataFolder.listFiles())
            f.delete();

    }

    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    /**
     * Remove a key and value completly from the hashmap and files.
     * 
     * @return removed value if hashmap contains it,<b>null<b> otherwise.
     */
    public V remove(K key) {
        for (File f : dataFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return converter.toKey(name).equals(key);
            }
        }))
            f.delete();
        return map.remove(key);
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        map.replaceAll(function);
    }

    public final File getDataFolder() {
        return dataFolder;
    }

    private static void saveObject(Object a, File f) {
        try {
            FileOutputStream d = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(d);
            out.writeObject(a);
            out.close();
            d.close();
        } catch (Exception e1) {

        }
    }

    private static Object loadObject(File f) {
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
        } catch (Exception e1) {

        }
        return null;
    }

    /**
     * @return A colleciton of hashmap's values.
     */
    public Collection<V> values() {
        return map.values();
    }

}
