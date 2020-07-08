package me.phantomknight.allshop.gshops;

import me.phantomknight.allshop.main.AllShop;
import me.phantomknight.allshop.utils.ListingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class Trades {

    final AllShop allShop;
    private final Inventory inv;
    private final Player one;
    private final Player two;
    private boolean ready1, ready2, commenced, completed;
    public Trades(AllShop allShop, Player one, Player two){
        this.allShop = allShop;
        this.one = one;
        completed = false;
        this.two = two;
        inv = Bukkit.createInventory(null, 9, "Trade");
        expiration();
    }

    public void expiration(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!commenced){
                    one.sendMessage(allShop.PREFIX+ ChatColor.RED+" The trade has expired!");
                    two.sendMessage(allShop.PREFIX+ ChatColor.RED+" The trade has expired!");
                    allShop.openTrades.remove(this);
                } else {
                    this.cancel();
                }
            }
        }.runTaskLaterAsynchronously(allShop,2400);
    }

    public void commenceTrade(){
        commenced = true;
        inv.setItem(0, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,one.getDisplayName()+ChatColor.RED+" is not ready!"));
        inv.setItem(1, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,one.getDisplayName()+ChatColor.RED+" is not ready!"));
        inv.setItem(2, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,one.getDisplayName()+ChatColor.RED+" is not ready!"));
        inv.setItem(4, ListingsUtil.createGuiItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.LIGHT_PURPLE+"Click on the glass panes on your side to change your status!"));
        inv.setItem(6, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,two.getDisplayName()+ChatColor.RED+" is not ready!"));
        inv.setItem(7, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,two.getDisplayName()+ChatColor.RED+" is not ready!"));
        inv.setItem(8, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,two.getDisplayName()+ChatColor.RED+" is not ready!"));
        one.openInventory(inv);
        two.openInventory(inv);
    }

    public void changeStatusOne(){
        if(inv.getItem(0).getType()==Material.RED_STAINED_GLASS_PANE){
            inv.setItem(0, ListingsUtil.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,one.getDisplayName()+ChatColor.GREEN+" is ready!"));
            inv.setItem(1, ListingsUtil.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,one.getDisplayName()+ChatColor.GREEN+" is ready!"));
            inv.setItem(2, ListingsUtil.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,one.getDisplayName()+ChatColor.GREEN+" is ready!"));
            ready1 = true;
        } else {
            inv.setItem(0, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,one.getDisplayName()+ChatColor.RED+" is not ready!"));
            inv.setItem(1, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,one.getDisplayName()+ChatColor.RED+" is not ready!"));
            inv.setItem(2, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,one.getDisplayName()+ChatColor.RED+" is not ready!"));
            ready1 = false;
        }
    }

    public void changeStatusTwo(){
        if(inv.getItem(6).getType()==Material.RED_STAINED_GLASS_PANE){
            inv.setItem(6, ListingsUtil.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,two.getDisplayName()+ChatColor.GREEN+" is ready!"));
            inv.setItem(7, ListingsUtil.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,two.getDisplayName()+ChatColor.GREEN+" is ready!"));
            inv.setItem(8, ListingsUtil.createGuiItem(Material.GREEN_STAINED_GLASS_PANE,two.getDisplayName()+ChatColor.GREEN+" is ready!"));
            ready2 = true;
        } else {
            inv.setItem(6, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,two.getDisplayName()+ChatColor.RED+" is not ready!"));
            inv.setItem(7, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,two.getDisplayName()+ChatColor.RED+" is not ready!"));
            inv.setItem(8, ListingsUtil.createGuiItem(Material.RED_STAINED_GLASS_PANE,two.getDisplayName()+ChatColor.RED+" is not ready!"));
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
