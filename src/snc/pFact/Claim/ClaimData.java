package snc.pFact.Claim;

import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import snc.pFact.utils.SerItem;

/**
 * ClaimData
 */
public class ClaimData implements Serializable {

    private static final long serialVersionUID = 1L;

    private HashMap<String, Object> fields = new HashMap<>();

    public void setObject(String str, Object obj) {
        fields.put(str, obj);
    }

    public void setItemStack(String str, ItemStack is) {
        fields.put(str, new SerItem(is));
    }

    public double getDouble(String str) {
        return (double) fields.get(str);
    }

    public int getInt(String str) {
        return (int) fields.get(str);
    }

    public String getString(String str) {
        return (String) fields.get(str);
    }

    public ItemStack getItemStack(String str) {
        return ((SerItem) fields.get(str)).getItemStack();
    }

    public Object getObject(String str) {
        return fields.get(str);
    }
}