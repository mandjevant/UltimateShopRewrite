// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops;

import thirtyvirus.ultimateshops.events.pluginsupport.ItemRemoveEventHandler;
import thirtyvirus.ultimateshops.events.chat.LoginHandler;
import thirtyvirus.ultimateshops.events.block.BlockEventHandler;
import thirtyvirus.ultimateshops.events.item.ItemBreakEventHandler;
import thirtyvirus.ultimateshops.events.block.BlockClickHandler;
import thirtyvirus.ultimateshops.events.inventory.InventoryClickHandler;
import org.bukkit.event.Listener;
import thirtyvirus.ultimateshops.events.chunk.ChunkLoadHandler;
import org.bukkit.command.TabCompleter;
import thirtyvirus.ultimateshops.events.chat.TabCompleteHandler;
import thirtyvirus.ultimateshops.commands.UshopsCommand;
import org.bukkit.command.CommandExecutor;
import thirtyvirus.ultimateshops.commands.UshopCommand;
import java.util.Collection;
import org.bukkit.OfflinePlayer;
import thirtyvirus.ultimateshops.multiversion.XMaterial;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.Chunk;
import java.util.Iterator;
import org.bukkit.plugin.RegisteredServiceProvider;
import thirtyvirus.ultimateshops.helpers.Utilities;
import thirtyvirus.ultimateshops.multiversion.API;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;
import thirtyvirus.ultimateshops.shops.UShop;
import java.util.Map;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public class UltimateShops extends JavaPlugin
{
    private File shopsFile;
    private FileConfiguration shopsFileConfig;
    private File priceFile;
    private FileConfiguration priceFileConfig;
    private File langFile;
    private FileConfiguration langFileConfig;
    private Economy econ;
    private Map<String, UShop> shops;
    private List<UShop> shopsList;
    private Map<String, ArrayList<UShop>> shopChunks;
    private Map<Material, Integer> suggestedBuyPrices;
    private Map<Material, Integer> suggestedSellPrices;
    private Map<Material, Integer> suggestedQuantity;
    private ArrayList<UUID> shopItems;
    public static String prefix;
    private String consolePrefix;
    public static Material toolItem;
    public static boolean displayShopItems;
    public static boolean buyItems;
    public static boolean sellItems;
    public static boolean ignoreBuildPermissions;
    private int maxShopStacks;
    private int chestInputRadius;
    private boolean autoPurge;
    private int purgeAge;
    private boolean enforceShopLimit;
    private boolean useUpperSlabs;
    private boolean suggestBuyPrices;
    private boolean suggestSellPrices;
    private boolean suggestQuantity;
    private boolean premium;
    private boolean debug;
    private boolean transactionLogs;
    private boolean shopsOnlyInTowns;
    public Map<String, String> phrases;
    public boolean informHostOfTransaction;
    public boolean informCustomerOfTransaction;
    public boolean informCustomerNotEnoughFunds;
    public boolean informCustomerShopOutOfFunds;
    public boolean informCustomerNotEnoughItems;
    public boolean informCustomerShopOutOfItems;
    public boolean informCustomerShopOutOfSpace;
    public boolean informUserShopOutOfSpace;
    public boolean informHostOfEmptyShops;
    public boolean informHostOfNearEmptyShops;
    public int nearEmptyShopThreshold;
    
    public UltimateShops() {
        this.shops = new HashMap<String, UShop>();
        this.shopsList = new ArrayList<UShop>();
        this.shopChunks = new HashMap<String, ArrayList<UShop>>();
        this.suggestedBuyPrices = new HashMap<Material, Integer>();
        this.suggestedSellPrices = new HashMap<Material, Integer>();
        this.suggestedQuantity = new HashMap<Material, Integer>();
        this.shopItems = new ArrayList<UUID>();
        this.consolePrefix = "[UltimateShops] ";
        this.maxShopStacks = 54;
        this.chestInputRadius = 32;
        this.autoPurge = false;
        this.purgeAge = 672;
        this.enforceShopLimit = false;
        this.useUpperSlabs = false;
        this.suggestBuyPrices = true;
        this.suggestSellPrices = true;
        this.suggestQuantity = true;
        this.premium = false;
        this.debug = false;
        this.transactionLogs = true;
        this.shopsOnlyInTowns = false;
        this.phrases = new HashMap<String, String>();
        this.informHostOfTransaction = true;
        this.informCustomerOfTransaction = true;
        this.informCustomerNotEnoughFunds = true;
        this.informCustomerShopOutOfFunds = true;
        this.informCustomerNotEnoughItems = true;
        this.informCustomerShopOutOfItems = true;
        this.informCustomerShopOutOfSpace = true;
        this.informUserShopOutOfSpace = true;
        this.informHostOfEmptyShops = true;
        this.informHostOfNearEmptyShops = true;
        this.nearEmptyShopThreshold = 5;
    }
    
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            this.getLogger().severe("UltimateShops requires Vault! disabled because Vault dependency not found");
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        final RegisteredServiceProvider<Economy> rsp = (RegisteredServiceProvider<Economy>)this.getServer().getServicesManager().getRegistration((Class)Economy.class);
        if (rsp == null) {
            this.getLogger().severe("Error Loading economy. disabling");
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        this.econ = (Economy)rsp.getProvider();
        this.loadConfiguration();
        this.loadShops();
        this.registerCommands();
        this.registerEvents();
        this.loadSuggestedPrices();
        this.loadLangFile();
        for (final World world : Bukkit.getWorlds()) {
            for (final Chunk chunk : world.getLoadedChunks()) {
                for (final Entity e : chunk.getEntities()) {
                    if (e.getCustomName() != null && e.getCustomName().equals("ShopItem")) {
                        this.shopItems.remove(e.getUniqueId());
                        e.remove();
                    }
                }
            }
        }
        for (final UUID id : this.shopItems) {
            if (API.getEntity(id) != null) {
                API.getEntity(id).remove();
            }
        }
        for (final UShop shop : this.shops.values()) {
            shop.spawnDisplayItem();
        }
        if (this.autoPurge) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    if (UltimateShops.this.debug) {
                        Bukkit.getLogger().info("Automatically Purged " + Utilities.purge(UltimateShops.this.shops, UltimateShops.this.consolePrefix, UltimateShops.this.debug, UltimateShops.this.purgeAge) + " empty shops that haven't been active in the past " + UltimateShops.this.purgeAge + " hour(s).");
                    }
                    if (!UltimateShops.this.debug) {
                        Utilities.purge(UltimateShops.this.shops, UltimateShops.this.consolePrefix, UltimateShops.this.debug, UltimateShops.this.purgeAge);
                    }
                }
            }, 72000L, 72000L);
        }
        this.getLogger().info(this.getDescription().getName() + " V: " + this.getDescription().getVersion() + " has been enabled");
    }
    
    public void onDisable() {
        for (final UUID id : this.shopItems) {
            if (API.getEntity(id) != null) {
                API.getEntity(id).remove();
            }
        }
        try {
            this.saveShops(this.shops);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.getLogger().info(this.getDescription().getName() + " V: " + this.getDescription().getVersion() + " has been disabled");
    }
    
    public void loadConfiguration() {
        final File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            Utilities.loadResource((Plugin)this, "config.yml");
        }
        final FileConfiguration config = this.getConfig();
        UltimateShops.toolItem = Material.matchMaterial(config.getString("tool-item"));
        if (UltimateShops.toolItem == null) {
            UltimateShops.toolItem = Material.STICK;
        }
        UltimateShops.displayShopItems = config.getBoolean("display-items");
        UltimateShops.buyItems = config.getBoolean("buy-items");
        UltimateShops.sellItems = config.getBoolean("sell-items");
        UltimateShops.ignoreBuildPermissions = config.getBoolean("ignore-build-permissions");
        this.maxShopStacks = config.getInt("shop-max-capacity-in-stacks");
        this.chestInputRadius = config.getInt("chest-remote-restock-radius");
        if (this.premium) {
            UltimateShops.prefix = config.getString("plugin-prefix");
        }
        UltimateShops.prefix = ChatColor.translateAlternateColorCodes('&', UltimateShops.prefix);
        this.enforceShopLimit = config.getBoolean("enforce-shop-limit");
        this.useUpperSlabs = config.getBoolean("use-upper-slabs");
        this.suggestBuyPrices = config.getBoolean("suggest-buy-prices");
        this.suggestSellPrices = config.getBoolean("suggest-sell-prices");
        this.suggestQuantity = config.getBoolean("suggest-quantity");
        this.autoPurge = config.getBoolean("auto-purge");
        this.purgeAge = config.getInt("purge-age");
        this.shopsOnlyInTowns = config.getBoolean("only-allow-shops-in-towns");
        this.informCustomerOfTransaction = config.getBoolean("inform-customer-of-transaction");
        this.informHostOfTransaction = config.getBoolean("inform-host-of-transaction");
        this.informCustomerNotEnoughFunds = config.getBoolean("inform-customer-not-enough-funds");
        this.informCustomerShopOutOfFunds = config.getBoolean("inform-customer-shop-out-of-funds");
        this.informCustomerNotEnoughItems = config.getBoolean("inform-customer-not-enough-items");
        this.informCustomerShopOutOfItems = config.getBoolean("inform-customer-shop-out-of-items");
        this.informCustomerShopOutOfSpace = config.getBoolean("inform-customer-shop-out-of-space");
        this.informUserShopOutOfSpace = config.getBoolean("inform-user-shop-out-of-space");
        this.informHostOfEmptyShops = config.getBoolean("inform-host-of-empty-shops");
        this.informHostOfNearEmptyShops = config.getBoolean("inform-host-of-near-empty-shops");
        this.nearEmptyShopThreshold = config.getInt("near-empty-shop-threshold");
        this.transactionLogs = config.getBoolean("transaction-logs");
        if (this.debug) {
            Bukkit.getLogger().info(this.consolePrefix + "Settings Reloaded from config");
        }
        this.debug = config.getBoolean("debug");
    }
    
    public void loadShops() {
        this.shopsFile = new File(this.getDataFolder(), "shops.yml");
        this.shopsFileConfig = (FileConfiguration)new YamlConfiguration();
        if (!this.shopsFile.exists()) {
            Utilities.loadResource((Plugin)this, "shops.yml");
        }
        try {
            this.shopsFileConfig.load(this.shopsFile);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        for (final String locationString : this.shopsFileConfig.getKeys(false)) {
            if (locationString.equals("")) {
                continue;
            }
            final String[] locParts = locationString.split(",");
            final Location loc = new Location(Bukkit.getWorld(locParts[0]), (double)Integer.parseInt(locParts[1]), (double)Integer.parseInt(locParts[2]), (double)Integer.parseInt(locParts[3]));
            if (Bukkit.getWorld(locParts[0]) == null) {
                Bukkit.getLogger().warning(this.consolePrefix + "Could not load shop! World '" + locParts[0] + "' does not exist. Could not place shop");
            }
            else if (loc.getChunk() == null) {
                Bukkit.getLogger().warning(this.consolePrefix + "Could not load shop! Location " + Utilities.toLocString(loc) + " does not exist. Did you rollback, rename, or otherwise change this world?");
            }
            else {
                final OfflinePlayer host = Bukkit.getOfflinePlayer(UUID.fromString(this.shopsFileConfig.getString(locationString + ".host")));
                final UShop newShop = new UShop(this, host, loc, this.shopsFileConfig.getItemStack(locationString + ".item"), this.shopsFileConfig.getInt(locationString + ".buyPrice"), this.shopsFileConfig.getInt(locationString + ".sellPrice"), this.shopsFileConfig.getInt(locationString + ".stack"), this.shopsFileConfig.getInt(locationString + ".amount"), this.shopsFileConfig.getString(locationString + ".vendorNote"), this.shopsFileConfig.getLong(locationString + ".lastActive"), this.shopsFileConfig.getBoolean(locationString + ".admin"), this.shopsFileConfig.getString(locationString + ".buyCommands"), this.shopsFileConfig.getString(locationString + ".sellCommands"), this.shopsFileConfig.getString(locationString + ".tag"));
                this.shops.put(Utilities.toLocString(loc), newShop);
                this.shopsList.add(newShop);
                this.shopChunks.computeIfAbsent(Utilities.toChunkString(loc.getChunk()), k -> new ArrayList());
                this.shopChunks.get(Utilities.toChunkString(loc.getChunk())).add(this.shops.get(Utilities.toLocString(loc)));
            }
        }
        for (final UShop shop : this.shops.values()) {
            if (shop.getLocation().getWorld().getBlockAt(shop.getLocation()).getType() == Material.AIR) {
                shop.getLocation().getWorld().getBlockAt(shop.getLocation()).setType(XMaterial.STONE_SLAB.parseMaterial());
                if (!this.debug) {
                    continue;
                }
                Bukkit.getLogger().info(this.consolePrefix + "Floating shop found at " + Utilities.toLocString(shop.getLocation()) + "! Replacing with stone slab");
                Bukkit.getLogger().info(this.consolePrefix + " - To properly remove a ushop, right click the slab with a stick :)");
            }
        }
    }
    
    public void saveShops(final Map<String, UShop> shops) {
        final ArrayList<UShop> allShops = new ArrayList<UShop>(shops.values());
        for (final UShop shop : allShops) {
            final String loc = Utilities.toLocString(shop.getLocation());
            this.shopsFileConfig.set(loc + ".host", (Object)shop.getVendor().getUniqueId().toString());
            this.shopsFileConfig.set(loc + ".item", (Object)shop.getItem());
            this.shopsFileConfig.set(loc + ".buyPrice", (Object)shop.getBuyPrice());
            this.shopsFileConfig.set(loc + ".sellPrice", (Object)shop.getSellPrice());
            this.shopsFileConfig.set(loc + ".stack", (Object)shop.getStack());
            this.shopsFileConfig.set(loc + ".amount", (Object)shop.getAmount());
            this.shopsFileConfig.set(loc + ".vendorNote", (Object)shop.getVendorNote());
            this.shopsFileConfig.set(loc + ".lastActive", (Object)shop.getLastActive());
            this.shopsFileConfig.set(loc + ".admin", (Object)shop.getAdmin());
            this.shopsFileConfig.set(loc + ".tag", (Object)shop.getTag());
            this.shopsFileConfig.set(loc + ".buyCommands", (Object)Utilities.joinStringList(shop.getBuyCommands(), ";"));
            this.shopsFileConfig.set(loc + ".sellCommands", (Object)Utilities.joinStringList(shop.getSellCommands(), ";"));
            if (this.debug) {
                Bukkit.getLogger().info(this.consolePrefix + "Shop Saved. Located at: " + Utilities.toLocString(shop.getLocation()));
            }
        }
        try {
            this.shopsFileConfig.save(this.shopsFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadSuggestedPrices() {
        this.priceFile = new File(this.getDataFolder(), "suggested_prices.yml");
        this.priceFileConfig = (FileConfiguration)new YamlConfiguration();
        if (!this.priceFile.exists()) {
            Utilities.loadResource((Plugin)this, "suggested_prices.yml");
        }
        try {
            this.priceFileConfig.load(this.priceFile);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        this.suggestedBuyPrices.clear();
        this.suggestedSellPrices.clear();
        this.suggestedQuantity.clear();
        for (final String priceString : this.priceFileConfig.getKeys(false)) {
            if (priceString.equals("")) {
                continue;
            }
            final Material item = Material.matchMaterial(priceString);
            if (this.suggestBuyPrices) {
                this.suggestedBuyPrices.put(item, this.priceFileConfig.getInt(priceString + ".buyPrice"));
            }
            if (this.suggestSellPrices) {
                this.suggestedSellPrices.put(item, this.priceFileConfig.getInt(priceString + ".sellPrice"));
            }
            if (this.suggestQuantity) {
                this.suggestedQuantity.put(item, this.priceFileConfig.getInt(priceString + ".quantity"));
            }
            if (item != null) {
                if (!this.debug) {
                    continue;
                }
                Bukkit.getLogger().info(this.consolePrefix + "processed: " + item.name());
            }
            else {
                if (!this.debug) {
                    continue;
                }
                Bukkit.getLogger().info("[UltimateShops] Failed to load: " + priceString);
            }
        }
    }
    
    public void loadLangFile() {
        this.langFile = new File(this.getDataFolder(), "language.yml");
        this.langFileConfig = (FileConfiguration)new YamlConfiguration();
        if (!this.langFile.exists()) {
            Utilities.loadResource((Plugin)this, "language.yml");
        }
        try {
            this.langFileConfig.load(this.langFile);
        }
        catch (Exception e3) {
            e3.printStackTrace();
        }
        for (final String priceString : this.langFileConfig.getKeys(false)) {
            this.phrases.put(priceString, this.langFileConfig.getString(priceString));
        }
    }
    
    private void registerCommands() {
        this.getCommand("ushop").setExecutor((CommandExecutor)new UshopCommand(this));
        this.getCommand("ushops").setExecutor((CommandExecutor)new UshopsCommand(this));
        this.getCommand("ushop").setTabCompleter((TabCompleter)new TabCompleteHandler());
        this.getCommand("ushops").setTabCompleter((TabCompleter)new TabCompleteHandler());
    }
    
    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents((Listener)new ChunkLoadHandler(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new InventoryClickHandler(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new BlockClickHandler(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ItemBreakEventHandler(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new BlockEventHandler(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new LoginHandler(this), (Plugin)this);
        if (this.getServer().getPluginManager().getPlugin("ClearLag") != null) {
            this.getServer().getPluginManager().registerEvents((Listener)new ItemRemoveEventHandler(this), (Plugin)this);
        }
    }
    
    public FileConfiguration getShopsFileConfig() {
        return this.shopsFileConfig;
    }
    
    public Economy getEcon() {
        return this.econ;
    }
    
    public Map<String, UShop> getShops() {
        return this.shops;
    }
    
    public List<UShop> getShopList() {
        return this.shopsList;
    }
    
    public Map<String, ArrayList<UShop>> getShopChunks() {
        return this.shopChunks;
    }
    
    public ArrayList<UUID> getShopItems() {
        return this.shopItems;
    }
    
    public Map<Material, Integer> getSuggestedBuyPrices() {
        return this.suggestedBuyPrices;
    }
    
    public Map<Material, Integer> getSuggestedSellPrices() {
        return this.suggestedSellPrices;
    }
    
    public Map<Material, Integer> getSuggestedQuantity() {
        return this.suggestedQuantity;
    }
    
    public boolean getShopsOnlyInTowns() {
        return this.shopsOnlyInTowns;
    }
    
    public String getConsolePrefix() {
        return this.consolePrefix;
    }
    
    public int getMaxShopStacks() {
        return this.maxShopStacks;
    }
    
    public int getChestInputRadius() {
        return this.chestInputRadius;
    }
    
    public boolean getAutoPurge() {
        return this.autoPurge;
    }
    
    public int getPurgeAge() {
        return this.purgeAge;
    }
    
    public boolean getEnforceShopLimit() {
        return this.enforceShopLimit;
    }
    
    public boolean getUseUpperSlabs() {
        return this.useUpperSlabs;
    }
    
    public boolean getPremium() {
        return this.premium;
    }
    
    public boolean getTransactionLogs() {
        return this.transactionLogs;
    }
    
    public boolean getDebug() {
        return this.debug;
    }
    
    public String getVersion() {
        return this.getDescription().getVersion();
    }
    
    static {
        UltimateShops.prefix = "&c&l[&5&luShops&c&l] &8&l";
        UltimateShops.toolItem = Material.STICK;
        UltimateShops.displayShopItems = true;
        UltimateShops.buyItems = true;
        UltimateShops.sellItems = true;
        UltimateShops.ignoreBuildPermissions = false;
    }
}
