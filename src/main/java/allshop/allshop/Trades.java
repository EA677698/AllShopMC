package allshop.allshop;

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
                    one.sendMessage(AllShop.PREFIX+ ChatColor.RED+" The trade has expired!");
                    two.sendMessage(AllShop.PREFIX+ ChatColor.RED+" The trade has expired!");
                    AllShop.openTrades.remove(this);
                } else {
                    this.cancel();
                }
            }
        }.runTaskLaterAsynchronously(AllShop.plugin,2400);
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
        System.out.println("Commence4 "+AllShop.openTrades.size());
        two.openInventory(inv);
        System.out.println("Commence5 "+AllShop.openTrades.size());
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

    public Player getOne() {
        return one;
    }

    public Player getTwo() {
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
