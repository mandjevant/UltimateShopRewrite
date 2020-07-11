// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.events.block;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import java.util.Iterator;
import org.bukkit.event.Event;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;
import thirtyvirus.ultimateshops.helpers.ActionSound;
import thirtyvirus.ultimateshops.helpers.MenuUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import thirtyvirus.ultimateshops.helpers.Utilities;
import java.util.Arrays;
import thirtyvirus.ultimateshops.events.pluginsupport.TownySupport;
import thirtyvirus.ultimateshops.multiversion.API;
import thirtyvirus.ultimateshops.shops.UShop;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import thirtyvirus.ultimateshops.multiversion.Version;
import org.bukkit.event.player.PlayerInteractEvent;
import thirtyvirus.ultimateshops.UltimateShops;
import org.bukkit.event.Listener;

public class BlockClickHandler implements Listener
{
    private UltimateShops main;
    
    public BlockClickHandler(final UltimateShops main) {
        this.main = null;
        this.main = main;
    }
    
    @EventHandler
    public void onBlockClick(final PlayerInteractEvent event) {
        if (Version.getVersion().isBiggerThan(Version.v1_8) && event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Player player = event.getPlayer();
            if (player.isSneaking()) {
                return;
            }
            if (UShop.isSlab(event.getClickedBlock().getType())) {
                if (!API.isBottomSlab(event.getClickedBlock()) && !this.main.getUseUpperSlabs()) {
                    return;
                }
                if (this.main.getServer().getPluginManager().getPlugin("Towny") != null && this.main.getShopsOnlyInTowns() && !TownySupport.isInTown(player)) {
                    Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("towny-only-in-town-message")));
                    return;
                }
                final UShop shop = this.main.getShops().get(Utilities.toLocString(event.getClickedBlock().getLocation()));
                if (shop != null) {
                    this.openShop(event, player, shop);
                }
                else {
                    this.makeShop(event, player);
                }
            }
            else if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
                this.openBulkRestockMenu(event, player);
            }
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (!UShop.isSlab(event.getClickedBlock().getType())) {
                return;
            }
            final Player player = event.getPlayer();
            final UShop shop = this.main.getShops().get(Utilities.toLocString(event.getClickedBlock().getLocation()));
            if (shop == null) {
                return;
            }
            if (!shop.getVendor().getUniqueId().equals(player.getUniqueId())) {
                return;
            }
            this.doQuickInput(event, player, shop);
        }
    }
    
    private void openShop(final PlayerInteractEvent event, final Player player, final UShop shop) {
        if (shop.getTag() != null && !shop.getTag().equals("") && !player.hasPermission("ushops.tag." + shop.getTag())) {
            Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("not-enough-permissions-message")));
            return;
        }
        if (!shop.getVendor().getUniqueId().equals(player.getUniqueId())) {
            player.openInventory(MenuUtilities.customerGUI(shop, player, this.main.getEcon()));
            event.setCancelled(true);
            return;
        }
        if (Version.getVersion().isBiggerThan(Version.v1_8)) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType() != UltimateShops.toolItem) {
                player.openInventory(MenuUtilities.vendorGUI(shop, player));
                event.setCancelled(true);
                return;
            }
        }
        else if (event.getPlayer().getInventory().getItemInHand().getType() != UltimateShops.toolItem) {
            player.openInventory(MenuUtilities.vendorGUI(shop, player));
            event.setCancelled(true);
            return;
        }
        player.openInventory(MenuUtilities.destroyShopGUI(shop));
        Utilities.playSound(ActionSound.OPEN, player);
        event.setCancelled(true);
    }
    
    private void makeShop(final PlayerInteractEvent event, final Player player) {
        if (Version.getVersion().isBiggerThan(Version.v1_8)) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType() != UltimateShops.toolItem) {
                return;
            }
        }
        else if (event.getPlayer().getInventory().getItemInHand().getType() != UltimateShops.toolItem) {
            return;
        }
        if (!this.main.getPremium() && Utilities.countShopsOwned(this.main.getShops(), player) >= 5) {
            Utilities.warnPlayer((CommandSender)player, Arrays.asList(this.main.phrases.get("lite-version-warning")));
            Utilities.warnPremium(player);
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
        if (player.hasPermission("ushops.make")) {
            final BlockBreakEvent e = new BlockBreakEvent(event.getClickedBlock(), player);
            Bukkit.getServer().getPluginManager().callEvent((Event)e);
            if (!e.isCancelled() || UltimateShops.ignoreBuildPermissions) {
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
        event.setCancelled(true);
    }
    
    private void openBulkRestockMenu(final PlayerInteractEvent event, final Player player) {
        if (Version.getVersion().isBiggerThan(Version.v1_8)) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType() != UltimateShops.toolItem) {
                return;
            }
        }
        else if (event.getPlayer().getInventory().getItemInHand().getType() != UltimateShops.toolItem) {
            return;
        }
        boolean isShopOwner = false;
        final String shopLabel = UltimateShops.prefix + "Restock";
        for (final UShop shop : this.main.getShops().values()) {
            if (shop.getLocation().getWorld().getName().equals(event.getClickedBlock().getLocation().getWorld().getName()) && (int)shop.getLocation().distance(event.getClickedBlock().getLocation()) < this.main.getChestInputRadius() && shop.getVendor().getUniqueId().equals(player.getUniqueId())) {
                isShopOwner = true;
            }
        }
        if (isShopOwner) {
            event.setCancelled(true);
            Utilities.playSound(ActionSound.OPEN, player);
            player.openInventory(MenuUtilities.chestInputGUI(shopLabel));
            event.setCancelled(true);
        }
    }
    
    private void doQuickInput(final PlayerInteractEvent event, final Player player, final UShop shop) {
        ItemStack i1 = null;
        if (Version.getVersion().isBiggerThan(Version.v1_8)) {
            i1 = player.getInventory().getItemInMainHand().clone();
        }
        else {
            i1 = player.getInventory().getItemInHand().clone();
        }
        i1.setAmount(1);
        final ItemStack i2 = shop.getItem().clone();
        i2.setAmount(1);
        if (!i1.equals((Object)i2)) {
            return;
        }
        final int shopSpaceLeft = this.main.getMaxShopStacks() * 64 - shop.getAmount();
        if (shopSpaceLeft == 0) {
            if (this.main.informUserShopOutOfSpace) {
                Utilities.warnPlayer((CommandSender)player, Arrays.asList(Utilities.toFormattedString(this.main.phrases.get("shop-out-of-space-message"), shop, player, shop.getBuyPrice(), this.main.getEcon(), ChatColor.RED)));
            }
            return;
        }
        if (!player.isSneaking()) {
            int attemptedNum = 1;
            if (Version.getVersion().isBiggerThan(Version.v1_8)) {
                attemptedNum = player.getInventory().getItemInMainHand().getAmount();
            }
            else {
                attemptedNum = player.getInventory().getItemInHand().getAmount();
            }
            if (shopSpaceLeft >= attemptedNum) {
                shop.setAmount(shop.getAmount() + attemptedNum);
                if (Version.getVersion().isBiggerThan(Version.v1_8)) {
                    player.getInventory().setItemInMainHand((ItemStack)null);
                }
                else {
                    player.getInventory().setItemInHand((ItemStack)null);
                }
            }
            else {
                shop.setAmount(shop.getAmount() + shopSpaceLeft);
                if (Version.getVersion().isBiggerThan(Version.v1_8)) {
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - shopSpaceLeft);
                }
                else {
                    player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - shopSpaceLeft);
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
        this.main.saveShops(this.main.getShops());
    }
}
