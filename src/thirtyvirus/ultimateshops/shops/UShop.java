// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.shops;

import org.bukkit.Chunk;
import java.util.Iterator;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;
import thirtyvirus.ultimateshops.multiversion.Version;
import thirtyvirus.ultimateshops.multiversion.API;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import thirtyvirus.ultimateshops.helpers.Utilities;
import org.bukkit.Bukkit;
import thirtyvirus.ultimateshops.multiversion.XMaterial;
import java.util.function.Predicate;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import thirtyvirus.ultimateshops.UltimateShops;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

public class UShop
{
    private OfflinePlayer vendor;
    private Location location;
    private ItemStack item;
    private int buyPrice;
    private int sellPrice;
    private int stack;
    private int amount;
    private String vendorNote;
    private long lastActive;
    private String tag;
    private boolean admin;
    private List<String> buyCommands;
    private List<String> sellCommands;
    private UltimateShops main;
    
    public UShop(final UltimateShops main, final OfflinePlayer vendor, final Location location, final ItemStack item, final int buyPrice, final int sellPrice, final int stack, final int amount, final String vendorNote, final long lastActive, final boolean admin, final String buyCommandsString, final String sellCommandsString, final String tag) {
        this.vendorNote = "";
        this.tag = "";
        this.buyCommands = new ArrayList<String>();
        this.sellCommands = new ArrayList<String>();
        this.main = null;
        this.main = main;
        this.vendor = vendor;
        this.location = location;
        this.item = item;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.stack = stack;
        this.amount = amount;
        this.vendorNote = vendorNote;
        this.lastActive = lastActive;
        this.admin = admin;
        this.tag = tag;
        if (!buyCommandsString.equals(null)) {
            for (final String comm : buyCommandsString.split(";")) {
                this.buyCommands.add(comm);
            }
        }
        if (!sellCommandsString.equals(null)) {
            for (final String comm : sellCommandsString.split(";")) {
                this.sellCommands.add(comm);
            }
        }
        this.buyCommands.removeIf(StringUtils::isBlank);
        this.sellCommands.removeIf(StringUtils::isBlank);
        if (location.getWorld() != null && !isSlab(location.getWorld().getBlockAt(location).getType())) {
            location.getWorld().getBlockAt(location).setType(XMaterial.STONE_SLAB.parseMaterial());
        }
        if (main.getDebug()) {
            Bukkit.getLogger().info(main.getConsolePrefix() + main.phrases.get("shop-created-message") + ": " + Utilities.toLocString(location));
        }
        this.spawnDisplayItem();
    }
    
    public static boolean isSlab(final Material material) {
        boolean y = false;
        if (material.name().toLowerCase().contains("slab") || material.name().toLowerCase().contains("step")) {
            y = true;
        }
        return y;
    }
    
    public void spawnDisplayItem() {
        if (!UltimateShops.displayShopItems) {
            return;
        }
        if (this.location == null) {
            return;
        }
        if (this.location.getWorld() == null) {
            return;
        }
        if (this.location.getWorld().getBlockAt(this.location) == null) {
            return;
        }
        ItemStack i = this.item.clone();
        i = Utilities.loreItem(i, Arrays.asList(ChatColor.RED + "Display Item!", UUID.randomUUID().toString()));
        if (this.stack == 0) {
            i.setAmount(1);
        }
        else {
            i.setAmount(this.stack);
        }
        if (i.getAmount() > 64) {
            i.setAmount(64);
        }
        final Location placeLocation = this.location.clone();
        double itemLoc = 0.6;
        if (API.isTopSlab(this.location.getBlock())) {
            itemLoc = 1.1;
        }
        Entity e = (Entity)placeLocation.getWorld().dropItem(placeLocation.add(0.5, itemLoc, 0.5), i);
        if (Version.getVersion().isBiggerThan(Version.v1_8)) {
            e.setInvulnerable(true);
        }
        e.setVelocity(new Vector(0, 0, 0));
        if (Version.getVersion().isBiggerThan(Version.v1_9)) {
            e.setGravity(false);
        }
        e.setCustomName("ShopItem");
        final Item e2 = (Item)e;
        e2.setPickupDelay(Integer.MAX_VALUE);
        e = (Entity)e2;
        this.main.getShopItems().add(e.getUniqueId());
    }
    
