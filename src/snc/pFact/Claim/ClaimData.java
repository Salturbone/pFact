package snc.pFact.Claim;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

    public long getLong(String str) {
        return (long) fields.get(str);
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

    public HashMap<String, Object> getConfigurations() {
        return fields;
    }

    public void giveInformation(String key, Player p) {
        Object val = fields.get(key);
        if (val == null)
            return;
        if (val instanceof SerItem) {
            p.getInventory().addItem(((SerItem) val).getItemStack());
        } else {
            p.sendMessage("Value of " + key + ": " + val.toString());
        }
    }

    public boolean configure(String key, Player p, String... args) {
        Object val = fields.get(key);
        if (val == null)
            return false;
        if (val instanceof SerItem) {
            ItemStack is = p.getInventory().getItemInMainHand();
            if (is == null || is.getType() == Material.AIR) {
                p.sendMessage(ChatColor.RED + "You need to have an item in your hand to configure " + key + ".");
                return false;
            }
            setItemStack(key, is);
            return true;
        }
        if (args.length < 1) {
            p.sendMessage(ChatColor.RED + "You need to enter an arg to configure " + key + ".");
            return false;
        }
        if (val instanceof Double) {
            try {
                double d = Double.parseDouble(args[0]);
                setObject(key, d);
                return true;
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.RED + "You need to enter a double to configure " + key + ".");
                return false;
            }
        }
        if (val instanceof Long) {
            try {
                long d = Long.parseLong(args[0]);
                setObject(key, d);
                return true;
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.RED + "You need to enter a long to configure " + key + ".");
                return false;
            }
        }
        if (val instanceof Integer) {
            try {
                Integer i = Integer.parseInt(args[0]);
                setObject(key, i);
                return true;
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.RED + "You need to enter a number to configure " + key + ".");
                return false;
            }
        }
        if (val instanceof String) {
            String st = "";
            for (int i = 0; i < args.length; i++) {
                st += args[i];
                if (i != args.length - 1)
                    st += " ";
            }
            setObject(key, st);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String st = "";
        for (Entry<String, Object> en : fields.entrySet()) {
            st += ChatColor.GREEN + "" + en.getKey() + ": " + ChatColor.AQUA + en.getValue().toString() + "\n";
        }
        return st;
    }
}