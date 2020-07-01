package allshop.allshop;

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
                        sender.sendMessage(AllShop.PREFIX + ChatColor.GREEN + " Successfully reloaded plugin");
                    } else {
                        sender.sendMessage(noPermission);
                    }
                } else {
                    sender.sendMessage(AllShop.PREFIX+ChatColor.GREEN + " Place holder for main command");
                }
            } else {
                sender.sendMessage(AllShop.PREFIX+ChatColor.GREEN + " Place holder for main command");
            }
        }
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase("shop")){
                if(sender.hasPermission("allshop.shop")) {
                    if (AllShop.SERVER_SHOP_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("sell")) {
                            if(sender.hasPermission("allshop.admin")) {
                                ListingsUtil.createListing(ShopType.SERVER_SHOP, sender, args);
                            } else {
                                sender.sendMessage(noPermission);
                            }
                        } else
                            AllShop.openShops.add(new Shop((Player) sender, ShopType.SERVER_SHOP));
                    } else {
                        sender.sendMessage(AllShop.PREFIX + ChatColor.RED + " The Server Shop is disabled on this server!");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
            if(command.getName().equalsIgnoreCase("auction")){
                if(sender.hasPermission("allshop.auction")) {
                    if (AllShop.AUCTIONS_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("bid")) {
                            ListingsUtil.createListing(ShopType.AUCTION_HOUSE, sender, args);
                        } else {
                            AllShop.openShops.add(new Shop((Player) sender, ShopType.AUCTION_HOUSE));
                        }
                    } else {
                        sender.sendMessage(AllShop.PREFIX + ChatColor.RED + " Auctions are disabled on this server!");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
            if(command.getName().equalsIgnoreCase("market")) {
                if(sender.hasPermission("allshop.market")) {
                    if (AllShop.DIGITAL_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("sell")) {
                            ListingsUtil.createListing(ShopType.PLAYER_SHOP, sender, args);
                        } else {
                            AllShop.openShops.add(new Shop((Player) sender, ShopType.PLAYER_SHOP));
                        }
                    } else {
                        sender.sendMessage(AllShop.PREFIX + ChatColor.RED + " The Digital Shop is disabled on this server!");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
            if(command.getName().equalsIgnoreCase("trade")){
                if(sender.hasPermission("allshop.trade")) {
                    if (AllShop.TRADING_ENABLED) {
                        if (args.length > 0) {
                            if (args[0].equalsIgnoreCase("accept")) {
                                for (Trades trade : AllShop.openTrades) {
                                    if (trade.getTraderTwo().equals(sender)) {
                                        trade.getTraderOne().sendMessage(AllShop.PREFIX + ChatColor.GREEN + " " + trade.getTraderTwo().getDisplayName() + " has accepted your request!");
                                        trade.commenceTrade();
                                    }
                                }
                            } else if (args[0].equalsIgnoreCase("deny")) {
                                for (Trades trade : AllShop.openTrades) {
                                    if (trade.getTraderTwo().equals(sender)) {
                                        trade.getTraderOne().sendMessage(AllShop.PREFIX + ChatColor.RED + " " + trade.getTraderTwo().getDisplayName() + " has denied your request!");
                                        sender.sendMessage(AllShop.PREFIX + ChatColor.RED + " Trade successfully denied!");
                                        AllShop.openTrades.remove(trade);
                                    }
                                }
                            } else if (Bukkit.getPlayer(args[0]).isOnline()) {
                                AllShop.openTrades.add(new Trades((Player) sender, Bukkit.getPlayer(args[0])));
                                sender.sendMessage(AllShop.PREFIX + ChatColor.GREEN + " Trade request successfully sent!");
                                Bukkit.getPlayer(args[0]).sendMessage(AllShop.PREFIX + ChatColor.GREEN + " " + ((Player) sender).getDisplayName() + " is requesting to trade with you!");
                            } else {
                                sender.sendMessage(AllShop.PREFIX + ChatColor.RED + " Player not online!");
                            }
                        } else {
                            sender.sendMessage(AllShop.PREFIX + ChatColor.RED + " Command input must include player name!");
                        }
                    } else {
                        sender.sendMessage(AllShop.PREFIX + ChatColor.RED + " Trading is disabled on this server");
                    }
                } else {
                    sender.sendMessage(noPermission);
                }
            }
        }
        return false;
    }

}

