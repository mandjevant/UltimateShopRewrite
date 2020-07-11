// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.helpers;

import java.util.EnumSet;
import org.bukkit.entity.Item;
import thirtyvirus.ultimateshops.multiversion.Version;
import java.io.OutputStream;
import java.io.InputStream;
import com.google.common.io.ByteStreams;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.text.Format;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import java.io.IOException;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import org.bukkit.block.Block;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Arrays;
import thirtyvirus.ultimateshops.multiversion.XMaterial;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.OfflinePlayer;
import net.milkbowl.vault.economy.Economy;
import thirtyvirus.ultimateshops.shops.UShop;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;
import thirtyvirus.ultimateshops.multiversion.Sound;
import org.bukkit.ChatColor;
import thirtyvirus.ultimateshops.UltimateShops;
import org.bukkit.entity.Player;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import java.util.Set;

public final class Utilities
{
    private static final Set<Material> TRANSPARENT;
    private static Utilities obj;
    
    private Utilities() {
    }
    
    public static Utilities objectCreationMethod() {
        if (Utilities.obj == null) {
            Utilities.obj = new Utilities();
        }
        return Utilities.obj;
    }
    
    public static String toLocString(final Location location) {
        if (location.equals((Object)null)) {
            return "";
        }
        return location.getWorld().getName() + "," + (int)location.getX() + "," + (int)location.getY() + "," + (int)location.getZ();
    }
    
    public static String toChunkString(final Chunk chunk) {
        return chunk.getX() + "," + chunk.getZ();
    }
    
    public static String toCommadNumber(final String start) {
        String reverseNum = "" + start.charAt(start.length() - 1) + start.charAt(start.length() - 2) + start.charAt(start.length() - 3);
        int index = start.length() - 4;
        int commaCounter = 0;
        while (index >= 0) {
            reverseNum += start.charAt(index);
            if (++commaCounter > 2) {
                reverseNum += ",";
                commaCounter = 0;
            }
            --index;
        }
        String end = "";
        for (final char c : reverseNum.toCharArray()) {
            end = c + end;
        }
        if (end.charAt(0) == ',') {
            end = end.substring(1);
        }
        return end;
    }
    
    public static void warnPremium(final Player player) {
        player.sendMessage(UltimateShops.prefix + ChatColor.RESET + "This feature is restricted in UltimateShops LITE. Buy your server Premium here: ");
        player.sendMessage(UltimateShops.prefix + ChatColor.GREEN + "https://www.spigotmc.org/resources/64925/");
        player.playSound(player.getLocation(), Sound.ANVIL_BREAK.bukkitSound(), 1.0f, 1.0f);
        player.sendMessage(UltimateShops.prefix + ChatColor.DARK_PURPLE + "--------------------");
    }
    
