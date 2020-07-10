package me.phantomknight.allshop.gshops;

import me.phantomknight.allshop.main.AllShop;
import me.phantomknight.allshop.utils.ColorUtils;
import me.phantomknight.allshop.utils.ListingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Trades {

    final AllShop allShop;
    private final Inventory inv;
    private final Player one;
    private final Player two;
    private boolean ready1, ready2, commenced, completed;
    private ItemStack ready3,notReady1, ready4, notready2;
    public Trades(AllShop allShop, Player one, Player two){
        this.allShop = allShop;
        this.one = one;
        completed = false;
        this.two = two;
        ready3 = ListingsUtil.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,one.getDisplayName()+ChatColor.GREEN+" is ready!");
        ready4 = ListingsUtil.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,two.getDisplayName()+ChatColor.GREEN+" is ready!");
        notReady1 = ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,one.getDisplayName()+ChatColor.RED+" is not ready!");
        notready2 = ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,two.getDisplayName()+ChatColor.RED+" is not ready!");
        inv = Bukkit.createInventory(null, 9, allShop.customMessages[32]);
        expiration();
    }

    public void expiration(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!commenced){
                    one.sendMessage(allShop.PREFIX+ ColorUtils.format(allShop.customMessages[25]));
                    two.sendMessage(allShop.PREFIX+ ColorUtils.format(allShop.customMessages[25]));
                    allShop.openTrades.remove(this);
                } else {
                    this.cancel();
                }
            }
        }.runTaskLaterAsynchronously(allShop,2400);
    }

    public void commenceTrade(){
        commenced = true;
        inv.setItem(0, notReady1);
        inv.setItem(1, notReady1);
        inv.setItem(2, notReady1);
        inv.setItem(4, ListingsUtil.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.LIGHT_PURPLE+"Click on the glass panes on your side to change your status!"));
        inv.setItem(6, notready2);
        inv.setItem(7, notready2);
        inv.setItem(8, notready2);
        one.openInventory(inv);
        two.openInventory(inv);
    }

    public void changeStatusOne(){
        if(inv.getItem(0).getType()==Material.RED_STAINED_GLASS_PANE){
            inv.setItem(0, ready3);
            inv.setItem(1, ready3);
            inv.setItem(2, ready3);
            ready1 = true;
        } else {
            inv.setItem(0, notReady1);
            inv.setItem(1, notReady1);
            inv.setItem(2, notReady1);
            ready1 = false;
        }
    }

    public void changeStatusTwo(){
        if(inv.getItem(6).getType()==Material.RED_STAINED_GLASS_PANE){
            inv.setItem(6, ready4);
            inv.setItem(7, ready4);
            inv.setItem(8, ready4);
            ready2 = true;
        } else {
            inv.setItem(6, notready2);
            inv.setItem(7, notready2);
            inv.setItem(8, notready2);
            ready2 = false;
        }
    }

    public void sendMessageToParticipants(String message){
        one.sendMessage(message);
        two.sendMessage(message);
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
