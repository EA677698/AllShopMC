package allshop.allshop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
//import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
//import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

public final class AllShop extends JavaPlugin implements Listener {

    private static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    public static FileConfiguration config;
    public static FileConfiguration data;
    public static ArrayList<Shop> openShops = new ArrayList<>();
    public static Object[] auctionListings;
    public static Object[] digitalListings;
    public static Object[] serverListings;
    public static File folder;
    public static int LISTINGS_LIMIT;
    public static boolean DIGITAL_ENABLED;
    public static boolean AUCTIONS_ENABLED;
    public static boolean SERVER_SHOP_ENABLED;
    public static String PREFIX;
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
        getServer().getPluginManager().registerEvents(this,this);
        getCommand("as").setExecutor(commands);
        getCommand("shop").setExecutor(commands);
        getCommand("auction").setExecutor(commands);
        getCommand("market").setExecutor(commands);
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
        LISTINGS_LIMIT = config.getInt("shop-listings-limit");
        DIGITAL_ENABLED = config.getBoolean("digital-shop-enabled");
        SERVER_SHOP_ENABLED = config.getBoolean("server-shop-enabled");
        if(DIGITAL_ENABLED){
            digitalListings = data.getConfigurationSection("digital").getKeys(false).toArray();
        }
        if(SERVER_SHOP_ENABLED){
            serverListings = data.getConfigurationSection("server").getKeys(false).toArray();
        }
        AUCTIONS_ENABLED = config.getBoolean("auction-house-enabled");
        if(AUCTIONS_ENABLED){
            auctionListings = data.getConfigurationSection("auction").getKeys(false).toArray();
        }
        PREFIX = ColorUtils.format(config.getString("prefix"));
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
        if(DIGITAL_ENABLED){
            if(event.getView().getTitle().equals("AllShop")){
                if(event.getSlot()==49){
                    event.setCancelled(true);
                } else if(event.getSlot()==53){
                    for(Shop shop: openShops){
                        if(shop.getPlayer().equals((event.getWhoClicked()))){
                            if(shop.getCurrentPage()+1<shop.getTotalPages()){
                                shop.setCurrentPage(shop.getCurrentPage()+1);
                                ListingsUtil.loadOptions(shop.getInv(),shop.getCurrentPage(),shop.getTotalPages());
                            }
                            break;
                        }
                    }
                } else if(event.getSlot()==45){
                        for(Shop shop: openShops){
                            if(shop.getPlayer().equals((event.getWhoClicked()))){
                                if(shop.getCurrentPage()>1){
                                    shop.setCurrentPage(shop.getCurrentPage()-1);
                                    ListingsUtil.loadOptions(shop.getInv(),shop.getCurrentPage(),shop.getTotalPages());
                                }
                                break;
                            }
                        }
                    } else if(!(event.getClickedInventory().getItem(event.getSlot())==null)) {
                    if (econ.getBalance((OfflinePlayer) event.getWhoClicked()) > data.getInt("digital." + AllShop.digitalListings[(event.getSlot() + 1)] + ".Price")) {
                        String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
                        econ.withdrawPlayer((OfflinePlayer) event.getWhoClicked(), data.getInt("digital." + AllShop.digitalListings[(event.getSlot() + 1)] + ".Price"));
                        econ.depositPlayer(Bukkit.getPlayer(UUID.fromString(data.getString("digital." + AllShop.digitalListings[(event.getSlot() + 1)] + ".UUID"))), data.getInt("digital." + (event.getSlot() + 1) + ".Price"));
                        event.getWhoClicked().getInventory().addItem(ListingsUtil.removeListingInfo(event.getCurrentItem(),event.getSlot()+1,ShopType.PLAYER_SHOP));
                        event.getWhoClicked().sendMessage(ChatColor.GREEN + "You have purchased [" + event.getCurrentItem().getAmount() + "] " + itemName);
                        data.set("digital." + AllShop.digitalListings[(event.getSlot() + 1)], null);
                        try {
                            AllShop.data.save(new File(AllShop.folder, "data.yml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        loadData();
                        event.getWhoClicked().closeInventory();
                    } else {
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().sendMessage(ChatColor.RED + "You do not have enough money to buy this!");
                    }
                }
                event.setCancelled(true);
            }
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
        }
    }

}
