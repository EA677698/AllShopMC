package allshop.allshop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public class Shop {

    private final Inventory inv;
    private int totalPages;
    private int currentPage = 1;
    private Player player;
    private ShopType type;

    public Shop(Player player, ShopType type){
        this.player = player;
        this.type = type;
        if(type==ShopType.PLAYER_SHOP){
            inv = Bukkit.createInventory(null,54, "AllShop");
        } else if(type==ShopType.AUCTION_HOUSE) {
            inv = Bukkit.createInventory(null,54, "AllShop Auctions");
        } else {
            inv = Bukkit.createInventory(null, 54, "Server Shop");
        }
        totalPages = (int)(((double)AllShop.digitalListings.length/44.0))+1;
        ListingsUtil.loadOptions(inv, currentPage, totalPages);
        ListingsUtil.loadListings(inv, type);
        player.openInventory(inv);
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
}
