package allshop.allshop.pshops;

import allshop.allshop.main.AllShop;
import allshop.allshop.main.Commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ChestShops {

    public static void retrieveInformation(Sign sign, Chest chest, Player player) {
        try {
            if(player.hasPermission("allshop.chest")){
                if (sign.getLines().length != 4) {
                    return;
                }
                Material itemSold = Material.getMaterial(sign.getLine(1).substring(0, sign.getLine(1).indexOf(" ")).toUpperCase());
                int amount = Integer.parseInt(sign.getLine(1).substring(sign.getLine(1).indexOf(" ") + 1));
                int buy;
                if (sign.getLine(2).contains("b")) {
                    buy = Integer.parseInt(sign.getLine(2).substring(sign.getLine(2).indexOf("b") + 2, sign.getLine(2).indexOf(" ")));
                } else {
                    buy = -1;
                }
                int sell;
                if (sign.getLine(2).contains("s")) {
                    sell = Integer.parseInt(sign.getLine(2).substring(sign.getLine(2).indexOf("s") + 2));
                } else {
                    sell = -1;
                }
                String seller = sign.getLine(3);
                if(seller.equals("")||itemSold == null){
                    return;
                }
                boolean isFull = chest.getBlockInventory().containsAtLeast(new ItemStack(itemSold), chest.getBlockInventory().getSize() * 64);
                if (player.getInventory().getItemInMainHand().getType() != itemSold) {
                    if (chest.getBlockInventory().containsAtLeast(new ItemStack(itemSold), amount)) {
                        if (sell != -1) {
                            if (AllShop.econ.getBalance(player) >= sell) {
                                AllShop.econ.withdrawPlayer(player, sell);
                                AllShop.econ.depositPlayer(Bukkit.getPlayer(seller), sell);
                                player.getInventory().addItem(new ItemStack(itemSold, amount));
                                chest.getBlockInventory().removeItem(new ItemStack(itemSold, amount));
                                player.sendMessage(AllShop.PREFIX + ChatColor.GREEN + "You have bought " + amount + " " + itemSold.name() + " for " + sell);
                            }
                        } else {
                            player.sendMessage(AllShop.PREFIX + ChatColor.RED + "You cannot buy from this shop!");
                        }
                    } else {
                        player.sendMessage(AllShop.PREFIX + ChatColor.RED + "This shop is out of stock!");
                    }
                } else if (player.getInventory().getItemInMainHand().getAmount() >= amount) {
                    if (!isFull) {
                        if (buy != -1) {
                            chest.getBlockInventory().addItem(new ItemStack(itemSold, amount));
                            player.getInventory().removeItem(new ItemStack(itemSold, amount));
                            AllShop.econ.depositPlayer(player, buy);
                            AllShop.econ.withdrawPlayer(Bukkit.getPlayer(seller), buy);
                            player.sendMessage(AllShop.PREFIX + ChatColor.GREEN + "You have sold " + amount + " " + itemSold.name() + " for " + buy);
                        } else {
                            player.sendMessage(AllShop.PREFIX + ChatColor.RED + "You cannot sell to this shop!");
                        }
                    } else {
                        player.sendMessage(AllShop.PREFIX + ChatColor.RED + "This shop is full!");
                    }
                }
            } else {
                player.sendMessage(Commands.noPermission);
            }
        } catch (Exception e){
            if(AllShop.DEBUG){
                e.printStackTrace();
            }
        }
    }


}
