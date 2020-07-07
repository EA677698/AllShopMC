package allshop.allshop.gshops;

import allshop.allshop.main.AllShop;
import allshop.allshop.utils.ListingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.concurrent.CopyOnWriteArrayList;


public class Shop {

    private final Inventory inv;
    private int totalPages;
    private int currentPage = 1;
    private Player player;
    private ShopType type;
    private boolean waiting;
    private ItemStack selected;
    private int storedSlot;
    private CopyOnWriteArrayList<ItemStack[]> pages;

    public Shop(Player player, ShopType type){
        this.player = player;
        this.type = type;
        pages = new CopyOnWriteArrayList<>();
        if(type==ShopType.PLAYER_SHOP){
            inv = Bukkit.createInventory(null,54, "Market");
        } else if(type==ShopType.AUCTION_HOUSE) {
            inv = Bukkit.createInventory(null,54, "Auctions");
        } else {
            inv = Bukkit.createInventory(null, 54, "Server Shop");
        }
        totalPages = ((int)Math.ceil(((double) (ListingsUtil.getListings(type).length-1)/45.0)));
        ListingsUtil.loadOptions(inv, currentPage, totalPages);
        for(int i = 0; i<totalPages; i++){
            pages.add(new ItemStack[45]);
        }
        ListingsUtil.loadListings(this, type);
        if(AllShop.DEBUG) {
            System.out.println(AllShop.PREFIX + toString());
        }
        ListingsUtil.loadPage(this);
        player.openInventory(inv);
    }

    public void refresh(){
        totalPages = ((int)Math.ceil(((double) (ListingsUtil.getListings(type).length-1)/45.0)));
        ListingsUtil.loadOptions(inv, currentPage, totalPages);
        ListingsUtil.loadListings(this, type);
        if(AllShop.DEBUG) {
            System.out.println(AllShop.PREFIX+"Listings size: "+ListingsUtil.getListings(type).length);
            System.out.println(AllShop.PREFIX + toString());
        }
        ListingsUtil.loadPage(this);
    }


    public CopyOnWriteArrayList<ItemStack[]> getPages(){
        return pages;
    }

    public ItemStack[] getPage(int page){
        return pages.get(page-1);
    }

    public Inventory getInv() {
        return inv;
    }

    public Player getPlayer() {
        return player;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages(){
        return totalPages;
    }

    public ShopType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "inv=" + inv +
                ", totalPages=" + totalPages +
                ", currentPage=" + currentPage +
                ", player=" + player +
                ", type=" + type +
                '}';
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }


    public ItemStack getSelected() {
        return selected;
    }

    public void setSelected(ItemStack selected) {
        this.selected = selected;
    }

    public int getStoredSlot() {
        return storedSlot;
    }

    public void setStoredSlot(int storedSlot) {
        this.storedSlot = storedSlot;
    }
}
