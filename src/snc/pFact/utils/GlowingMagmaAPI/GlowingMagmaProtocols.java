package snc.pFact.utils.GlowingMagmaAPI;

import java.util.UUID;

import org.bukkit.entity.Player;

public interface GlowingMagmaProtocols {
    void spawnGlowingMagma(Player p, GlowingMagma magma);

    void removeGlowingMagma(Player p, int entityID);

    void createTeam(Player p, Color color, UUID uid);

    void addToTeam(Player p, Color color, UUID uid);

    void removeFromTeam(Player p, Color color, UUID uid);

    String getMinecraftVersion();

    String getVersion();

    public enum Color {

        BLACK(0, "0"), DARK_BLUE(1, "1"), DARK_GREEN(2, "2"), DARK_AQUA(3, "3"), DARK_RED(4, "4"), DARK_PURPLE(5, "5"),
        GOLD(6, "6"), GRAY(7, "7"), DARK_GRAY(8, "8"), BLUE(9, "9"), GREEN(10, "a"), AQUA(11, "b"), RED(12, "c"),
        PURPLE(13, "d"), YELLOW(14, "e"), WHITE(15, "f"), NONE(-1, "");
        public String pre;
        public int i;

        Color(int i, String pre) {
            this.i = i;
            this.pre = pre;
        }
    }
}
