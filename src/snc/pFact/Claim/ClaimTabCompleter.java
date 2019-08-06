
package snc.pFact.Claim;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import snc.pFact.Main;

/**
 * ClaimTabCompleter
 */
public class ClaimTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.equals(Main.ekl.getCommand("claim"))) {
            if (args.length == 1) {
                List<String> list = new ArrayList<>();
                /*
                 * if (args[0].length() == 0) {
                 * ClaimFactory.claimDatas.keySet().forEach(action); } else {
                 */
                ClaimFactory.claimDatas.keySet().forEach(s -> {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase()))
                        list.add(s);
                });
                // }
                return list;
            }
            if (args.length == 3 && args[1].equalsIgnoreCase("configure")
                    && ClaimFactory.claimDatas.containsKey(args[0])) {
                List<String> list = new ArrayList<>();
                /*
                 * if (args[0].length() == 0) {
                 * ClaimFactory.claimDatas.keySet().forEach(action); } else {
                 */
                ClaimData cd = ClaimFactory.claimDatas.get(args[0]);
                cd.getConfigurations().keySet().forEach(s -> {
                    if (s.toLowerCase().startsWith(args[2].toLowerCase()))
                        list.add(s);
                });
                // }
                return list;
            }
        }
        return null;
    }

}