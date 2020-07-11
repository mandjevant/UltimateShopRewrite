// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.commands;

import java.util.Iterator;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;
import thirtyvirus.ultimateshops.events.pluginsupport.TownySupport;
import thirtyvirus.ultimateshops.multiversion.API;
import java.util.Set;
import thirtyvirus.ultimateshops.multiversion.XMaterial;
import org.bukkit.Material;
import java.util.HashSet;
import thirtyvirus.ultimateshops.shops.UShop;
import thirtyvirus.ultimateshops.helpers.ActionSound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import thirtyvirus.ultimateshops.helpers.MenuUtilities;
import java.util.Arrays;
import thirtyvirus.ultimateshops.helpers.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import thirtyvirus.ultimateshops.UltimateShops;
import org.bukkit.command.CommandExecutor;

public class UshopCommand implements CommandExecutor
{
    private UltimateShops main;
    
    public UshopCommand(final UltimateShops main) {
        this.main = null;
        this.main = main;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            try {
                if (args.length == 0) {
                    player.performCommand("ushops help");
                    return true;
                }
                final UShop shop = Utilities.getShopLookingAt(this.main.getShops(), player);
                if (shop == null && !args[0].toLowerCase().equals("create")) {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("shop-not-selected-message")));
                    return true;
                }
                String cmdString = "";
                for (final String arg : args) {
                    cmdString = cmdString + " " + arg;
                }
                cmdString = cmdString.substring(1);
                final String lowerCase = args[0].toLowerCase();
                switch (lowerCase) {
                    case "info": {
                        this.info(shop, player);
                        break;
                    }
                    case "create": {
                        this.makeShop(player);
                        break;
                    }
                    case "openasowner": {
                        if (player.hasPermission("ushops.admin")) {
                            player.openInventory(MenuUtilities.vendorGUI(shop, player));
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "modify": {
                        if (shop.getVendor().getUniqueId().equals(player.getUniqueId()) || player.hasPermission("ushops.admin")) {
                            player.openInventory(MenuUtilities.modifyShopGUI(shop, player));
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "modifybuyprice": {
                        if (shop.getVendor().getUniqueId().equals(player.getUniqueId()) || player.hasPermission("ushops.admin")) {
                            shop.setBuyPrice(Integer.parseInt(cmdString.substring(15)));
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("buyprice-set-message") + ": " + cmdString.substring(15)));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "modifysellprice": {
                        if (shop.getVendor().getUniqueId().equals(player.getUniqueId()) || player.hasPermission("ushops.admin")) {
                            shop.setSellPrice(Integer.parseInt(cmdString.substring(16)));
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("sellprice-set-message") + ": " + cmdString.substring(16)));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "modifyquantity": {
                        if (shop.getVendor().getUniqueId().equals(player.getUniqueId()) || player.hasPermission("ushops.admin")) {
                            shop.setStack(Integer.parseInt(cmdString.substring(15)));
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("quantity-set-message") + ": " + cmdString.substring(15)));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "modifyvendornote": {
                        if (shop.getVendor().getUniqueId().equals(player.getUniqueId()) || player.hasPermission("ushops.admin")) {
                            shop.setVendorNote(cmdString.substring(17));
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("vendornote-set-message") + ": " + ChatColor.translateAlternateColorCodes('&', cmdString.substring(17))));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "destroy": {
                        if (shop.getVendor().getUniqueId().equals(player.getUniqueId()) || player.hasPermission("ushops.admin")) {
                            player.openInventory(MenuUtilities.destroyShopGUI(shop));
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "toggleadmin": {
                        if (player.hasPermission("ushops.admin")) {
                            shop.setAdmin(!shop.getAdmin());
                            if (!shop.getAdmin()) {
                                shop.setAmount(0);
                            }
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("adminmode-set-message") + ": " + shop.getAdmin()));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "settag": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (player.hasPermission("ushops.admin")) {
                            shop.setTag(args[1]);
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("tag-set-message") + ": " + shop.getTag()));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "gettag": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (player.hasPermission("ushops.admin")) {
                            Utilities.informPlayer((CommandSender)player, Arrays.asList("Tag: " + shop.getTag()));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "removetag": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (player.hasPermission("ushops.admin")) {
                            shop.setTag("");
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("tag-removed-message") + ": " + shop.getTag()));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "getitem": {
                        if (player.hasPermission("ushops.admin")) {
                            player.getInventory().addItem(new ItemStack[] { shop.getItem() });
                            Utilities.playSound(ActionSound.POP, player);
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("shop-item-given-message")));
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "addbuycommand": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (player.hasPermission("ushops.admin")) {
                            shop.getBuyCommands().add(cmdString.substring(14));
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("add-buy-command-message") + ": " + ChatColor.YELLOW + "/" + cmdString.substring(14)));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "addsellcommand": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (player.hasPermission("ushops.admin")) {
                            shop.getSellCommands().add(cmdString.substring(15));
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("add-sell-command-message") + ": " + ChatColor.YELLOW + "/" + cmdString.substring(15)));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "removebuycommand": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (!player.hasPermission("ushops.admin")) {
                            Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                            return true;
                        }
                        if (Integer.parseInt(args[1]) >= shop.getBuyCommands().size()) {
                            Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("index-out-of-range-buy-commands-message") + ": " + args[1]));
                            return true;
                        }
                        Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("remove-buy-command-message") + ": " + ChatColor.YELLOW + "/" + shop.getBuyCommands().get(Integer.parseInt(args[1]))));
                        shop.getBuyCommands().remove(Integer.parseInt(args[1]));
                        this.main.saveShops(this.main.getShops());
                        break;
                    }
                    case "removesellcommand": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (!player.hasPermission("ushops.admin")) {
                            Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                            return true;
                        }
                        if (Integer.parseInt(args[1]) >= shop.getSellCommands().size()) {
                            Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("index-out-of-range-sell-commands-message") + ": " + args[1]));
                            return true;
                        }
                        Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("remove-sell-command-message") + ": " + ChatColor.YELLOW + "/" + shop.getSellCommands().get(Integer.parseInt(args[1]))));
                        shop.getSellCommands().remove(Integer.parseInt(args[1]));
                        this.main.saveShops(this.main.getShops());
                        break;
                    }
                    case "listbuycommands": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (!player.hasPermission("ushops.admin")) {
                            Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                            return true;
                        }
                        if (shop.getBuyCommands().size() == 0) {
                            Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("no-buy-commands-message")));
                            return true;
                        }
                        Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("buycommands-message")));
                        for (int counter = 0; counter < shop.getBuyCommands().size(); ++counter) {
                            player.sendMessage(ChatColor.GRAY + "" + counter + ": " + ChatColor.YELLOW + "/" + shop.getBuyCommands().get(counter));
                        }
                        this.main.saveShops(this.main.getShops());
                        break;
                    }
                    case "listsellcommands": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (!player.hasPermission("ushops.admin")) {
                            Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                            return true;
                        }
                        if (shop.getSellCommands().size() == 0) {
                            Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("no-sell-commands-message")));
                            return true;
                        }
                        Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("sellcommands-message")));
                        for (int counter = 0; counter < shop.getSellCommands().size(); ++counter) {
                            player.sendMessage(ChatColor.GRAY + "" + counter + ": " + ChatColor.YELLOW + "/" + shop.getSellCommands().get(counter));
                        }
                        this.main.saveShops(this.main.getShops());
                        break;
                    }
                    case "clearbuycommands": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (player.hasPermission("ushops.admin")) {
                            shop.getBuyCommands().clear();
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("buy-commands-cleared-message")));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "clearsellcommands": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (player.hasPermission("ushops.admin")) {
                            shop.getSellCommands().clear();
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("sell-commands-cleared-message")));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    case "clearcommands": {
                        if (!this.main.getPremium()) {
                            Utilities.warnPremium(player);
                            return true;
                        }
                        if (player.hasPermission("ushops.admin")) {
                            shop.getBuyCommands().clear();
                            shop.getSellCommands().clear();
                            Utilities.informPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("all-commands-cleared-message")));
                            this.main.saveShops(this.main.getShops());
                            break;
                        }
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    default: {
                        player.performCommand("ushops help");
                        break;
                    }
                }
            }
            catch (Exception e) {
                Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("formatting-error-message")));
            }
        }
        else {
            Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("only-possible-in-game-message")));
        }
        return true;
    }
    
    private void info(final UShop shop, final Player player) {
        player.sendMessage(UltimateShops.prefix + ChatColor.GRAY + "Shop Info");
        player.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "Location: " + Utilities.toLocString(shop.getLocation()).replaceAll(",", ", "));
        player.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "Item: " + shop.getItem().getType().name() + ", Quantity: " + shop.getStack());
        player.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "BuyPrice: " + shop.getBuyPrice() + ", SellPrice: " + shop.getSellPrice());
        player.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "Stock: " + shop.getAmount());
        player.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "Vendor's Note: " + shop.getVendorNote());
        player.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "Admin Shop: " + shop.getAdmin());
        int daysInactive = 0;
        int hoursInactive = 0;
        int minutesInactive;
        for (minutesInactive = (int)((System.currentTimeMillis() - shop.getLastActive()) / 60000L); minutesInactive > 60; minutesInactive -= 60) {
            ++hoursInactive;
        }
        while (hoursInactive > 24) {
            ++daysInactive;
            hoursInactive -= 24;
        }
        String timeString = "";
        if (daysInactive > 0) {
            if (daysInactive == 1) {
                timeString = timeString + daysInactive + " day, ";
            }
            else {
                timeString = timeString + daysInactive + " days, ";
            }
        }
        if (hoursInactive > 0) {
            if (hoursInactive == 1) {
                timeString = timeString + hoursInactive + " hour, ";
            }
            else {
                timeString = timeString + hoursInactive + " hours, ";
            }
        }
        if (minutesInactive == 1) {
            timeString = timeString + minutesInactive + " minute ago";
        }
        else {
            timeString = timeString + minutesInactive + " minutes ago";
        }
        player.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "Last active: " + timeString);
    }
    
    private void makeShop(final Player player) {
        final HashSet<Material> transparent = new HashSet<Material>();
        transparent.add(XMaterial.AIR.parseMaterial());
        transparent.add(XMaterial.BLACK_CARPET.parseMaterial());
        transparent.add(XMaterial.BLUE_CARPET.parseMaterial());
        transparent.add(XMaterial.BROWN_CARPET.parseMaterial());
        transparent.add(XMaterial.CYAN_CARPET.parseMaterial());
        transparent.add(XMaterial.GRAY_CARPET.parseMaterial());
        transparent.add(XMaterial.GREEN_CARPET.parseMaterial());
        transparent.add(XMaterial.LIGHT_BLUE_CARPET.parseMaterial());
        transparent.add(XMaterial.LIGHT_GRAY_CARPET.parseMaterial());
        transparent.add(XMaterial.LIME_CARPET.parseMaterial());
        transparent.add(XMaterial.MAGENTA_CARPET.parseMaterial());
        transparent.add(XMaterial.ORANGE_CARPET.parseMaterial());
        transparent.add(XMaterial.PINK_CARPET.parseMaterial());
        transparent.add(XMaterial.PURPLE_CARPET.parseMaterial());
        transparent.add(XMaterial.RED_CARPET.parseMaterial());
        transparent.add(XMaterial.WHITE_CARPET.parseMaterial());
        transparent.add(XMaterial.YELLOW_CARPET.parseMaterial());
        final Block block = player.getTargetBlock((Set)transparent, 120);
        if (UShop.isSlab(block.getType())) {
            if (!API.isBottomSlab(block) && !this.main.getUseUpperSlabs()) {
                return;
            }
            if (this.main.getServer().getPluginManager().getPlugin("Towny") != null && this.main.getShopsOnlyInTowns() && !TownySupport.isInTown(player)) {
                Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("towny-only-in-town-message")));
                return;
            }
            if (this.main.getEnforceShopLimit() && !player.hasPermission("ushops.makeshops.*")) {
                final int shopsOwned = Utilities.countShopsOwned(this.main.getShops(), player);
                int max = 0;
                for (final PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
                    String str = perm.getPermission();
                    if (str.contains("ushops.makeshops.")) {
                        str = str.replaceAll("ushops.makeshops.", "");
                        final int i = Integer.parseInt(str);
                        if (i <= max) {
                            continue;
                        }
                        max = i;
                    }
                }
                if (shopsOwned >= max) {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("max-shops-reached-message")));
                    return;
                }
            }
            if (this.main.getPremium()) {
                if (player.hasPermission("ushops.make")) {
                    final BlockBreakEvent e = new BlockBreakEvent(block, player);
                    Bukkit.getServer().getPluginManager().callEvent((Event)e);
                    if (!e.isCancelled() || UltimateShops.ignoreBuildPermissions) {
                        e.getPlayer().openInventory(MenuUtilities.createShopGUI(e.getBlock().getLocation()));
                    }
                    else {
                        Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        e.setCancelled(true);
                    }
                }
                else {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                }
            }
            else if (Utilities.countShopsOwned(this.main.getShops(), player) >= 5) {
                Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("lite-version-warning")));
                Utilities.warnPremium(player);
            }
            else if (player.hasPermission("ushops.user")) {
                final BlockBreakEvent e = new BlockBreakEvent(block, player);
                Bukkit.getServer().getPluginManager().callEvent((Event)e);
                if (!e.isCancelled()) {
                    e.getPlayer().openInventory(MenuUtilities.createShopGUI(e.getBlock().getLocation()));
                }
                else {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("no-build-permissions-message")));
                    e.setCancelled(true);
                }
            }
            else {
                Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
            }
        }
        else {
            Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("block-not-a-slab-message")));
        }
    }
}
