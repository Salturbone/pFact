package snc.pFact.Claim;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import snc.pFact.DM.DataIssues;
import snc.pFact.obj.cl.B_Faction;
import snc.pFact.utils.SerItem;

/**
 * ClaimCommand
 */
public class ClaimCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("claim"))
            return false;
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("items")) {
            if (args.length <= 2) {
                sender.sendMessage("/claim items <noup/break/cores> <set/>");
                return true;
            }
            if (args[1].equalsIgnoreCase("noup")) {
                Player p = (Player) sender;
                if (args.length >= 3 && args[2].equalsIgnoreCase("set")) {
                    ItemStack is = p.getInventory().getItemInMainHand();
                    if (is == null || is.getType() == Material.AIR) {
                        sender.sendMessage("Hold a item");
                        return true;
                    }
                    ClaimFactory.noUpgradeItem = new SerItem(is);
                    sender.sendMessage("Changed no upgrade item.");
                    return true;
                }
                p.getInventory().addItem(ClaimFactory.noUpgradeItem.getItemStack());
                sender.sendMessage("Given no upgrade item.");
                return true;
            }
            if (args[1].equalsIgnoreCase("break")) {
                Player p = (Player) sender;
                if (args.length >= 3 && args[2].equalsIgnoreCase("set")) {
                    ItemStack is = p.getInventory().getItemInMainHand();
                    if (is == null || is.getType() == Material.AIR) {
                        sender.sendMessage("Hold a item");
                        return true;
                    }
                    ClaimFactory.breakClaim = new SerItem(is);
                    sender.sendMessage("Changed no upgrade item.");
                    return true;
                }
                p.getInventory().addItem(ClaimFactory.noUpgradeItem.getItemStack());
                sender.sendMessage("Given break item.");
                return true;
            }
            if (args[1].equalsIgnoreCase("cores")) {
                if (args.length <= 2) {
                    sender.sendMessage("Enter a number");
                    return true;
                }
                Player p = (Player) sender;
                int i;
                try {
                    i = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("Enter a number");
                    return true;
                }
                if (!ClaimFactory.craftLevelIS.containsKey(i)) {
                    sender.sendMessage("Enter a number in range 1-3");
                    return true;
                }
                if (args.length >= 4 && args[2].equalsIgnoreCase("set")) {
                    ItemStack is = p.getInventory().getItemInMainHand();
                    if (is == null || is.getType() == Material.AIR) {
                        sender.sendMessage("Hold a item");
                        return true;
                    }
                    ClaimFactory.craftLevelIS.put(i, new SerItem(is));
                    sender.sendMessage("Changed core item of level " + i);
                    return true;
                }
                ItemStack is = ClaimFactory.craftLevelIS.get(i).getItemStack();
                p.getInventory().addItem(is);
                sender.sendMessage("Given core item of level " + i);
                return true;
            }
        }

        if (args.length >= 2 && args[1].equalsIgnoreCase("shard") && hasPermission(sender)) {
            Claim cl = null;
            cl = ClaimFactory.standartClaims.get(args[0]);
            if (cl == null) {
                sender.sendMessage("couldn't find a claim with name " + args[0] + ".");
                return true;
            }
            Player p = (Player) sender;
            ItemStack is = cl.getShard();
            p.getInventory().addItem(is);
            p.sendMessage("Given shard of " + args[0]);
            return true;
        }
        if (args.length >= 2 && args[1].equalsIgnoreCase("block") && hasPermission(sender)) {
            Claim cl = null;
            cl = ClaimFactory.standartClaims.get(args[0]);
            if (cl == null) {
                sender.sendMessage("couldn't find a claim with name " + args[0] + ".");
                return true;
            }
            UUID uid = null;
            if (args.length >= 3) {

                if (!DataIssues.factions.containsKey(args[2])) {
                    sender.sendMessage("Invalid faction name: " + args[2]);
                    return true;
                }
                B_Faction bf = DataIssues.factions.get(args[2]);
                uid = bf.getUUID();
            }
            Player p = (Player) sender;
            ItemStack is = cl.getClaimItem(uid);
            p.getInventory().addItem(is);
            p.sendMessage("Given block of " + args[0]);
            return true;
        }
        if (args.length >= 2 && args[1].equalsIgnoreCase("configure") && hasPermission(sender)) {
            ClaimData cd = null;
            cd = ClaimFactory.claimDatas.get(args[0]);
            if (cd == null) {
                sender.sendMessage("couldn't find a claim with name " + args[0] + ".");
                return true;
            }
            if (args.length >= 3) {
                Object val = cd.getObject(args[2]);
                if (val == null) {
                    sender.sendMessage("Couldn't find a configuration as" + args[3] + ".");
                    sender.sendMessage("/claim <claim name> configure <configuration> <get/set> <value>");
                    return true;
                }
                Player p = (Player) sender;
                if (args.length == 2) {
                    sender.sendMessage("/claim <claim name> configure <configuration> <get/set> <value>");
                    return true;
                }
                if (args[3].equalsIgnoreCase("get")) {
                    cd.giveInformation(args[2], p);
                    return true;
                } else if (args[3].equalsIgnoreCase("set")) {
                    String[] cArgs = new String[args.length - 4];
                    for (int i = 4; i < args.length; i++) {
                        cArgs[i - 4] = args[i];
                    }
                    if (cd.configure(args[2], p, cArgs)) {
                        p.sendMessage(ChatColor.GREEN + "Successfully configured " + args[2] + ".");
                        return true;
                    }
                    return true;
                }

            }
            if (args.length >= 2) {
                sender.sendMessage(ChatColor.AQUA + "Configuration of " + args[0] + ": ");
                sender.sendMessage(cd.toString());
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("list") && hasPermission(sender)) {
            String st = "";
            for (String key : ClaimFactory.claimDatas.keySet()) {
                st += key + ",";
                if (st.length() > 20) {
                    sender.sendMessage(st);
                    st = "";
                }
            }
            sender.sendMessage(st);
            return true;
        }
        if (args[0].equalsIgnoreCase("clearfiles") && hasPermission(sender)) {
            ClaimFactory.claimDatas.clear();
            ClaimFactory.craftLevelIS.clear();
            ClaimFactory.upgradeDatas.clear();
            Bukkit.broadcastMessage(ChatColor.AQUA + "Cleared claim & upgrade datas");
            return true;
        }
        return false;
    }

    public boolean hasPermission(Permissible sender) {
        return sender.hasPermission("claim.commands") || sender.isOp();
    }

}