package allshop.allshop.shops;

import allshop.allshop.main.AllShop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Trades {

    private Inventory inv;
    private Player one, two;
    private boolean ready1, ready2, commenced;
    public Trades(Player one, Player two){
        this.one = one;
        this.two = two;
        inv = Bukkit.createInventory(null, 9, "Trade");
        expiration();
    }

    public void expiration(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!commenced){
                    one.sendMessage(AllShop.instance.PREFIX+ ChatColor.RED+" The trade has expired!");
                    two.sendMessage(AllShop.instance.PREFIX+ ChatColor.RED+" The trade has expired!");
                    AllShop.instance.openTrades.remove(this);
                } else {
                    this.cancel();
                }
            }
        }.runTaskLaterAsynchronously(AllShop.instance.plugin,2400);
    }

    public void commenceTrade(){
        commenced = true;
        inv.setItem(0, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        inv.setItem(1, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        inv.setItem(2, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        inv.setItem(4, new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        inv.setItem(6, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        inv.setItem(7, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        inv.setItem(8, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        one.openInventory(inv);
        two.openInventory(inv);
    }

    public void changeStatusOne(){
        if(inv.getItem(0).getType()==Material.RED_STAINED_GLASS_PANE){
            inv.setItem(0, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
            inv.setItem(1, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
            inv.setItem(2, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
            ready1 = true;
        } else {
            inv.setItem(0, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            inv.setItem(1, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            inv.setItem(2, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            ready1 = false;
        }
    }

    public void changeStatusTwo(){
        if(inv.getItem(6).getType()==Material.RED_STAINED_GLASS_PANE){
            inv.setItem(6, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
            inv.setItem(7, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
            inv.setItem(8, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
            ready2 = true;
        } else {
            inv.setItem(6, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            inv.setItem(7, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            inv.setItem(8, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            ready2 = false;
        }
    }

    public Player getTraderOne() {
        return one;
    }

    public Player getTraderTwo() {
        return two;
    }

    public Inventory getInv() {
        return inv;
    }

    public boolean isReady1() {
        return ready1;
    }

    public boolean isReady2() {
        return ready2;
    }
}
