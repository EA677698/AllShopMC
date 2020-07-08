package allshop.allshop.main;

import allshop.allshop.gshops.Shop;
import allshop.allshop.gshops.ShopType;
import allshop.allshop.gshops.Trades;
import allshop.allshop.pshops.ChestShops;
import allshop.allshop.utils.DoubleChestsUtil;
import allshop.allshop.utils.ColorUtils;
import allshop.allshop.utils.ListingsUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public final class AllShop extends JavaPlugin implements Listener {

    private static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    public static FileConfiguration config;
    public static FileConfiguration data;
    public static final CopyOnWriteArrayList<Shop> openShops = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Trades> openTrades = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<ChestShops> openTransactions = new CopyOnWriteArrayList<>();
    public static Object[] auctionListings;
    public static Object[] digitalListings;
    public static Object[] serverListings;
    public static File folder;
    public static int LISTINGS_LIMIT;
    public static int EXPIRATION;
    public static boolean DIGITAL_ENABLED;
    public static boolean AUCTIONS_ENABLED;
    public static boolean PHYSICAL_ENABLED;
    public static boolean SERVER_SHOP_ENABLED;
    public static boolean TRADING_ENABLED;
    public static boolean DEBUG;
    public static String PREFIX;
    public static JavaPlugin plugin;
    final Commands commands = new Commands();

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        plugin = this;
        getServer().getPluginManager().registerEvents(this,this);
        getCommand("allshop").setExecutor(commands);
        getCommand("shop").setExecutor(commands);
        getCommand("auction").setExecutor(commands);
        getCommand("market").setExecutor(commands);
        getCommand("trade").setExecutor(commands);
        saveDefaultConfig();
        folder = getDataFolder();
        data = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data.yml"));
        saveResource("data.yml",false);
        config = getConfig();
        loadData();
        Commands.noPermission = PREFIX+ChatColor.RED+" You do not have permission to do this!";
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"ALLSHOP INITIATED");

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static void loadData(){
        data = YamlConfiguration.loadConfiguration(new File(folder, "data.yml"));
        DEBUG = config.getBoolean("debug");
        LISTINGS_LIMIT = config.getInt("shop-listings-limit");
        DIGITAL_ENABLED = config.getBoolean("player-shop-enabled");
        PHYSICAL_ENABLED = config.getBoolean("chest-shop-enabled");
        SERVER_SHOP_ENABLED = config.getBoolean("server-shop-enabled");
        TRADING_ENABLED = config.getBoolean("trading-enabled");
        EXPIRATION = config.getInt("days-before-removal");
        AUCTIONS_ENABLED = false;
        if(DIGITAL_ENABLED){
            digitalListings = data.getConfigurationSection("digital").getKeys(false).toArray();
        }
        if(SERVER_SHOP_ENABLED){
            serverListings = data.getConfigurationSection("server").getKeys(false).toArray();
        }
        if(AUCTIONS_ENABLED){
            auctionListings = data.getConfigurationSection("auction").getKeys(false).toArray();
        }
        PREFIX = ColorUtils.format(config.getString("prefix"));
        PREFIX = PREFIX+" ";
        if(DEBUG){
            System.out.println(PREFIX+"Listings Limit: "+LISTINGS_LIMIT);
            System.out.println(PREFIX+"Player Shop Enabled: "+DIGITAL_ENABLED);
            System.out.println(PREFIX+"Server Shop Enabled: "+SERVER_SHOP_ENABLED);
            System.out.println(PREFIX+"Trading Enabled: "+TRADING_ENABLED);
            System.out.println(PREFIX+"Auctions Enabled: "+AUCTIONS_ENABLED);
            System.out.println(PREFIX+"Chest Shop Enabled: "+PHYSICAL_ENABLED);
            if(DIGITAL_ENABLED) {
                System.out.println(PREFIX + "Player Listings: " + (digitalListings.length - 1));
            }
            if(SERVER_SHOP_ENABLED) {
                System.out.println(PREFIX + "Server Shop Listings: " + (serverListings.length - 1));
            }
            if(AUCTIONS_ENABLED) {
                System.out.println(PREFIX + "Auction Listings: " + (auctionListings.length - 1));
            }
        }
    }


    @Override
    public void onDisable() {
        data = YamlConfiguration.loadConfiguration(new File(folder, "data.yml"));
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
//        if(mysql){
//            try {
//                if (connection!=null && !connection.isClosed()){
//                    connection.close();
//                }
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    @EventHandler
    public void stopItemMovement(InventoryClickEvent event){
        int slot = event.getSlot();
        Player player = (Player) event.getWhoClicked();
        if(DEBUG){
            System.out.println(PREFIX+"EVENT HANDLER SECTION");
            System.out.println(PREFIX+"SLOT: "+slot);
            System.out.println(PREFIX+"PLAYER: "+player);
            System.out.println(PREFIX+"INVENTORY TITLE: "+event.getView().getTitle());
            System.out.println(AllShop.PREFIX+"Shops: "+AllShop.openShops.size());
            for(Shop shop: openShops){
                System.out.println(PREFIX+shop.toString());
            }
        }
        if(event.getView().getTitle().equals("Trade")) {
            if (openTrades.size() > 0 && TRADING_ENABLED) {
                Trades trade = null;
                for (Trades trades : openTrades) {
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
                        trade.getTraderOne().sendMessage(PREFIX + ChatColor.GREEN + " You have successfully traded for [" + event.getInventory().getItem(5).getAmount() + "] " + event.getClickedInventory().getItem(5).getType().name());
                        trade.getTraderTwo().sendMessage(PREFIX + ChatColor.GREEN + " You have successfully traded for [" + event.getInventory().getItem(3).getAmount() + "] " + event.getClickedInventory().getItem(3).getType().name());
                        trade.getTraderOne().getInventory().addItem(event.getClickedInventory().getItem(5));
                        trade.getTraderTwo().getInventory().addItem(event.getClickedInventory().getItem(3));
                        trade.getTraderOne().closeInventory();
                        trade.getTraderTwo().closeInventory();
                        openTrades.remove(trade);
                    }
                }
            }
        }
        if(openShops.size()>0) {
            if (DIGITAL_ENABLED || SERVER_SHOP_ENABLED) {
                if (event.getView().getTitle().equals("Market") || event.getView().getTitle().equals("Server Shop")) {
                    Shop shop = null;
                    for (Shop shops : openShops) {
                        if (shops.getType() == ShopType.PLAYER_SHOP || shops.getType() == ShopType.SERVER_SHOP) {
                            if (shops.getPlayer().equals(player)) {
                                shop = shops;
                                break;
                            }
                        }
                    }
                    if (slot == 49) {
                        event.setCancelled(true);
                    } else if (slot == 53) {
                        if (shop.getCurrentPage() < shop.getTotalPages()) {
                            shop.setCurrentPage(shop.getCurrentPage() + 1);
                            shop.refresh();
                        }
                    } else if (slot == 45) {
                        if (shop.getCurrentPage() > 1) {
                            shop.setCurrentPage(shop.getCurrentPage() - 1);
                            shop.refresh();
                        }
                    } else {
                        try {
                            if (event.getClickedInventory().getItem(slot) != null) {
                                String ID = ListingsUtil.getListings(shop.getType())[((shop.getCurrentPage() - 1) * 45) + (slot + 1)].toString();
                                if (DEBUG) {
                                    System.out.println(PREFIX+ListingsUtil.getMainKey(shop.getType()));
                                    System.out.println(PREFIX+shop.getType());
                                    System.out.println(PREFIX+ListingsUtil.getListings(shop.getType()).length);
                                    System.out.println(PREFIX+ListingsUtil.getListings(shop.getType())[((shop.getCurrentPage() - 1) * 45) + (slot + 1)]);
                                    System.out.println(PREFIX+"ID: "+ID);
                                    System.out.println(PREFIX+shop.getCurrentPage());
                                    System.out.println(PREFIX+slot);
                                }
                                if (econ.getBalance(player) > data.getInt(ListingsUtil.getMainKey(shop.getType()) + ListingsUtil.getListings(shop.getType())[((shop.getCurrentPage() - 1) * 45) + (slot + 1)] + ".Price")) {
                                    ItemStack item = event.getCurrentItem();
                                    if(shop.getType()==ShopType.SERVER_SHOP){
                                        shop.setStoredSlot(slot);
                                        shop.setWaiting(true);
                                        shop.setSelected(item);
                                        player.sendMessage(PREFIX+ChatColor.LIGHT_PURPLE+"Please enter in chat the amount of " + item.getType().name() + " you would like to buy.");
                                        player.closeInventory();
                                    } else {
                                        econ.withdrawPlayer(player, data.getInt(ListingsUtil.getMainKey(shop.getType()) + ListingsUtil.getListings(shop.getType())[((shop.getCurrentPage() - 1) * 45) + (slot + 1)] + ".Price"));
                                        player.getInventory().addItem(ListingsUtil.removeListingInfo(item, ((shop.getCurrentPage() - 1) * 45) + (slot + 1), shop.getType()));
                                        player.sendMessage(PREFIX + ChatColor.GREEN + "You have purchased [" + item.getAmount() + "] " + item.getType().name());
                                        if (shop.getType() == ShopType.PLAYER_SHOP) {
                                            System.out.println("digital." +ID + ".UUID");
                                            System.out.println("UUID: "+data.getString("digital." +ID + ".UUID"));
                                            System.out.println(Bukkit.getOfflinePlayer(UUID.fromString(data.getString("digital." +ID + ".UUID"))));
                                            System.out.println(data.getInt("digital." +ID + ".Price"));
                                            econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(data.getString("digital." +ID + ".UUID"))), data.getInt("digital." +ID + ".Price"));
                                            data.set("digital." +ID, null);
                                        }
                                        try {
                                            data.save(new File(folder, "data.yml"));
                                        } catch (IOException e) {
                                            if(DEBUG) {
                                                e.printStackTrace();
                                            }
                                        }
                                        loadData();
                                        player.closeInventory();
                                        if(shop.getType()!=ShopType.SERVER_SHOP) {
                                            openShops.remove(shop);
                                        }
                                    }
                                } else {
                                    player.closeInventory();
                                    openShops.remove(shop);
                                    player.sendMessage(PREFIX + ChatColor.RED + "You do not have enough money to buy this!");
                                }
                            }
                        } catch (Exception e) {
                            if(DEBUG){
                                e.printStackTrace();
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            } else if(AUCTIONS_ENABLED){
                if(event.getView().getTitle().equals("Auctions")){
                    Shop shop = null;
                    for (Shop shops : openShops) {
                        if (shop.getType()==ShopType.AUCTION_HOUSE) {
                            if (shops.getPlayer().equals(player)) {
                                shop = shops;
                                break;
                            }
                        }
                    }
                    if (slot == 49) {
                        event.setCancelled(true);
                    } else if (slot == 53) {
                        if (shop.getCurrentPage() < shop.getTotalPages()) {
                            shop.setCurrentPage(shop.getCurrentPage() + 1);
                            shop.refresh();
                        }
                    } else if (slot == 45) {
                        if (shop.getCurrentPage() > 1) {
                            shop.setCurrentPage(shop.getCurrentPage() - 1);
                            shop.refresh();
                        }
                    }else {
                        try {
                            if (event.getClickedInventory().getItem(slot) != null) {
                                if (DEBUG) {
                                    System.out.println(ListingsUtil.getMainKey(shop.getType()));
                                    System.out.println(shop.getType());
                                    System.out.println(ListingsUtil.getListings(shop.getType())[((shop.getCurrentPage() - 1) * 45) + (slot + 1)]);
                                }

                            }
                        } catch (Exception e) {
                            if(DEBUG){
                                e.printStackTrace();
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    @EventHandler
    public void chestOpen(PlayerInteractEvent event) {
        if (PHYSICAL_ENABLED) {
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
                if (DEBUG) {
                    p.sendMessage(PREFIX + "Sign find on " + face);
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
        if(PHYSICAL_ENABLED) {
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
                        event.getPlayer().sendMessage(Commands.noPermission);
                    }
                }
            }
        }
    }

    @EventHandler
    public void shopBreak(BlockBreakEvent event){
        if(PHYSICAL_ENABLED){
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
                                    return;
                                }
                            } else {
                                if(checkSign(DoubleChestsUtil.getRightChest(chest).getBlock(),event.getPlayer())){
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

    @EventHandler
    public void catchQuantity(AsyncPlayerChatEvent e){
        if(PHYSICAL_ENABLED) {
            for (ChestShops shops : openTransactions) {
                if (shops.getPlayer().equals(e.getPlayer()) || shops.getPlayer() == e.getPlayer()) {
                    if (e.getMessage().toLowerCase().equals("cancel")) {
                        openTransactions.remove(shops);
                        e.getPlayer().sendMessage(PREFIX + ChatColor.RED + "Transaction Cancelled!");
                        e.setCancelled(true);
                    } else {
                        try {
                            shops.setAmount(Integer.parseInt(e.getMessage()));
                            shops.processInformation();
                            e.setCancelled(true);
                        } catch (Exception ex) {
                            if (DEBUG) {
                                ex.printStackTrace();
                            } else {
                                e.getPlayer().sendMessage(PREFIX + ChatColor.RED + "Amount must be an integer!");
                            }
                        }
                    }
                }
            }
        }
        for(Shop shop:openShops){
            if(!shop.isWaiting()){
            } else {
                if(e.getPlayer().equals(shop.getPlayer())){
                    Player player = e.getPlayer();
                    ItemStack item = shop.getSelected();
                    ItemStack give = ListingsUtil.removeListingInfo(item, ((shop.getCurrentPage() - 1) * 45) + (shop.getStoredSlot() + 1), shop.getType());
                    try{
                        if (econ.getBalance(player) > (data.getInt(ListingsUtil.getMainKey(shop.getType()) + ListingsUtil.getListings(shop.getType())[((shop.getCurrentPage() - 1) * 45) + (shop.getStoredSlot() + 1)] + ".Price"))*Integer.parseInt(e.getMessage())) {
                            econ.withdrawPlayer(player, (data.getInt(ListingsUtil.getMainKey(shop.getType()) + ListingsUtil.getListings(shop.getType())[((shop.getCurrentPage() - 1) * 45) + (shop.getStoredSlot() + 1)] + ".Price"))*Integer.parseInt(e.getMessage()));
                            give.setAmount(Integer.parseInt(e.getMessage()));
                            player.getInventory().addItem(give);
                            player.sendMessage(PREFIX + ChatColor.GREEN + "You have purchased [" + item.getAmount() + "] " + item.getType().name());
                        } else {
                            player.sendMessage(PREFIX + ChatColor.RED + "You do not have enough money to buy this!");
                        }
                        openShops.remove(shop);
                        e.setCancelled(true);
                    } catch (Exception e1){
                        if(DEBUG){
                            e1.printStackTrace();
                        }
                        player.sendMessage(PREFIX+ChatColor.RED+"Amount must be an integer!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void SignChecker(PlayerInteractEvent event){
        if(PHYSICAL_ENABLED) {
            Player p = event.getPlayer();
            if (p.hasPermission("allshop.chest")) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getClickedBlock().getType() == Material.OAK_WALL_SIGN) {
                        Sign s = (Sign) event.getClickedBlock().getState();
                        if (s.getLine(0).equals(ChatColor.YELLOW + "[" + ChatColor.GREEN + "Shop" + ChatColor.YELLOW + "]")) {
                            String[] faces = {"EAST", "NORTH", "SOUTH", "WEST"};
                            for (String face : faces) {
                                if (event.getClickedBlock().getRelative(BlockFace.valueOf(face)).getType() == Material.CHEST) {
                                    if (DEBUG) {
                                        p.sendMessage(PREFIX + "Chest find on " + face);
                                    }
                                    if (!s.getLine(3).equals(ChatColor.RED + "Server")) {
                                        new ChestShops(s, (Chest) event.getClickedBlock().getRelative(BlockFace.valueOf(face)).getState(), p, s.getLine(3));
                                        openTransactions.get(openTransactions.size() - 1).setAmount(Integer.parseInt(s.getLine(1).substring(s.getLine(1).indexOf(" ") + 1)));
                                        openTransactions.get(openTransactions.size() - 1).processInformation();
                                    } else {
                                        new ChestShops(s, (Chest) event.getClickedBlock().getRelative(BlockFace.valueOf(face)).getState(), p, s.getLine(3));
                                        Material itemSold;
                                        if (!s.getLine(3).equals(ChatColor.RED + "Server")) {
                                            itemSold = Material.getMaterial(s.getLine(1).substring(0, s.getLine(1).indexOf(" ")).toUpperCase());
                                        } else {
                                            itemSold = Material.getMaterial(s.getLine(1).toUpperCase());
                                        }
                                        if (itemSold == p.getInventory().getItemInMainHand().getType()) {
                                            p.sendMessage(PREFIX + ChatColor.LIGHT_PURPLE + "Please enter in chat the amount of " + s.getLine(1) + " you would like to sell.");
                                        } else {
                                            p.sendMessage(PREFIX + ChatColor.LIGHT_PURPLE + "Please enter in chat the amount of " + s.getLine(1) + " you would like to buy.");
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                p.sendMessage(Commands.noPermission);
            }
        }
    }

    @EventHandler
    public void digitalShopClosed(InventoryCloseEvent event){
        if(event.getView().getTitle().equals("Market")||event.getView().getTitle().equals("Server Shop")||event.getView().getTitle().equals("Auctions")){
            for(Shop shop : openShops){
                if(!shop.isWaiting()) {
                    if (shop.getPlayer().equals(event.getPlayer())) {
                        openShops.remove(shop);
                        break;
                    }
                }
            }
        } else if(event.getView().getTitle().equals("Trade")){
            for(Trades trade : openTrades){
                if(trade.getInv().equals(event.getInventory())){
                    if(!trade.isCompleted()){
                        trade.sendMessageToParticipants(PREFIX+ChatColor.RED+"Trade Cancelled");
                        trade.getTraderOne().getInventory().addItem(trade.getInv().getItem(3));
                        trade.getTraderTwo().getInventory().addItem(trade.getInv().getItem(5));
                    }
                    if(event.getPlayer().equals(trade.getTraderOne())){
                        openTrades.remove(trade);
                        trade.getTraderTwo().closeInventory();
                    } else{
                        openTrades.remove(trade);
                        trade.getTraderOne().closeInventory();
                    }
                    break;
                }
            }
        }
    }



}
