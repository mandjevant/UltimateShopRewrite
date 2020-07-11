// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.multiversion;

import org.bukkit.Bukkit;

public enum Version
{
    UNKNOWN, 
    v1_7, 
    v1_8, 
    v1_9, 
    v1_10, 
    v1_11, 
    v1_12, 
    v1_13, 
    v1_14, 
    v1_14_3;
    
    public static Version getVersion() {
        if (Bukkit.getVersion().contains("1.7")) {
            return Version.v1_7;
        }
        if (Bukkit.getVersion().contains("1.8")) {
            return Version.v1_8;
        }
        if (Bukkit.getVersion().contains("1.9")) {
            return Version.v1_9;
        }
        if (Bukkit.getVersion().contains("1.10")) {
            return Version.v1_10;
        }
        if (Bukkit.getVersion().contains("1.11")) {
            return Version.v1_11;
        }
        if (Bukkit.getVersion().contains("1.12")) {
            return Version.v1_12;
        }
        if (Bukkit.getVersion().contains("1.13")) {
            return Version.v1_13;
        }
        if (!Bukkit.getVersion().contains("1.14")) {
            return Version.UNKNOWN;
        }
        if (Bukkit.getVersion().equals("1.14") || Bukkit.getVersion().contains("1.14 ") || Bukkit.getVersion().contains("1.14.1") || Bukkit.getVersion().contains("1.14.2") || Bukkit.getVersion().contains("1.14.3")) {
            return Version.v1_14;
        }
        return Version.v1_14_3;
    }
    
    public static String getBukkitVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }
    
    public boolean isBiggerThan(final Version version) {
        final int current = this.getIndex();
        final int param = version.getIndex();
        return current > param;
    }
    
    public int getIndex() {
        int index = 0;
        for (final Version v : values()) {
            if (this.equals(v)) {
                return index;
            }
            ++index;
        }
        return -1;
    }
}
