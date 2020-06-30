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
                }
                if (args[0].equalsIgnoreCase("shop")) {
                    if (AllShop.DIGITAL_ENABLED) {
                        AllShop.openShops.add(new Shop((Player) sender,ShopType.PLAYER_SHOP));
                    } else {
                        sender.sendMessage(AllShop.PREFIX+ChatColor.RED + " The Digital Shop is disabled on this server!");
                    }
                }
                if (args[0].equalsIgnoreCase("sell")) {
                    if (AllShop.DIGITAL_ENABLED) {
                        createListing(false, sender, args);
                    } else {
                        sender.sendMessage(AllShop.PREFIX+ChatColor.RED + " The Digital Shop is disabled on this server!");
                    }
                }
                if(args[0].equalsIgnoreCase("auction")){
                    if(AllShop.AUCTIONS_ENABLED){
                        AllShop.openShops.add(new Shop((Player) sender, ShopType.AUCTION_HOUSE));
                    } else {
                        sender.sendMessage(AllShop.PREFIX+ChatColor.RED+" Auctions are disabled on this server!");
                    }
                }
                if(args[0].equalsIgnoreCase("bid")){
                    if(AllShop.AUCTIONS_ENABLED){
                        createListing(true, sender, args);
                    } else {
                        sender.sendMessage(AllShop.PREFIX+ChatColor.RED+" Auctions are disabled on this server!");
                    }
                }
            } else {
                sender.sendMessage(AllShop.PREFIX+ChatColor.GREEN + " Place holder for main command");
            }
        }
        return false;
    }

    public boolean createListing(boolean auction, CommandSender sender, String[] args) {
        int count = 0;
        if (!auction) {
            for (int i = 1; i < AllShop.digitalListings.length; i++) {
                if(AllShop.data.getString("digital." + AllShop.digitalListings[i] + ".Name")==null){
                    continue;
                }
                if (AllShop.data.getString("digital." + AllShop.digitalListings[i] + ".Name").equals(sender.getName())) {
                    count++;
                }
            }
        } else {
            for (int i = 1; i < AllShop.auctionListings.length; i++) {
                if(AllShop.data.getString("auction." + AllShop.auctionListings[i] + ".Name")==null){
                    continue;
                }
                if (AllShop.data.getString("auction." + AllShop.auctionListings[i] + ".Name").equals(sender.getName())) {
                    count++;
                }
            }
        }
        if (AllShop.LISTINGS_LIMIT == -1 || count < AllShop.LISTINGS_LIMIT) {
            Player player = (Player) sender;
            String UUID = String.valueOf(player.getUniqueId());
            if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                if (args.length > 1) {
                    try {
                        Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Price must be an integer");
                        return false;
                    }
                    String id;
                    if (!auction) {
                        id = "digital." + (int)(Math.random()*9824);
                    } else {
                        id = "auction." + (int)(Math.random()*9824);
                    }
                    AllShop.data.createSection(id);
                    AllShop.data.set(id + ".Date", java.time.LocalDate.now().toString());
                    AllShop.data.set(id + ".UUID", UUID);
                    AllShop.data.set(id + ".Name", player.getName());
                    if (!auction) {
                        AllShop.data.set(id + ".Price", Integer.parseInt(args[1]));
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
                    if (!auction) {
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

