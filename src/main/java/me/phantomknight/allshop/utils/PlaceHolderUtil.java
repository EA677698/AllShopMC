package me.phantomknight.allshop.utils;

import me.phantomknight.allshop.gshops.ShopType;
import org.bukkit.Bukkit;


public class PlaceHolderUtil {

    public static String format(String message, int index, ShopType type){
        int finds = 0;
        if(!message.contains("%")){
            return message;
        }
        if(message.contains("%id%")){
            message = message.replace("%id%",String.valueOf(ListingsUtil.getListingID(index, type)));
            finds++;
        }
        if(message.contains("%seller%")){
            message = message.replace("%seller%",ListingsUtil.getSellerName(index, type));
            finds++;
        }
        if(message.contains("%price%")){
            message = message.replace("%price%",String.valueOf(ListingsUtil.getListingPrice(index, type)));
            finds++;
        }
        if(message.contains("%amount%")){
            message = message.replace("%amount%",String.valueOf(ListingsUtil.getListingItem(index, type).getAmount()));
            finds++;
        }
        if(message.contains("%item%")){
            message = message.replace("%item%",ListingsUtil.getListingItem(index, type).getType().name());
            finds++;
        }
        if(message.contains("%date%")){
            String date = ListingsUtil.getListingDate(index, type);
            date = date.replace("T"," ");
            if(date.contains(".")) {
                date = date.substring(0, date.indexOf("."));
            }
            message = message.replace("%date%",date);
            finds++;
        }
        if(message.contains("%UUID%")){
            message = message.replace("%UUID%",ListingsUtil.getSellerUUID(index, type).toString());
            finds++;
        }
        if(message.contains("%expires%")){
            message = message.replace("%expires%",ListingsUtil.timeUntilExpiration(type, index));
            finds++;
        }
        if(message.contains("%minBid%")){
            message = message.replace("%minBid%",String.valueOf(ListingsUtil.getMinBid(index)));
            finds++;
        }
        if(message.contains("%bid%")){
            message = message.replace("%bid%", String.valueOf(ListingsUtil.getCurrentBid(index)));
            finds++;
        }
        if(message.contains("%bidder%")){
            message = message.replace("%bidder%", Bukkit.getOfflinePlayer(ListingsUtil.getCurrentBidder(index)).getName());
        }
        if(finds>0){
            return format(message, index, type);
        } else {
            return message;
        }
    }

}
