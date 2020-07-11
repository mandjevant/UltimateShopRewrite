// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.events.inventory;

import org.apache.commons.lang.WordUtils;
import org.bukkit.event.inventory.ClickType;
import java.util.Iterator;
import org.bukkit.plugin.Plugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import thirtyvirus.ultimateshops.shops.UShop;
import org.bukkit.Material;
import java.util.Arrays;
import org.bukkit.ChatColor;
import thirtyvirus.ultimateshops.multiversion.XMaterial;
import thirtyvirus.ultimateshops.helpers.MenuUtilities;
import thirtyvirus.ultimateshops.helpers.Utilities;
import thirtyvirus.ultimateshops.helpers.ActionSound;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import thirtyvirus.ultimateshops.UltimateShops;
import org.bukkit.event.Listener;

public class InventoryClickHandler implements Listener
{
    private UltimateShops main;
    
    public InventoryClickHandler(final UltimateShops main) {
        this.main = null;
        this.main = main;
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final Inventory inventory = event.getInventory();
        final ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        final Player player = (Player)event.getWhoClicked();
        if (item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).contains("Display Item!")) {
            inventory.remove(item);
        }
        if (event.getView().getTitle().contains(UltimateShops.prefix + "New")) {
            if (event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.HOTBAR_SWAP) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                player.updateInventory();
                return;
            }
            this.createShopGUI(event, inventory, item, player);
        }
        else if (event.getView().getTitle().contains(UltimateShops.prefix + "Vendor")) {
            if (event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || event.getAction() == InventoryAction.HOTBAR_SWAP) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                player.updateInventory();
                return;
            }
            this.vendorGUI(event, inventory, item, player);
        }
        else if (event.getView().getTitle().contains(UltimateShops.prefix + "Modify")) {
            if (event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.HOTBAR_SWAP) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                player.updateInventory();
                return;
            }
            this.modifyShopGUI(event, inventory, item, player);
        }
        else if (event.getView().getTitle().contains(UltimateShops.prefix + "Client")) {
            if (event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.HOTBAR_SWAP) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                player.updateInventory();
                return;
            }
            this.customerGUI(event, inventory, item, player);
        }
        else if (event.getView().getTitle().contains(UltimateShops.prefix + "Restock")) {
            if (event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.HOTBAR_SWAP) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                player.updateInventory();
                return;
            }
            this.chestInputGUI(event, inventory, item, player);
        }
        else {
            if (!event.getView().getTitle().contains(UltimateShops.prefix + "Destroy")) {
                return;
            }
            if (event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.HOTBAR_SWAP) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                player.updateInventory();
                return;
            }
            this.destroyShopGUI(event, inventory, item, player);
        }
    }
    
    private void createShopGUI(final InventoryClickEvent event, final Inventory inventory, final ItemStack item, final Player player) {
        event.setCancelled(true);
        player.updateInventory();
        if (event.getRawSlot() <= 8) {
            if (inventory.getItem(event.getRawSlot()) == null) {
                return;
            }
            if (inventory.getItem(event.getRawSlot()).getItemMeta().getDisplayName().contains("Click Item Below")) {
                return;
            }
        }
        else if (player.getInventory().getItem(event.getSlot()) == null) {
            return;
        }
        final String[] locationParts = inventory.getItem(7).getItemMeta().getLore().get(1).substring(2).split(",");
        final Location location = new Location(Bukkit.getWorld(locationParts[0]), (double)Integer.parseInt(locationParts[1]), (double)Integer.parseInt(locationParts[2]), (double)Integer.parseInt(locationParts[3]));
        if (event.getRawSlot() > 8) {
            final ItemStack i = player.getInventory().getItem(event.getSlot()).clone();
            i.setAmount(1);
            Utilities.playSound(ActionSound.CLICK, player);
            inventory.setItem(0, i);
            int buyPrice = 0;
            int sellPrice = 0;
            int quantity = 0;
            if (this.main.getSuggestedBuyPrices().get(inventory.getItem(0).getType()) != null) {
                buyPrice = this.main.getSuggestedBuyPrices().get(inventory.getItem(0).getType());
            }
            if (this.main.getSuggestedSellPrices().get(inventory.getItem(0).getType()) != null) {
                sellPrice = this.main.getSuggestedSellPrices().get(inventory.getItem(0).getType());
            }
            if (this.main.getSuggestedSellPrices().get(inventory.getItem(0).getType()) != null) {
                quantity = this.main.getSuggestedQuantity().get(inventory.getItem(0).getType());
            }
            if (buyPrice == 0) {
                buyPrice = 1;
            }
            if (sellPrice == 0) {
                sellPrice = 1;
            }
            if (quantity == 0) {
                quantity = 1;
            }
            final String buyPriceString = "$" + buyPrice + ".00";
            final String sellPriceString = "$" + sellPrice + ".00";
            inventory.setItem(1, MenuUtilities.INVISIBLE_ITEM);
            if (UltimateShops.buyItems) {
                inventory.setItem(2, Utilities.loreItem(Utilities.nameItem(XMaterial.GREEN_STAINED_GLASS_PANE.parseItem(), ChatColor.GREEN + "Buy Price"), Arrays.asList(ChatColor.GRAY + buyPriceString, "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not for sale)")));
            }
            else {
                inventory.setItem(2, MenuUtilities.INVISIBLE_ITEM);
            }
            if (UltimateShops.sellItems) {
                inventory.setItem(3, Utilities.loreItem(Utilities.nameItem(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), ChatColor.RED + "Sell Price"), Arrays.asList(ChatColor.GRAY + sellPriceString, "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not buying)")));
            }
            else {
                inventory.setItem(3, MenuUtilities.INVISIBLE_ITEM);
            }
            if (UltimateShops.buyItems || UltimateShops.sellItems) {
                inventory.setItem(4, Utilities.loreItem(Utilities.nameItem(XMaterial.YELLOW_STAINED_GLASS_PANE.parseItem(), ChatColor.YELLOW + "Quantity"), Arrays.asList(ChatColor.GRAY + "Amount Per Transaction: " + quantity, "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(amount of items per buy / sell)")));
                inventory.setItem(5, Utilities.loreItem(Utilities.nameItem(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem(), ChatColor.BLUE + "Change Rate"), Arrays.asList(ChatColor.GRAY + "Amount Per Click: 1", "Change the rate of", "previous 3 buttons")));
            }
            else {
                inventory.setItem(4, MenuUtilities.INVISIBLE_ITEM);
                inventory.setItem(5, MenuUtilities.INVISIBLE_ITEM);
            }
            inventory.setItem(6, MenuUtilities.INVISIBLE_ITEM);
            inventory.setItem(7, Utilities.loreItem(Utilities.nameItem(Material.NETHER_STAR, ChatColor.GREEN + "Confirm"), Arrays.asList(ChatColor.DARK_PURPLE + "New Shop", inventory.getItem(7).getItemMeta().getLore().get(1))));
            inventory.setItem(8, Utilities.nameItem(Material.BARRIER, ChatColor.RED + "Cancel"));
        }
        int rateChange = 1;
        if (UltimateShops.buyItems || UltimateShops.sellItems) {
            final String[] stringSplit = inventory.getItem(5).getItemMeta().getLore().get(0).split(": ");
            rateChange = Integer.parseInt(stringSplit[1]);
        }
        if (event.getRawSlot() == 2 && UltimateShops.buyItems) {
            int buyPrice = Integer.parseInt(inventory.getItem(2).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", "").substring(0, inventory.getItem(2).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", "").length() - 3));
            if (event.isLeftClick()) {
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempPrice = buyPrice + rateChange;
                final String tempPrice2 = (int)tempPrice + ".00";
                Utilities.loreItem(inventory.getItem(2), Arrays.asList(ChatColor.GRAY + "$" + Utilities.toCommadNumber(tempPrice2), "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not for sale)"));
            }
            if (event.isRightClick()) {
                if (buyPrice == 0) {
                    return;
                }
                if (buyPrice - rateChange < 0) {
                    buyPrice = 0;
                    rateChange = 0;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempPrice = buyPrice - rateChange;
                final String tempPrice2 = (int)tempPrice + ".00";
                Utilities.loreItem(inventory.getItem(2), Arrays.asList(ChatColor.GRAY + "$" + Utilities.toCommadNumber(tempPrice2), "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not for sale)"));
            }
        }
        else if (event.getRawSlot() == 3 && UltimateShops.sellItems) {
            int sellPrice2 = Integer.parseInt(inventory.getItem(3).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", "").substring(0, inventory.getItem(3).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", "").length() - 3));
            if (event.isLeftClick()) {
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempPrice = sellPrice2 + rateChange;
                final String tempPrice2 = (int)tempPrice + ".00";
                Utilities.loreItem(inventory.getItem(3), Arrays.asList(ChatColor.GRAY + "$" + Utilities.toCommadNumber(tempPrice2), "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not buying)"));
            }
            if (event.isRightClick()) {
                if (sellPrice2 == 0) {
                    return;
                }
                if (sellPrice2 - rateChange < 0) {
                    sellPrice2 = 0;
                    rateChange = 0;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempPrice = sellPrice2 - rateChange;
                final String tempPrice2 = (int)tempPrice + ".00";
                Utilities.loreItem(inventory.getItem(3), Arrays.asList(ChatColor.GRAY + "$" + Utilities.toCommadNumber(tempPrice2), "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not buying)"));
            }
        }
        else if (event.getRawSlot() == 4 && (UltimateShops.buyItems || UltimateShops.sellItems)) {
            int quantity2 = Integer.parseInt(inventory.getItem(4).getItemMeta().getLore().get(0).split(": ")[1].replaceAll(",", ""));
            if (event.isLeftClick()) {
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempAmount = quantity2 + rateChange;
                String commad = Utilities.toCommadNumber(String.format("%.2f", tempAmount));
                commad = commad.substring(0, commad.length() - 3);
                Utilities.loreItem(inventory.getItem(4), Arrays.asList(ChatColor.GRAY + "Amount Per Transaction: " + commad, "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(amount of items per buy / sell)"));
            }
            if (event.isRightClick()) {
                int minimumQuantity = 1;
                if (player.hasPermission("ushops.admin")) {
                    minimumQuantity = 0;
                }
                if (quantity2 == minimumQuantity) {
                    return;
                }
                if (quantity2 - rateChange < minimumQuantity) {
                    quantity2 = minimumQuantity;
                    rateChange = 0;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempAmount2 = quantity2 - rateChange;
                String commad2 = Utilities.toCommadNumber(String.format("%.2f", tempAmount2));
                commad2 = commad2.substring(0, commad2.length() - 3);
                Utilities.loreItem(inventory.getItem(4), Arrays.asList(ChatColor.GRAY + "Amount Per Transaction: " + commad2, "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(amount of items per buy / sell)"));
            }
        }
        else if (event.getRawSlot() == 5 && (UltimateShops.buyItems || UltimateShops.sellItems)) {
            if (event.isLeftClick()) {
                if (rateChange == 2048) {
                    return;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                Utilities.loreItem(inventory.getItem(5), Arrays.asList(ChatColor.GRAY + "Amount Per Click: " + Integer.toString(rateChange * 2), "Change the rate of", "previous 3 buttons"));
            }
            if (event.isRightClick()) {
                if (rateChange == 1) {
                    return;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                Utilities.loreItem(inventory.getItem(5), Arrays.asList(ChatColor.GRAY + "Amount Per Click: " + Integer.toString(rateChange / 2), "Change the rate of", "previous 3 buttons"));
            }
        }
        else if (event.getRawSlot() == 7) {
            int buyPrice = 0;
            if (UltimateShops.buyItems) {
                buyPrice = (int)Double.parseDouble(inventory.getItem(2).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", ""));
            }
            int sellPrice = 0;
            if (UltimateShops.sellItems) {
                sellPrice = (int)Double.parseDouble(inventory.getItem(3).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", ""));
            }
            int quantity = 1;
            if (UltimateShops.buyItems || UltimateShops.sellItems) {
                quantity = Integer.parseInt(inventory.getItem(4).getItemMeta().getLore().get(0).split(": ")[1]);
            }
            final UShop newShop = new UShop(this.main, Bukkit.getOfflinePlayer(player.getUniqueId()), location, inventory.getItem(0), buyPrice, sellPrice, quantity, 0, "", System.currentTimeMillis(), false, "", "", "");
            this.main.getShops().put(Utilities.toLocString(location), newShop);
            this.main.getShopList().add(newShop);
            this.main.saveShops(this.main.getShops());
            this.main.getShopChunks().computeIfAbsent(Utilities.toChunkString(location.getChunk()), k -> new ArrayList());
            this.main.getShopChunks().get(Utilities.toChunkString(location.getChunk())).add(this.main.getShops().get(Utilities.toLocString(location)));
            Utilities.playSound(ActionSound.MODIFY, player);
            Utilities.informPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedString(this.main.phrases.get("shop-creation-message"), newShop, player, 0, this.main.getEcon(), ChatColor.GRAY)));
            player.closeInventory();
        }
        else if (event.getRawSlot() == 8) {
            Utilities.playSound(ActionSound.ERROR, player);
            player.closeInventory();
        }
    }
    
    private void vendorGUI(final InventoryClickEvent event, final Inventory inventory, final ItemStack item, final Player player) {
        event.setCancelled(true);
        player.updateInventory();
        final UShop shop = UShop.fromItem(this.main, inventory.getItem(6));
        final String[] stringSplit = inventory.getItem(2).getItemMeta().getLore().get(0).split(": ");
        int rateChange = Integer.parseInt(stringSplit[1]);
        if (event.getRawSlot() == 0) {
            final int shopSpaceLeft = this.main.getMaxShopStacks() * 64 - shop.getAmount();
            if (shop.getAdmin()) {
                Utilities.playSound(ActionSound.ERROR, player);
                return;
            }
            if (shopSpaceLeft == 0) {
                if (this.main.informUserShopOutOfSpace) {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedString(this.main.phrases.get("shop-out-of-space-message"), shop, player, shop.getBuyPrice(), this.main.getEcon(), ChatColor.RED)));
                }
                return;
            }
            if (!event.isShiftClick()) {
                final int numItem = Utilities.countItemsInInventory((Inventory)player.getInventory(), shop.getItem());
                if (numItem >= rateChange) {
                    if (shopSpaceLeft >= rateChange) {
                        Utilities.deposit(shop, player, rateChange);
                    }
                    else {
                        Utilities.deposit(shop, player, shopSpaceLeft);
                    }
                }
                else {
                    final int leftOver = rateChange - numItem;
                    if (shopSpaceLeft >= leftOver) {
                        Utilities.deposit(shop, player, leftOver);
                    }
                    else {
                        Utilities.deposit(shop, player, shopSpaceLeft);
                    }
                }
            }
            else {
                final int attemptedNum = Utilities.countItemsInInventory((Inventory)player.getInventory(), shop.getItem());
                if (shopSpaceLeft >= attemptedNum) {
                    Utilities.deposit(shop, player, attemptedNum);
                }
                else {
                    Utilities.deposit(shop, player, shopSpaceLeft);
                }
            }
            Utilities.playSound(ActionSound.POP, player);
            shop.setLastActive(System.currentTimeMillis());
            this.main.saveShops(this.main.getShops());
        }
        else if (event.getRawSlot() == 1) {
            if (!event.isShiftClick()) {
                final int numSlots = Utilities.getAvailableItemSlots((Inventory)player.getInventory(), shop.getItem());
                if (rateChange > shop.getAmount()) {
                    rateChange = shop.getAmount();
                }
                if (numSlots >= rateChange) {
                    Utilities.withdraw(shop, player, rateChange);
                }
                else {
                    final int leftOver2 = rateChange - numSlots;
                    Utilities.withdraw(shop, player, leftOver2);
                }
            }
            else {
                final int freeSpace = Utilities.getAvailableItemSlots((Inventory)player.getInventory(), shop.getItem());
                if (freeSpace >= shop.getAmount()) {
                    Utilities.withdraw(shop, player, shop.getAmount());
                }
                else {
                    Utilities.withdraw(shop, player, freeSpace);
                }
            }
            Utilities.playSound(ActionSound.POP, player);
            shop.setLastActive(System.currentTimeMillis());
            this.main.saveShops(this.main.getShops());
        }
        else if (event.getRawSlot() == 2) {
            if (event.isLeftClick()) {
                if (rateChange == 2048) {
                    return;
                }
                Utilities.loreItem(inventory.getItem(2), Arrays.asList(ChatColor.GRAY + "Amount Per Click: " + Integer.toString(rateChange * 2), ChatColor.GRAY + "Change Transfer Rate", "", "Left Click to Increase", "Right Click to Decrease"));
                Utilities.playSound(ActionSound.CLICK, player);
            }
            if (event.isRightClick()) {
                if (rateChange == 1) {
                    return;
                }
                Utilities.loreItem(inventory.getItem(2), Arrays.asList(ChatColor.GRAY + "Amount Per Click: " + Integer.toString(rateChange / 2), ChatColor.GRAY + "Change Transfer Rate", "", "Left Click to Increase", "Right Click to Decrease"));
                Utilities.playSound(ActionSound.CLICK, player);
            }
        }
        else if (event.getRawSlot() == 7) {
            player.openInventory(MenuUtilities.modifyShopGUI(shop, player));
            Utilities.playSound(ActionSound.MODIFY, player);
        }
        else if (event.getRawSlot() == 8) {
            player.openInventory(MenuUtilities.customerGUI(shop, player, this.main.getEcon()));
            Utilities.playSound(ActionSound.OPEN, player);
        }
        MenuUtilities.updateVendorGUI(shop, inventory, player);
    }
    
    private void modifyShopGUI(final InventoryClickEvent event, final Inventory inventory, final ItemStack item, final Player player) {
        event.setCancelled(true);
        player.updateInventory();
        final UShop shop = UShop.fromItem(this.main, inventory.getItem(1));
        final String[] stringSplit = inventory.getItem(5).getItemMeta().getLore().get(0).split(": ");
        int rateChange = Integer.parseInt(stringSplit[1]);
        if (event.getRawSlot() == 2) {
            int buyPrice = Integer.parseInt(inventory.getItem(2).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", "").substring(0, inventory.getItem(2).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", "").length() - 3));
            if (event.isLeftClick()) {
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempPrice = buyPrice + rateChange;
                final String tempPrice2 = (int)tempPrice + ".00";
                Utilities.loreItem(inventory.getItem(2), Arrays.asList(ChatColor.GRAY + "$" + Utilities.toCommadNumber(tempPrice2), "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not for sale)"));
            }
            if (event.isRightClick()) {
                if (buyPrice == 0) {
                    return;
                }
                if (buyPrice - rateChange < 0) {
                    buyPrice = 0;
                    rateChange = 0;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempPrice = buyPrice - rateChange;
                final String tempPrice2 = (int)tempPrice + ".00";
                Utilities.loreItem(inventory.getItem(2), Arrays.asList(ChatColor.GRAY + "$" + Utilities.toCommadNumber(tempPrice2), "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not for sale)"));
            }
        }
        else if (event.getRawSlot() == 3) {
            int sellPrice = Integer.parseInt(inventory.getItem(3).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", "").substring(0, inventory.getItem(3).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", "").length() - 3));
            if (event.isLeftClick()) {
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempPrice = sellPrice + rateChange;
                final String tempPrice2 = (int)tempPrice + ".00";
                Utilities.loreItem(inventory.getItem(3), Arrays.asList(ChatColor.GRAY + "$" + Utilities.toCommadNumber(tempPrice2), "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not buying)"));
            }
            if (event.isRightClick()) {
                if (sellPrice == 0) {
                    return;
                }
                if (sellPrice - rateChange < 0) {
                    sellPrice = 0;
                    rateChange = 0;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempPrice = sellPrice - rateChange;
                final String tempPrice2 = (int)tempPrice + ".00";
                Utilities.loreItem(inventory.getItem(3), Arrays.asList(ChatColor.GRAY + "$" + Utilities.toCommadNumber(tempPrice2), "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(zero means not buying)"));
            }
        }
        else if (event.getRawSlot() == 4) {
            int quantity = Integer.parseInt(inventory.getItem(4).getItemMeta().getLore().get(0).split(": ")[1].replaceAll(",", ""));
            if (event.isLeftClick()) {
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempAmount = quantity + rateChange;
                String commad = Utilities.toCommadNumber(String.format("%.2f", tempAmount));
                commad = commad.substring(0, commad.length() - 3);
                Utilities.loreItem(inventory.getItem(4), Arrays.asList(ChatColor.GRAY + "Amount Per Transaction: " + commad, "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(amount of items per buy / sell)"));
            }
            if (event.isRightClick()) {
                int minimumQuantity = 1;
                if (player.hasPermission("ushops.admin")) {
                    minimumQuantity = 0;
                }
                if (quantity == minimumQuantity) {
                    return;
                }
                if (quantity - rateChange < minimumQuantity) {
                    quantity = minimumQuantity;
                    rateChange = 0;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                final double tempAmount2 = quantity - rateChange;
                String commad2 = Utilities.toCommadNumber(String.format("%.2f", tempAmount2));
                commad2 = commad2.substring(0, commad2.length() - 3);
                Utilities.loreItem(inventory.getItem(4), Arrays.asList(ChatColor.GRAY + "Amount Per Transaction: " + commad2, "Left Click to Add", "Right Click to Subtract", ChatColor.GRAY + "(amount of items per buy / sell)"));
            }
        }
        else if (event.getRawSlot() == 5) {
            if (event.isLeftClick()) {
                if (rateChange == 2048) {
                    return;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                Utilities.loreItem(inventory.getItem(5), Arrays.asList(ChatColor.GRAY + "Amount Per Click: " + Integer.toString(rateChange * 2), "Change the rate of", "previous 3 buttons"));
            }
            if (event.isRightClick()) {
                if (rateChange == 1) {
                    return;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                Utilities.loreItem(inventory.getItem(5), Arrays.asList(ChatColor.GRAY + "Amount Per Click: " + Integer.toString(rateChange / 2), "Change the rate of", "previous 3 buttons"));
            }
        }
        else if (event.getRawSlot() == 6) {
            MenuUtilities.vendorNoteGUI(shop, player);
        }
        else if (event.getRawSlot() == 7) {
            shop.setBuyPrice((int)Double.parseDouble(inventory.getItem(2).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", "")));
            shop.setSellPrice((int)Double.parseDouble(inventory.getItem(3).getItemMeta().getLore().get(0).substring(2).replace('$', '0').replaceAll(",", "")));
            shop.setStack(Integer.parseInt(inventory.getItem(4).getItemMeta().getLore().get(0).split(": ")[1]));
            shop.setLastActive(System.currentTimeMillis());
            this.main.saveShops(this.main.getShops());
            Utilities.playSound(ActionSound.MODIFY, player);
            player.closeInventory();
        }
        else if (event.getRawSlot() == 8) {
            Utilities.playSound(ActionSound.ERROR, player);
            player.closeInventory();
        }
    }
    
    private void customerGUI(final InventoryClickEvent event, final Inventory inventory, final ItemStack item, final Player player) {
        event.setCancelled(true);
        player.updateInventory();
        final UShop shop = UShop.fromItem(this.main, inventory.getItem(6));
        final String[] stringSplit = inventory.getItem(3).getItemMeta().getLore().get(0).split(": ");
        int rateChange = Integer.parseInt(stringSplit[1]);
        if (event.getRawSlot() == 0) {
            if (shop.getBuyPrice() == 0 || !UltimateShops.buyItems) {
                return;
            }
            if (shop.getAmount() < shop.getStack() * rateChange) {
                if (this.main.informCustomerShopOutOfItems) {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedStringWAmount(this.main.phrases.get("shop-out-of-items-message"), shop, player, shop.getBuyPrice(), this.main.getEcon(), ChatColor.RED, rateChange)));
                }
                return;
            }
            if (this.main.getEcon().getBalance((OfflinePlayer)player) < shop.getBuyPrice() * rateChange) {
                if (this.main.informCustomerNotEnoughFunds) {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedStringWAmount(this.main.phrases.get("not-enough-funds-message"), shop, player, shop.getBuyPrice(), this.main.getEcon(), ChatColor.RED, rateChange)));
                }
                return;
            }
//          // Not sure if this is done well
            if (Utilities.getAvailableItemSlots((Inventory)player.getInventory(), shop.getItem()) < shop.getStack()) {
                if (this.main.informCustomerShopOutOfSpace) {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedString(this.main.phrases.get("customer-out-of-space-message"), shop, player, shop.getBuyPrice(), this.main.getEcon(), ChatColor.RED)));
                }
                return;
            }
            this.main.getEcon().withdrawPlayer((OfflinePlayer)player, (double)shop.getBuyPrice()*rateChange);
            if (!shop.getAdmin()) {
                this.main.getEcon().depositPlayer(shop.getVendor(), (double)shop.getBuyPrice()*rateChange);
            }
            Utilities.withdraw(shop, player, shop.getStack()*rateChange);
            final boolean players_same = player.getUniqueId().equals(shop.getVendor().getUniqueId());
