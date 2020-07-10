package me.phantomknight.allshop.utils;

import me.phantomknight.allshop.gshops.Shop;
import me.phantomknight.allshop.main.AllShop;
import me.phantomknight.allshop.gshops.ShopType;
import org.bukkit.Bukkit;
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
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListingsUtil {


    public static void loadOptions(Inventory inv, int currentPage, int totalPages){
        inv.setItem(53,createGuiItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN+"Next", ChatColor.LIGHT_PURPLE+"Next Page"));
        inv.setItem(45,createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED+"Back", ChatColor.LIGHT_PURPLE+"Back to the Last Page"));
        inv.setItem(49,createGuiItem(Material.PAPER, ChatColor.AQUA+"Page: "+currentPage+"/"+totalPages));
    }

    public static void loadListings(Shop shop, ShopType type){
        if(type==ShopType.PLAYER_SHOP) {
            for (int i = 1; i < getListings(type).length; i++) {
                if (isListingExpired(type, i)) {
                    AllShop.instance.data.set(getMainKey(type) + getListings(type)[i], null);
                }
            }
            try {
                AllShop.instance.data.save(new File(AllShop.instance.folder, "data.yml"));
            } catch (IOException e) {
                if (AllShop.instance.DEBUG) {
                    e.printStackTrace();
                }
            }
            AllShop.instance.loadData();
        }
        Object[] listings = getListings(type);
        if(AllShop.instance.DEBUG){
            System.out.println(AllShop.instance.PREFIX+"ShopType: "+type);
            System.out.println(AllShop.instance.PREFIX+"Listings size: "+(listings.length-1));
        }
        int page = 1;
        int item = 0;
        for(int index = 1; index<listings.length; index++) {
            if(item>44){
                page++;
                item = 0;
            }
            if(AllShop.instance.DEBUG){
                System.out.println(AllShop.instance.PREFIX+"Listings size: "+getListings(type).length);
                System.out.println(AllShop.instance.PREFIX+"Available Pages: "+shop.getPages().size());
                System.out.println(AllShop.instance.PREFIX+"Page: "+ page);
                System.out.println(AllShop.instance.PREFIX+"item: "+ item);
                System.out.println(AllShop.instance.PREFIX+"index: "+ index);

            }
            shop.getPage(page)[item] = addListingInfo(getListingItem(index, type), index, type);
            item++;
        }
    }

    public static boolean isListingExpired(ShopType type, int index){
        String day = getListingDate(index, type).substring(getListingDate(index, type).lastIndexOf("-")+1);
        String now = java.time.LocalDate.now().toString().substring(java.time.LocalDate.now().toString().lastIndexOf("-")+1);
        if(day.contains("0")){
            day = day.substring(1);
        }
        if(now.contains("0")){
            now = now.substring(1);
        }
        return Integer.parseInt(now)-Integer.parseInt(day)>=AllShop.instance.EXPIRATION;
    }

    public static void loadPage(Shop shop){
        int index = 0;
        for(ItemStack item: shop.getPage(shop.getCurrentPage())){
            shop.getInv().setItem(index,item);
            index++;
        }
    }

    @SuppressWarnings("UnnecessaryContinue")
    public static ItemStack removeListingInfo(ItemStack item, int index, ShopType type){
        ItemMeta meta = item.getItemMeta();
        if(meta.getLore()!=null) {
            CopyOnWriteArrayList<String> lore = new CopyOnWriteArrayList<>(meta.getLore());
            for (String line : lore) {
                if (line.equals(ChatColor.LIGHT_PURPLE + "ID: " + getListingID(index, type))) {
                    lore.remove(line);
                    continue;
                }
                if (type != ShopType.SERVER_SHOP) {
                    if (line.equals(ChatColor.LIGHT_PURPLE + "Seller: " + getSellerName(index, type))) {
                        lore.remove(line);
                        continue;
                    }
                    if (line.equals(ChatColor.LIGHT_PURPLE + "Added: " + getListingDate(index, type))) {
                        lore.remove(line);
                        continue;
                    }
                }
                if (type != ShopType.AUCTION_HOUSE) {
                    if (line.equals(ChatColor.LIGHT_PURPLE + "Price: " + getListingPrice(index, type))) {
                        lore.remove(line);
                        continue;
                    }
                } else if (type == ShopType.AUCTION_HOUSE) {
                    if (line.equals(ChatColor.LIGHT_PURPLE + "Starting Bid: " + getMinBid(index))) {
                        lore.remove(line);
                        continue;
                    }
                    if (line.equals(ChatColor.LIGHT_PURPLE + "Current Bid: " + getCurrentBid(index))) {
                        lore.remove(line);
                        continue;
                    }
                }
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static String generateID(ShopType type){
        String id = getMainKey(type)+(int)(Math.random()*9824);
        for(Object obj : getListings(type)){
            if(obj.toString().equals(id)){
                return generateID(type);
            }
        }
        return id;
    }

    public static void removeListing(Player player ,ShopType type, String ID, boolean returnItem, boolean admin){
        try{
            boolean located = false;
            int id = Integer.parseInt(ID);
            int index = -1;
            for(Object key:getListings(type)){
                index++;
                if(id==Integer.parseInt(key.toString())){
                    located = true;
                    break;
                }
            }
            if(located) {
                if(!admin){
                    if(getSellerName(index, type).equals(player.getName())){
                        if(returnItem){
                            Bukkit.getOfflinePlayer(UUID.fromString(getSellerUUID(index,type))).getPlayer().getInventory().addItem(ListingsUtil.removeListingInfo(getListingItem(index,type),index,type));
                        }
                        AllShop.instance.data.set(getMainKey(type) + id, null);
                        try {
                            AllShop.instance.data.save(new File(AllShop.instance.folder, "data.yml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        AllShop.instance.loadData();
                        player.sendMessage(AllShop.instance.PREFIX+ColorUtils.format(AllShop.instance.customMessages[26]));
                    } else {
                        player.sendMessage(AllShop.instance.PREFIX+ColorUtils.format(AllShop.instance.customMessages[19]));
                    }
                } else {
                    if(returnItem){
                        Bukkit.getOfflinePlayer(UUID.fromString(getSellerUUID(index,type))).getPlayer().getInventory().addItem(ListingsUtil.removeListingInfo(getListingItem(index,type),index,type));
                    }
                    AllShop.instance.data.set(getMainKey(type) + id, null);
                    try {
                        AllShop.instance.data.save(new File(AllShop.instance.folder, "data.yml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AllShop.instance.loadData();
                    player.sendMessage(AllShop.instance.PREFIX+ColorUtils.format(AllShop.instance.customMessages[26]));
                }
            } else {
                player.sendMessage(AllShop.instance.PREFIX+ColorUtils.format(AllShop.instance.customMessages[28]));
            }

        } catch (Exception e){
            e.printStackTrace();
            if(AllShop.instance.DEBUG){
                e.printStackTrace();
            }
            player.sendMessage(AllShop.instance.PREFIX+AllShop.instance.customMessages[2]);
            return;
        }
    }


    public static void createListing(ShopType type, CommandSender sender, String[] args) {
        int count = 0;
        if(AllShop.instance.LISTINGS_LIMIT != -1) {
            if (type != ShopType.SERVER_SHOP) {
                for (int i = 1; i < getListings(type).length; i++) {
                    if (AllShop.instance.data.getString(getMainKey(type) + getListings(type)[i] + ".Name") == null) {
                        continue;
                    }
                    if (AllShop.instance.data.getString(getMainKey(type) + getListings(type)[i] + ".Name").equals(sender.getName())) {
                        count++;
                    }
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
                        if(AllShop.instance.DEBUG){
                            e.printStackTrace();
                        }
                        sender.sendMessage(AllShop.instance.PREFIX+AllShop.instance.customMessages[3]);
                        return;
                    }
                    String id = generateID(type);
                    AllShop.instance.data.createSection(id);
                    if(type!=ShopType.SERVER_SHOP){
                        AllShop.instance.data.set(id + ".Date", java.time.LocalDate.now().toString());
                        AllShop.instance.data.set(id + ".UUID", UUID);
                        AllShop.instance.data.set(id + ".Name", player.getName());
                    }
                    if (type!=ShopType.AUCTION_HOUSE) {
                        AllShop.instance.data.set(id + ".Price", price);
                    } else {
                        AllShop.instance.data.set(id + ".minBid", Integer.parseInt(args[1]));
                        AllShop.instance.data.set(id + ".Bid", Integer.parseInt(args[1]));
                    }
                    AllShop.instance.data.set(id + ".Items", player.getInventory().getItemInMainHand());
                    ItemStack item = player.getInventory().getItemInMainHand();
                    String name;
                    if(item.hasItemMeta()){
                        name = item.getItemMeta().getDisplayName();
                    }
                    else{
                        name = item.getType().toString();
                    }
                    if(type!=ShopType.AUCTION_HOUSE) {
                        player.sendMessage(AllShop.instance.PREFIX + ChatColor.GREEN + "You have successfully sold your " + name + "["
                                + player.getInventory().getItemInMainHand().getAmount() + "] for " + price);
                    } else {
                        player.sendMessage(AllShop.instance.PREFIX + ChatColor.GREEN + "You have successfully auctioned your " + name + "["
                                + player.getInventory().getItemInMainHand().getAmount() + "] for a minimum bid of " + args[1]);

                    }
                    try {
                        AllShop.instance.data.save(new File(AllShop.instance.folder, "data.yml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    AllShop.instance.loadData();
                } else {
                    if (type==ShopType.PLAYER_SHOP) {
                        sender.sendMessage(AllShop.instance.PREFIX+ColorUtils.format(AllShop.instance.customMessages[9]));
                    } else {
                        sender.sendMessage(AllShop.instance.PREFIX+ChatColor.RED + "You must give a starting bid!");
                    }
                }
            } else {
                sender.sendMessage(AllShop.instance.PREFIX+ColorUtils.format(AllShop.instance.customMessages[7]));
            }
        } else {
            sender.sendMessage(AllShop.instance.PREFIX+ColorUtils.format(AllShop.instance.customMessages[8]));
        }
    }

    private static ItemStack addListingInfo(ItemStack item, int index, ShopType type){
        ItemMeta meta = item.getItemMeta();
        List<String> lore;
        if(meta.hasLore()){
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }
        for(String line : lore){
            if(type==ShopType.SERVER_SHOP){
                if(line.equals(ChatColor.LIGHT_PURPLE+"Price: "+getListingPrice(index,type))){
                    return item;
                }
            } else if(line.equals(ChatColor.LIGHT_PURPLE+"Seller: "+getSellerName(index, type))){
                return item;
            }
        }
        //lore.add("");
        lore.add(ChatColor.LIGHT_PURPLE+"ID: "+getListingID(index, type));
        if(type!=ShopType.SERVER_SHOP) {
            lore.add(ChatColor.LIGHT_PURPLE + "Seller: " + getSellerName(index, type));
            lore.add(ChatColor.LIGHT_PURPLE + "Added: " + getListingDate(index, type));
        }
        if(type==ShopType.PLAYER_SHOP||type==ShopType.SERVER_SHOP){
            lore.add(ChatColor.LIGHT_PURPLE+"Price: "+getListingPrice(index,type));
        } else if(type==ShopType.AUCTION_HOUSE) {
            lore.add(ChatColor.LIGHT_PURPLE+"Starting Bid: "+getMinBid(index));
            lore.add(ChatColor.LIGHT_PURPLE+"Current Bid: "+getCurrentBid(index));
        }meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public static int getListingID(int index, ShopType type){
        String num = (String) getListings(type)[index];
        return Integer.parseInt(num);
    }

    public static ItemStack getListingItem(int index, ShopType type){
            return AllShop.instance.data.getItemStack(getMainKey(type)+getListings(type)[index]+".Items").clone();
    }

    public static String getSellerName(int index, ShopType type){
        if(type!=ShopType.SERVER_SHOP){
            return AllShop.instance.data.getString(getMainKey(type)+getListings(type)[index]+".Name");
        }
        return null;
    }

    public static String getSellerUUID(int index, ShopType type){
        if(type!=ShopType.SERVER_SHOP) {
            return AllShop.instance.data.getString(getMainKey(type)+getListings(type)[index] + ".UUID");
        }
        return null;
    }

    public static int getListingPrice(int index, ShopType type) {
        if(type!=ShopType.AUCTION_HOUSE) {
            return AllShop.instance.data.getInt(getMainKey(type)+getListings(type)[index] + ".Price");
        }
        return 0;
    }

    public static String getListingDate(int index, ShopType type){
        if(type!=ShopType.SERVER_SHOP){
            return AllShop.instance.data.getString(getMainKey(type)+getListings(type)[index]+".Date");
        }
        return null;
    }

    public static int getMinBid(int index){
        return AllShop.instance.data.getInt("auction."+AllShop.instance.auctionListings[index]+".minBid");
    }

    public static int getCurrentBid(int index){
        return AllShop.instance.data.getInt("auction."+AllShop.instance.auctionListings[index]+".Bid");
    }

    public static String getMainKey(ShopType type){
        switch (type){
            case PLAYER_SHOP:
                return "digital.";
            case AUCTION_HOUSE:
                return "auction.";
            default:
                return "server.";
        }
    }

    public static Object[] getListings(ShopType type){
        switch (type){
            case PLAYER_SHOP:
                return AllShop.instance.digitalListings;
            case AUCTION_HOUSE:
                return AllShop.instance.auctionListings;
            default:
                return AllShop.instance.serverListings;
        }
    }

}
