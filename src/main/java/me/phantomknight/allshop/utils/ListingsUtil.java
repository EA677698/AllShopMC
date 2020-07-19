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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListingsUtil {


    public static void loadOptions(Inventory inv, int currentPage, int totalPages) {
        inv.setItem(53, createGuiItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + "Next", ChatColor.LIGHT_PURPLE + "Next Page"));
        inv.setItem(45, createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Back", ChatColor.LIGHT_PURPLE + "Back to the Last Page"));
        inv.setItem(49, createGuiItem(Material.PAPER, ChatColor.AQUA + "Page: " + currentPage + "/" + totalPages));
    }

    public static void loadListings(Shop shop, ShopType type) {
        if (type != ShopType.SERVER_SHOP) {
            for (int i = 1; i < getListings(type).length; i++) {
                if (AllShop.instance.config.getInt("days-before-removal") != 0&&isListingExpired(type, i)) {
                    Player player;
                    if (type == ShopType.PLAYER_SHOP) {
                        player = Bukkit.getOfflinePlayer(getSellerUUID(i, type)).getPlayer();
                        player.getInventory().addItem(getListingItem(i, type));
                        if (player.isOnline()) {
                            player.sendMessage(AllShop.instance.PREFIX + ChatColor.GREEN + "Your item has been returned");
                        }
                    } else {
                        player = Bukkit.getOfflinePlayer(getCurrentBidder(i)).getPlayer();
                        player.getInventory().addItem(getListingItem(i, type));
                        AllShop.instance.econ.withdrawPlayer(player, getCurrentBid(i));
                        AllShop.instance.econ.depositPlayer(Bukkit.getPlayer(getSellerUUID(i, type)), getCurrentBid(i));
                        if (player.isOnline()) {
                            if(player.equals(Bukkit.getPlayer(getSellerUUID(i,type)).getPlayer())){
                                player.sendMessage(AllShop.instance.PREFIX+ChatColor.GREEN+"Your item has been returned");
                            } else {
                                player.sendMessage(AllShop.instance.PREFIX + ChatColor.GREEN + "You have successfully won an auction!");
                            }
                        }
                    }
                    AllShop.instance.data.set(getMainKey(type) + getListings(type)[i], null);
                }
            }
            try {
                AllShop.instance.data.save(new File(AllShop.instance.folder, "data.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            AllShop.instance.loadData();
        }
        Object[] listings = getListings(type);
        if (AllShop.instance.DEBUG) {
            System.out.println(AllShop.instance.PREFIX + "LOAD LISTINGS SECTION");
            System.out.println(AllShop.instance.PREFIX + "ShopType: " + type);
            System.out.println(AllShop.instance.PREFIX + "Listings size: " + (listings.length - 1));
        }
        int page = 1;
        int item = 0;
        for (int index = 1; index < listings.length; index++) {
            if (item > 44) {
                page++;
                item = 0;
            }
            if (AllShop.instance.DEBUG) {
                System.out.println(AllShop.instance.PREFIX + "Listings size: " + getListings(type).length);
                System.out.println(AllShop.instance.PREFIX + "Available Pages: " + shop.getPages().size());
                System.out.println(AllShop.instance.PREFIX + "Page: " + page);
                System.out.println(AllShop.instance.PREFIX + "item: " + item);
                System.out.println(AllShop.instance.PREFIX + "index: " + index);

            }
            shop.getPage(page)[item] = addListingInfo(getListingItem(index, type), index, type);
            item++;
        }
    }

    public static boolean isListingExpired(ShopType type, int index) {
        return LocalDateTime.now().isAfter(LocalDateTime.parse(getListingDate(index, type)).plusDays(AllShop.instance.config.getInt("days-before-removal")));
    }

    public static String timeUntilExpiration(ShopType type, int index) {
        if (!isListingExpired(type, index)) {
            if (AllShop.instance.config.getInt("days-before-removal") == 0) {
                return "never";
            }
            Duration duration = Duration.between(LocalDateTime.now()
                    , LocalDateTime.parse(getListingDate(index, type)).plusDays(AllShop.instance.config.getInt("days-before-removal")));
            long seconds = duration.getSeconds();
            long absSeconds = Math.abs(seconds);
            String positive = String.format(
                    "%d:%02d:%02d",
                    absSeconds / 3600,
                    (absSeconds % 3600) / 60,
                    absSeconds % 60);
            return seconds < 0 ? "-" + positive : positive;
        }
        return null;
    }

    public static void loadPage(Shop shop) {
        int index = 0;
        for (ItemStack item : shop.getPage(shop.getCurrentPage())) {
            shop.getInv().setItem(index, item);
            index++;
        }
    }

    public static ItemStack removeListingInfo(ItemStack item, ShopType type) {
        ItemMeta meta = item.getItemMeta();
        if (meta.getLore() != null) {
            CopyOnWriteArrayList<String> lore = new CopyOnWriteArrayList<>(meta.getLore());
            int indexes;
            switch (type) {
                case SERVER_SHOP:
                    indexes = AllShop.instance.server.size();
                    break;
                case AUCTION_HOUSE:
                    indexes = AllShop.instance.auction.size();
                    break;
                default:
                    indexes = AllShop.instance.market.size();
                    break;
            }
            for (int i = lore.size() - 1; indexes != 0; i--) {
                lore.remove(i);
                indexes--;
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static String generateID(ShopType type) {
        String id = getMainKey(type) + (int) (Math.random() * 9824);
        for (Object obj : getListings(type)) {
            if (obj.toString().equals(id)) {
                return generateID(type);
            }
        }
        return id;
    }

    public static void removeListing(Player player, ShopType type, String ID, boolean returnItem, boolean admin) {
        try {
            boolean located = false;
            int id = Integer.parseInt(ID);
            int index = -1;
            for (Object key : getListings(type)) {
                index++;
                if (id == Integer.parseInt(key.toString())) {
                    located = true;
                    break;
                }
            }
            if (located) {
                if (!admin) {
                    if (getSellerName(index, type).equals(player.getName())) {
                        if (returnItem) {
                            Bukkit.getOfflinePlayer(getSellerUUID(index, type)).getPlayer().getInventory().addItem(removeListingInfo(getListingItem(index, type), type));
                        }
                        AllShop.instance.data.set(getMainKey(type) + id, null);
                        try {
                            AllShop.instance.data.save(new File(AllShop.instance.folder, "data.yml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        AllShop.instance.loadData();
                        player.sendMessage(AllShop.instance.PREFIX + ColorUtils.format(AllShop.instance.customMessages[26]));
                    } else {
                        player.sendMessage(AllShop.instance.PREFIX + ColorUtils.format(AllShop.instance.customMessages[19]));
                    }
                } else {
                    if (returnItem) {
                        Bukkit.getOfflinePlayer(getSellerUUID(index, type)).getPlayer().getInventory().addItem(removeListingInfo(getListingItem(index, type), type));
                    }
                    AllShop.instance.data.set(getMainKey(type) + id, null);
                    try {
                        AllShop.instance.data.save(new File(AllShop.instance.folder, "data.yml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AllShop.instance.loadData();
                    player.sendMessage(AllShop.instance.PREFIX + ColorUtils.format(AllShop.instance.customMessages[26]));
                }
            } else {
                player.sendMessage(AllShop.instance.PREFIX + ColorUtils.format(AllShop.instance.customMessages[28]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(AllShop.instance.PREFIX + AllShop.instance.customMessages[2]);
        }
    }


    public static void createListing(ShopType type, CommandSender sender, String[] args) {
        int count = 0;
        if (AllShop.instance.LISTINGS_LIMIT != -1) {
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
        if (type == ShopType.SERVER_SHOP || AllShop.instance.LISTINGS_LIMIT == -1 || count < AllShop.instance.LISTINGS_LIMIT) {
            Player player = (Player) sender;
            int price = 0;
            String UUID = String.valueOf(player.getUniqueId());
            if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                if (args.length > 1) {
                    try {
                        if (type == ShopType.PLAYER_SHOP || type == ShopType.SERVER_SHOP) {
                            price = Integer.parseInt(args[1]);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        sender.sendMessage(AllShop.instance.PREFIX + AllShop.instance.customMessages[3]);
                        return;
                    }
                    String id = generateID(type);
                    AllShop.instance.data.createSection(id);
                    if (type != ShopType.SERVER_SHOP) {
                        String time = LocalDateTime.now().toString();
                        AllShop.instance.data.set(id + ".Date", time);
                        AllShop.instance.data.set(id + ".UUID", UUID);
                        AllShop.instance.data.set(id + ".Name", player.getName());
                    }
                    if (type != ShopType.AUCTION_HOUSE) {
                        AllShop.instance.data.set(id + ".Price", price);
                    } else {
                        AllShop.instance.data.set(id + ".minBid", Integer.parseInt(args[1]));
                        AllShop.instance.data.set(id + ".Bid", Integer.parseInt(args[1]));
                        AllShop.instance.data.set(id + ".Bidder", UUID);
                    }
                    AllShop.instance.data.set(id + ".Items", player.getInventory().getItemInMainHand());
                    ItemStack item = player.getInventory().getItemInMainHand();
                    String name;
                    if (item.hasItemMeta()) {
                        name = item.getItemMeta().getDisplayName();
                    } else {
                        name = item.getType().name();
                    }
                    if (type != ShopType.AUCTION_HOUSE) {
                        player.sendMessage(AllShop.instance.PREFIX + ChatColor.GREEN + "You have successfully sold your " +"["
                                + player.getInventory().getItemInMainHand().getAmount() + "] "+ name +" for " + price);
                    } else {
                        player.sendMessage(AllShop.instance.PREFIX + ChatColor.GREEN + "You have successfully auctioned your "+ "["
                                + player.getInventory().getItemInMainHand().getAmount() + "] " + name +" for a minimum bid of " + args[1]);

                    }
                    try {
                        AllShop.instance.data.save(new File(AllShop.instance.folder, "data.yml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (type != ShopType.SERVER_SHOP) {
                        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    }
                    AllShop.instance.loadData();
                } else {
                    if (type == ShopType.PLAYER_SHOP) {
                        sender.sendMessage(AllShop.instance.PREFIX + ColorUtils.format(AllShop.instance.customMessages[9]));
                    } else {
                        sender.sendMessage(AllShop.instance.PREFIX + ColorUtils.format(AllShop.instance.customMessages[36]));
                    }
                }
            } else {
                sender.sendMessage(AllShop.instance.PREFIX + ColorUtils.format(AllShop.instance.customMessages[7]));
            }
        } else {
            sender.sendMessage(AllShop.instance.PREFIX + ColorUtils.format(AllShop.instance.customMessages[8]));
        }
    }

    private static ItemStack addListingInfo(ItemStack item, int index, ShopType type) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore;
        List<String> addOnLore;
        switch (type) {
            case SERVER_SHOP:
                addOnLore = AllShop.instance.server;
                break;
            case AUCTION_HOUSE:
                addOnLore = AllShop.instance.auction;
                break;
            default:
                addOnLore = AllShop.instance.market;
                break;
        }
        if (meta.hasLore()) {
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }
        int count = 0;
        for (String message : addOnLore) {
            for (String line : lore) {
                if (line.equals(message)) {
                    count++;
                }
            }
            lore.add(ColorUtils.format(PlaceHolderUtil.format(message, index, type)));
        }
        if (count == addOnLore.size()) {
            return item;
        }
        meta.setLore(lore);
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

    public static int getListingID(int index, ShopType type) {
        String num = (String) getListings(type)[index];
        return Integer.parseInt(num);
    }

    public static ItemStack getListingItem(int index, ShopType type) {
        return AllShop.instance.data.getItemStack(getMainKey(type) + getListings(type)[index] + ".Items").clone();
    }

    public static String getSellerName(int index, ShopType type) {
        if (type != ShopType.SERVER_SHOP) {
            return AllShop.instance.data.getString(getMainKey(type) + getListings(type)[index] + ".Name");
        }
        return null;
    }

    public static UUID getSellerUUID(int index, ShopType type) {
        if (type != ShopType.SERVER_SHOP) {
            return UUID.fromString(AllShop.instance.data.getString(getMainKey(type) + getListings(type)[index] + ".UUID"));
        }
        return null;
    }

    public static int getListingPrice(int index, ShopType type) {
        if (type != ShopType.AUCTION_HOUSE) {
            return AllShop.instance.data.getInt(getMainKey(type) + getListings(type)[index] + ".Price");
        }
        return 0;
    }

    public static String getListingDate(int index, ShopType type) {
        if (type != ShopType.SERVER_SHOP) {
            return AllShop.instance.data.getString(getMainKey(type) + getListings(type)[index] + ".Date");
        }
        return null;
    }

    public static int getMinBid(int index) {
        return AllShop.instance.data.getInt("auction." + AllShop.instance.auctionListings[index] + ".minBid");
    }

    public static int getCurrentBid(int index) {
        return AllShop.instance.data.getInt("auction." + AllShop.instance.auctionListings[index] + ".Bid");
    }

    public static UUID getCurrentBidder(int index) {
        return UUID.fromString(AllShop.instance.data.getString("auction." + AllShop.instance.auctionListings[index] + ".Bidder"));
    }

    public static String getMainKey(ShopType type) {
        switch (type) {
            case PLAYER_SHOP:
                return "digital.";
            case AUCTION_HOUSE:
                return "auction.";
            default:
                return "server.";
        }
    }

    public static Object[] getListings(ShopType type) {
        switch (type) {
            case PLAYER_SHOP:
                return AllShop.instance.digitalListings;
            case AUCTION_HOUSE:
                return AllShop.instance.auctionListings;
            default:
                return AllShop.instance.serverListings;
        }
    }

}
