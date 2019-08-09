package snc.pFact.Claim.Upgrade;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import snc.pFact.Claim.ClaimFactory;

/**
 * UpgradeCommand
 */
public class UpgradeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("upgrade"))
            return false;
        if (args.length == 0) {
            return false;
        }
        if (args.length >= 2 && args[1].equalsIgnoreCase("item") && hasPermission(sender)) {
            ClaimUpgrade cu = null;
            cu = ClaimFactory.standartUpgrades.get(args[0]);
            if (cu == null) {
                sender.sendMessage("couldn't find a upgrade with name " + args[0] + ".");
                return true;
            }
            Player p = (Player) sender;
            ItemStack is = cu.getUpgradeItem();
            p.getInventory().addItem(is);
            p.sendMessage("Given item of " + args[0]);
            return true;
        }
        if (args.length >= 2 && args[1].equalsIgnoreCase("configure") && hasPermission(sender)) {
            UpgradeData ud = null;
            ud = ClaimFactory.upgradeDatas.get(args[0]);
            if (ud == null) {
                sender.sendMessage("couldn't find a upgrade with name " + args[0] + ".");
                return true;
            }
            if (args.length >= 3) {
                Object val = ud.getObject(args[2]);
                if (val == null) {
                    sender.sendMessage("Couldn't find a configuration as" + args[2] + ".");
                    sender.sendMessage("/upgrade <upgrade name> configure <configuration> <get/set> <value>");
                    return true;
                }
                Player p = (Player) sender;
                if (args.length == 3) {
                    sender.sendMessage("/claim <upgrade name> configure <configuration> <get/set> <value>");
                    return true;
                }
                if (args[3].equalsIgnoreCase("get")) {
                    ud.giveInformation(args[2], p);
                    return true;
                } else if (args[3].equalsIgnoreCase("set")) {
                    String[] cArgs = new String[args.length - 4];
                    for (int i = 4; i < args.length; i++) {
                        cArgs[i - 4] = args[i];
                    }
                    if (ud.configure(args[2], p, cArgs)) {
                        p.sendMessage(ChatColor.GREEN + "Successfully configured " + args[2] + ".");
                        return true;
                    }
                    return true;
                }

            }
            if (args.length >= 2) {
                sender.sendMessage(ChatColor.AQUA + "Configuration of " + args[0] + ": ");
                sender.sendMessage(ud.toString());
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("list") && hasPermission(sender)) {
            String st = "";
            for (String key : ClaimFactory.upgradeDatas.keySet()) {
                st += key + ",";
                if (st.length() > 20) {
                    sender.sendMessage(st);
                    st = "";
                }
            }
            sender.sendMessage(st);
            return true;
        }
        return false;
    }

    public boolean hasPermission(Permissible sender) {
        return sender.hasPermission("claim.commands") || sender.isOp();
    }

}