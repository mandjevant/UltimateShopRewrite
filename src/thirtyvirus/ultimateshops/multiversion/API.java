// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.multiversion;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.Material;
import java.util.Iterator;
import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import java.util.UUID;

public class API
{
    public static Entity getEntity(final UUID id) {
        if (Version.getVersion().isBiggerThan(Version.v1_10)) {
            return Bukkit.getEntity(id);
        }
        for (final World w : Bukkit.getWorlds()) {
            for (final Entity entity : w.getEntities()) {
                if (entity.getUniqueId() == id) {
                    return entity;
                }
            }
        }
        return null;
    }
    
    public static boolean isSlab(final Material material) {
        return (material.name().toLowerCase().contains("slab") || material.name().toLowerCase().contains("step")) && !material.name().toLowerCase().contains("double");
    }
    
    public static boolean isBottomSlab(final Block block) {
        if (block == null || !isSlab(block.getType())) {
            return false;
        }
        if (Version.getVersion().isBiggerThan(Version.v1_12)) {
            return block.getBlockData().getAsString().contains("type=bottom");
        }
        return block.getData() < 8;
    }
    
    public static boolean isTopSlab(final Block block) {
        if (block == null || !isSlab(block.getType())) {
            return false;
        }
        if (Version.getVersion().isBiggerThan(Version.v1_12)) {
            return block.getBlockData().getAsString().contains("type=top");
        }
        return block.getData() > 7;
    }
    
    public static void openBook(final ItemStack book, final Player player) {
        if (Version.getVersion().isBiggerThan(Version.v1_14)) {
            BookUtil_1_14_4.openBook(book, player);
        }
        else if (Version.getVersion().isBiggerThan(Version.v1_8)) {
            BookUtil_1_9.openBook(book, player);
        }
        else {
            BookUtil_1_8.openBook(book, player);
        }
    }
    
    public ItemStack getItemInHand(final Player player) {
        if (Version.getVersion().isBiggerThan(Version.v1_8)) {
            return player.getInventory().getItemInMainHand();
        }
        return player.getInventory().getItemInHand();
    }
}
