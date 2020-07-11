// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.helpers;

import org.apache.commons.lang.WordUtils;
import thirtyvirus.ultimateshops.multiversion.API;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.inventory.meta.BookMeta;
import net.milkbowl.vault.economy.Economy;
import thirtyvirus.ultimateshops.multiversion.XMaterial;
import org.bukkit.entity.Player;
import thirtyvirus.ultimateshops.shops.UShop;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import thirtyvirus.ultimateshops.UltimateShops;
import org.bukkit.inventory.Inventory;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public final class MenuUtilities
{
    private static final ItemStack CLICK_BELOW;
    private static final ItemStack CHANGE_RATE;
    private static final ItemStack CONFIRM;
    private static final ItemStack CANCEL;
    private static final ItemStack CHANGE_RATE_VENDOR;
    private static final ItemStack MODIFY_SHOP;
    private static final ItemStack VIEW_AS_CUSTOMER;
    private static final ItemStack DESTROY_SHOP;
    public static final ItemStack INVISIBLE_ITEM;
    private static MenuUtilities obj;
    
    private MenuUtilities() {
    }
    
    public static MenuUtilities objectCreationMethod() {
        if (MenuUtilities.obj == null) {
            MenuUtilities.obj = new MenuUtilities();
        }
        return MenuUtilities.obj;
    }
    
    public static final Inventory createShopGUI(final Location location) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, UltimateShops.prefix + "New");
        for (int i = 0; i < 9; ++i) {
            inv.setItem(i, Utilities.loreItem(MenuUtilities.CLICK_BELOW.clone(), Arrays.asList(ChatColor.DARK_PURPLE + "New Shop", ChatColor.GRAY + Utilities.toLocString(location))));
        }
        return inv;
    }
    
    public static final Inventory modifyShopGUI(final UShop shop, final Player vendor) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, UltimateShops.prefix + "Modify");
        if (shop.getStack() != 0) {
            inv.setItem(0, shop.getItem());
        }
        else {
            final ItemStack i = shop.getItem().clone();
            i.setAmount(1);
            inv.setItem(0, i);
        }
        inv.setItem(1, Utilities.createIdentifier(shop));
        final String buyPriceString = "$" + shop.getBuyPrice() + ".00";
        final String sellPriceString = "$" + shop.getSellPrice() + ".00";
        inv.setItem(2, Utilities.loreItem(Utilities.nameItem(XMaterial.GREEN_STAINED_GLASS_PANE.parseItem(), ChatColor.GREEN + "Buy Price"), Arrays.asList(ChatColor.GRAY + buyPriceString, "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not for sale)")));
        inv.setItem(3, Utilities.loreItem(Utilities.nameItem(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), ChatColor.RED + "Sell Price"), Arrays.asList(ChatColor.GRAY + sellPriceString, "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not buying)")));
        inv.setItem(4, Utilities.loreItem(Utilities.nameItem(XMaterial.YELLOW_STAINED_GLASS_PANE.parseItem(), ChatColor.YELLOW + "Quantity"), Arrays.asList(ChatColor.GRAY + "Amount Per Transaction: " + shop.getStack(), "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(amount of items per buy / sell)")));
        inv.setItem(5, MenuUtilities.CHANGE_RATE);
        inv.setItem(6, (ItemStack)null);
        inv.setItem(7, MenuUtilities.CONFIRM);
        inv.setItem(8, MenuUtilities.CANCEL);
        updateModifyShopGUI(shop, inv, vendor);
        return inv;
    }
    
    public static final Inventory vendorGUI(final UShop shop, final Player vendor) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, UltimateShops.prefix + "Vendor");
        inv.setItem(0, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(1, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(2, MenuUtilities.CHANGE_RATE_VENDOR);
        inv.setItem(3, MenuUtilities.INVISIBLE_ITEM);
        if (shop.getStack() != 0) {
            inv.setItem(4, Utilities.stackItem(shop.getItem(), shop.getStack()));
        }
        else {
            inv.setItem(4, Utilities.stackItem(shop.getItem(), 1));
        }
        inv.setItem(5, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(6, Utilities.createIdentifier(shop));
        inv.setItem(7, MenuUtilities.MODIFY_SHOP);
        inv.setItem(8, MenuUtilities.VIEW_AS_CUSTOMER);
        updateVendorGUI(shop, inv, vendor);
        return inv;
    }
    
    public static final Inventory customerGUI(final UShop shop, final Player customer, final Economy econ) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, UltimateShops.prefix + "Client");
        inv.setItem(6, Utilities.createIdentifier(shop));
        inv.setItem(7, Utilities.createVendorNoteItem(shop));
        inv.setItem(3, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(5, MenuUtilities.INVISIBLE_ITEM);
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
        inv.setItem(8, Utilities.loreItem(Utilities.nameItem(XMaterial.BOOKSHELF.parseMaterial(), ChatColor.GOLD + "More Info"), Arrays.asList("Location: ", ChatColor.GRAY + Utilities.toLocString(shop.getLocation()).replaceAll(",", ", "), "", "Most Recent Activity:", ChatColor.GRAY + timeString)));
        updateCustomerGUI(shop, inv, customer, econ);
        return inv;
    }
    
    public static final Inventory chestInputGUI(final String name) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, name);
        inv.setItem(0, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(1, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(2, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(3, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(4, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(5, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(6, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(7, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(8, MenuUtilities.INVISIBLE_ITEM);
        return inv;
    }
    
    public static final Inventory destroyShopGUI(final UShop shop) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, UltimateShops.prefix + "Destroy");
        inv.setItem(0, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(1, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(2, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(3, MenuUtilities.DESTROY_SHOP);
        inv.setItem(4, Utilities.createIdentifier(shop));
        inv.setItem(5, MenuUtilities.CANCEL);
        inv.setItem(6, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(7, MenuUtilities.INVISIBLE_ITEM);
        inv.setItem(8, MenuUtilities.INVISIBLE_ITEM);
        return inv;
    }
    
    public static final void vendorNoteGUI(final UShop shop, final Player vendor) {
        vendor.sendMessage(UltimateShops.prefix + ChatColor.DARK_PURPLE + "--------------------");
        vendor.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.RESET + "Have your cursor on the shop and type:");
        vendor.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.RESET + "/ushop modifyVendorNote MESSAGE");
        vendor.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "- " + ChatColor.RESET + "(96 character limit)");
        vendor.sendMessage(ChatColor.DARK_PURPLE + "------------------------------");
        vendor.closeInventory();
    }
    
    public static final void tutorialMenu(final Player player) {
        final ItemStack book = new ItemStack(XMaterial.WRITTEN_BOOK.parseMaterial());
        final BookMeta meta = (BookMeta)book.getItemMeta();
        meta.setAuthor("ThirtyVirus");
        meta.setTitle("Welcome to UltimateShops!");
        final List<String> pages = new ArrayList<String>();
        String frontPage = "";
        frontPage = addLine(frontPage, "      &7&lWelcome to:");
        frontPage = addLine(frontPage, "   &c&lUltimate&5&lShops&r");
        frontPage = addLine(frontPage, "");
        frontPage = addLine(frontPage, "This guide book will show you everything you need to know about ushops! Happy reading!");
        frontPage = addLine(frontPage, "");
        frontPage = addLine(frontPage, " - ThirtyVirus");
        frontPage = addLine(frontPage, "");
        frontPage = addLine(frontPage, "&7&lGo to the next page for info on making shops!");
        pages.add(frontPage);
        String createShopPage = "";
        createShopPage = addLine(createShopPage, "&c&lCreate a uShop&r");
        createShopPage = addLine(createShopPage, "");
        createShopPage = addLine(createShopPage, "Right click any slab with a stick... OR");
        createShopPage = addLine(createShopPage, "");
        createShopPage = addLine(createShopPage, "point your cursor at any slab and use '/ushop create'");
        createShopPage = addLine(createShopPage, "");
        createShopPage = addLine(createShopPage, "This opens the create ushop menu!");
        createShopPage = addLine(createShopPage, "");
        createShopPage = addLine(createShopPage, "&7&lNext: Maintain Shops");
        pages.add(createShopPage);
        String mainTainShopsPage = "";
        mainTainShopsPage = addLine(mainTainShopsPage, "&c&lMaintain uShops&r");
        mainTainShopsPage = addLine(mainTainShopsPage, "");
        mainTainShopsPage = addLine(mainTainShopsPage, "Right click your ushop to open the Vendor menu.");
        mainTainShopsPage = addLine(mainTainShopsPage, "");
        mainTainShopsPage = addLine(mainTainShopsPage, "Here you can deposit or withdraw items, modify your shop, or view it as a customer would!");
        mainTainShopsPage = addLine(mainTainShopsPage, "");
        mainTainShopsPage = addLine(mainTainShopsPage, "&7&lNext: Quick Deposit");
        pages.add(mainTainShopsPage);
        String quickDepositPage = "";
        quickDepositPage = addLine(quickDepositPage, "&c&lQuick Deposit&r");
        quickDepositPage = addLine(quickDepositPage, "");
        quickDepositPage = addLine(quickDepositPage, "To restock faster, you can punch your ushop while holding the item it trades!");
        quickDepositPage = addLine(quickDepositPage, "");
        quickDepositPage = addLine(quickDepositPage, "Crouching and punching empties your entire inventory of that item and stocks the ushop super quick!");
        quickDepositPage = addLine(quickDepositPage, "");
        quickDepositPage = addLine(quickDepositPage, "&7&lNext: RestockChest");
        pages.add(quickDepositPage);
        String restockChestPage = "";
        restockChestPage = addLine(restockChestPage, "&c&lRestock Chest&r");
        restockChestPage = addLine(restockChestPage, "");
        restockChestPage = addLine(restockChestPage, "Right click any chest w/ a stick to open the chest restock menu.");
        restockChestPage = addLine(restockChestPage, "");
        restockChestPage = addLine(restockChestPage, "Any of your ushops within a (default 32 block) radius will recieve items that you throw in! Shift clicking also works!");
        restockChestPage = addLine(restockChestPage, "");
        restockChestPage = addLine(restockChestPage, "&7&lNext: Modify Shop");
        pages.add(restockChestPage);
        String modifyShopPage = "";
        modifyShopPage = addLine(modifyShopPage, "&c&lModify uShop&r");
        modifyShopPage = addLine(modifyShopPage, "");
        modifyShopPage = addLine(modifyShopPage, "Click the comparator in the Vendor menu OR");
        modifyShopPage = addLine(modifyShopPage, "");
        modifyShopPage = addLine(modifyShopPage, "point your cursor at your ushop and use '/ushop modify'");
        modifyShopPage = addLine(modifyShopPage, "");
        modifyShopPage = addLine(modifyShopPage, "You can change: buyprice, sellprice, quantity, vendor note.");
        modifyShopPage = addLine(modifyShopPage, "");
        modifyShopPage = addLine(modifyShopPage, "&7&lNext: Delete Shop");
        pages.add(modifyShopPage);
        String deleteShopPage = "";
        deleteShopPage = addLine(deleteShopPage, "&c&lDelete uShop&r");
        deleteShopPage = addLine(deleteShopPage, "");
        deleteShopPage = addLine(deleteShopPage, "Right Click your ushop with a stick... OR");
        deleteShopPage = addLine(deleteShopPage, "");
        deleteShopPage = addLine(deleteShopPage, "point your cursor at your ushop and use '/ushop destroy'");
        deleteShopPage = addLine(deleteShopPage, "");
        deleteShopPage = addLine(deleteShopPage, "This opens the destroy ushop menu.  &cWARNING: &rThis cannot be undone!");
        deleteShopPage = addLine(deleteShopPage, "");
        deleteShopPage = addLine(deleteShopPage, "&7&lNext: Delete Shop");
        pages.add(deleteShopPage);
        meta.setPages((List)pages);
        book.setItemMeta((ItemMeta)meta);
        Utilities.playSound(ActionSound.CLICK, player);
        API.openBook(book, player);
    }
    
    public static final void adminTutorialMenu(final Player player) {
        final ItemStack book = new ItemStack(XMaterial.WRITTEN_BOOK.parseMaterial());
        final BookMeta meta = (BookMeta)book.getItemMeta();
        meta.setAuthor("ThirtyVirus");
        meta.setTitle("Welcome to UltimateShops!");
        final List<String> pages = new ArrayList<String>();
        String frontPage = "";
        frontPage = addLine(frontPage, "      &7&lWelcome to:");
        frontPage = addLine(frontPage, "   &c&lUltimate&5&lShops&r");
        frontPage = addLine(frontPage, "     &c~Admin Mode~&r");
        frontPage = addLine(frontPage, "This guide book will show you everything you need to know about ushops admin stuffs! Happy reading!");
        frontPage = addLine(frontPage, "");
        frontPage = addLine(frontPage, " - ThirtyVirus");
        frontPage = addLine(frontPage, "");
        frontPage = addLine(frontPage, "&7&lGo to the next page to see staff permissions!");
        pages.add(frontPage);
        String permissionsPage = "";
        permissionsPage = addLine(permissionsPage, "&c&lPermissions&r");
        permissionsPage = addLine(permissionsPage, "");
        permissionsPage = addLine(permissionsPage, "General users should get &cushops.user&r and staff should get &cushops.admin&r.");
        permissionsPage = addLine(permissionsPage, "");
        permissionsPage = addLine(permissionsPage, "Staff can use any ushop as the owner, and have special extra permissions.");
        permissionsPage = addLine(permissionsPage, "");
        permissionsPage = addLine(permissionsPage, "&7&lNext: GetItem and Admin Mode");
        pages.add(permissionsPage);
        String extraPermsPage1 = "";
        extraPermsPage1 = addLine(extraPermsPage1, "&c&lGetItem and Admin Mode&r");
        extraPermsPage1 = addLine(extraPermsPage1, "");
        extraPermsPage1 = addLine(extraPermsPage1, "Use &c/ushop getItem&r to retrieve the item a ushop sells.");
        extraPermsPage1 = addLine(extraPermsPage1, "");
        extraPermsPage1 = addLine(extraPermsPage1, "or &c/ushop toggleAdmin&r  for infinite ushop stock + no financial ties to the owner.");
        extraPermsPage1 = addLine(extraPermsPage1, "");
        extraPermsPage1 = addLine(extraPermsPage1, "&7&lNext: uShop Commands");
        pages.add(extraPermsPage1);
        String commandsPage = "";
        commandsPage = addLine(commandsPage, "&c&luShop Commands&r");
        commandsPage = addLine(commandsPage, "");
        commandsPage = addLine(commandsPage, "&2UltimateShops Premium&r allows you to put commands in your shops! buy/sell commands execute every time someone buys/sells from a ushop!");
        commandsPage = addLine(commandsPage, "");
        commandsPage = addLine(commandsPage, "&7&lNext: Command Example");
        pages.add(commandsPage);
        String commandsPage2 = "";
        commandsPage2 = addLine(commandsPage2, "&c&lCommand Example&r");
        commandsPage2 = addLine(commandsPage2, "");
        commandsPage2 = addLine(commandsPage2, "/ushop addBuyCommand kill customer");
        commandsPage2 = addLine(commandsPage2, "");
        commandsPage2 = addLine(commandsPage2, "This ushop will kill anyone who buys items from it! :O");
        commandsPage2 = addLine(commandsPage2, "");
        commandsPage2 = addLine(commandsPage2, "&7&lNext: Command Placeholders");
        pages.add(commandsPage2);
        String commandsPage3 = "";
        commandsPage3 = addLine(commandsPage3, "&c&lPlaceholders&r");
        commandsPage3 = addLine(commandsPage3, "");
        commandsPage3 = addLine(commandsPage3, "ushop commands will replace keywords with special data.");
        commandsPage3 = addLine(commandsPage3, "");
        commandsPage3 = addLine(commandsPage3, "Ex: 'customer' is replaced w/ the buyer's name. Get the full list with: &c/ushops shopCommandHelp&r");
        commandsPage3 = addLine(commandsPage3, "");
        commandsPage3 = addLine(commandsPage3, "&7&lNext: Save & Reload");
        pages.add(commandsPage3);
        String saveReloadPage = "";
        saveReloadPage = addLine(saveReloadPage, "&c&lSave & Reload&r");
        saveReloadPage = addLine(saveReloadPage, "");
        saveReloadPage = addLine(saveReloadPage, "use &c/ushops save&r to save all shops to disk. All shop actions save shops so this won't be needed often.");
        saveReloadPage = addLine(saveReloadPage, "");
        saveReloadPage = addLine(saveReloadPage, "use &c/ushops reload&r to reload all shops, display items, and the config");
        saveReloadPage = addLine(saveReloadPage, "");
        saveReloadPage = addLine(saveReloadPage, "&7&lNext: Purging");
        pages.add(saveReloadPage);
        String purgePage = "";
        purgePage = addLine(purgePage, "&c&lPurging&r");
        purgePage = addLine(purgePage, "");
        purgePage = addLine(purgePage, "Mass delete shops! args: t=max time last used (h=hour, d=day, m=month), r=radius, p=player, e=empty shops only");
        purgePage = addLine(purgePage, "");
        purgePage = addLine(purgePage, "r, p, e are optional. Default: r=infinite, p=all players, e=true");
        purgePage = addLine(purgePage, "");
        purgePage = addLine(purgePage, "&7&lNext: Examples");
        pages.add(purgePage);
        String purgePage2 = "";
        purgePage2 = addLine(purgePage2, "&c&lPurge Examples&r");
        purgePage2 = addLine(purgePage2, "");
        purgePage2 = addLine(purgePage2, "1: &c/ushops purge t:1h r:10 p:notch e:true&r");
        purgePage2 = addLine(purgePage2, "");
        purgePage2 = addLine(purgePage2, "2: &c/ushops purge t:2d&r");
        purgePage2 = addLine(purgePage2, "");
        purgePage2 = addLine(purgePage2, "3: &c/ushops purge p:gb80 t:12m e:false&r");
        purgePage2 = addLine(purgePage2, "");
        purgePage2 = addLine(purgePage2, "4: &c/ushops purge r:100 t:12m&r");
        purgePage2 = addLine(purgePage2, "");
        purgePage2 = addLine(purgePage2, "&7&lNext: Config");
        pages.add(purgePage2);
        String configPage = "";
        configPage = addLine(configPage, "&c&lConfig Changes&r");
        configPage = addLine(configPage, "");
        configPage = addLine(configPage, "In the config you may change settings like chat prefix &2[Premium Only]&r, and certain chat messages. You can also change the chest restock radius, max shop capacity, and auto-purge.");
        configPage = addLine(configPage, "");
        configPage = addLine(configPage, "&7&lNext: Debug Mode");
        pages.add(configPage);
        String debugPage = "";
        debugPage = addLine(debugPage, "&c&lDebug Mode&r");
        debugPage = addLine(debugPage, "");
        debugPage = addLine(debugPage, "Enabling debug mode in the config lets staff w/ console access see more info for ushops events:");
        debugPage = addLine(debugPage, "");
        debugPage = addLine(debugPage, "Loading shop chunks, making, breaking, modifying shops, saving, loading shops, and transactions, commands.");
        pages.add(debugPage);
        meta.setPages((List)pages);
        book.setItemMeta((ItemMeta)meta);
        Utilities.playSound(ActionSound.CLICK, player);
        API.openBook(book, player);
    }
    
    public static final String addLine(final String page, final String line) {
        return page + ChatColor.translateAlternateColorCodes('&', line) + "\n";
    }
    
    public static final void updateVendorGUI(final UShop uShop, final Inventory inventory, final Player player) {
        final String capitalizeFully = WordUtils.capitalizeFully(uShop.getItem().getType().name().replace('_', ' ').toLowerCase());
        final double n = uShop.getAmount() / (double)uShop.getItem().getMaxStackSize();
        if (uShop.getAdmin()) {
            uShop.setAmount((int)4.20696934E9f);
        }
        inventory.setItem(0, Utilities.loreItem(Utilities.nameItem(XMaterial.CHEST_MINECART.parseMaterial(), ChatColor.GOLD + "Deposit '" + capitalizeFully + "'"), Arrays.asList(ChatColor.GRAY + "+Shift for Bulk Deposit", "In Stock: " + uShop.getAmount(), "or " + String.format("%.2f", n) + " stacks")));
        inventory.setItem(1, Utilities.loreItem(Utilities.nameItem(XMaterial.HOPPER_MINECART.parseMaterial(), ChatColor.GOLD + "Withdraw '" + capitalizeFully + "'"), Arrays.asList(ChatColor.GRAY + "+Shift for Bulk Withdraw", "In Stock: " + uShop.getAmount(), "or " + String.format("%.2f", n) + " stacks")));
    }
    
    public static final void updateModifyShopGUI(final UShop shop, final Inventory inventory, final Player player) {
        inventory.setItem(6, Utilities.createVendorNoteItem(shop));
    }
    
    public static final void updateCustomerGUI(final UShop shop, final Inventory inventory, final Player player, final Economy econ) {
        String name = WordUtils.capitalizeFully(shop.getItem().getType().name().replace('_', ' ').toLowerCase());
        final double stacks = shop.getAmount() / (double)shop.getItem().getMaxStackSize();
        if (shop.getAdmin()) {
            shop.setAmount(Integer.MAX_VALUE);
        }
        final String moneyString = Utilities.toCommadNumber(String.format("%.2f", econ.getBalance(Bukkit.getOfflinePlayer(player.getUniqueId()))));
        inventory.setItem(2, Utilities.loreItem(Utilities.nameItem(XMaterial.PAPER.parseMaterial(), ChatColor.GOLD + "Current Funds"), Arrays.asList(ChatColor.GRAY + "Funds: " + ChatColor.GREEN + "$" + moneyString)));
        if (shop.getItem().getItemMeta().getDisplayName() != null && shop.getItem().getItemMeta().getDisplayName() != "") {
            name = shop.getItem().getItemMeta().getDisplayName();
        }
        if (shop.getStack() == 1) {
            if (shop.getBuyPrice() != 0 && UltimateShops.buyItems) {
                inventory.setItem(0, Utilities.loreItem(Utilities.nameItem(XMaterial.GOLD_INGOT.parseMaterial(), ChatColor.GOLD + "Buy '" + name + "'"), Arrays.asList(ChatColor.GRAY + "Buy for: $" + shop.getBuyPrice(), "In Stock: " + shop.getAmount(), "or " + String.format("%.2f", stacks) + " stacks")));
            }
            if (shop.getSellPrice() != 0 && UltimateShops.sellItems) {
                inventory.setItem(1, Utilities.loreItem(Utilities.nameItem(XMaterial.IRON_INGOT.parseMaterial(), ChatColor.GOLD + "Sell '" + name + "'"), Arrays.asList(ChatColor.GRAY + "Sell for: $" + shop.getSellPrice(), "In Stock: " + shop.getAmount(), "or " + String.format("%.2f", stacks) + " stacks")));
            }
            inventory.setItem(4, shop.getItem());
        }
        else {
            if (shop.getBuyPrice() != 0 && UltimateShops.buyItems) {
                inventory.setItem(0, Utilities.loreItem(Utilities.nameItem(XMaterial.GOLD_INGOT.parseMaterial(), ChatColor.GOLD + "Buy '" + name + "' * " + shop.getStack()), Arrays.asList(ChatColor.GRAY + "Buy for: $" + shop.getBuyPrice(), "In Stock: " + shop.getAmount(), "or " + String.format("%.2f", stacks) + " stacks")));
            }
            if (shop.getSellPrice() != 0 && UltimateShops.sellItems) {
                inventory.setItem(1, Utilities.loreItem(Utilities.nameItem(XMaterial.IRON_INGOT.parseMaterial(), ChatColor.GOLD + "Sell '" + name + "' * " + shop.getStack()), Arrays.asList(ChatColor.GRAY + "Sell for: $" + shop.getSellPrice(), "In Stock: " + shop.getAmount(), "or " + String.format("%.2f", stacks) + " stacks")));
            }
            if (shop.getStack() != 0) {
                inventory.setItem(4, Utilities.stackItem(shop.getItem(), shop.getStack()));
            }
            else {
                inventory.setItem(4, Utilities.stackItem(shop.getItem(), 1));
            }
        }
    }
    
    static {
        CLICK_BELOW = Utilities.nameItem(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), ChatColor.RED + "Click Item Below");
        CHANGE_RATE = Utilities.loreItem(Utilities.nameItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem(), ChatColor.BLUE + "Change Rate"), Arrays.asList(ChatColor.GRAY + "Amount Per Click: 1", "Change the rate of", "previous 3 buttons"));
        CONFIRM = Utilities.nameItem(XMaterial.NETHER_STAR.parseItem(), ChatColor.GREEN + "Confirm");
        CANCEL = Utilities.nameItem(XMaterial.BARRIER.parseItem(), ChatColor.RED + "Cancel");
        CHANGE_RATE_VENDOR = Utilities.loreItem(Utilities.nameItem(XMaterial.MINECART.parseItem(), ChatColor.GOLD + "Change Rate"), Arrays.asList(ChatColor.GRAY + "Amount Per Click: 1", ChatColor.GRAY + "Change Transfer Rate", "", "Left Click to Increase", "Right Click to Decrease"));
        MODIFY_SHOP = Utilities.nameItem(XMaterial.COMPARATOR.parseItem(), ChatColor.GOLD + "Modify Shop");
        VIEW_AS_CUSTOMER = Utilities.nameItem(XMaterial.OAK_SIGN.parseItem(), ChatColor.GOLD + "View as Customer");
        DESTROY_SHOP = Utilities.nameItem(XMaterial.LIME_STAINED_GLASS_PANE.parseItem(), ChatColor.GREEN + "Destroy Shop");
        INVISIBLE_ITEM = Utilities.nameItem(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE.parseItem(), " ");
        MenuUtilities.obj = null;
    }
}
