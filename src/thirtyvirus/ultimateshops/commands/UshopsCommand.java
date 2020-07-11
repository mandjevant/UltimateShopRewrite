// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.commands;

import java.util.ArrayList;
import org.bukkit.entity.Entity;
import org.bukkit.Chunk;
import thirtyvirus.ultimateshops.multiversion.API;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.Bukkit;
import java.util.Iterator;
import thirtyvirus.ultimateshops.helpers.MenuUtilities;
import thirtyvirus.ultimateshops.helpers.Utilities;
import java.util.Arrays;
import thirtyvirus.ultimateshops.shops.UShop;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import thirtyvirus.ultimateshops.UltimateShops;
import org.bukkit.command.CommandExecutor;

public class UshopsCommand implements CommandExecutor
{
    private UltimateShops main;
    
    public UshopsCommand(final UltimateShops main) {
        this.main = null;
        this.main = main;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        try {
            if (args.length == 0) {
                this.help(sender);
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
                    sender.sendMessage(UltimateShops.prefix + ChatColor.DARK_PURPLE + "--------------------");
                    sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.GREEN + "Version " + this.main.getVersion() + " - By ThirtyVirus");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.GREEN + "~The best shopping plugin for Minecraft Multiplayer!");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.RESET + ChatColor.RED + "" + ChatColor.BOLD + "You" + ChatColor.WHITE + ChatColor.BOLD + "Tube" + ChatColor.GREEN + " - https://youtube.com/thirtyvirus");
                    sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.RESET + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Twitter" + ChatColor.GREEN + " - https://twitter.com/thirtyvirus");
                    sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.RESET + ChatColor.GOLD + "" + ChatColor.BOLD + "SpigotMC" + ChatColor.GREEN + " - https://spigotmc.org/members/179587/");
                    if (!this.main.getPremium()) {
                        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.RESET + ChatColor.GOLD + "" + ChatColor.BOLD + "Download Premium" + ChatColor.GREEN + " - https://spigotmc.org/resources/61048/");
                    }
                    else {
                        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.RESET + ChatColor.GOLD + "" + ChatColor.BOLD + "Thank you for installing Premium!");
                    }
                    sender.sendMessage(ChatColor.DARK_PURPLE + "------------------------------");
                    break;
                }
                case "listradius": {
                    if (sender instanceof Player) {
                        final Double radius = Double.parseDouble(args[1]);
                        final Player player = (Player)sender;
                        int numInRadius = 0;
                        for (final UShop shop : this.main.getShops().values()) {
                            if (player.getLocation().distance(shop.getLocation()) <= radius) {
                                ++numInRadius;
                            }
                        }
                        sender.sendMessage(UltimateShops.prefix + ChatColor.GRAY + "Found " + numInRadius + " shop(s) whithin radius " + radius + ".");
                        boolean whiteText = false;
                        for (final UShop shop2 : this.main.getShops().values()) {
                            if (shop2.getLocation().getWorld().getName().equals(player.getLocation().getWorld().getName()) && player.getLocation().distance(shop2.getLocation()) <= radius) {
                                if (whiteText) {
                                    sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.RESET + shop2.toShopString());
                                }
                                else {
                                    sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.RESET + ChatColor.GRAY + shop2.toShopString());
                                }
                                whiteText = !whiteText;
                            }
                        }
                        break;
                    }
                    Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("only-possible-in-game-message")));
                    break;
                }
                case "myshops": {
                    if (sender instanceof Player) {
                        final Player player2 = (Player)sender;
                        int numOwned = 0;
                        for (final UShop shop3 : this.main.getShops().values()) {
                            if (shop3.getVendor().getUniqueId().equals(player2.getUniqueId())) {
                                ++numOwned;
                            }
                        }
                        Utilities.informPlayer(sender, Arrays.asList(this.main.phrases.get("shops-found-message") + " " + numOwned));
                        boolean whiteText2 = false;
                        for (final UShop shop : this.main.getShops().values()) {
                            if (shop.getVendor().getUniqueId().equals(player2.getUniqueId())) {
                                if (whiteText2) {
                                    Utilities.informPlayer(sender, Arrays.asList(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.RESET + shop.toShopString()));
                                }
                                else {
                                    Utilities.informPlayer(sender, Arrays.asList(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.RESET + ChatColor.GRAY + shop.toShopString()));
                                }
                                whiteText2 = !whiteText2;
                            }
                        }
                        break;
                    }
                    Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("only-possible-in-game-message")));
                    break;
                }
                case "remotedestroyshop": {
                    if (!sender.hasPermission("ushops.admin")) {
                        Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                        return true;
                    }
                    final UShop shop4 = this.main.getShops().get(cmdString.substring(18));
                    if (shop4 == null) {
                        Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("shop-not-at-location-message")));
                        break;
                    }
                    Utilities.informPlayer(sender, Arrays.asList(Utilities.toFormattedString(this.main.phrases.get("shop-broken-message"), shop4, (Player)sender, 0, this.main.getEcon(), ChatColor.WHITE)));
                    shop4.destroy(false);
                    break;
                }
                case "tutorial": {
                    if (sender instanceof Player) {
                        MenuUtilities.tutorialMenu((Player)sender);
                        break;
                    }
                    Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("only-possible-in-game-message")));
                    break;
                }
                case "admintutorial": {
                    if (sender instanceof Player) {
                        MenuUtilities.adminTutorialMenu((Player)sender);
                        break;
                    }
                    Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("only-possible-in-game-message")));
                    break;
                }
                case "save": {
                    if (sender.hasPermission("ushops.admin")) {
                        this.main.saveShops(this.main.getShops());
                        Utilities.informPlayer(sender, Arrays.asList(this.main.phrases.get("shops-saved-message")));
                        break;
                    }
                    Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                    return true;
                }
                case "reload": {
                    if (sender.hasPermission("ushops.admin")) {
                        this.reload();
                        Utilities.informPlayer(sender, Arrays.asList(this.main.phrases.get("plugin-reloaded-message")));
                        break;
                    }
                    Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                    return true;
                }
                case "purge": {
                    if (sender.hasPermission("ushops.admin")) {
                        this.purge(sender, args);
                        break;
                    }
                    Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                    return true;
                }
                case "shopcommandhelp": {
                    if (sender.hasPermission("ushops.admin")) {
                        this.shopCommandHelp(sender);
                        break;
                    }
                    Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
                    return true;
                }
                default: {
                    this.help(sender);
                    break;
                }
            }
        }
        catch (Exception e) {
            Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("formatting-error-message")));
        }
        return true;
    }
    
    private void reload() {
        this.main.reloadConfig();
        this.main.loadConfiguration();
        this.main.saveShops(this.main.getShops());
        this.main.getShops().clear();
        this.main.loadShops();
        this.main.loadSuggestedPrices();
        this.main.loadLangFile();
        for (final World world : Bukkit.getWorlds()) {
            for (final Chunk chunk : world.getLoadedChunks()) {
                for (final Entity e : chunk.getEntities()) {
                    if (e.getCustomName() != null && e.getCustomName().equals("ShopItem")) {
                        this.main.getShopItems().remove(e.getUniqueId());
                        e.remove();
                    }
                }
            }
        }
        for (final UUID id : this.main.getShopItems()) {
            if (API.getEntity(id) != null) {
                API.getEntity(id).remove();
            }
        }
        for (final UShop shop : this.main.getShops().values()) {
            shop.spawnDisplayItem();
        }
    }
    
    private void purge(final CommandSender sender, final String[] args) {
        int timeInHours = 0;
        char timeUnit = 'h';
        int timeUnitNum = 0;
        String timeUnitString = "";
        int radius = -1;
        String playerName = "";
        boolean emptyOnly = true;
        for (final String token : args) {
            final String[] pieces = token.split(":");
            if (pieces.length == 2) {
                if (pieces[0].equals("t")) {
                    String num = "";
                    for (final char n : pieces[1].toCharArray()) {
                        if (Character.isDigit(n)) {
                            num += n;
                        }
                        if (Character.isAlphabetic(n)) {
                            timeUnit = n;
                            break;
                        }
                    }
                    switch (timeUnit) {
                        case 'h': {
                            timeUnitString = "hour";
                            timeUnitNum = Integer.parseInt(num);
                            timeInHours = Integer.parseInt(num);
                            break;
                        }
                        case 'd': {
                            timeUnitString = "day";
                            timeUnitNum = Integer.parseInt(num);
                            timeInHours = Integer.parseInt(num) * 24;
                            break;
                        }
                        case 'w': {
                            timeUnitString = "week";
                            timeUnitNum = Integer.parseInt(num);
                            timeInHours = Integer.parseInt(num) * 24 * 7;
                            break;
                        }
                        case 'm': {
                            timeUnitString = "month";
                            timeUnitNum = Integer.parseInt(num);
                            timeInHours = Integer.parseInt(num) * 24 * 7 * 4;
                            break;
                        }
                        default: {
                            Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("only-possible-in-game-message")));
                            return;
                        }
                    }
                }
                if (pieces[0].equals("r")) {
                    radius = Integer.parseInt(pieces[1]);
                }
                if (pieces[0].equals("p")) {
                    playerName = pieces[1];
                }
                if (pieces[0].equals("e")) {
                    emptyOnly = Boolean.valueOf(pieces[1]);
                }
            }
        }
        final ArrayList<UShop> shopsToDestroy = new ArrayList<UShop>();
        final long currentTime = System.currentTimeMillis();
        for (final UShop shop : this.main.getShops().values()) {
            final int hoursInactive = (int)((currentTime - shop.getLastActive()) / 3600000L);
            if (hoursInactive > timeInHours && (shop.getAmount() == 0 || !emptyOnly) && (playerName == "" || playerName == shop.getVendor().getName())) {
                if (radius == -1) {
                    shopsToDestroy.add(shop);
                }
                else {
                    if (!(sender instanceof Player)) {
                        Utilities.warnPlayer(sender, Arrays.asList(this.main.phrases.get("only-possible-in-game-message")));
                        return;
                    }
                    final Player player = (Player)sender;
                    if (!shop.getLocation().getWorld().getName().equals(player.getLocation().getWorld().getName()) || player.getLocation().distance(shop.getLocation()) < radius) {
                        continue;
                    }
                    shopsToDestroy.add(shop);
                }
            }
        }
        int shopsPurged = 0;
        for (final UShop shop2 : shopsToDestroy) {
            if (this.main.getDebug()) {
                Bukkit.getLogger().info(this.main.getConsolePrefix() + this.main.phrases.get("only-possible-in-game-message") + ": " + Utilities.toLocString(shop2.getLocation()));
            }
            shop2.destroy(false);
            ++shopsPurged;
        }
        String isEmpty = "";
        if (emptyOnly) {
            isEmpty = "empty ";
        }
        String isInRadius = "";
        if (radius != -1) {
            isInRadius = "in radius " + radius + " ";
        }
        String isPlayerSpecific = "";
        if (playerName != "") {
            isPlayerSpecific = "owned by " + playerName + " ";
        }
        sender.sendMessage(UltimateShops.prefix + ChatColor.GRAY + "Purged " + shopsPurged + " " + isEmpty + "shops " + isInRadius + isPlayerSpecific + "that haven't been active in the past " + timeUnitNum + " " + timeUnitString + "(s).");
    }
    
    private void help(final CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_PURPLE + "----------" + ChatColor.RED + "User Commands" + ChatColor.DARK_PURPLE + "----------");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushops help");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushops info");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushops tutorial");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushops listRadius RADIUS");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushops myShops");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop info");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop create");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop modify");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop modifyBuyPrice PRICE");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop modifySellPrice PRICE");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop modifyQuantity QUANTITY");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop modifyVendorNote NOTE");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop destroy");
        sender.sendMessage("");
        if (sender.hasPermission("ushops.admin")) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "----------" + ChatColor.RED + "Staff Commands" + ChatColor.DARK_PURPLE + "----------");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushops save");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushops reload");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushops purge");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushops remoteDestroyShop worldname,x,y,z");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushops shopCommandHelp");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushops adminTutorial");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop toggleAdmin");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop openAsOwner");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop getItem");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop addBuyCommand COMMAND (do not include '/')");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop addSellCommand COMMAND (do not include '/')");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop removeBuyCommand INDEX");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop removeSellCommand INDEX");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop listBuyCommands");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop listSellCommands");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop clearBuyCommands");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop clearSellCommands");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/ushop clearCommands");
        }
    }
    
    private void shopCommandHelp(final CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_PURPLE + "----------" + ChatColor.RED + "uShop Shop Command Help" + ChatColor.DARK_PURPLE + "----------");
        sender.sendMessage(ChatColor.DARK_PURPLE + "     When adding commands, do not include '/'");
        sender.sendMessage(ChatColor.DARK_PURPLE + "-----" + ChatColor.GRAY + "Placeholders for buy/sell commands" + ChatColor.DARK_PURPLE + "-----");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "customer  = name of player using shop");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "vendor     = name of owner of shop");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "world       = world shop is in");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "shopx      = x coordinate of shop");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "shopy      = y coordinate of shop");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "shopz      = z coordinate of shop");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "item         = shop item");
    }
}
