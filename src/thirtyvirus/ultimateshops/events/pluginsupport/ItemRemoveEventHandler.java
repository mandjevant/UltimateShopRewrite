// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.events.pluginsupport;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.Iterator;
import org.bukkit.entity.Entity;
import java.util.ArrayList;
import me.minebuilders.clearlag.events.EntityRemoveEvent;
import thirtyvirus.ultimateshops.UltimateShops;
import org.bukkit.event.Listener;

public class ItemRemoveEventHandler implements Listener
{
    private UltimateShops main;
    
    public ItemRemoveEventHandler(final UltimateShops main) {
        this.main = null;
        this.main = main;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemRemove(final EntityRemoveEvent event) {
        final ArrayList<Entity> toRemove = new ArrayList<Entity>();
        for (final Entity e : event.getEntityList()) {
            if (this.main.getShopItems().contains(e.getUniqueId())) {
                toRemove.add(e);
            }
        }
        for (final Entity e : toRemove) {
            event.removeEntity(e);
        }
    }
}
