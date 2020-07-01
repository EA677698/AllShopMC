package allshop.allshop.main;

import allshop.allshop.shops.Shop;
import allshop.allshop.shops.ShopType;
import allshop.allshop.shops.Trades;
import allshop.allshop.utils.ColorUtils;
import allshop.allshop.utils.ListingsUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
//import org.bukkit.entity.Player;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
//import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public final class AllShop extends JavaPlugin implements Listener {

    private static final Logger log = Logger.getLogger("Minecraft");
    public static AllShop instance;
    public Economy econ = null;
    public FileConfiguration config;
    public FileConfiguration data;
    public CopyOnWriteArrayList<Shop> openShops = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Trades> openTrades = new CopyOnWriteArrayList<>();
    public Object[] auctionListings;
    public Object[] digitalListings;
    public Object[] serverListings;
    public File folder;
    public int LISTINGS_LIMIT;
    public boolean DIGITAL_ENABLED;
    public boolean AUCTIONS_ENABLED;
    public boolean SERVER_SHOP_ENABLED;
    public boolean TRADING_ENABLED;
    public String PREFIX;
    public JavaPlugin plugin;
//    static boolean mysql;
//    String username;
//    String password;
//    String url;
//    static Connection connection;
    Commands commands = new Commands();

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        plugin = this;
        getServer().getPluginManager().registerEvents(this,this);
        getCommand("as").setExecutor(commands);
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
//        mysql = config.getBoolean("mysql.enabled");
////        if(mysql){
////            username = config.getString("mysql.username");
////            password = config.getString("mysql.password");
////            url = config.getString("mysql.host")+"/"+config.getString("mysql.database");
////
////            try {
////                Class.forName("com.mysql.jdbc.Driver");
////            } catch (ClassNotFoundException e) {
////                e.printStackTrace();
////                System.err.println("jdbc driver unavailable!");
////                return;
////            }
////            try {
////                connection = DriverManager.getConnection(url,username,password);
////            } catch (SQLException e) { //catching errors)
////                e.printStackTrace(); //prints out SQLException errors to the console (if any)
////            }
////        }
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
        instance.data = YamlConfiguration.loadConfiguration(new File(instance.folder, "data.yml"));
        instance.LISTINGS_LIMIT = instance.config.getInt("shop-listings-limit");
        instance.DIGITAL_ENABLED = instance.config.getBoolean("digital-shop-enabled");
        instance.SERVER_SHOP_ENABLED = instance.config.getBoolean("server-shop-enabled");
        instance.TRADING_ENABLED = instance.config.getBoolean("trading-enabled");
        if(instance.DIGITAL_ENABLED){
            instance.digitalListings = instance.data.getConfigurationSection("digital").getKeys(false).toArray();
        }
        if(instance.SERVER_SHOP_ENABLED){
            instance.serverListings = instance.data.getConfigurationSection("server").getKeys(false).toArray();
        }
        instance.AUCTIONS_ENABLED = instance.config.getBoolean("auction-house-enabled");
        if(instance.AUCTIONS_ENABLED){
            instance.auctionListings = instance.data.getConfigurationSection("auction").getKeys(false).toArray();
        }
        instance.PREFIX = ColorUtils.format(instance.config.getString("prefix"));
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
                        trade.getTraderOne().sendMessage(PREFIX + ChatColor.GREEN + " You have successfully traded for [" + event.getInventory().getItem(5).getAmount() + "] " + event.getClickedInventory().getItem(5));
                        trade.getTraderOne().sendMessage(PREFIX + ChatColor.GREEN + " You have successfully traded for [" + event.getInventory().getItem(3).getAmount() + "] " + event.getClickedInventory().getItem(3));
                        trade.getTraderOne().getInventory().addItem(event.getClickedInventory().getItem(5));
                        trade.getTraderTwo().getInventory().addItem(event.getClickedInventory().getItem(3));
                        trade.getTraderOne().closeInventory();
                        trade.getTraderTwo().closeInventory();
                        openTrades.remove(trade);
                    }
                }
            }
        }
        if(openShops.size()>0&&(DIGITAL_ENABLED||SERVER_SHOP_ENABLED)){
            if(event.getView().getTitle().equals("AllShop")||event.getView().getTitle().equals("Server Shop")){
                Shop shop = null;
                for(Shop shops: openShops){
                    if(shops.getPlayer().equals(player)){
                        shop = shops;
                        break;
                    }
                }
                if(slot==49){
                    event.setCancelled(true);
                } else if(slot==53){
                    if(shop.getCurrentPage()+1<shop.getTotalPages()){
                        shop.setCurrentPage(shop.getCurrentPage()+1);
                        ListingsUtil.loadOptions(shop.getInv(),shop.getCurrentPage(),shop.getTotalPages());
                    }
                } else if(slot==45){
                    if(shop.getCurrentPage()>1){
                        shop.setCurrentPage(shop.getCurrentPage()-1);
                        ListingsUtil.loadOptions(shop.getInv(),shop.getCurrentPage(),shop.getTotalPages());
                    }
                    } else if(!(event.getClickedInventory().getItem(slot)==null)) {
                    if (econ.getBalance(player) > data.getInt(getMainKey(shop.getType()) + getListings(shop.getType())[(slot + 1)] + ".Price")) {
                        ItemStack item = event.getCurrentItem();
                        econ.withdrawPlayer(player, data.getInt(getMainKey(shop.getType()) + getListings(shop.getType())[(slot + 1)] + ".Price"));
                        player.getInventory().addItem(ListingsUtil.removeListingInfo(item,slot+1, ShopType.PLAYER_SHOP));
                        player.sendMessage(ChatColor.GREEN + "You have purchased [" + item.getAmount() + "] " + item.getItemMeta().getDisplayName());
                        if(shop.getType()==ShopType.PLAYER_SHOP) {
                            econ.depositPlayer(Bukkit.getPlayer(UUID.fromString(data.getString("digital." + digitalListings[(event.getSlot() + 1)] + ".UUID"))), data.getInt("digital." + (event.getSlot() + 1) + ".Price"));
                            data.set("digital." + digitalListings[(event.getSlot() + 1)], null);
                        }
                        try {
                            data.save(new File(folder, "data.yml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        loadData();
                        player.closeInventory();
                    } else {
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "You do not have enough money to buy this!");
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    public String getMainKey(ShopType type){
        switch (type){
            case PLAYER_SHOP:
                return "digital.";
            case AUCTION_HOUSE:
                return "auction.";
            default:
                return "server.";
        }
    }

    public Object[] getListings(ShopType type){
        switch (type){
            case PLAYER_SHOP:
                return digitalListings;
            case AUCTION_HOUSE:
                return auctionListings;
            default:
                return serverListings;
        }
    }

    @EventHandler
    public void digitalShopClosed(InventoryCloseEvent event){
        if(event.getView().getTitle().equals("AllShop")){
            for(Shop shop : openShops){
                if(shop.getPlayer().equals(event.getPlayer())){
                    openShops.remove(shop);
                    break;
                }
            }
        } else if(event.getView().getTitle().equals("Trade")){
            for(Trades trade : openTrades){
                if(trade.getInv().equals(event.getInventory())){
                    openTrades.remove(trade);
                    break;
                }
            }
        }
    }


}
