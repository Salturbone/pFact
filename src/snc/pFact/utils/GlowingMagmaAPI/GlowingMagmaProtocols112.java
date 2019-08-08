package snc.pFact.utils.GlowingMagmaAPI;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GlowingMagmaProtocols112 implements GlowingMagmaProtocols {
    public static ProtocolManager pm;

    @Override
    public void spawnGlowingMagma(Player p, GlowingMagma stand) {
        PacketContainer pk = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        Location loc = stand.getLocation();
        int entityID = stand.getEntityID();
        pk.getIntegers().write(0, entityID);
        pk.getDoubles().write(0, loc.getX());
        pk.getDoubles().write(1, loc.getY());
        pk.getDoubles().write(2, loc.getZ());
        pk.getBytes().write(0, (byte) 0);
        pk.getBytes().write(1, (byte) 0);
        pk.getBytes().write(2, (byte) 0);
        pk.getSpecificModifier(UUID.class).write(0, stand.getUUID());
        pk.getIntegers().write(1, 62);
        pk.getIntegers().write(2, 0);
        pk.getIntegers().write(3, 0);
        pk.getIntegers().write(4, 0);
        pk.getDataWatcherModifier().write(0, createMetadata(getDefaultMagmaMetadata(stand.getSize())));
        try {
            pm.sendServerPacket(p, pk);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeGlowingMagma(Player p, int entityID) {
        PacketContainer pk = pm.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        pk.getIntegerArrays().write(0, new int[] { entityID });
        try {
            pm.sendServerPacket(p, pk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HashMap<Integer, Object> getDefaultMagmaMetadata(int size) {
        return new HashMap<Integer, Object>() {
            private static final long serialVersionUID = 1L;
            {
                byte b = 0x40;
                b |= 0x20;
                put(0, Byte.valueOf(b)); // Entity options
                put(1, Integer.valueOf(300)); // air
                put(2, String.valueOf("")); // custom name
                put(3, Boolean.valueOf(false)); // custom name visible
                put(4, Boolean.valueOf(true)); // is silent
                put(5, Boolean.valueOf(false)); // unknown
                put(6, Byte.valueOf((byte) 0x01)); // Health
                put(7, Float.valueOf((float) 1.0f)); // Health
                put(8, Integer.valueOf(0)); // Potion effect color
                put(9, Boolean.valueOf(true)); // Is potion effect active?
                put(10, Integer.valueOf(0)); // Number of Arrows
                put(11, Byte.valueOf((byte) 0));
                put(12, Integer.valueOf(size)); // size
            }
        };
    }

    public WrappedDataWatcher createMetadata(HashMap<Integer, Object> metadata) {
        WrappedDataWatcher wdw = new WrappedDataWatcher();
        for (Entry<Integer, Object> en : metadata.entrySet()) {

            Serializer s = Registry.get(en.getValue().getClass());
            Optional<?> t = null;
            if (s.isOptional())
                t = Optional.of(en.getValue());

            WrappedDataWatcherObject wd = new WrappedDataWatcherObject(en.getKey(), s);
            wdw.setObject(wd, t == null ? en.getValue() : t);
        }
        return wdw;
    }

    @Override
    public String getMinecraftVersion() {
        return "net.minecraft.server.v1_12_2_R1";
    }

    @Override
    public String getVersion() {
        return "Minecraft 1.12.2";
    }

    static {
        pm = ProtocolLibrary.getProtocolManager();
    }

    public void createTeam(Player p, Color color, UUID uid) {
        PacketContainer pk = pm.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        pk.getModifier().writeDefaults();
        pk.getStrings().write(0, color.name());// team name
        pk.getStrings().write(1, "");// display name
        pk.getStrings().write(2, "§" + color.pre);// prefix determines color
        pk.getStrings().write(3, "");// suffix
        pk.getStrings().write(4, "always");// name tag visibility
        pk.getStrings().write(5, "always");// collision rule

        pk.getIntegers().write(0, color.i);
        pk.getIntegers().write(1, 0); // mode 0=create 1= remove team 2= update 3= add players 4=remove players
        pk.getIntegers().write(2, 0); // options
        pk.getModifier().write(7, new ArrayList<String>(Arrays.asList(uid.toString())));// 7=h field(string list)
        try {
            pm.sendServerPacket(p, pk);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void addToTeam(Player p, Color color, UUID uid) {
        PacketContainer pk = pm.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        pk.getModifier().writeDefaults();

        pk.getStrings().write(0, color.name());
        pk.getStrings().write(1, "");
        pk.getStrings().write(2, "§" + color.pre);
        pk.getStrings().write(3, "");
        pk.getStrings().write(4, "always");
        pk.getStrings().write(5, "always");

        pk.getIntegers().write(0, color.i);
        pk.getIntegers().write(1, 3); // mode 0=create 1= remove team 2= update 3= add players 4=remove players
        pk.getIntegers().write(2, 0); // options
        pk.getModifier().write(7, new ArrayList<String>(Arrays.asList(uid.toString())));// 7=h field(string list)
        try {
            pm.sendServerPacket(p, pk);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void removeFromTeam(Player p, Color color, UUID uid) {
        PacketContainer pk = pm.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        pk.getModifier().writeDefaults();

        pk.getStrings().write(0, color.name());// team name
        pk.getStrings().write(1, "");// display name
        pk.getStrings().write(2, "§" + color.pre);// prefix
        pk.getStrings().write(3, "");// suffix
        pk.getStrings().write(4, "always");// name tag visibility
        pk.getStrings().write(5, "always");// collision

        pk.getIntegers().write(0, color.i);// color
        pk.getIntegers().write(1, 4); // mode 0=create 1= remove team 2= update 3= add players 4=remove players
        pk.getIntegers().write(2, 0); // options
        pk.getModifier().write(7, new ArrayList<String>(Arrays.asList(uid.toString())));// 7=h field(string list)
        try {
            pm.sendServerPacket(p, pk);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