    public static void warnPlayer(final CommandSender sender, final List<String> messages) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            playSound(ActionSound.ERROR, player);
        }
        for (final String message : messages) {
            sender.sendMessage(UltimateShops.prefix + ChatColor.RESET + ChatColor.RED + message);
        }
    }
    
    public static void informPlayer(final CommandSender player, final List<String> messages) {
        for (final String message : messages) {
            player.sendMessage(UltimateShops.prefix + ChatColor.RESET + ChatColor.GRAY + message);
        }
    }
    
    public static String toFormattedString(final String msg, final UShop shop, final Player player, final int moneyAmount, final Economy econ, final ChatColor defaultColor) {
        if (msg == null) {
            return msg;
        }
        String modified = msg + defaultColor;
        modified = modified.replaceAll(" ", " " + defaultColor);
        modified = modified.replaceAll("<location>", ChatColor.YELLOW + toLocString(shop.getLocation())) + defaultColor;
        if (shop.getItem().getItemMeta().getDisplayName() != null && shop.getItem().getItemMeta().getDisplayName() != "") {
            modified = modified.replaceAll("<item>", ChatColor.AQUA + shop.getItem().getItemMeta().getDisplayName()) + defaultColor;
        }
        else {
            modified = modified.replaceAll("<item>", ChatColor.AQUA + shop.getItem().getType().name()) + defaultColor;
        }
        modified = modified.replaceAll("<amount>", "" + ChatColor.LIGHT_PURPLE + shop.getStack()) + defaultColor;
        if (!shop.getAdmin()) {
            modified = modified.replaceAll("<host>", ChatColor.GOLD + shop.getVendor().getName()) + defaultColor;
        }
        else {
            modified = modified.replaceAll("<host>", ChatColor.RED + "ADMIN") + defaultColor;
        }
        modified = modified.replaceAll("<customer>", ChatColor.GOLD + player.getName()) + defaultColor;
        modified = modified.replaceAll("<money>", "" + ChatColor.GREEN + moneyAmount) + defaultColor;
        if (shop.getStack() != 0) {
            modified = modified.replaceAll("<unitPrice>", "" + ChatColor.GREEN + moneyAmount / shop.getStack()) + defaultColor;
        }
        else {
            modified = modified.replaceAll("<unitPrice>", "" + ChatColor.GREEN + 0) + defaultColor;
        }
        modified = modified.replaceAll("<funds>", "" + ChatColor.GREEN + econ.getBalance((OfflinePlayer)player)) + defaultColor;
        return modified;
    }
    
    public static ItemStack nameItem(final ItemStack item, final String name) {
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack nameItem(final Material item, final String name) {
        return nameItem(new ItemStack(item), name);
    }
    
    public static ItemStack loreItem(final ItemStack item, final List<String> lore) {
        final ItemMeta meta = item.getItemMeta();
        meta.setLore((List)lore);
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack stackItem(final ItemStack item, final int amount) {
        item.setAmount(amount);
        return item;
    }
    
    public static String convertToInvisibleString(final String s) {
        String hidden = "";
        for (final char c : s.toCharArray()) {
            hidden = hidden + "§" + c;
        }
        return hidden;
    }
    
    public static String convertToVisibleString(final String s) {
        String c = "";
        c += '§';
        return s.replaceAll(c, "");
    }
    
    public static ItemStack createIdentifier(final UShop shop) {
        String name = ChatColor.GOLD + "Owned By: " + shop.getVendor().getName();
        if (shop.getAdmin()) {
            name = ChatColor.RED + "ADMIN SHOP";
        }
        if (shop.getStack() > 1) {
            if (shop.getBuyPrice() == 0) {
                return loreItem(nameItem(XMaterial.COMMAND_BLOCK.parseItem(), UltimateShops.prefix + "Shop"), Arrays.asList(name, convertToInvisibleString(shop.getLocation().getWorld().getName() + "," + shop.getLocation().getBlockX() + "," + shop.getLocation().getBlockY() + "," + shop.getLocation().getBlockZ()), ChatColor.GRAY + "Buying: " + shop.getItem().getType().name(), "Sell " + shop.getStack() + " for $" + shop.getSellPrice() + " ($" + Double.toString(round(shop.getSellPrice() / (double)shop.getStack(), 2)) + " each)"));
            }
            if (shop.getSellPrice() == 0) {
                return loreItem(nameItem(XMaterial.COMMAND_BLOCK.parseItem(), UltimateShops.prefix + "Shop"), Arrays.asList(name, convertToInvisibleString(shop.getLocation().getWorld().getName() + "," + shop.getLocation().getBlockX() + "," + shop.getLocation().getBlockY() + "," + shop.getLocation().getBlockZ()), ChatColor.GRAY + "Selling: " + shop.getItem().getType().name(), "Buy " + shop.getStack() + " for $" + shop.getBuyPrice() + " ($" + Double.toString(round(shop.getBuyPrice() / (double)shop.getStack(), 2)) + " each)"));
            }
            return loreItem(nameItem(XMaterial.COMMAND_BLOCK.parseItem(), UltimateShops.prefix + "Shop"), Arrays.asList(name, convertToInvisibleString(shop.getLocation().getWorld().getName() + "," + shop.getLocation().getBlockX() + "," + shop.getLocation().getBlockY() + "," + shop.getLocation().getBlockZ()), ChatColor.GRAY + "Selling: " + shop.getItem().getType().name(), "Buy " + shop.getStack() + " for $" + shop.getBuyPrice() + " ($" + Double.toString(round(shop.getBuyPrice() / (double)shop.getStack(), 2)) + " each)", "Sell " + shop.getStack() + " for $" + shop.getSellPrice() + " ($" + Double.toString(round(shop.getSellPrice() / (double)shop.getStack(), 2)) + " each)"));
        }
        else {
            if (shop.getBuyPrice() == 0) {
                return loreItem(nameItem(XMaterial.COMMAND_BLOCK.parseItem(), UltimateShops.prefix + "Shop"), Arrays.asList(name, convertToInvisibleString(shop.getLocation().getWorld().getName() + "," + shop.getLocation().getBlockX() + "," + shop.getLocation().getBlockY() + "," + shop.getLocation().getBlockZ()), ChatColor.GRAY + "Buying: " + shop.getItem().getType().name(), "Sell " + shop.getStack() + " for $" + shop.getSellPrice()));
            }
            if (shop.getSellPrice() == 0) {
                return loreItem(nameItem(XMaterial.COMMAND_BLOCK.parseItem(), UltimateShops.prefix + "Shop"), Arrays.asList(name, convertToInvisibleString(shop.getLocation().getWorld().getName() + "," + shop.getLocation().getBlockX() + "," + shop.getLocation().getBlockY() + "," + shop.getLocation().getBlockZ()), ChatColor.GRAY + "Selling: " + shop.getItem().getType().name(), "Buy " + shop.getStack() + " for $" + shop.getBuyPrice()));
            }
            return loreItem(nameItem(XMaterial.COMMAND_BLOCK.parseItem(), UltimateShops.prefix + "Shop"), Arrays.asList(name, convertToInvisibleString(shop.getLocation().getWorld().getName() + "," + shop.getLocation().getBlockX() + "," + shop.getLocation().getBlockY() + "," + shop.getLocation().getBlockZ()), ChatColor.GRAY + "Selling: " + shop.getItem().getType().name(), "Buy " + shop.getStack() + " for $" + shop.getBuyPrice(), "Sell " + shop.getStack() + " for $" + shop.getSellPrice()));
        }
    }
    
    public static double round(final double num, final int decimals) {
        BigDecimal bd = new BigDecimal(Double.toString(num));
        bd = bd.setScale(decimals, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static ItemStack createVendorNoteItem(final UShop shop) {
        final ItemStack noteItem = nameItem(XMaterial.OAK_SIGN.parseItem(), ChatColor.GOLD + "Note from Seller:");
        if (shop.getVendorNote() == null) {
            shop.setVendorNote("");
        }
        if (shop.getVendorNote().equals("")) {
            if (shop.getBuyPrice() > 0 && shop.getSellPrice() == 0) {
                loreItem(noteItem, Arrays.asList("Enjoy!"));
            }
            else if (shop.getSellPrice() > 0 && shop.getBuyPrice() == 0) {
                loreItem(noteItem, Arrays.asList("Take My Money!"));
            }
            else {
                loreItem(noteItem, Arrays.asList("Let's do business!"));
            }
        }
        else {
            if (shop.getVendorNote().length() > 96) {
                shop.setVendorNote(shop.getVendorNote().substring(0, 95));
            }
            while (shop.getVendorNote().length() < 96) {
                shop.setVendorNote(shop.getVendorNote() + " ");
            }
            final String line1 = ChatColor.translateAlternateColorCodes('&', shop.getVendorNote().substring(0, 31));
            final String line2 = ChatColor.translateAlternateColorCodes('&', shop.getVendorNote().substring(31, 63));
            final String line3 = ChatColor.translateAlternateColorCodes('&', shop.getVendorNote().substring(63, 95));
            loreItem(noteItem, Arrays.asList(line1, line2, line3));
        }
        return noteItem;
    }
    
    public static int countItemsInInventory(final Inventory inventory, final ItemStack item) {
        int total = 0;
        for (int counter = 0; counter < 36; ++counter) {
            if (inventory.getItem(counter) != null) {
                final ItemStack item2 = item.clone();
                item2.setAmount(1);
                final ItemStack item3 = inventory.getItem(counter).clone();
                item3.setAmount(1);
                if (item2.equals((Object)item3)) {
                    total += inventory.getItem(counter).getAmount();
                }
            }
        }
        return total;
    }
    
    public static int getAvailableItemSlots(final Inventory inventory, final ItemStack item) {
        int total = 0;
        for (int counter = 0; counter < 36; ++counter) {
            if (inventory.getItem(counter) == null) {
                total += item.getMaxStackSize();
            }
            else {
                final ItemStack item2 = item.clone();
                item2.setAmount(1);
                final ItemStack item3 = inventory.getItem(counter).clone();
                item3.setAmount(1);
                if (item2.equals((Object)item3)) {
                    total += item.getMaxStackSize() - inventory.getItem(counter).getAmount();
                }
            }
        }
        return total;
    }
    
    public static void deposit(final UShop shop, final Player player, int amount) {
        for (int counter = 0; counter < 36; ++counter) {
            if (player.getInventory().getItem(counter) != null) {
                final ItemStack item1 = player.getInventory().getItem(counter).clone();
                item1.setAmount(1);
                final ItemStack item2 = shop.getItem().clone();
                item2.setAmount(1);
                if (item1.equals((Object)item2)) {
                    if (amount >= shop.getItem().getMaxStackSize()) {
                        amount -= player.getInventory().getItem(counter).getAmount();
                        shop.setAmount(shop.getAmount() + player.getInventory().getItem(counter).getAmount());
                        player.getInventory().setItem(counter, (ItemStack)null);
                    }
                    else {
                        if (amount < player.getInventory().getItem(counter).getAmount()) {
                            shop.setAmount(shop.getAmount() + amount);
                            player.getInventory().getItem(counter).setAmount(player.getInventory().getItem(counter).getAmount() - amount);
                            amount = 0;
                            break;
                        }
                        amount -= player.getInventory().getItem(counter).getAmount();
                        shop.setAmount(shop.getAmount() + player.getInventory().getItem(counter).getAmount());
                        player.getInventory().setItem(counter, (ItemStack)null);
                    }
                }
            }
        }
    }
    
    public static void withdraw(final UShop shop, final Player player, final int am) {
        int amount = am;
        if (shop.getAmount() <= 0) {
            return;
        }
        for (int counter = 0; counter < 36; ++counter) {
            if (amount == 0) {
                break;
            }
            if (player.getInventory().getItem(counter) == null) {
                if (amount < shop.getItem().getMaxStackSize()) {
                    final ItemStack item = shop.getItem().clone();
                    item.setAmount(amount);
                    shop.setAmount(shop.getAmount() - amount);
                    amount = 0;
                    player.getInventory().setItem(counter, item);
                    break;
                }
                final ItemStack item = shop.getItem().clone();
                item.setAmount(shop.getItem().getMaxStackSize());
                amount -= shop.getItem().getMaxStackSize();
                shop.setAmount(shop.getAmount() - shop.getItem().getMaxStackSize());
                player.getInventory().setItem(counter, item);
            }
            else {
                final ItemStack item2 = player.getInventory().getItem(counter).clone();
                item2.setAmount(1);
                final ItemStack item3 = shop.getItem().clone();
                item3.setAmount(1);
                if (item2.equals((Object)item3) && player.getInventory().getItem(counter).getAmount() < shop.getItem().getMaxStackSize()) {
                    if (amount + player.getInventory().getItem(counter).getAmount() > shop.getItem().getMaxStackSize()) {
                        final int leftOver = shop.getItem().getMaxStackSize() - player.getInventory().getItem(counter).getAmount();
                        shop.setAmount(shop.getAmount() - leftOver);
                        amount -= leftOver;
                        player.getInventory().getItem(counter).setAmount(shop.getItem().getMaxStackSize());
                    }
                    else {
                        player.getInventory().getItem(counter).setAmount(player.getInventory().getItem(counter).getAmount() + amount);
                        shop.setAmount(shop.getAmount() - amount);
                        amount = 0;
                    }
                }
            }
        }
    }
    
    public static UShop getShopLookingAt(final Map<String, UShop> shops, final Player player) {
        final Block block = player.getTargetBlock((Set)Utilities.TRANSPARENT, 120);
        return shops.get(toLocString(block.getLocation()));
    }
    
    public static int purge(final Map<String, UShop> shops, final String prefix, final boolean debug, final int hours) {
        final ArrayList<UShop> shopsToDestroy = new ArrayList<UShop>();
        final long currentTime = System.currentTimeMillis();
        for (final UShop shop : shops.values()) {
            final int hoursInactive = (int)((currentTime - shop.getLastActive()) / 3600000L);
            if (hoursInactive > hours && shop.getAmount() == 0) {
                shopsToDestroy.add(shop);
            }
        }
        int shopsPurged = 0;
        for (final UShop shop2 : shopsToDestroy) {
            if (debug) {
                Bukkit.getLogger().info(prefix + "Purged old shop, located at: " + toLocString(shop2.getLocation()));
            }
            shop2.destroy(false);
            ++shopsPurged;
        }
        return shopsPurged;
    }
    
    public static int countShopsOwned(final Map<String, UShop> shops, final Player player) {
        int shopsOwned = 0;
        for (final UShop shop : shops.values()) {
            if (shop.getVendor().getUniqueId().equals(player.getUniqueId())) {
                ++shopsOwned;
            }
        }
        return shopsOwned;
    }
    
    public static void writeLog(final Plugin plugin, String message) {
        final Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = new Date();
        final String dateString = formatter.format(date);
        final Format formatter2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String dateString2 = formatter2.format(date);
        final Path pathToFile = Paths.get(plugin.getDataFolder() + File.separator + "logs" + File.separator + dateString + ".txt", new String[0]);
        Label_0163: {
            if (Files.exists(pathToFile.getParent(), new LinkOption[0])) {
                if (Files.exists(pathToFile, new LinkOption[0])) {
                    break Label_0163;
                }
            }
            try {
                Files.createDirectories(pathToFile.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
                Files.createFile(pathToFile, (FileAttribute<?>[])new FileAttribute[0]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        message = "[" + dateString2 + "] " + message + "\n";
        try {
            Files.write(pathToFile, message.getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static File loadResource(final Plugin plugin, final String resource) {
        final File folder = plugin.getDataFolder();
        if (!folder.exists()) {
            folder.mkdir();
        }
        final File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (final InputStream in = plugin.getResource(resource);
                     final OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }
    
    public static String joinStringList(final List<String> strs, final String seperator) {
        String str = "";
        for (final String strr : strs) {
            str = str + seperator + strr;
        }
        if (strs.size() > 0) {
            str = str.substring(1);
        }
        return str;
    }
    
    public static void playSound(final ActionSound sound, final Player player) {
        switch (sound) {
            case OPEN: {
                Sound.CHEST_OPEN.playSound(player);
                break;
            }
            case MODIFY: {
                Sound.ANVIL_USE.playSound(player);
                break;
            }
            case SELECT: {
                if (Version.getVersion().isBiggerThan(Version.v1_8)) {
                    player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    break;
                }
                Sound.LEVEL_UP.playSound(player);
                break;
            }
            case CLICK: {
                Sound.CLICK.playSound(player);
                break;
            }
            case POP: {
                Sound.CHICKEN_EGG_POP.playSound(player);
                break;
            }
            case BREAK: {
                Sound.ANVIL_LAND.playSound(player);
                break;
            }
            case ERROR: {
                if (Version.getVersion().isBiggerThan(Version.v1_11)) {
                    player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PARROT_IMITATE_SHULKER, 1.0f, 1.0f);
                    break;
                }
                Sound.BAT_DEATH.playSound(player);
                break;
            }
        }
    }
    
    public static boolean isDisplayItem(final Item i) {
        return i.getItemStack().hasItemMeta() && i.getItemStack().getItemMeta().hasLore() && i.getItemStack().getItemMeta().getLore().get(0).contains("Display Item!");
    }
    
    static {
        TRANSPARENT = EnumSet.of(XMaterial.AIR.parseMaterial(), XMaterial.BLACK_CARPET.parseMaterial(), XMaterial.BLUE_CARPET.parseMaterial(), XMaterial.BROWN_CARPET.parseMaterial(), XMaterial.CYAN_CARPET.parseMaterial(), XMaterial.GRAY_CARPET.parseMaterial(), XMaterial.GREEN_CARPET.parseMaterial(), XMaterial.LIGHT_BLUE_CARPET.parseMaterial(), XMaterial.LIME_CARPET.parseMaterial(), XMaterial.MAGENTA_CARPET.parseMaterial(), XMaterial.ORANGE_CARPET.parseMaterial(), XMaterial.PINK_CARPET.parseMaterial(), XMaterial.PURPLE_CARPET.parseMaterial(), XMaterial.RED_CARPET.parseMaterial(), XMaterial.WHITE_CARPET.parseMaterial(), XMaterial.YELLOW_CARPET.parseMaterial());
        Utilities.obj = null;
    }
}
