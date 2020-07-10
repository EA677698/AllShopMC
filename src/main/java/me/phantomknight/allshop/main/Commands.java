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

import java.util.Arrays;

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
                        if (allShop.DEBUG) {
                            System.out.println(allShop.PREFIX + "has permission allShop.admin " + sender.hasPermission("allShop.admin"));
                        }
                        allShop.loadData();
                        sender.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[0]));
                    } else {
                        if(args[0].equalsIgnoreCase("version")){
                            sender.sendMessage(allShop.PREFIX+"You are using version 1.1.0");
                        } else {
                            sender.sendMessage(allShop.PREFIX+ChatColor.RED+"Unknown command!");
                        }
                    }
                } else {
                    sender.sendMessage(allShop.PREFIX+ColorUtils.format(allShop.customMessages[27]));
                }
            } else {
                sender.sendMessage(allShop.PREFIX+ChatColor.GREEN + "Place holder for main command");
            }
        }
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase("shop")){
                if(allShop.DEBUG){
                    System.out.println(allShop.PREFIX+"has permission allShop.shop "+sender.hasPermission("allShop.shop"));
                }
                if(sender.hasPermission("allShop.shop")) {
                    if(allShop.DEBUG){
                        System.out.println("Server Shop Enabled: "+allShop.SERVER_SHOP_ENABLED);
                    }
                    if (allShop.SERVER_SHOP_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("sell")) {
                            if(allShop.DEBUG){
                                System.out.println(allShop.PREFIX+"has permission allShop.admin "+sender.hasPermission("allShop.admin"));
                            }
                            if(sender.hasPermission("allShop.admin")) {
                                if(allShop.DEBUG) {
                                    System.out.println("ListingsUtil.CreateListing(" + ShopType.SERVER_SHOP + "," + sender + "," + Arrays.toString(args));
                                }
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
                            if (allShop.DEBUG) {
                                System.out.println("allShop.openShop.add(new Shop((Player) " + sender + "," + ShopType.SERVER_SHOP);
                            }
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
                if(allShop.DEBUG){
                    System.out.println(allShop.PREFIX+"has permission allShop.auction "+sender.hasPermission("allShop.auction"));
                }
                if(sender.hasPermission("allShop.auction")) {
                    if(allShop.DEBUG){
                        System.out.println("Auctions Enabled: "+allShop.AUCTIONS_ENABLED);
                    }
                    if (allShop.AUCTIONS_ENABLED) {
                        if (args.length > 0 && args[0].equalsIgnoreCase("bid")) {
                            if(allShop.DEBUG) {
                                System.out.println("ListingsUtil.CreateListing(" + ShopType.AUCTION_HOUSE + "," + sender + "," + Arrays.toString(args));
                            }
                            ListingsUtil.createListing(ShopType.AUCTION_HOUSE, sender, args);
                        } else {
                            if(allShop.DEBUG) {
                                System.out.println("allShop.openShop.add(new Shop((Player) " + sender + "," + ShopType.AUCTION_HOUSE);
                            }
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
                if(allShop.DEBUG){
                    System.out.println(allShop.PREFIX+"has permission allShop.market "+sender.hasPermission("allShop.market"));
                }
                if(sender.hasPermission("allShop.market")) {
                    if(allShop.DEBUG){
                        System.out.println("Player Shops Enabled: "+allShop.DIGITAL_ENABLED);
                    }
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
                            if(allShop.DEBUG) {
                                System.out.println("allShop.openShop.add(new Shop((Player) " + sender + "," + ShopType.PLAYER_SHOP);
                            }
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
                if(allShop.DEBUG){
                    System.out.println(allShop.PREFIX+"has permission allShop.trade "+sender.hasPermission("allShop.trade"));
                }
                if(sender.hasPermission("allShop.trade")) {
                    if(allShop.DEBUG){
                        System.out.println("Trading Enabled: "+allShop.TRADING_ENABLED);
                    }
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
                                    if(allShop.DEBUG){
                                        e.printStackTrace();
                                    }
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