//          Since amount money and amount bought is different. Use new utilities formatted.
            if (this.main.informCustomerOfTransaction && !players_same) {
                Utilities.informPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedStringWAmount(this.main.phrases.get("customer-buy-message"), shop, player, shop.getBuyPrice(), this.main.getEcon(), ChatColor.GRAY, rateChange)));
            }
            if (this.main.informHostOfTransaction && shop.getVendor().isOnline() && !shop.getAdmin() && !players_same) {
                final Player vendor = Bukkit.getPlayer(shop.getVendor().getUniqueId());
                Utilities.informPlayer((CommandSender)vendor, Arrays.asList(Utilities.toFormattedStringWAmount(this.main.phrases.get("host-buy-message"), shop, player, shop.getBuyPrice(), this.main.getEcon(), ChatColor.GRAY, rateChange)));
                Utilities.playSound(ActionSound.POP, player);
            }
//            Fixed
            if (this.main.getTransactionLogs() && !players_same) {
                Utilities.writeLog((Plugin)this.main, player.getName() + " bought " + shop.getStack()*rateChange + " of " + shop.getItem().getType().toString() + " from " + shop.getVendor().getName() + " for $" + shop.getBuyPrice()*rateChange + " at shop located at: " + Utilities.toLocString(shop.getLocation()));
            }
            if (this.main.getPremium()) {
                for (final String command : shop.getBuyCommands()) {
                    final String com = command.replaceAll("customer", player.getName()).replaceAll("vendor", shop.getVendor().getName()).replaceAll("world", shop.getLocation().getWorld().getName()).replaceAll("shopx", Integer.toString(shop.getLocation().getBlockX())).replaceAll("shopy", Integer.toString(shop.getLocation().getBlockY())).replaceAll("shopz", Integer.toString(shop.getLocation().getBlockZ())).replaceAll("item", shop.getItem().getType().name());
                    if (this.main.getDebug()) {
                        Bukkit.getLogger().info(this.main.getConsolePrefix() + "Executing command on buy for ushop at: " + Utilities.toLocString(shop.getLocation()));
                        Bukkit.getLogger().info(this.main.getConsolePrefix() + " - " + com);
                    }
                    Bukkit.dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), com);
                }
            }
            Utilities.playSound(ActionSound.POP, player);
            shop.setLastActive(System.currentTimeMillis());
            this.main.saveShops(this.main.getShops());
        }
        else if (event.getRawSlot() == 1) {
            if (shop.getSellPrice() == 0 || !UltimateShops.sellItems) {
                return;
            }
            if (Utilities.countItemsInInventory((Inventory)player.getInventory(), shop.getItem()) < shop.getStack()*rateChange) {
                if (this.main.informCustomerNotEnoughItems) {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedString(this.main.phrases.get("not-enough-items-message"), shop, player, shop.getSellPrice(), this.main.getEcon(), ChatColor.RED)));
                }
                return;
            }
            if (this.main.getEcon().getBalance(shop.getVendor()) < shop.getSellPrice()*rateChange && !shop.getAdmin()) {
                if (this.main.informCustomerShopOutOfFunds) {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedString(this.main.phrases.get("shop-out-of-funds-message"), shop, player, shop.getSellPrice(), this.main.getEcon(), ChatColor.RED)));
                }
                return;
            }
            if (shop.getAmount() + shop.getStack() * rateChange > this.main.getMaxShopStacks() * 64 && shop.getStack() != 0) {
                if (this.main.informUserShopOutOfSpace) {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedString(this.main.phrases.get("shop-out-of-space-message"), shop, player, shop.getSellPrice(), this.main.getEcon(), ChatColor.RED)));
                }
                return;
            }
