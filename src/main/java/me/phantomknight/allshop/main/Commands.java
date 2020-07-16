package me.phantomknight.allshop.main;

import me.phantomknight.allshop.gshops.Shop;
import me.phantomknight.allshop.gshops.ShopType;
import me.phantomknight.allshop.gshops.Trades;
import me.phantomknight.allshop.utils.ColorUtils;
import me.phantomknight.allshop.utils.ListingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private final AllShop allShop;

    public Commands(AllShop allShop){
        this.allShop = allShop;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("allshop") || command.getName().equalsIgnoreCase("as")) {
            if (args.length > 0) {
                if(sender.hasPermission("allshop.admin")){
                    if(args[0].equalsIgnoreCase("reload")) {
                        allShop.loadData();
                        sender.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[0]));
                    } else {
                        if(args[0].equalsIgnoreCase("version")){
                            sender.sendMessage(allShop.PREFIX+"You are using version 1.2.0");
                        } else {
                            sender.sendMessage(allShop.PREFIX+ChatColor.RED+"Unknown command!");
                        }
                    }
                } else {
                    sender.sendMessage(allShop.PREFIX+ColorUtils.format(allShop.customMessages[27]));
                }
            } else {
                sender.sendMessage(allShop.PREFIX+ChatColor.LIGHT_PURPLE + "Commands:");
                sender.sendMessage(ChatColor.GREEN+"/as reload");
                sender.sendMessage(ChatColor.GREEN+"/Shop");
                sender.sendMessage(ChatColor.GREEN+"/Shop sell [price]");
                sender.sendMessage(ChatColor.GREEN+"/Shop remove [id] [return boolean]");
                sender.sendMessage(ChatColor.GREEN+"/Market");
                sender.sendMessage(ChatColor.GREEN+"/Market sell [price]");
                sender.sendMessage(ChatColor.GREEN+"/Market remove [id] [return boolean]");
                sender.sendMessage(ChatColor.GREEN+"/Trade [Player]");
                sender.sendMessage(ChatColor.GREEN+"/Trade deny");
                sender.sendMessage(ChatColor.GREEN+"/Trade accept");
            }
        }
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase("shop")){
                if(sender.hasPermission("allShop.shop")) {
                    if (allShop.SERVER_SHOP_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("sell")) {
                            if(sender.hasPermission("allShop.admin")) {
                                ListingsUtil.createListing(ShopType.SERVER_SHOP, sender, args);
                            } else {
                                sender.sendMessage(allShop.PREFIX+ ColorUtils.format(allShop.customMessages[27]));
                            }
                        } else if(args.length>0 && args[0].equalsIgnoreCase("remove")){
                            if(sender.hasPermission("allShop.admin")) {
                                if (args.length > 1) {
                                    ListingsUtil.removeListing((Player) sender, ShopType.SERVER_SHOP, args[1], false, true);
                                } else {
                                    sender.sendMessage(allShop.PREFIX + ChatColor.RED + ColorUtils.format(allShop.customMessages[1]));
                                }
                            } else {
                                sender.sendMessage(allShop.PREFIX+ ColorUtils.format(allShop.customMessages[27]));
                            }
                        } else {
                            allShop.openShops.add(new Shop(allShop,(Player) sender, ShopType.SERVER_SHOP));
                        }
                    } else {
                        sender.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[5]));
                    }
                } else {
                    sender.sendMessage(allShop.PREFIX+ ColorUtils.format(allShop.customMessages[27]));
                }
            }
            if(command.getName().equalsIgnoreCase("auction")){
                if(sender.hasPermission("allShop.auction")) {
                    if (allShop.AUCTIONS_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("bid")) {
                            ListingsUtil.createListing(ShopType.AUCTION_HOUSE, sender, args);
                        } else {
                            allShop.openShops.add(new Shop(allShop,(Player) sender, ShopType.AUCTION_HOUSE));
                        }
                    } else {
                        sender.sendMessage(allShop.PREFIX + ChatColor.RED + "Auctions are disabled on this server!");
                    }
                } else {
                    sender.sendMessage(allShop.PREFIX+ ColorUtils.format(allShop.customMessages[27]));
                }
            }
            if(command.getName().equalsIgnoreCase("market")) {
                if(sender.hasPermission("allShop.market")) {
                    if (allShop.DIGITAL_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("sell")) {
                            ListingsUtil.createListing(ShopType.PLAYER_SHOP, sender, args);
                        } else if(args.length>0 && args[0].equalsIgnoreCase("remove")){
                            if (args.length > 1) {
                                boolean item;
                                if(args.length==3){
                                    item = Boolean.parseBoolean(args[2]);
                                } else {
                                    item = true;
                                }
                                ListingsUtil.removeListing((Player) sender, ShopType.PLAYER_SHOP, args[1], item, sender.hasPermission("allShop.admin"));
                            } else {
                                sender.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[1]));
                            }
                        } else {
                            allShop.openShops.add(new Shop(allShop,(Player) sender, ShopType.PLAYER_SHOP));
                        }
                    } else {
                        sender.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[6]));
                    }
                } else {
                    sender.sendMessage(allShop.PREFIX+ ColorUtils.format(allShop.customMessages[27]));
                }
            }
            if(command.getName().equalsIgnoreCase("trade")){
                if(sender.hasPermission("allShop.trade")) {
                    if (allShop.TRADING_ENABLED) {
                        if (args.length > 0) {
                            if (args[0].equalsIgnoreCase("accept")) {
                                for (Trades trade : allShop.openTrades) {
                                    if (trade.getTraderTwo().equals(sender)) {
                                        trade.getTraderOne().sendMessage(allShop.PREFIX + trade.getTraderTwo().getDisplayName()+ ChatColor.GREEN + " has accepted your request!");
                                        trade.commenceTrade();
                                    }
                                }
                            } else if (args[0].equalsIgnoreCase("deny")) {
                                for (Trades trade : allShop.openTrades) {
                                    if (trade.getTraderTwo().equals(sender)) {
                                        trade.getTraderOne().sendMessage(allShop.PREFIX + trade.getTraderTwo().getDisplayName() +ChatColor.RED+ " has denied your request!");
                                        sender.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[11]));
                                        allShop.openTrades.remove(trade);
                                    }
                                }
                            } else if (!(args[0].equals(sender.getName()))) {
                                try {
                                    if (Bukkit.getPlayer(args[0]).isOnline()) {
                                        allShop.openTrades.add(new Trades(allShop,(Player) sender, Bukkit.getPlayer(args[0])));
                                        sender.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[13]));
                                        Bukkit.getPlayer(args[0]).sendMessage(allShop.PREFIX + ChatColor.GREEN + ((Player) sender).getDisplayName() + " is requesting to trade with you!");
                                        Bukkit.getPlayer(args[0]).sendMessage(allShop.PREFIX + ChatColor.YELLOW + "Do " + ChatColor.GREEN + "(/trade accept)" + ChatColor.YELLOW +
                                                " or " + ChatColor.RED + "(/trade deny)" + ChatColor.YELLOW + " to reply!");
                                    } else {
                                        sender.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[15]));
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                    sender.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[15]));
                                }
                            } else {
                                sender.sendMessage(allShop.PREFIX+ allShop.customMessages[17]);
                            }
                        } else {
                            sender.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[18]));
                        }
                    } else {
                        sender.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[10]));
                    }
                } else {
                    sender.sendMessage(allShop.PREFIX+ ColorUtils.format(allShop.customMessages[27]));
                }
            }
        } else {
            sender.sendMessage(allShop.PREFIX+ColorUtils.format(allShop.customMessages[16]));
        }
        return false;
    }

}

