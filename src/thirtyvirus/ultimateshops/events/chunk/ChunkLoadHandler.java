// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.events.chunk;

import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import java.util.Iterator;
import org.bukkit.entity.Entity;
import thirtyvirus.ultimateshops.shops.UShop;
import org.bukkit.entity.Item;
import org.bukkit.Bukkit;
import thirtyvirus.ultimateshops.helpers.Utilities;
import org.bukkit.event.world.ChunkLoadEvent;
import thirtyvirus.ultimateshops.UltimateShops;
import org.bukkit.event.Listener;

public class ChunkLoadHandler implements Listener
{
    private UltimateShops main;
    
    public ChunkLoadHandler(final UltimateShops main) {
        this.main = null;
        this.main = main;
    }
    
    @EventHandler
    public void onChunkLoad(final ChunkLoadEvent event) {
        if (this.main.getShopChunks().get(Utilities.toChunkString(event.getChunk())) == null) {
            return;
        }
        if (this.main.getDebug()) {
            Bukkit.getLogger().info(this.main.getConsolePrefix() + "uShop Chunk Loaded: " + event.getChunk().getWorld().getName() + ", " + Utilities.toChunkString(event.getChunk()).replaceAll(",", ", "));
        }
        for (final Entity e : event.getChunk().getEntities()) {
            if (this.main.getShopItems().contains(e.getUniqueId()) || (e instanceof Item && Utilities.isDisplayItem((Item)e))) {
                e.remove();
                this.main.getShopItems().remove(e.getUniqueId());
            }
        }
        for (final UShop shop : this.main.getShopList()) {
            final Location location = shop.getLocation();
            if (location != null) {
                if (!location.getWorld().getName().equals(event.getChunk().getWorld().getName())) {
                    continue;
                }
                if (location.getBlockX() < event.getChunk().getX() * 16 || location.getBlockX() >= event.getChunk().getX() * 16 + 16 || location.getBlockZ() < event.getChunk().getZ() * 16 || location.getBlockZ() >= event.getChunk().getZ() * 16 + 16) {
                    continue;
                }
                shop.spawnDisplayItem();
            }
        }
    }
    
    @EventHandler
    public void onChunkUnload(final ChunkUnloadEvent event) {
        if (this.main.getShopChunks().get(Utilities.toChunkString(event.getChunk())) == null) {
            return;
        }
        if (this.main.getDebug()) {
            Bukkit.getLogger().info(this.main.getConsolePrefix() + "uShop Chunk Unloaded: " + event.getChunk().getWorld().getName() + ", " + Utilities.toChunkString(event.getChunk()).replaceAll(",", ", "));
        }
        for (final Entity e : event.getChunk().getEntities()) {
            if (this.main.getShopItems().contains(e.getUniqueId()) || (e instanceof Item && Utilities.isDisplayItem((Item)e))) {
                e.remove();
                this.main.getShopItems().remove(e.getUniqueId());
            }
        }
    }
}