    public static UShop fromItem(final UltimateShops main, final ItemStack item) {
        final String locationString = Utilities.convertToVisibleString(item.getItemMeta().getLore().get(1));
        return main.getShops().get(locationString);
    }
    
    public void destroy(final boolean dropItems) {
        for (final UUID id : this.main.getShopItems()) {
            if (API.getEntity(id) != null) {
                API.getEntity(id).remove();
            }
        }
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
        if (dropItems) {
            int dropAmount = this.amount;
            while (dropAmount > 0) {
                int currentStack = 0;
                if (dropAmount >= this.item.getMaxStackSize()) {
                    currentStack = this.item.getMaxStackSize();
                    dropAmount -= this.item.getMaxStackSize();
                }
                else {
                    currentStack = dropAmount;
                    dropAmount = 0;
                }
                if (currentStack != 0) {
                    final ItemStack i = this.item.clone();
                    i.setAmount(currentStack);
                    final Entity e2 = (Entity)this.location.getWorld().dropItem(this.location, i);
                    e2.setVelocity(new Vector(0, 0, 0));
                }
            }
        }
        final String locString = Utilities.toLocString(this.location);
        this.main.getShops().remove(locString);
        this.main.getShopList().remove(this);
        this.main.getShopChunks().get(Utilities.toChunkString(this.location.getChunk())).remove(this);
        if (this.main.getShopChunks().get(Utilities.toChunkString(this.location.getChunk())).size() == 0) {
            this.main.getShopChunks().remove(Utilities.toChunkString(this.location.getChunk()));
        }
        this.main.getShopsFileConfig().set(locString, (Object)null);
        this.main.saveShops(this.main.getShops());
        for (final UShop shop : this.main.getShops().values()) {
            shop.spawnDisplayItem();
        }
    }
    
    public String toShopString() {
        return "uShop: " + Utilities.toLocString(this.location) + " | Item: " + this.item.getType().name() + " | BuyPrice: " + this.buyPrice + " | SellPrice: " + this.sellPrice + " | Stack: " + this.stack + " | Amount: " + this.amount + " | Admin: " + this.admin;
    }
    
    public OfflinePlayer getVendor() {
        return this.vendor;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public ItemStack getItem() {
        return this.item;
    }
    
    public int getBuyPrice() {
        return this.buyPrice;
    }
    
    public int getSellPrice() {
        return this.sellPrice;
    }
    
    public int getStack() {
        return this.stack;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public String getVendorNote() {
        return this.vendorNote;
    }
    
    public long getLastActive() {
        return this.lastActive;
    }
    
    public boolean getAdmin() {
        return this.admin;
    }
    
    public List<String> getBuyCommands() {
        return this.buyCommands;
    }
    
    public List<String> getSellCommands() {
        return this.sellCommands;
    }
    
    public void setVendor(final OfflinePlayer vendor) {
        this.vendor = vendor;
    }
    
    public void setLocation(final Location location) {
        this.location = location;
    }
    
    public void setItem(final ItemStack item) {
        this.item = item;
    }
    
    public void setBuyPrice(final int buyPrice) {
        this.buyPrice = buyPrice;
    }
    
    public void setSellPrice(final int sellPrice) {
        this.sellPrice = sellPrice;
    }
    
    public void setStack(final int stack) {
        this.stack = stack;
    }
    
    public void setAmount(final int amount) {
        this.amount = amount;
    }
    
    public void setTag(final String tag) {
        this.tag = tag;
    }
    
    public void setVendorNote(final String vendorNote) {
        this.vendorNote = vendorNote;
    }
    
    public void setLastActive(final long lastActive) {
        this.lastActive = lastActive;
    }
    
    public void setAdmin(final boolean admin) {
        this.admin = admin;
    }
    
    public void setBuyCommands(final ArrayList<String> buyCommands) {
        this.buyCommands = buyCommands;
    }
    
    public void setSellCommands(final ArrayList<String> sellCommands) {
        this.sellCommands = sellCommands;
    }
}
