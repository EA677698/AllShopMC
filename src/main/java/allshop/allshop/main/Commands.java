package allshop.allshop.main;

import allshop.allshop.main.AllShop;
import allshop.allshop.shops.Shop;
import allshop.allshop.shops.ShopType;
import allshop.allshop.shops.Trades;
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
                    if(sender.hasPermission("allshop.admin")) {
                        AllShop.loadData();
                        sender.sendMessage(AllShop.instance.PREFIX + ChatColor.GREEN + " Successfully reloaded plugin");
                    } else {
                        sender.sendMessage(noPermission);
                    }
                } else {
                    sender.sendMessage(AllShop.instance.PREFIX+ChatColor.GREEN + " Place holder for main command");
                }
            } else {
                sender.sendMessage(AllShop.instance.PREFIX+ChatColor.GREEN + " Place holder for main command");
            }
        }
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase("shop")){
                if(sender.hasPermission("allshop.shop")) {
                    if (AllShop.instance.SERVER_SHOP_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("sell")) {
                            if(sender.hasPermission("allshop.admin")) {
                                ListingsUtil.createListing(ShopType.SERVER_SHOP, sender, args);
                            } else {
                                sender.sendMessage(noPermission);
                            }
                        } else
                            AllShop.instance.openShops.add(new Shop((Player) sender, ShopType.SERVER_SHOP));
                    } else {
                        sender.sendMessage(AllShop.instance.PREFIX + ChatColor.RED + " The Server Shop is disabled on this server!");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
            if(command.getName().equalsIgnoreCase("auction")){
                if(sender.hasPermission("allshop.auction")) {
                    if (AllShop.instance.AUCTIONS_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("bid")) {
                            ListingsUtil.createListing(ShopType.AUCTION_HOUSE, sender, args);
                        } else {
                            AllShop.instance.openShops.add(new Shop((Player) sender, ShopType.AUCTION_HOUSE));
                        }
                    } else {
                        sender.sendMessage(AllShop.instance.PREFIX + ChatColor.RED + " Auctions are disabled on this server!");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
            if(command.getName().equalsIgnoreCase("market")) {
                if(sender.hasPermission("allshop.market")) {
                    if (AllShop.instance.DIGITAL_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("sell")) {
                            ListingsUtil.createListing(ShopType.PLAYER_SHOP, sender, args);
                        } else {
                            AllShop.instance.openShops.add(new Shop((Player) sender, ShopType.PLAYER_SHOP));
                        }
                    } else {
                        sender.sendMessage(AllShop.instance.PREFIX + ChatColor.RED + " The Digital Shop is disabled on this server!");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
            if(command.getName().equalsIgnoreCase("trade")){
                if(sender.hasPermission("allshop.trade")) {
                    if (AllShop.instance.TRADING_ENABLED) {
                        if (args.length > 0) {
                            if (args[0].equalsIgnoreCase("accept")) {
                                for (Trades trade : AllShop.instance.openTrades) {
                                    if (trade.getTraderTwo().equals(sender)) {
                                        trade.getTraderOne().sendMessage(AllShop.instance.PREFIX + ChatColor.GREEN + " " + trade.getTraderTwo().getDisplayName() + " has accepted your request!");
                                        trade.commenceTrade();
                                    }
                                }
                            } else if (args[0].equalsIgnoreCase("deny")) {
                                for (Trades trade : AllShop.instance.openTrades) {
                                    if (trade.getTraderTwo().equals(sender)) {
                                        trade.getTraderOne().sendMessage(AllShop.instance.PREFIX + ChatColor.RED + " " + trade.getTraderTwo().getDisplayName() + " has denied your request!");
                                        sender.sendMessage(AllShop.instance.PREFIX + ChatColor.RED + " Trade successfully denied!");
                                        AllShop.instance.openTrades.remove(trade);
                                    }
                                }
                            } else if (Bukkit.getPlayer(args[0]).isOnline()) {
                                AllShop.instance.openTrades.add(new Trades((Player) sender, Bukkit.getPlayer(args[0])));
                                sender.sendMessage(AllShop.instance.PREFIX + ChatColor.GREEN + " Trade request successfully sent!");
                                Bukkit.getPlayer(args[0]).sendMessage(AllShop.instance.PREFIX + ChatColor.GREEN + " " + ((Player) sender).getDisplayName() + " is requesting to trade with you!");
                            } else {
                                sender.sendMessage(AllShop.instance.PREFIX + ChatColor.RED + " Player not online!");
                            }
                        } else {
                            sender.sendMessage(AllShop.instance.PREFIX + ChatColor.RED + " Command input must include player name!");
                        }
                    } else {
                        sender.sendMessage(AllShop.instance.PREFIX + ChatColor.RED + " Trading is disabled on this server");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
        }
        return false;
    }

}

