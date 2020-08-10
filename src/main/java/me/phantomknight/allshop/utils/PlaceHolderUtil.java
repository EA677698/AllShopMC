package me.phantomknight.allshop.utils;

import me.phantomknight.allshop.gshops.ShopType;
import org.bukkit.Bukkit;

import java.util.Objects;


public class PlaceHolderUtil {

    public static String format(String message, int index, ShopType type) {
        message = message.replace("%id%", String.valueOf(ListingsUtil.getListingID(index, type)));
        message = message.replace("%seller%", Objects.requireNonNull(ListingsUtil.getSellerName(index, type)));
        message = message.replace("%price%", String.valueOf(ListingsUtil.getListingPrice(index, type)));
        message = message.replace("%amount%", String.valueOf(ListingsUtil.getListingItem(index, type).getAmount()));
        message = message.replace("%item%", ListingsUtil.getListingItem(index, type).getType().name());
        if (message.contains("%date%")) {
            String date = ListingsUtil.getListingDate(index, type);
            date = Objects.requireNonNull(date).replace("T", " ");
            if (date.contains(".")) {
                date = date.substring(0, date.indexOf("."));
            }
            message = message.replace("%date%", date);
        }
        message = message.replace("%UUID%", Objects.requireNonNull(ListingsUtil.getSellerUUID(index, type)).toString());
        message = message.replace("%expires%", Objects.requireNonNull(ListingsUtil.timeUntilExpiration(type, index)));
        message = message.replace("%minBid%", String.valueOf(ListingsUtil.getMinBid(index)));
        message = message.replace("%bid%", String.valueOf(ListingsUtil.getCurrentBid(index)));
        message = message.replace("%bidder%", Objects.requireNonNull(Bukkit.getOfflinePlayer(ListingsUtil.getCurrentBidder(index)).getName()));
        return message;
    }

}
