package snc.pFact.obj.cl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RaidHistory implements Serializable {

    private static final long serialVersionUID = 7700834292704341001L;

    String attacker, defender;
    int d_lost_claim, a_lost_claim;
    private List<String> d_allies = new ArrayList<String>();
    private List<String> a_allies = new ArrayList<String>();
    String winner;

    public RaidHistory(String att, String def, int d_lost, int a_lost, List<String> d_a, List<String> a_a, String win) {

        this.attacker = att;
        this.defender = def;
        this.d_lost_claim = d_lost;
        this.a_lost_claim = a_lost;
        this.d_allies = d_a;
        this.a_allies = a_a;
        this.winner = win;

    }

    public void sendPlayer(Player p) {
        p.sendMessage("");
        p.sendMessage(ChatColor.RED + "Saldıran: " + ChatColor.RESET + attacker);
        String allies = "";
        if (!a_allies.isEmpty()) {
            for (String s : a_allies) {
                allies += s + ",";
            }
            allies = allies.substring(0, allies.length() - 1);
            p.sendMessage(ChatColor.RED + "Müttefikleri: " + ChatColor.RESET + allies);
        }
        p.sendMessage(ChatColor.RED + "Kaybedilen Claim: " + ChatColor.RESET + a_lost_claim);
        allies = "";

        p.sendMessage(ChatColor.BLUE + "Savunan: " + ChatColor.RESET + defender);
        if (!d_allies.isEmpty()) {
            for (String s : d_allies) {
                allies += s + ",";
            }
            allies = allies.substring(0, allies.length() - 1);
            p.sendMessage(ChatColor.BLUE + "Savunanın Müttefikleri: " + ChatColor.RESET + allies);
        }
        p.sendMessage(ChatColor.BLUE + "Kaybedilen Claim: " + ChatColor.RESET + d_lost_claim);
        
        p.sendMessage("");
        p.sendMessage(ChatColor.GREEN + "Kazanan: " + ChatColor.RESET + winner);
        p.sendMessage("");
    }
}