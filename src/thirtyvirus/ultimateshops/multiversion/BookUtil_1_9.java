// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.multiversion;

import org.bukkit.Bukkit;
import java.util.Iterator;
import java.util.List;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.lang.reflect.Method;

public class BookUtil_1_9
{
    private static boolean initialised;
    private static Method getHandle;
    private static Method openBook;
    
    public static boolean isInitialised() {
        return BookUtil_1_9.initialised;
    }
    
    public static boolean openBook(final ItemStack i, final Player p) {
        if (!BookUtil_1_9.initialised) {
            return false;
        }
        final ItemStack held = p.getInventory().getItemInMainHand();
        try {
            p.getInventory().setItemInMainHand(i);
            sendPacket(i, p);
        }
        catch (ReflectiveOperationException e) {
            e.printStackTrace();
            BookUtil_1_9.initialised = false;
        }
        p.getInventory().setItemInMainHand(held);
        return BookUtil_1_9.initialised;
    }
    
    private static void sendPacket(final ItemStack i, final Player p) throws ReflectiveOperationException {
        final Object entityplayer = BookUtil_1_9.getHandle.invoke(p, new Object[0]);
        final Class<?> enumHand = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("EnumHand");
        final Object[] enumArray = (Object[])enumHand.getEnumConstants();
        BookUtil_1_9.openBook.invoke(entityplayer, getItemStack(i), enumArray[0]);
    }
    
    public static Object getItemStack(final ItemStack item) {
        try {
            final Method asNMSCopy = ReflectionUtils.getMethod(ReflectionUtils.PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack"), "asNMSCopy", ItemStack.class);
            return asNMSCopy.invoke(ReflectionUtils.PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack"), item);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void setPages(final BookMeta metadata, final List<String> pages) {
        try {
            final List<Object> p = (List<Object>)ReflectionUtils.getField(ReflectionUtils.PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftMetaBook"), true, "pages").get(metadata);
            for (final String text : pages) {
                final Object page = ReflectionUtils.invokeMethod(ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent$ChatSerializer").newInstance(), "a", text);
                p.add(page);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        BookUtil_1_9.initialised = false;
        try {
            BookUtil_1_9.getHandle = ReflectionUtils.getMethod("CraftPlayer", ReflectionUtils.PackageType.CRAFTBUKKIT_ENTITY, "getHandle", (Class<?>[])new Class[0]);
            BookUtil_1_9.openBook = ReflectionUtils.getMethod("EntityPlayer", ReflectionUtils.PackageType.MINECRAFT_SERVER, "a", ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("ItemStack"), ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("EnumHand"));
            BookUtil_1_9.initialised = true;
        }
        catch (ReflectiveOperationException e) {
            e.printStackTrace();
            Bukkit.getServer().getLogger().warning("Cannot force open book!");
            BookUtil_1_9.initialised = false;
        }
    }
}
