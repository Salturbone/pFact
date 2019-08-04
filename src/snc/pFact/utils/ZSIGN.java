package snc.pFact.utils;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

public class ZSIGN implements Listener {
    public ProtocolManager pm = ProtocolLibrary.getProtocolManager();
    public static HashMap<String, ItemStack> list = new HashMap<String, ItemStack>();

    public static ItemStack imzala(String str, ItemStack is) {
        ItemStack ret = is.clone();
        ret = MinecraftReflection.getBukkitItemStack(ret);
        NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
        tag.put("ZSignature", str);
        NbtFactory.setItemTag(ret, tag);
        list.put(str, ret);
        return ret;

    }

    public static ItemStack imzalaZ(String baslik, String str, ItemStack is) {
        ItemStack ret = is.clone();
        ret = MinecraftReflection.getBukkitItemStack(ret);
        NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
        tag.put("ZSign_" + baslik, str);
        NbtFactory.setItemTag(ret, tag);
        return ret;

    }

    public static boolean sorImza(ItemStack is) {
        ItemStack ret = is.clone();
        ret = MinecraftReflection.getBukkitItemStack(ret);
        NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
        if (tag.containsKey("ZSignature")) {
            return true;
        }
        return false;
    }

    public static boolean sorImzaZ(ItemStack is, String baslik) {
        ItemStack ret = is.clone();
        ret = MinecraftReflection.getBukkitItemStack(ret);
        NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
        if (tag.containsKey("ZSign_" + baslik)) {
            return true;
        }
        return false;
    }

    public static String alImza(ItemStack is) {
        if (sorImza(is)) {
            ItemStack ret = is.clone();
            ret = MinecraftReflection.getBukkitItemStack(ret);
            NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
            return tag.getString("ZSignature");
        }
        return null;
    }

    public static String alImzaZ(ItemStack is, String baslik) {
        if (sorImzaZ(is, baslik)) {
            ItemStack ret = is.clone();
            ret = MinecraftReflection.getBukkitItemStack(ret);
            NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(MinecraftReflection.getBukkitItemStack(is));
            return tag.getString("ZSign_" + baslik);
        }
        return null;
    }

    public static ItemStack gecir(ItemStack eski, ItemStack yeni) {
        ItemStack is = MinecraftReflection.getBukkitItemStack(eski);
        NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(is);
        ItemStack is2 = MinecraftReflection.getBukkitItemStack(yeni);
        NbtCompound tag2 = (NbtCompound) NbtFactory.fromItemTag(is2);
        for (String key : tag.getKeys()) {
            if (key.startsWith("ZSign_")) {
                tag2.put(key, tag.getString(key));
            }
        }
        NbtFactory.setItemTag(is2, tag2);
        return is2.clone();
    }

    @EventHandler
    public void onItem(InventoryClickEvent e) {
        ItemStack it = e.getCurrentItem();
        if (it == null) {
            return;
        }
        if (it.getType().equals(Material.AIR)) {
            return;
        }
        ItemStack is = MinecraftReflection.getBukkitItemStack(it);
        NbtCompound tag = (NbtCompound) NbtFactory.fromItemTag(is);
        if (!tag.containsKey("ZSignature")) {
            for (String str : list.keySet()) {
                ItemStack is2 = MinecraftReflection.getBukkitItemStack(list.get(str).clone());
                NbtCompound tag2 = (NbtCompound) NbtFactory.fromItemTag(is2);
                tag2.remove("ZSignature");
                for (String key : tag.getKeys()) {
                    if (key.startsWith("ZSign_")) {
                        tag2.put(key, tag.getString(key));
                    }
                }
                NbtFactory.setItemTag(is2, tag2);
                if (is.isSimilar(is2)) {
                    ItemStack lit = list.get(str);
                    e.setCancelled(true);
                    e.setCurrentItem(null);
                    ItemStack ret = gecir(is, lit.clone());
                    ret.setAmount(is.getAmount());
                    e.getClickedInventory().setItem(e.getSlot(), ret);
                    return;
                }

            }
            return;
        }
        if (list.containsKey(tag.getString("ZSignature"))) {
            ItemStack lit = list.get(tag.getString("ZSignature")).clone();
            NbtCompound tag2 = (NbtCompound) NbtFactory.fromItemTag(lit);
            for (String key : tag.getKeys()) {
                if (key.startsWith("ZSign_")) {
                    tag2.put(key, tag.getString(key));
                }
            }
            NbtFactory.setItemTag(lit, tag2);
            if (!lit.isSimilar(is)) {
                e.setCancelled(true);
                e.setCurrentItem(null);
                ItemStack ret = gecir(is, lit.clone());
                ret.setAmount(is.getAmount());
                e.getClickedInventory().setItem(e.getSlot(), ret);
            }
        }

    }

}
