// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.events.pluginsupport;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import org.bukkit.entity.Entity;
import com.palmergames.bukkit.towny.object.WorldCoord;
import org.bukkit.entity.Player;

public class TownySupport
{
    public static boolean isInTown(final Player player) {
        try {
            WorldCoord.parseWorldCoord((Entity)player).getTownBlock();
        }
        catch (NotRegisteredException e) {
            return false;
        }
        return true;
    }
}
