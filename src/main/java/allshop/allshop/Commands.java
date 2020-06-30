package allshop.allshop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class Commands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("allshop") || command.getName().equalsIgnoreCase("as")) {
            if (args.length > 0) {
                if(args[0].equalsIgnoreCase("reload")){
                    AllShop.loadData();
                    sender.sendMessage(AllShop.PREFIX+ChatColor.GREEN+" Successfully reloaded plugin");
                } else {
                    sender.sendMessage(AllShop.PREFIX+ChatColor.GREEN + " Place holder for main command");
                }
            } else {
                sender.sendMessage(AllShop.PREFIX+ChatColor.GREEN + " Place holder for main command");
            }
        }
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase("shop")){
                if(AllShop.SERVER_SHOP_ENABLED){
                    if (args.length>0&&args[0].equalsIgnoreCase("sell")) {
                        createListing(ShopType.SERVER_SHOP, sender, args);
                    } else
                        AllShop.openShops.add(new Shop((Player) sender,ShopType.SERVER_SHOP));
                } else {
                    sender.sendMessage(AllShop.PREFIX+ChatColor.RED+" The Server Shop is disabled on this server!");
                }
            }
            if(command.getName().equalsIgnoreCase("auction")){
                if(AllShop.AUCTIONS_ENABLED){
                    if(args.length>0&&args[0].equalsIgnoreCase("bid")){
                        createListing(ShopType.AUCTION_HOUSE, sender, args);
                    } else {
                        AllShop.openShops.add(new Shop((Player) sender, ShopType.AUCTION_HOUSE));
                    }
                } else {
                    sender.sendMessage(AllShop.PREFIX+ChatColor.RED+" Auctions are disabled on this server!");
                }
            }
            if(command.getName().equalsIgnoreCase("market")) {
                if (AllShop.DIGITAL_ENABLED) {
                    if (args.length>0&&args[0].equalsIgnoreCase("sell")) {
                        createListing(ShopType.PLAYER_SHOP, sender, args);
                    } else {
                        AllShop.openShops.add(new Shop((Player) sender, ShopType.PLAYER_SHOP));
                    }
                } else {
                    sender.sendMessage(AllShop.PREFIX + ChatColor.RED + " The Digital Shop is disabled on this server!");
                }
            }
        }
        return false;
    }

    public boolean createListing(ShopType type, CommandSender sender, String[] args) {
        int count = 0;
        if (type==ShopType.PLAYER_SHOP) {
            for (int i = 1; i < AllShop.digitalListings.length; i++) {
                if(AllShop.data.getString("digital." + AllShop.digitalListings[i] + ".Name")==null){
                    continue;
                }
                if (AllShop.data.getString("digital." + AllShop.digitalListings[i] + ".Name").equals(sender.getName())) {
                    count++;
                }
            }
        } else if(type==ShopType.AUCTION_HOUSE) {
            for (int i = 1; i < AllShop.auctionListings.length; i++) {
                if(AllShop.data.getString("auction." + AllShop.auctionListings[i] + ".Name")==null){
                    continue;
                }
                if (AllShop.data.getString("auction." + AllShop.auctionListings[i] + ".Name").equals(sender.getName())) {
                    count++;
                }
            }
        }
        if (type==ShopType.SERVER_SHOP||AllShop.LISTINGS_LIMIT == -1 || count < AllShop.LISTINGS_LIMIT) {
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
                    AllShop.data.createSection(id);
                    if(type!=ShopType.SERVER_SHOP){
                        AllShop.data.set(id + ".Date", java.time.LocalDate.now().toString());
                        AllShop.data.set(id + ".UUID", UUID);
                        AllShop.data.set(id + ".Name", player.getName());
                    }
                    if (type==ShopType.PLAYER_SHOP||type==ShopType.SERVER_SHOP) {
                        AllShop.data.set(id + ".Price", price);
                    } else {
                        AllShop.data.set(id + ".minBid", Integer.parseInt(args[1]));
                        AllShop.data.set(id + ".Bid", Integer.parseInt(args[1]));
                    }
                    AllShop.data.set(id + ".Items", player.getInventory().getItemInMainHand());
                    try {
                        AllShop.data.save(new File(AllShop.folder, "data.yml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    AllShop.loadData();
                } else {
                    if (type==ShopType.PLAYER_SHOP) {
                        sender.sendMessage(AllShop.PREFIX+ChatColor.RED + " You must give a price!");
                    } else {
                        sender.sendMessage(AllShop.PREFIX+ChatColor.RED + " You must give a starting bid!");
                    }
                }
            } else {
                sender.sendMessage(AllShop.PREFIX+ChatColor.RED + " Your hand cannot be empty!");
            }
        } else {
            sender.sendMessage(AllShop.PREFIX+ChatColor.RED + " You have reached the maximum listings limit");
        }
        return false;
    }
}

