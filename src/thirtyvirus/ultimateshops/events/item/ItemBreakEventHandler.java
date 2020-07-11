// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.events.item;

import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import thirtyvirus.ultimateshops.helpers.Utilities;
import org.bukkit.event.entity.ItemDespawnEvent;
import thirtyvirus.ultimateshops.UltimateShops;
import org.bukkit.event.Listener;

public class ItemBreakEventHandler implements Listener
{
    private UltimateShops main;
    
    public ItemBreakEventHandler(final UltimateShops main) {
        this.main = null;
        this.main = main;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDespawn(final ItemDespawnEvent event) {
        if (Utilities.isDisplayItem(event.getEntity())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHopperPickup(final InventoryPickupItemEvent event) {
        if (Utilities.isDisplayItem(event.getItem())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerFish(final PlayerFishEvent event) {
        if (event.getCaught() instanceof Item) {
            final Item item = (Item)event.getCaught();
            if (Utilities.isDisplayItem(item)) {
                event.setCancelled(true);
            }
        }
    }
}
