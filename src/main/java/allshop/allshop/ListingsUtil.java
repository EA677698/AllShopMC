package allshop.allshop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListingsUtil {



    public static void loadOptions(Inventory inv, int currentPage, int totalPages){
        inv.setItem(53,createGuiItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN+"Next", ChatColor.LIGHT_PURPLE+"Next Page"));
        inv.setItem(45,createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED+"Back", ChatColor.LIGHT_PURPLE+"Back to the Last Page"));
        inv.setItem(49,createGuiItem(Material.PAPER, ChatColor.AQUA+"Page: "+currentPage+"/"+totalPages));
    }

    public static void loadListings(Inventory inv, ShopType type){
        if(type==ShopType.PLAYER_SHOP){
            for(int index = 1; index<AllShop.digitalListings.length; index++) {
                inv.addItem(addListingInfo(getListingItem(index, type), index, type));
            }
        } else if(type==ShopType.AUCTION_HOUSE) {
            for(int index = 1; index<AllShop.auctionListings.length; index++){
                inv.addItem(addListingInfo(getListingItem(index, type), index, type));
            }
        }
    }

    public static ItemStack removeListingInfo(ItemStack item, int index, ShopType type){
        ItemStack temp = item;
        ItemMeta meta = temp.getItemMeta();
        CopyOnWriteArrayList<String> lore = new CopyOnWriteArrayList<>();
        for(String line: meta.getLore()){
            lore.add(line);
        }
        for(String line : lore){
            if(line.equals(ChatColor.LIGHT_PURPLE+"Seller: "+getSellerName(index, type))){
                lore.remove(line);
                continue;
            }
            if(line.equals(ChatColor.LIGHT_PURPLE+"Added: "+ getListingDate(index, type))){
                lore.remove(line);
                continue;
            }
            if(type==ShopType.PLAYER_SHOP){
                if(line.equals(ChatColor.LIGHT_PURPLE+"Price: "+getListingPrice(index))){
                    lore.remove(line);
                    continue;
                }
            } else if(type==ShopType.AUCTION_HOUSE) {
                if(line.equals(ChatColor.LIGHT_PURPLE+"Starting Bid: "+getMinBid(index))){
                    lore.remove(line);
                    continue;
                }
                if(line.equals(ChatColor.LIGHT_PURPLE+"Current Bid: "+getCurrentBid(index))){
                    lore.remove(line);
                    continue;
                }
            }
        }
        meta.setLore(lore);
        temp.setItemMeta(meta);
        return temp;
    }

    private static ItemStack addListingInfo(ItemStack item, int index, ShopType type){
        ItemStack temp = item;
        ItemMeta meta = temp.getItemMeta();
        List<String> lore;
        if(meta.hasLore()){
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }
        for(String line : lore){
            if(line.equals(ChatColor.LIGHT_PURPLE+"Seller: "+getSellerName(index, type))){
                return item;
            }
        }
        lore.add("");
        lore.add(ChatColor.LIGHT_PURPLE+"Seller: "+getSellerName(index, type));
        if(type==ShopType.PLAYER_SHOP){
            lore.add(ChatColor.LIGHT_PURPLE+"Price: "+getListingPrice(index));
        } else if(type==ShopType.AUCTION_HOUSE) {
            lore.add(ChatColor.LIGHT_PURPLE+"Starting Bid: "+getMinBid(index));
            lore.add(ChatColor.LIGHT_PURPLE+"Current Bid: "+getCurrentBid(index));
        }
        lore.add(ChatColor.LIGHT_PURPLE+"Added: "+ getListingDate(index, type));
        meta.setLore(lore);
        temp.setItemMeta(meta);
        return temp;
    }

    protected static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getListingItem(int index, ShopType type){
        if(type==ShopType.PLAYER_SHOP){
            return AllShop.data.getItemStack("digital."+AllShop.digitalListings[index]+".Items");
        } else {
            return AllShop.data.getItemStack("auction."+AllShop.auctionListings[index]+".Items");
        }
    }

    public static String getSellerName(int index, ShopType type){
        if(type==ShopType.PLAYER_SHOP){
            return AllShop.data.getString("digital."+AllShop.digitalListings[index]+".Name");
        } else {
            return AllShop.data.getString("auction."+AllShop.auctionListings[index]+".Name");

        }
    }

    public static String getSellerUUID(int index, ShopType type){
        if(type==ShopType.PLAYER_SHOP) {
            return AllShop.data.getString("digital."+AllShop.digitalListings[index]+".UUID");
        } else {
            return AllShop.data.getString("auction." + AllShop.auctionListings[index] + ".UUID");
        }
    }

    public static int getListingPrice(int index) {
        return AllShop.data.getInt("digital."+AllShop.digitalListings[index]+".Price");
    }

    public static String getListingDate(int index, ShopType type){
        if(type==ShopType.PLAYER_SHOP){
            return AllShop.data.getString("digital."+AllShop.digitalListings[index]+".Date");
        } else {
            return AllShop.data.getString("auction."+AllShop.auctionListings[index]+".Date");
        }
    }

    public static int getMinBid(int index){
        return AllShop.data.getInt("auction."+AllShop.auctionListings[index]+".minBid");
    }

    public static int getCurrentBid(int index){
        return AllShop.data.getInt("auction."+AllShop.auctionListings[index]+".Bid");
    }

}