//            Done
            if (!shop.getAdmin()) {
                this.main.getEcon().withdrawPlayer(shop.getVendor(), (double)shop.getSellPrice()*rateChange);
            }
            this.main.getEcon().depositPlayer((OfflinePlayer)player, (double)shop.getSellPrice()*rateChange);
            Utilities.deposit(shop, player, shop.getStack());
            final boolean players_same = player.getUniqueId().equals(shop.getVendor().getUniqueId());
//            Again the messaging
            if (this.main.informCustomerOfTransaction && !players_same) {
                Utilities.informPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedStringWAmount(this.main.phrases.get("customer-sell-message"), shop, player, shop.getSellPrice(), this.main.getEcon(), ChatColor.GRAY, rateChange)));
            }
            if (this.main.informHostOfTransaction && shop.getVendor().isOnline() && !shop.getAdmin() && !players_same) {
                final Player vendor = Bukkit.getPlayer(shop.getVendor().getUniqueId());
                Utilities.informPlayer((CommandSender)vendor, Arrays.asList(Utilities.toFormattedStringWAmount(this.main.phrases.get("host-sell-message"), shop, player, shop.getSellPrice(), this.main.getEcon(), ChatColor.GRAY, rateChange)));
                Utilities.playSound(ActionSound.POP, player);
            }
