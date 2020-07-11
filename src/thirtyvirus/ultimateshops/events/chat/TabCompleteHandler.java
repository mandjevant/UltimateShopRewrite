// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.events.chat;

import org.bukkit.event.EventHandler;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompleteHandler implements TabCompleter
{
    @EventHandler
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        final Player player = (Player)sender;
        final ArrayList<String> arguments = new ArrayList<String>();
        if (command.getName().equals("ushops") && args.length == 1) {
            if (!player.hasPermission("ushops.admin")) {
                arguments.addAll(Arrays.asList("help", "info", "tutorial", "listRadius", "remoteDestroyShop"));
            }
            else {
                arguments.addAll(Arrays.asList("help", "info", "tutorial", "listRadius", "myShops", "adminTutorial", "save", "reload", "purge", "shopCommandHelp"));
            }
            final Iterator<String> iter = arguments.iterator();
            while (iter.hasNext()) {
                final String str = iter.next().toLowerCase();
                if (!str.contains(args[0].toLowerCase())) {
                    iter.remove();
                }
            }
        }
        if (command.getName().equals("ushop") && args.length == 1) {
            if (!player.hasPermission("ushops.admin")) {
                arguments.addAll(Arrays.asList("info", "create", "modify", "modifyBuyPrice", "modifySellPrice", "modifyQuantity", "modifyVendorNote", "destroy"));
            }
            else {
                arguments.addAll(Arrays.asList("info", "create", "openAsOwner", "modify", "modifyBuyPrice", "modifySellPrice", "modifyQuantity", "modifyVendorNote", "destroy", "toggleAdmin", "setTag", "getTag", "removeTag", "getItem", "addBuyCommand", "addSellCommand", "removeBuyCommand", "removeSellCommand", "listBuyCommands", "listSellCommands", "clearBuyCommands", "clearSellCommands", "clearCommands"));
            }
            final Iterator<String> iter = arguments.iterator();
            while (iter.hasNext()) {
                final String str = iter.next().toLowerCase();
                if (!str.contains(args[0].toLowerCase())) {
                    iter.remove();
                }
            }
        }
        return arguments;
    }
}
