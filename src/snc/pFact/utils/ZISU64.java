package snc.pFact.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ZISU64 {

    public static String cevObject(Object a) {
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(byteout);
            out.writeObject(a);
            out.close();
            return Base64Coder.encodeLines(byteout.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save object.", e);
        }

    }

    public static String cevIS(ItemStack is) {
        try {
            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitout = new BukkitObjectOutputStream(byteout);
            bukkitout.writeObject(is);
            bukkitout.close();
            return Base64Coder.encodeLines(byteout.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }

    public static String cevLok(Location lok) {
        try {
            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitout = new BukkitObjectOutputStream(byteout);
            bukkitout.writeObject(lok);
            bukkitout.close();
            return Base64Coder.encodeLines(byteout.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save location.", e);
        }
    }

    public static String cevEnv(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static Object yukleObjectByte(byte[] data) {
        try {
            ByteArrayInputStream bytein = new ByteArrayInputStream(data);
            ObjectInputStream obj = new ObjectInputStream(bytein);
            Object is = obj.readObject();
            obj.close();
            return is;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load object.", e);
        }
    }

    public static Object yukleObject(String data) {
        try {
            ByteArrayInputStream bytein = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            ObjectInputStream obj = new ObjectInputStream(bytein);
            Object is = obj.readObject();
            obj.close();
            return is;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to decode object.", e);
        }
    }

    public static ItemStack yukleIS(String data) {
        try {
            ByteArrayInputStream bytein = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream bukkitin = new BukkitObjectInputStream(bytein);
            ItemStack is = (ItemStack) bukkitin.readObject();
            bukkitin.close();
            return is;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to decode item stack.", e);
        }
    }

    public static Location yukleLok(String data) {
        try {
            ByteArrayInputStream bytein = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream bukkitin = new BukkitObjectInputStream(bytein);
            Location is = (Location) bukkitin.readObject();
            bukkitin.close();
            return is;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to decode location.", e);
        }
    }

    public static Inventory yukleEnv(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}
