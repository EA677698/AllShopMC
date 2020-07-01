package allshop.allshop.utils;

import allshop.allshop.main.AllShop;
import allshop.allshop.shops.ShopType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
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
        Object[] listings;
        switch (type){
            case PLAYER_SHOP:
                listings = AllShop.instance.digitalListings;
                break;
            case AUCTION_HOUSE:
                listings = AllShop.instance.auctionListings;
                break;
            default:
                listings = AllShop.instance.serverListings;
            break;
        }
        for(int index = 1; index<listings.length; index++) {
            inv.addItem(addListingInfo(getListingItem(index, type), index, type));
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
            if(type!=ShopType.SERVER_SHOP){
                if(line.equals(ChatColor.LIGHT_PURPLE+"Seller: "+getSellerName(index, type))){
                    lore.remove(line);
                    continue;
                }
                if(line.equals(ChatColor.LIGHT_PURPLE+"Added: "+ getListingDate(index, type))){
                    lore.remove(line);
                    continue;
                }
            }
            if(type==ShopType.PLAYER_SHOP||type==ShopType.SERVER_SHOP){
                if(line.equals(ChatColor.LIGHT_PURPLE+"Price: "+getListingPrice(index,type))){
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

    public static boolean createListing(ShopType type, CommandSender sender, String[] args) {
        int count = 0;
        if (type==ShopType.PLAYER_SHOP) {
            for (int i = 1; i < AllShop.instance.digitalListings.length; i++) {
                if(AllShop.instance.data.getString("digital." + AllShop.instance.digitalListings[i] + ".Name")==null){
                    continue;
                }
                if (AllShop.instance.data.getString("digital." + AllShop.instance.digitalListings[i] + ".Name").equals(sender.getName())) {
                    count++;
                }
            }
        } else if(type==ShopType.AUCTION_HOUSE) {
            for (int i = 1; i < AllShop.instance.auctionListings.length; i++) {
                if(AllShop.instance.data.getString("auction." + AllShop.instance.auctionListings[i] + ".Name")==null){
                    continue;
                }
                if (AllShop.instance.data.getString("auction." + AllShop.instance.auctionListings[i] + ".Name").equals(sender.getName())) {
                    count++;
                }
            }
        }
        if (type==ShopType.SERVER_SHOP||AllShop.instance.LISTINGS_LIMIT == -1 || count < AllShop.instance.LISTINGS_LIMIT) {
            Player player = (Player) sender;
            int price = 0;
            String UUID = String.valueOf(player.getUniqueId());
            if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                if (args.length > 1) {
                    try {
                        if(type==ShopType.PLAYER_SHOP||type==ShopType.SERVER_SHOP) {
                            price = Integer.parseInt(args[1]);
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Price must be an integer");
                        return false;
                    }
                    String id;
                    if (type==ShopType.PLAYER_SHOP) {
                        id = "digital." + (int)(Math.random()*9824);
                    } else if(type==ShopType.AUCTION_HOUSE) {
                        id = "auction." + (int)(Math.random()*9824);
                    } else {
                        id = "server." + (int)(Math.random()*9824);
                    }
                    AllShop.instance.data.createSection(id);
                    if(type!=ShopType.SERVER_SHOP){
                        AllShop.instance.data.set(id + ".Date", java.time.LocalDate.now().toString());
                        AllShop.instance.data.set(id + ".UUID", UUID);
                        AllShop.instance.data.set(id + ".Name", player.getName());
                    }
                    if (type==ShopType.PLAYER_SHOP||type==ShopType.SERVER_SHOP) {
                        AllShop.instance.data.set(id + ".Price", price);
                    } else {
                        AllShop.instance.data.set(id + ".minBid", Integer.parseInt(args[1]));
                        AllShop.instance.data.set(id + ".Bid", Integer.parseInt(args[1]));
                    }
                    AllShop.instance.data.set(id + ".Items", player.getInventory().getItemInMainHand());
                    try {
                        AllShop.instance.data.save(new File(AllShop.instance.folder, "data.yml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    AllShop.loadData();
                } else {
                    if (type==ShopType.PLAYER_SHOP) {
                        sender.sendMessage(AllShop.instance.PREFIX+ChatColor.RED + " You must give a price!");
                    } else {
                        sender.sendMessage(AllShop.instance.PREFIX+ChatColor.RED + " You must give a starting bid!");
                    }
                }
            } else {
                sender.sendMessage(AllShop.instance.PREFIX+ChatColor.RED + " Your hand cannot be empty!");
            }
        } else {
            sender.sendMessage(AllShop.instance.PREFIX+ChatColor.RED + " You have reached the maximum listings limit");
        }
        return false;
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
            if(line.equals(ChatColor.LIGHT_PURPLE+"Seller: "+getSellerName(index, type))||line.equals(ChatColor.LIGHT_PURPLE+"Price: "+getListingPrice(index,type))){
                return item;
            }
        }
        lore.add("");
        lore.add(ChatColor.LIGHT_PURPLE+"Seller: "+getSellerName(index, type));
        if(type==ShopType.PLAYER_SHOP||type==ShopType.SERVER_SHOP){
            lore.add(ChatColor.LIGHT_PURPLE+"Price: "+getListingPrice(index,type));
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
            return AllShop.instance.data.getItemStack("digital."+AllShop.instance.digitalListings[index]+".Items");
        } else if(type==ShopType.AUCTION_HOUSE) {
            return AllShop.instance.data.getItemStack("auction."+AllShop.instance.auctionListings[index]+".Items");
        } else {
            return AllShop.instance.data.getItemStack("server."+AllShop.instance.serverListings[index]+".Items");
        }
    }

    public static String getSellerName(int index, ShopType type){
        if(type==ShopType.PLAYER_SHOP){
            return AllShop.instance.data.getString("digital."+AllShop.instance.digitalListings[index]+".Name");
        } else {
            return AllShop.instance.data.getString("auction."+AllShop.instance.auctionListings[index]+".Name");
        }
    }

    public static String getSellerUUID(int index, ShopType type){
        if(type==ShopType.PLAYER_SHOP) {
            return AllShop.instance.data.getString("digital."+AllShop.instance.digitalListings[index]+".UUID");
        } else {
            return AllShop.instance.data.getString("auction." + AllShop.instance.auctionListings[index] + ".UUID");
        }
    }

    public static int getListingPrice(int index, ShopType type) {
        if(type==ShopType.PLAYER_SHOP) {
            return AllShop.instance.data.getInt("digital." + AllShop.instance.digitalListings[index] + ".Price");
        } else {
            return AllShop.instance.data.getInt("server."+AllShop.instance.serverListings[index]+".Price");
        }
    }

    public static String getListingDate(int index, ShopType type){
        if(type==ShopType.PLAYER_SHOP){
            return AllShop.instance.data.getString("digital."+AllShop.instance.digitalListings[index]+".Date");
        } else {
            return AllShop.instance.data.getString("auction."+AllShop.instance.auctionListings[index]+".Date");
        }
    }

    public static int getMinBid(int index){
        return AllShop.instance.data.getInt("auction."+AllShop.instance.auctionListings[index]+".minBid");
    }

    public static int getCurrentBid(int index){
        return AllShop.instance.data.getInt("auction."+AllShop.instance.auctionListings[index]+".Bid");
    }

}
