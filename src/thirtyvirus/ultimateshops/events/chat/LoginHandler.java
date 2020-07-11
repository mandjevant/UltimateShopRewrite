// 
// Decompiled by Procyon v0.5.36
// 

package thirtyvirus.ultimateshops.events.chat;

import org.bukkit.event.EventHandler;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import thirtyvirus.ultimateshops.helpers.Utilities;
import java.util.Arrays;
import thirtyvirus.ultimateshops.shops.UShop;
import java.util.ArrayList;
import org.bukkit.event.player.PlayerJoinEvent;
import thirtyvirus.ultimateshops.UltimateShops;
import org.bukkit.event.Listener;

public class LoginHandler implements Listener
{
    private UltimateShops main;
    
    public LoginHandler(final UltimateShops main) {
        this.main = null;
        this.main = main;
    }
    
    @EventHandler
    public void onPlayerLogin(final PlayerJoinEvent e) {
        if (this.main.informHostOfNearEmptyShops) {
            final List<UShop> shops = new ArrayList<UShop>();
            for (final UShop shop : this.main.getShops().values()) {
                if (shop.getVendor().getUniqueId().equals(e.getPlayer().getUniqueId()) && shop.getAmount() <= shop.getStack() * this.main.nearEmptyShopThreshold && shop.getAmount() > 0 && shop.getBuyPrice() > 0 && !shop.getAdmin()) {
                    shops.add(shop);
                }
            }
            if (shops.size() > 0) {
                Utilities.informPlayer((CommandSender)e.getPlayer(), Arrays.asList(this.main.phrases.get("near-empty-shops-message") + ":"));
                ChatColor color = ChatColor.WHITE;
                for (final UShop shop2 : shops) {
                    Utilities.informPlayer((CommandSender)e.getPlayer(), Arrays.asList(color + Utilities.toLocString(shop2.getLocation()) + ": " + shop2.getItem().getType().name()));
                    if (color == ChatColor.GRAY) {
                        color = ChatColor.WHITE;
                    }
                    else {
                        color = ChatColor.GRAY;
                    }
                }
            }
        }
        if (this.main.informHostOfEmptyShops) {
            final List<UShop> shops = new ArrayList<UShop>();
            for (final UShop shop : this.main.getShops().values()) {
                if (shop.getVendor().getUniqueId().equals(e.getPlayer().getUniqueId()) && shop.getAmount() <= 0 && shop.getBuyPrice() > 0 && !shop.getAdmin()) {
                    shops.add(shop);
                }
            }
            if (shops.size() > 0) {
                Utilities.informPlayer((CommandSender)e.getPlayer(), Arrays.asList(this.main.phrases.get("empty-shops-message") + ":"));
                ChatColor color = ChatColor.WHITE;
                for (final UShop shop2 : shops) {
                    Utilities.informPlayer((CommandSender)e.getPlayer(), Arrays.asList(color + Utilities.toLocString(shop2.getLocation()) + ": " + shop2.getItem().getType().name()));
                    if (color == ChatColor.GRAY) {
                        color = ChatColor.WHITE;
                    }
                    else {
                        color = ChatColor.GRAY;
                    }
                }
            }
        }
    }
}
