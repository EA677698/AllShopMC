package allshop.allshop.pshops;

import allshop.allshop.main.AllShop;
import allshop.allshop.main.Commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ChestShops {

    private int buy, sell, amount;
    private String seller;
    Chest chest;
    Sign sign;
    Player player;

    public ChestShops(Sign sign, Chest chest, Player player, String seller){
        this.sign = sign;
        this.chest = chest;
        this.player = player;
        this.seller = seller;
        if (sign.getLine(2).contains("s")) {
            buy = Integer.parseInt(sign.getLine(2).substring(sign.getLine(2).indexOf("s") + 2, sign.getLine(2).indexOf("s") + 3));
        } else {
            buy = -1;
        }
        if (sign.getLine(2).contains("b")) {
            sell = Integer.parseInt(sign.getLine(2).substring(sign.getLine(2).indexOf("b") + 2,sign.getLine(2).indexOf("b") + 3));
        } else {
            sell = -1;
        }
        if(!seller.equals(ChatColor.RED+"Server")){
            this.seller = sign.getLine(3);
        }
        AllShop.openTransactions.add(this);

    }

    @SuppressWarnings("deprecation")
    public void processInformation() {
        boolean serverStore = seller.equals(ChatColor.RED+"Server");
        try {
            if(player.hasPermission("allshop.chest")){
                if (sign.getLines().length != 4) {
                    return;
                }
                boolean isDouble = false;
                DoubleChest doubleChest = null;
                BlockState chestState = chest;
                if (chestState instanceof Chest) {
                    Chest chest = (Chest) chestState;
                    Inventory inventory = chest.getInventory();
                    if (inventory instanceof DoubleChestInventory) {
                         doubleChest = (DoubleChest) inventory.getHolder();
                         isDouble = true;
                    }
                }
                Material itemSold;
                if(!serverStore){
                    itemSold = Material.getMaterial(sign.getLine(1).substring(0, sign.getLine(1).indexOf(" ")).toUpperCase());
                } else {
                    itemSold = Material.getMaterial(sign.getLine(1).toUpperCase());
                }
                boolean isFull;
                if(isDouble){
                    isFull = doubleChest.getInventory().containsAtLeast(new ItemStack(itemSold), doubleChest.getInventory().getSize() * 64);
                } else {
                    isFull = chest.getInventory().containsAtLeast(new ItemStack(itemSold), chest.getInventory().getSize() * 64);
                }
                if (player.getInventory().getItemInMainHand().getType() != itemSold) {
                        if (sell != -1) {
                            if(serverStore){
                                if((AllShop.econ.getBalance(player)) >=sell*(amount)){
                                    AllShop.econ.withdrawPlayer(player, sell*amount);
                                    player.getInventory().addItem(new ItemStack(itemSold, amount));
                                    player.sendMessage(AllShop.PREFIX + ChatColor.GREEN + "You have bought " + amount + " " + itemSold.name() + " for " + (sell*amount));
                                } else {
                                    player.sendMessage(AllShop.PREFIX+ChatColor.RED+"You do not have enough money for this!");
                                }
                            } else if (chest.getBlockInventory().containsAtLeast(new ItemStack(itemSold), amount)) {
                                if (AllShop.econ.getBalance(player) >= sell) {
                                    AllShop.econ.withdrawPlayer(player, sell);
                                    AllShop.econ.depositPlayer(Bukkit.getOfflinePlayer(seller), sell);
                                    player.getInventory().addItem(new ItemStack(itemSold, amount));
                                    if(isDouble){
                                        doubleChest.getInventory().removeItem(new ItemStack(itemSold, amount));
                                    } else {
                                        chest.getInventory().removeItem(new ItemStack(itemSold, amount));
                                    }
                                    player.sendMessage(AllShop.PREFIX + ChatColor.GREEN + "You have bought " + amount + " " + itemSold.name() + " for " + sell);
                            } else {
                                player.sendMessage(AllShop.PREFIX+ChatColor.RED+"You do not have enough money for this!");
                            }
                        } else {
                            player.sendMessage(AllShop.PREFIX + ChatColor.RED + "This shop is out of stock!");
                        }
                    } else {
                        player.sendMessage(AllShop.PREFIX + ChatColor.RED + "You cannot buy from this shop!");
                    }
                } else if (player.getInventory().getItemInMainHand().getAmount() >= amount) {
                    if (buy != -1) {
                        if(serverStore){
                            player.getInventory().removeItem(new ItemStack(itemSold, amount));
                            AllShop.econ.depositPlayer(player, buy*amount);
                            player.sendMessage(AllShop.PREFIX + ChatColor.GREEN + "You have sold " + amount + " " + itemSold.name() + " for " + buy*amount);
                        } else if (!isFull) {
                            if(isDouble){
                                doubleChest.getInventory().addItem(new ItemStack(itemSold, amount));
                            } else {
                                chest.getInventory().addItem(new ItemStack(itemSold, amount));
                            }
                            player.getInventory().removeItem(new ItemStack(itemSold, amount));
                            AllShop.econ.depositPlayer(player, buy);
                            AllShop.econ.withdrawPlayer(Bukkit.getOfflinePlayer(seller), buy);
                            player.sendMessage(AllShop.PREFIX + ChatColor.GREEN + "You have sold " + amount + " " + itemSold.name() + " for " + buy);
                        } else {
                            player.sendMessage(AllShop.PREFIX + ChatColor.RED + "This shop is full!");
                        }
                    } else {
                        player.sendMessage(AllShop.PREFIX + ChatColor.RED + "You cannot sell to this shop!");
                    }
                } else {
                    player.sendMessage(AllShop.PREFIX+ChatColor.RED+"You do not have enough "+itemSold.name()+" to sell!");
                }
            } else {
                player.sendMessage(Commands.noPermission);
            }
        } catch (Exception e){
            e.printStackTrace();
            if(AllShop.DEBUG){
                e.printStackTrace();
            }
        }
        AllShop.openTransactions.remove(this);
    }


    @Override
    public String toString() {
        return "ChestShops{" +
                "buy=" + buy +
                ", sell=" + sell +
                ", amount=" + amount +
                ", seller='" + seller + '\'' +
                ", chest=" + chest +
                ", sign=" + sign +
                ", player=" + player +
                '}';
    }

    public Player getPlayer() {
        return player;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }
}