//            Messaging done, now logs again
            if (this.main.getTransactionLogs() && !players_same) {
                Utilities.writeLog((Plugin)this.main, player.getName() + " sold " + shop.getStack()*rateChange + " of " + shop.getItem().getType().toString() + " to " + shop.getVendor().getName() + " for $" + shop.getSellPrice()*rateChange + " at shop located at: " + Utilities.toLocString(shop.getLocation()));
            }
            if (this.main.getPremium()) {
                for (final String command : shop.getSellCommands()) {
                    final String com = command.replaceAll("customer", player.getName()).replaceAll("vendor", shop.getVendor().getName()).replaceAll("world", shop.getLocation().getWorld().getName()).replaceAll("shopx", Integer.toString(shop.getLocation().getBlockX())).replaceAll("shopy", Integer.toString(shop.getLocation().getBlockY())).replaceAll("shopz", Integer.toString(shop.getLocation().getBlockZ())).replaceAll("item", shop.getItem().getType().name());
                    if (this.main.getDebug()) {
                        Bukkit.getLogger().info(this.main.getConsolePrefix() + "Executing command on sell for ushop at: " + Utilities.toLocString(shop.getLocation()));
                        Bukkit.getLogger().info(this.main.getConsolePrefix() + " - " + com);
                    }
                    Bukkit.dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), com);
                }
            }
            Utilities.playSound(ActionSound.POP, player);
            shop.setLastActive(System.currentTimeMillis());
            this.main.saveShops(this.main.getShops());
        }
        else if (event.getRawSlot() == 3) {
            String name = WordUtils.capitalizeFully(shop.getItem().getType().name().replace('_', ' ').toLowerCase());
            final double stacks = shop.getAmount() / (double)shop.getItem().getMaxStackSize();
            if (shop.getItem().getItemMeta().getDisplayName() != null && shop.getItem().getItemMeta().getDisplayName() != "") {
                name = shop.getItem().getItemMeta().getDisplayName();
            }
            if (event.isLeftClick()) {
                if (rateChange == 2048) {
                    return;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                Utilities.loreItem(inventory.getItem(3), Arrays.asList(ChatColor.GRAY + "Amount Per Click: " + Integer.toString(rateChange * 2), "Change the rate you", "would like to buy/sell"));
                if (shop.getBuyPrice() != 0 && UltimateShops.buyItems) {
                    inventory.setItem(0, Utilities.loreItem(Utilities.nameItem(XMaterial.GOLD_INGOT.parseMaterial(), ChatColor.GOLD + "Buy '" + name + "' * " + shop.getStack()*(rateChange*2)), Arrays.asList(ChatColor.GRAY + "Buy for: $" + shop.getBuyPrice()*(rateChange*2), "In Stock: " + shop.getAmount(), "or " + String.format("%.2f", stacks) + " stacks")));
                }
                if (shop.getSellPrice() != 0 && UltimateShops.sellItems) {
                    inventory.setItem(1, Utilities.loreItem(Utilities.nameItem(XMaterial.IRON_INGOT.parseMaterial(), ChatColor.GOLD + "Sell '" + name + "' * " + shop.getStack()*(rateChange*2)), Arrays.asList(ChatColor.GRAY + "Sell for: $" + shop.getSellPrice()*(rateChange*2), "In Stock: " + shop.getAmount(), "or " + String.format("%.2f", stacks) + " stacks")));
                }
            }
            if (event.isRightClick()) {
                if (rateChange == 1) {
                    return;
                }
                Utilities.playSound(ActionSound.CLICK, player);
                Utilities.loreItem(inventory.getItem(3), Arrays.asList(ChatColor.GRAY + "Amount Per Click: " + Integer.toString(rateChange / 2), "Change the rate you", "would like to buy/sell"));
                if (shop.getBuyPrice() != 0 && UltimateShops.buyItems) {
                    inventory.setItem(0, Utilities.loreItem(Utilities.nameItem(XMaterial.GOLD_INGOT.parseMaterial(), ChatColor.GOLD + "Buy '" + name + "' * " + shop.getStack()*(rateChange/2)), Arrays.asList(ChatColor.GRAY + "Buy for: $" + shop.getBuyPrice()*(rateChange/2), "In Stock: " + shop.getAmount(), "or " + String.format("%.2f", stacks) + " stacks")));
                }
                if (shop.getSellPrice() != 0 && UltimateShops.sellItems) {
                    inventory.setItem(1, Utilities.loreItem(Utilities.nameItem(XMaterial.IRON_INGOT.parseMaterial(), ChatColor.GOLD + "Sell '" + name + "' * " + shop.getStack()*(rateChange/2)), Arrays.asList(ChatColor.GRAY + "Sell for: $" + shop.getSellPrice()*(rateChange/2), "In Stock: " + shop.getAmount(), "or " + String.format("%.2f", stacks) + " stacks")));
                }
            }
        }
        if (event.getRawSlot() != 3) {
            MenuUtilities.updateCustomerGUI(shop, inventory, player, this.main.getEcon());
        }
    }
    
    private void chestInputGUI(final InventoryClickEvent event, final Inventory inventory, final ItemStack item, final Player player) {
        event.setCancelled(true);
        final ArrayList<UShop> shops = new ArrayList<UShop>();
        for (final UShop shop : this.main.getShops().values()) {
            if ((int)shop.getLocation().distance(player.getLocation()) < this.main.getChestInputRadius() && shop.getVendor().getUniqueId().equals(player.getUniqueId())) {
                shops.add(shop);
            }
        }
        UShop effectedShop = null;
        final ItemStack actionItem = event.getCurrentItem().clone();
        actionItem.setAmount(1);
        for (final UShop shop2 : shops) {
            final ItemStack shopItem = shop2.getItem().clone();
            shopItem.setAmount(1);
            if (actionItem.equals((Object)shopItem)) {
                effectedShop = shop2;
                break;
            }
        }
        if (effectedShop != null) {
            final int shopSpaceLeft = this.main.getMaxShopStacks() * 64 - effectedShop.getAmount();
            if (shopSpaceLeft == 0) {
                if (this.main.informUserShopOutOfSpace) {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedString(this.main.phrases.get("shop-out-of-space-message"), effectedShop, player, effectedShop.getBuyPrice(), this.main.getEcon(), ChatColor.RED)));
                }
                return;
            }
            if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                final int attemptedNum = Utilities.countItemsInInventory((Inventory)player.getInventory(), effectedShop.getItem());
                if (shopSpaceLeft >= attemptedNum) {
                    Utilities.deposit(effectedShop, player, attemptedNum);
                    Utilities.playSound(ActionSound.POP, player);
                }
                else {
                    Utilities.deposit(effectedShop, player, shopSpaceLeft);
                    Utilities.playSound(ActionSound.POP, player);
                }
                this.main.saveShops(this.main.getShops());
            }
            else {
                final int attemptedNum = event.getCurrentItem().getAmount();
                if (shopSpaceLeft >= attemptedNum) {
                    effectedShop.setAmount(effectedShop.getAmount() + attemptedNum);
                    event.setCurrentItem((ItemStack)null);
                }
                else {
                    effectedShop.setAmount(effectedShop.getAmount() + shopSpaceLeft);
                    event.getCurrentItem().setAmount(event.getCurrentItem().getAmount() - shopSpaceLeft);
                    player.getInventory().addItem(new ItemStack[] { event.getCurrentItem().clone() });
                    event.setCurrentItem((ItemStack)null);
                }
                Utilities.playSound(ActionSound.POP, player);
                this.main.saveShops(this.main.getShops());
            }
        }
        else {
            Utilities.playSound(ActionSound.ERROR, player);
        }
    }
    
    private void destroyShopGUI(final InventoryClickEvent event, final Inventory inventory, final ItemStack item, final Player player) {
        event.setCancelled(true);
        player.updateInventory();
        final UShop shop = UShop.fromItem(this.main, inventory.getItem(4));
        if (event.getRawSlot() == 3) {
            if (shop.getAmount() <= 3456) {
                shop.destroy(true);
                player.closeInventory();
                Utilities.informPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedString(this.main.phrases.get("shop-broken-message"), shop, player, 0, this.main.getEcon(), ChatColor.GRAY)));
                Utilities.playSound(ActionSound.BREAK, player);
            }
            else if (shop.getAdmin()) {
                shop.destroy(false);
                player.closeInventory();
                Utilities.playSound(ActionSound.BREAK, player);
            }
            else {
                Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("shop-too-full-to-break-message")));
            }
            player.closeInventory();
        }
        if (event.getRawSlot() == 5) {
            Utilities.playSound(ActionSound.ERROR, player);
            player.closeInventory();
        }
    }
}
