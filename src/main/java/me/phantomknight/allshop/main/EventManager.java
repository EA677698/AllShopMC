package me.phantomknight.allshop.main;

import me.phantomknight.allshop.gshops.Shop;
import me.phantomknight.allshop.gshops.ShopType;
import me.phantomknight.allshop.gshops.Trades;
import me.phantomknight.allshop.pshops.ChestShops;
import me.phantomknight.allshop.utils.ColorUtils;
import me.phantomknight.allshop.utils.DoubleChestsUtil;
import me.phantomknight.allshop.utils.ListingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import java.io.File;
import java.io.IOException;

public class EventManager implements Listener {

    public final AllShop allShop;

    public EventManager(AllShop allShop){
        allShop.getServer().getPluginManager().registerEvents(this,allShop);
        this.allShop = allShop;
    }

    @EventHandler
    public void stopItemMovement(InventoryClickEvent event){
        int slot = event.getSlot();
        Player player = (Player) event.getWhoClicked();
        if(allShop.DEBUG){
            System.out.println(allShop.PREFIX+"EVENT HANDLER SECTION");
            System.out.println(allShop.PREFIX+"SLOT: "+slot);
            System.out.println(allShop.PREFIX+"PLAYER: "+player);
            System.out.println(allShop.PREFIX+"INVENTORY TITLE: "+event.getView().getTitle());
            System.out.println(allShop.PREFIX+"Shops: "+allShop.openShops.size());
            for(Shop shop: allShop.openShops){
                System.out.println(allShop.PREFIX+shop.toString());
            }
        }
        if(event.getView().getTitle().equals(ColorUtils.format(allShop.customMessages[32]))) {
            if (allShop.openTrades.size() > 0 && allShop.TRADING_ENABLED) {
                Trades trade = null;
                for (Trades trades : allShop.openTrades) {
                    if (trades.getInv().equals(event.getClickedInventory())) {
                        trade = trades;
                        break;
                    }
                }
                if (trade != null) {
                    if (trade.getTraderOne().equals(player)) {
                        if (slot == 0 || slot == 1 || slot == 2) {
                            trade.changeStatusOne();
                        }
                        if (slot != 3) {
                            event.setCancelled(true);
                        }
                    } else {
                        if (slot == 6 || slot == 7 || slot == 8) {
                            trade.changeStatusTwo();
                        }
                        if (slot != 5) {
                            event.setCancelled(true);
                        }
                    }
                    if (trade.isReady1() && trade.isReady2()) {
                        trade.setCompleted(true);
                        trade.getTraderOne().sendMessage(allShop.PREFIX + ChatColor.GREEN + " You have successfully traded for [" + event.getInventory().getItem(5).getAmount() + "] " + event.getClickedInventory().getItem(5).getType().name());
                        trade.getTraderTwo().sendMessage(allShop.PREFIX + ChatColor.GREEN + " You have successfully traded for [" + event.getInventory().getItem(3).getAmount() + "] " + event.getClickedInventory().getItem(3).getType().name());
                        trade.getTraderOne().getInventory().addItem(event.getClickedInventory().getItem(5));
                        trade.getTraderTwo().getInventory().addItem(event.getClickedInventory().getItem(3));
                        trade.getTraderOne().closeInventory();
                        trade.getTraderTwo().closeInventory();
                        allShop.openTrades.remove(trade);
                    }
                }
            }
        }
        if(allShop.openShops.size()>0) {
            if (allShop.DIGITAL_ENABLED || allShop.SERVER_SHOP_ENABLED) {
                if (event.getView().getTitle().equals(ColorUtils.format(allShop.customMessages[31])) || event.getView().getTitle().equals(ColorUtils.format(allShop.customMessages[30]))) {
                    Shop shop = null;
                    for (Shop shops : allShop.openShops) {
                        if (shops.getType() == ShopType.PLAYER_SHOP || shops.getType() == ShopType.SERVER_SHOP) {
                            if (shops.getPlayer().equals(player)) {
                                shop = shops;
                                break;
                            }
                        }
                    }
                    try {
                        if(!blockOptionsPickUp(shop,event,slot)) {
                            if (event.getClickedInventory().getItem(slot) != null) {
                                int index = (((shop.getCurrentPage() - 1) * 45) + (slot + 1));
                                if (allShop.econ.getBalance(player) > ListingsUtil.getListingPrice(index, shop.getType())) {
                                    ItemStack item = event.getCurrentItem();
                                    if (shop.getType() == ShopType.SERVER_SHOP) {
                                        shop.setStoredSlot(slot);
                                        shop.setWaiting(true);
                                        shop.setSelected(item);
                                        player.sendMessage(allShop.PREFIX + ChatColor.LIGHT_PURPLE + "Please enter in chat the amount of " + item.getType().name() + " you would like to buy.");
                                        player.sendMessage(allShop.PREFIX+ChatColor.LIGHT_PURPLE+"Type in chat 'Cancel' to cancel");
                                        player.closeInventory();
                                    } else {
                                        allShop.econ.withdrawPlayer(player, ListingsUtil.getListingPrice(index, shop.getType()));
                                        player.getInventory().addItem(ListingsUtil.removeListingInfo(item, shop.getType()));
                                        player.sendMessage(allShop.PREFIX + ChatColor.GREEN + "You have purchased "+"[" + item.getAmount() + "] "+ item.getType().name());
                                        if (shop.getType() == ShopType.PLAYER_SHOP) {
                                            allShop.econ.depositPlayer(Bukkit.getOfflinePlayer(ListingsUtil.getSellerUUID(index, shop.getType())), ListingsUtil.getListingPrice(index,shop.getType()));
                                            allShop.data.set("digital." + ListingsUtil.getListings(shop.getType())[index], null);
                                        }
                                        try {
                                            allShop.data.save(new File(allShop.folder, "data.yml"));
                                        } catch (IOException e) {
                                            if (allShop.DEBUG) {
                                                e.printStackTrace();
                                            }
                                        }
                                        allShop.loadData();
                                        player.closeInventory();
                                        if (shop.getType() != ShopType.SERVER_SHOP) {
                                            allShop.openShops.remove(shop);
                                        }
                                    }
                                } else {
                                    player.closeInventory();
                                    allShop.openShops.remove(shop);
                                    player.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[20]));
                                }
                            }
                        } else {
                            event.setCancelled(true);
                            return;
                        }
                    } catch (Exception e) {
                        if(allShop.DEBUG){
                            e.printStackTrace();
                        }
                    }
                    event.setCancelled(true);
                } else if(allShop.AUCTIONS_ENABLED){
                    if(event.getView().getTitle().equals(ColorUtils.format(allShop.customMessages[33]))){
                        Shop auction = null;
                        for (Shop shops : allShop.openShops) {
                            if (shops.getType()==ShopType.AUCTION_HOUSE) {
                                if (shops.getPlayer().equals(player)) {
                                    auction = shops;
                                    break;
                                }
                            }
                        }
                        try {
                            if(!blockOptionsPickUp(auction,event,slot)) {
                                if (event.getClickedInventory().getItem(slot) != null) {
                                    int index = (((auction.getCurrentPage() - 1) * 45) + (slot + 1));
                                    if(allShop.econ.getBalance(player)>ListingsUtil.getCurrentBid(index)){
                                        ItemStack item = ListingsUtil.getListingItem(index,auction.getType());
                                        auction.setStoredSlot(slot);
                                        auction.setWaiting(true);
                                        auction.setSelected(item);
                                        player.sendMessage(allShop.PREFIX + ChatColor.LIGHT_PURPLE + "Please enter in chat how much you would like to bid for the "+item.getType().name());
                                        player.sendMessage(allShop.PREFIX+ChatColor.LIGHT_PURPLE+"Type in chat 'Cancel' to cancel");
                                        player.closeInventory();
                                    } else{
                                        player.sendMessage(allShop.PREFIX+ChatColor.RED+"You do not have enough money to bid on this item!");
                                    }

                                }
                            } else {
                                event.setCancelled(true);
                                return;
                            }
                        } catch (Exception e) {
                            if(allShop.DEBUG){
                                e.printStackTrace();
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    public boolean blockOptionsPickUp(Shop shop, InventoryClickEvent event, int slot){
        if (slot == 49) {
            event.setCancelled(true);
            return true;
        } else if (slot == 53) {
            if (shop.getCurrentPage() < shop.getTotalPages()) {
                shop.setCurrentPage(shop.getCurrentPage() + 1);
                shop.refresh();
                shop.getPlayer().updateInventory();
            }
            return true;
        } else if (slot == 45) {
            if (shop.getCurrentPage() > 1) {
                shop.setCurrentPage(shop.getCurrentPage() - 1);
                shop.refresh();
                shop.getPlayer().updateInventory();
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    @EventHandler
    public void chestOpen(PlayerInteractEvent event) {
        if (allShop.PHYSICAL_ENABLED) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Player p = event.getPlayer();
                if (event.getClickedBlock().getState().getType() == Material.CHEST) {
                    if(checkSign(event.getClickedBlock(),p)){
                        event.setCancelled(true);
                        return;
                    } else {
                        Chest chest = (Chest) event.getClickedBlock().getState();
                        if(DoubleChestsUtil.isChestDoubleChest(chest)){
                            if(DoubleChestsUtil.getRightChest(chest).equals(chest)){
                                if(checkSign(DoubleChestsUtil.getLeftChest(chest).getBlock(),p)){
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                if(checkSign(DoubleChestsUtil.getRightChest(chest).getBlock(),p)){
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean checkSign(Block block, Player p){
        Sign sign = null;
        String[] faces = {"EAST", "NORTH", "SOUTH", "WEST"};
        for (String face : faces) {
            if (block.getRelative(BlockFace.valueOf(face)).getType() == Material.OAK_WALL_SIGN) {
                sign = (Sign) block.getRelative(BlockFace.valueOf(face)).getState();
                if (allShop.DEBUG) {
                    p.sendMessage(allShop.PREFIX + "Sign find on " + face);
                }
            }
        }
        if (sign != null) {
            if (sign.getLine(0).equals(ChatColor.YELLOW + "[" + ChatColor.GREEN + "Shop" + ChatColor.YELLOW + "]") && !sign.getLine(3).equals("")) {
                if (!sign.getLine(3).equals(p.getName()) && !p.hasPermission("allshop.admin")) {
                    String data = sign.getBlockData().getAsString();
                    String direction = data.substring(data.indexOf("facing=")+7,data.indexOf(","));
                    Location temp = DoubleChestsUtil.getBackChestLocation(direction,sign.getLocation());
                    return temp.getBlock().equals(block);
                }
            }
        }
        return false;
    }


    @EventHandler
    public void signPlace(SignChangeEvent event){
        if(allShop.PHYSICAL_ENABLED) {
            Sign sign;
            if (event.getBlock().getState().getType() == Material.OAK_WALL_SIGN) {
                sign = (Sign) event.getBlock().getState();
                if (event.getLine(0).equals("[Shop]")) {
                    if (event.getPlayer().hasPermission("allshop.chest")) {
                        sign.setEditable(true);
                        event.setLine(0, ChatColor.YELLOW + "[" + ChatColor.GREEN + "Shop" + ChatColor.YELLOW + "]");
                        if(event.getLine(3).equals("Server")){
                            if(event.getPlayer().hasPermission("allshop.admin")){
                                event.setLine(3,ChatColor.RED+"Server");
                            } else {
                                event.setLine(3, event.getPlayer().getName());
                            }
                        } else {
                            event.setLine(3, event.getPlayer().getName());
                        }
                        sign.setEditable(false);
                    } else {
                        event.getPlayer().sendMessage(allShop.PREFIX+ ColorUtils.format(allShop.customMessages[27]));
                    }
                }
            }
        }
    }

    @EventHandler
    public void shopBreak(BlockBreakEvent event){
        if(allShop.PHYSICAL_ENABLED){
            if(event.getBlock().getState().getType()==Material.OAK_WALL_SIGN){
                Sign sign = (Sign) event.getBlock().getState();
                if(sign.getLine(0).equals(ChatColor.YELLOW+"["+ChatColor.GREEN+"Shop"+ChatColor.YELLOW+"]")){
                    if(!sign.getLine(3).equals("")) {
                        if (!sign.getLine(3).equals(event.getPlayer().getName()) && !event.getPlayer().hasPermission("allshop.admin")) {
                            event.setCancelled(true);
                        }
                    }
                }
            } else {
                if(event.getBlock().getState().getType()==Material.CHEST){
                    if(checkSign(event.getBlock(), event.getPlayer())){
                        event.setCancelled(true);
                    } else {
                        Chest chest = (Chest) event.getBlock().getState();
                        if(DoubleChestsUtil.isChestDoubleChest(chest)){
                            if(DoubleChestsUtil.getRightChest(chest).equals(chest)){
                                if(checkSign(DoubleChestsUtil.getLeftChest(chest).getBlock(),event.getPlayer())){
                                    event.setCancelled(true);
                                }
                            } else {
                                if(checkSign(DoubleChestsUtil.getRightChest(chest).getBlock(),event.getPlayer())){
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void catchQuantity(AsyncPlayerChatEvent e){
        if(allShop.PHYSICAL_ENABLED) {
            for (ChestShops shops : allShop.openTransactions) {
                if (shops.getPlayer().equals(e.getPlayer()) || shops.getPlayer() == e.getPlayer()) {
                    if (e.getMessage().toLowerCase().equals("cancel")) {
                        allShop.openTransactions.remove(shops);
                        e.getPlayer().sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[14]));
                        e.setCancelled(true);
                    } else {
                        try {
                            shops.setAmount(Integer.parseInt(e.getMessage()));
                            shops.processInformation();
                            e.setCancelled(true);
                        } catch (Exception ex) {
                            if (allShop.DEBUG) {
                                ex.printStackTrace();
                            } else {
                                e.getPlayer().sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[4]));
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
        for (Shop shop : allShop.openShops) {
            if (shop.isWaiting()) {
                if (e.getPlayer().equals(shop.getPlayer())) {
                    if (shop.getType() == ShopType.SERVER_SHOP) {
                        if (e.getMessage().toLowerCase().equals("cancel")) {
                            allShop.openShops.remove(shop);
                            e.getPlayer().sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[14]));
                            e.setCancelled(true);
                            return;
                        }
                        Player player = e.getPlayer();
                        ItemStack item = shop.getSelected();
                        ItemStack give = ListingsUtil.removeListingInfo(item, shop.getType());
                        int index = (((shop.getCurrentPage() - 1) * 45) + (shop.getStoredSlot() + 1));
                        try {
                            if (allShop.econ.getBalance(player) > (ListingsUtil.getListingPrice(index,shop.getType())) * Integer.parseInt(e.getMessage())) {
                                allShop.econ.withdrawPlayer(player, (ListingsUtil.getListingPrice(index,shop.getType())) * Integer.parseInt(e.getMessage()));
                                give.setAmount(Integer.parseInt(e.getMessage()));
                                player.getInventory().addItem(give);
                                player.sendMessage(allShop.PREFIX + ChatColor.GREEN + "You have purchased [" + item.getAmount() + "] " + item.getType().name());
                            } else {
                                player.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[20]));
                                e.setCancelled(true);
                            }
                            allShop.openShops.remove(shop);
                            e.setCancelled(true);
                        } catch (Exception e1) {
                            if (allShop.DEBUG) {
                                e1.printStackTrace();
                            }
                            player.sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[4]));
                        }
                    } else if(shop.getType()==ShopType.AUCTION_HOUSE){
                        if (e.getMessage().toLowerCase().equals("cancel")) {
                            allShop.openShops.remove(shop);
                            e.getPlayer().sendMessage(allShop.PREFIX + ColorUtils.format(allShop.customMessages[14]));
                            e.setCancelled(true);
                            return;
                        }
                        Player player = e.getPlayer();
                        int index = (((shop.getCurrentPage() - 1) * 45) + (shop.getStoredSlot() + 1));
                        try{
                            if(Integer.parseInt(e.getMessage())>ListingsUtil.getCurrentBid(index)){
                                if(Integer.parseInt(e.getMessage())<=allShop.econ.getBalance(player)) {
                                    player.sendMessage(allShop.PREFIX + ChatColor.GREEN + "You have bid " + Integer.parseInt(e.getMessage()) + " for the " + shop.getSelected().getType().name());
                                    allShop.data.set("auction." + ListingsUtil.getListings(ShopType.AUCTION_HOUSE)[index] + ".Bid", Integer.parseInt(e.getMessage()));
                                    allShop.data.set("auction." + ListingsUtil.getListings(ShopType.AUCTION_HOUSE)[index] + ".Bidder", String.valueOf(player.getUniqueId()));
                                    allShop.openShops.remove(shop);
                                } else {
                                    player.sendMessage(allShop.PREFIX+ChatColor.RED+"You do not have enough money for this!");
                                }
                            } else {
                                player.sendMessage(allShop.PREFIX+ColorUtils.format(allShop.customMessages[35]));
                            }
                            e.setCancelled(true);
                        } catch (Exception error){
                            player.sendMessage(allShop.PREFIX+ColorUtils.format(allShop.customMessages[34]));
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void SignChecker(PlayerInteractEvent event){
        if(allShop.PHYSICAL_ENABLED) {
            Player p = event.getPlayer();
            if (p.hasPermission("allshop.chest")) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getClickedBlock().getType() == Material.OAK_WALL_SIGN) {
                        Sign s = (Sign) event.getClickedBlock().getState();
                        if (s.getLine(0).equals(ChatColor.YELLOW + "[" + ChatColor.GREEN + "Shop" + ChatColor.YELLOW + "]")) {
                            String[] faces = {"EAST", "NORTH", "SOUTH", "WEST"};
                            for (String face : faces) {
                                if (event.getClickedBlock().getRelative(BlockFace.valueOf(face)).getType() == Material.CHEST) {
                                    if (allShop.DEBUG) {
                                        p.sendMessage(allShop.PREFIX + "Chest find on " + face);
                                    }
                                    if (!s.getLine(3).equals(ChatColor.RED + "Server")) {
                                        new ChestShops(allShop,s, (Chest) event.getClickedBlock().getRelative(BlockFace.valueOf(face)).getState(), p, s.getLine(3));
                                        allShop.openTransactions.get(allShop.openTransactions.size() - 1).setAmount(Integer.parseInt(s.getLine(1).substring(s.getLine(1).indexOf(" ") + 1)));
                                        allShop.openTransactions.get(allShop.openTransactions.size() - 1).processInformation();
                                    } else {
                                        new ChestShops(allShop,s, (Chest) event.getClickedBlock().getRelative(BlockFace.valueOf(face)).getState(), p, s.getLine(3));
                                        Material itemSold;
                                        if (!s.getLine(3).equals(ChatColor.RED + "Server")) {
                                            itemSold = Material.getMaterial(s.getLine(1).substring(0, s.getLine(1).indexOf(" ")).toUpperCase());
                                        } else {
                                            itemSold = Material.getMaterial(s.getLine(1).toUpperCase());
                                        }
                                        if (itemSold == p.getInventory().getItemInMainHand().getType()) {
                                            p.sendMessage(allShop.PREFIX + ChatColor.LIGHT_PURPLE + "Please enter in chat the amount of " + s.getLine(1) + " you would like to sell.");
                                        } else {
                                            p.sendMessage(allShop.PREFIX + ChatColor.LIGHT_PURPLE + "Please enter in chat the amount of " + s.getLine(1) + " you would like to buy.");

                                        }
                                        p.sendMessage(allShop.PREFIX+ChatColor.LIGHT_PURPLE+"Type in chat 'Cancel' to cancel");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                p.sendMessage(allShop.PREFIX+ ColorUtils.format(allShop.customMessages[27]));
            }
        }
    }

    @EventHandler
    public void digitalShopClosed(InventoryCloseEvent event){
        if(event.getView().getTitle().equals(ColorUtils.format(allShop.customMessages[31]))||event.getView().getTitle().equals(ColorUtils.format(allShop.customMessages[30]))||event.getView().getTitle().equals(ColorUtils.format(allShop.customMessages[33]))){
            for(Shop shop : allShop.openShops){
                if(!shop.isWaiting()) {
                    if (shop.getPlayer().equals(event.getPlayer())) {
                        allShop.openShops.remove(shop);
                        break;
                    }
                }
            }
        } else if(event.getView().getTitle().equals(ColorUtils.format(allShop.customMessages[32]))){
            for(Trades trade : allShop.openTrades){
                if(trade.getInv().equals(event.getInventory())){
                    if(!trade.isCompleted()){
                        trade.sendMessageToParticipants(allShop.PREFIX+ColorUtils.format(allShop.customMessages[12]));
                        trade.getTraderOne().getInventory().addItem(trade.getInv().getItem(3));
                        trade.getTraderTwo().getInventory().addItem(trade.getInv().getItem(5));
                    }
                    if(event.getPlayer().equals(trade.getTraderOne())){
                        allShop.openTrades.remove(trade);
                        trade.getTraderTwo().closeInventory();
                    } else{
                        allShop.openTrades.remove(trade);
                        trade.getTraderOne().closeInventory();
                    }
                    break;
                }
            }
        }
    }

}
