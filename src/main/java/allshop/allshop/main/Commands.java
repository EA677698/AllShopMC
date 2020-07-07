package allshop.allshop.main;

import allshop.allshop.gshops.Shop;
import allshop.allshop.gshops.ShopType;
import allshop.allshop.gshops.Trades;
import allshop.allshop.utils.ListingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    public static String noPermission;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("allshop") || command.getName().equalsIgnoreCase("as")) {
            if (args.length > 0) {
                if(args[0].equalsIgnoreCase("reload")){
                    if(AllShop.DEBUG){
                        System.out.println(AllShop.PREFIX+"has permission allshop.admin "+sender.hasPermission("allshop.admin"));
                    }
                    if(sender.hasPermission("allshop.admin")) {
                        AllShop.loadData();
                        sender.sendMessage(AllShop.PREFIX + ChatColor.GREEN + "Successfully reloaded plugin");
                    } else {
                        sender.sendMessage(noPermission);
                    }
                } else {
                    sender.sendMessage(AllShop.PREFIX+ChatColor.GREEN + "Place holder for main command");
                }
            } else {
                sender.sendMessage(AllShop.PREFIX+ChatColor.GREEN + "Place holder for main command");
            }
        }
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase("shop")){
                if(AllShop.DEBUG){
                    System.out.println(AllShop.PREFIX+"has permission allshop.shop "+sender.hasPermission("allshop.shop"));
                }
                if(sender.hasPermission("allshop.shop")) {
                    if(AllShop.DEBUG){
                        System.out.println("Server Shop Enabled: "+AllShop.SERVER_SHOP_ENABLED);
                    }
                    if (AllShop.SERVER_SHOP_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("sell")) {
                            if(AllShop.DEBUG){
                                System.out.println(AllShop.PREFIX+"has permission allshop.admin "+sender.hasPermission("allshop.admin"));
                            }
                            if(sender.hasPermission("allshop.admin")) {
                                if(AllShop.DEBUG) {
                                    System.out.println("ListingsUtil.CreateListing(" + ShopType.SERVER_SHOP + "," + sender + "," + args);
                                }
                                ListingsUtil.createListing(ShopType.SERVER_SHOP, sender, args);
                            } else {
                                sender.sendMessage(noPermission);
                            }
                        } else if(args.length>0 && args[0].equalsIgnoreCase("remove")){
                            if(sender.hasPermission("allshop.admin")) {
                                if (args.length > 1) {
                                    ListingsUtil.removeListing((Player) sender, ShopType.SERVER_SHOP, args[1], false, true);
                                } else {
                                    sender.sendMessage(AllShop.PREFIX + ChatColor.RED + "You must include the item ID!");
                                }
                            } else {
                                sender.sendMessage(noPermission);
                            }
                        } else {
                            if (AllShop.DEBUG) {
                                System.out.println("AllShop.openShop.add(new Shop((Player) " + sender + "," + ShopType.SERVER_SHOP);
                            }
                            AllShop.openShops.add(new Shop((Player) sender, ShopType.SERVER_SHOP));
                        }
                    } else {
                        sender.sendMessage(AllShop.PREFIX + ChatColor.RED + "The Server Shop is disabled on this server!");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
            if(command.getName().equalsIgnoreCase("auction")){
                if(AllShop.DEBUG){
                    System.out.println(AllShop.PREFIX+"has permission allshop.auction "+sender.hasPermission("allshop.auction"));
                }
                if(sender.hasPermission("allshop.auction")) {
                    if(AllShop.DEBUG){
                        System.out.println("Auctions Enabled: "+AllShop.AUCTIONS_ENABLED);
                    }
                    if (AllShop.AUCTIONS_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("bid")) {
                            if(AllShop.DEBUG) {
                                System.out.println("ListingsUtil.CreateListing(" + ShopType.AUCTION_HOUSE + "," + sender + "," + args);
                            }
                            ListingsUtil.createListing(ShopType.AUCTION_HOUSE, sender, args);
                        } else {
                            if(AllShop.DEBUG) {
                                System.out.println("AllShop.openShop.add(new Shop((Player) " + sender + "," + ShopType.AUCTION_HOUSE);
                            }
                            AllShop.openShops.add(new Shop((Player) sender, ShopType.AUCTION_HOUSE));
                        }
                    } else {
                        sender.sendMessage(AllShop.PREFIX + ChatColor.RED + "Auctions are disabled on this server!");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
            if(command.getName().equalsIgnoreCase("market")) {
                if(AllShop.DEBUG){
                    System.out.println(AllShop.PREFIX+"has permission allshop.market "+sender.hasPermission("allshop.market"));
                }
                if(sender.hasPermission("allshop.market")) {
                    if(AllShop.DEBUG){
                        System.out.println("Player Shops Enabled: "+AllShop.DIGITAL_ENABLED);
                    }
                    if (AllShop.DIGITAL_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("sell")) {
                            ListingsUtil.createListing(ShopType.PLAYER_SHOP, sender, args);
                        } else if(args.length>0 && args[0].equalsIgnoreCase("remove")){
                            if (args.length > 1) {
                                boolean item;
                                if(args.length==3){
                                    item = Boolean.valueOf(args[2]);
                                } else {
                                    item = false;
                                }
                                ListingsUtil.removeListing((Player) sender, ShopType.PLAYER_SHOP, args[1], item, sender.hasPermission("allshop.admin"));
                            } else {
                                sender.sendMessage(AllShop.PREFIX + ChatColor.RED + "You must include the item ID!");
                            }
                        } else {
                            if(AllShop.DEBUG) {
                                System.out.println("AllShop.openShop.add(new Shop((Player) " + sender + "," + ShopType.PLAYER_SHOP);
                            }
                            AllShop.openShops.add(new Shop((Player) sender, ShopType.PLAYER_SHOP));
                        }
                    } else {
                        sender.sendMessage(AllShop.PREFIX + ChatColor.RED + "The Digital Shop is disabled on this server!");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
            if(command.getName().equalsIgnoreCase("trade")){
                if(AllShop.DEBUG){
                    System.out.println(AllShop.PREFIX+"has permission allshop.trade "+sender.hasPermission("allshop.trade"));
                }
                if(sender.hasPermission("allshop.trade")) {
                    if(AllShop.DEBUG){
                        System.out.println("Trading Enabled: "+AllShop.TRADING_ENABLED);
                    }
                    if (AllShop.TRADING_ENABLED) {
                        if (args.length > 0) {
                            if (args[0].equalsIgnoreCase("accept")) {
                                for (Trades trade : AllShop.openTrades) {
                                    if (trade.getTraderTwo().equals(sender)) {
                                        trade.getTraderOne().sendMessage(AllShop.PREFIX + trade.getTraderTwo().getDisplayName()+ ChatColor.GREEN + " has accepted your request!");
                                        trade.commenceTrade();
                                    }
                                }
                            } else if (args[0].equalsIgnoreCase("deny")) {
                                for (Trades trade : AllShop.openTrades) {
                                    if (trade.getTraderTwo().equals(sender)) {
                                        trade.getTraderOne().sendMessage(AllShop.PREFIX + trade.getTraderTwo().getDisplayName() +ChatColor.RED+ " has denied your request!");
                                        sender.sendMessage(AllShop.PREFIX + ChatColor.RED + "Trade successfully denied!");
                                        AllShop.openTrades.remove(trade);
                                    }
                                }
                            } else if (!(args[0].equals(sender.getName()))) {
                                if(Bukkit.getPlayer(args[0]).isOnline()) {
                                    AllShop.openTrades.add(new Trades((Player) sender, Bukkit.getPlayer(args[0])));
                                    sender.sendMessage(AllShop.PREFIX + ChatColor.GREEN + "Trade request successfully sent!");
                                    Bukkit.getPlayer(args[0]).sendMessage(AllShop.PREFIX + ChatColor.GREEN + ((Player) sender).getDisplayName() + " is requesting to trade with you!");
                                    Bukkit.getPlayer(args[0]).sendMessage(AllShop.PREFIX + ChatColor.YELLOW + "Do " + ChatColor.GREEN + "(/trade accept)" + ChatColor.YELLOW +
                                            " or " + ChatColor.RED + "(/trade deny)" + ChatColor.YELLOW + " to reply!");
                                } else {
                                    sender.sendMessage(AllShop.PREFIX + ChatColor.RED + "Player not online!");
                                }
                            } else {
                                sender.sendMessage(AllShop.PREFIX+ ChatColor.RED+"You cannot trade with yourself!");
                            }
                        } else {
                            sender.sendMessage(AllShop.PREFIX + ChatColor.RED + "Command input must include player name!");
                        }
                    } else {
                        sender.sendMessage(AllShop.PREFIX + ChatColor.RED + "Trading is disabled on this server");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
        } else {
            sender.sendMessage(AllShop.PREFIX+"You must be a player to execute this command!");
        }
        return false;
    }

}

