// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.events.block;

import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.EventPriority;
import java.util.Iterator;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import thirtyvirus.ultimateshops.helpers.Utilities;
import thirtyvirus.ultimateshops.shops.UShop;
import org.bukkit.event.block.BlockPlaceEvent;
import thirtyvirus.ultimateshops.UltimateShops;
import org.bukkit.event.Listener;

public class BlockEventHandler implements Listener
{
    private UltimateShops main;
    
    public BlockEventHandler(final UltimateShops main) {
        this.main = null;
        this.main = main;
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (UShop.isSlab(event.getBlock().getType()) && this.main.getShops().get(Utilities.toLocString(event.getBlock().getLocation())) != null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        if (UShop.isSlab(event.getBlock().getType()) && this.main.getShops().get(Utilities.toLocString(event.getBlock().getLocation())) != null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBurn(final BlockBurnEvent event) {
        if (UShop.isSlab(event.getBlock().getType()) && this.main.getShops().get(Utilities.toLocString(event.getBlock().getLocation())) != null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockIgnite(final BlockIgniteEvent event) {
        if (UShop.isSlab(event.getBlock().getType()) && this.main.getShops().get(Utilities.toLocString(event.getBlock().getLocation())) != null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplode(final EntityExplodeEvent e) {
        final List<Block> blocks = (List<Block>)e.blockList();
        synchronized (blocks) {
            final Iterator<Block> it = blocks.iterator();
            while (it.hasNext()) {
                final Block block = it.next();
                if (UShop.isSlab(block.getType()) && this.main.getShops().containsKey(Utilities.toLocString(block.getLocation()))) {
                    it.remove();
                }
            }
        }
    }
    
    @EventHandler
    public void onBlockPickup(final EntityChangeBlockEvent event) {
        if (UShop.isSlab(event.getBlock().getType()) && this.main.getShops().get(Utilities.toLocString(event.getBlock().getLocation())) != null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPistonEvent(final BlockPistonExtendEvent event) {
        for (final Block block : event.getBlocks()) {
            if (UShop.isSlab(block.getType()) && this.main.getShops().get(Utilities.toLocString(block.getLocation())) != null) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPistonEvent(final BlockPistonRetractEvent event) {
        for (final Block block : event.getBlocks()) {
            if (UShop.isSlab(block.getType()) && this.main.getShops().get(Utilities.toLocString(block.getLocation())) != null) {
                event.setCancelled(true);
            }
        }
    }
}
