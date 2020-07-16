package me.phantomknight.allshop.main;

import me.phantomknight.allshop.Metrics;
import me.phantomknight.allshop.gshops.Shop;
import me.phantomknight.allshop.gshops.Trades;
import me.phantomknight.allshop.pshops.ChestShops;
import me.phantomknight.allshop.utils.ColorUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public final class AllShop extends JavaPlugin {

    private final Logger log = Logger.getLogger("Minecraft");
    public Economy econ = null;
    public FileConfiguration config,data,messages;
    public final CopyOnWriteArrayList<Shop> openShops = new CopyOnWriteArrayList<>();
    public final CopyOnWriteArrayList<Trades> openTrades = new CopyOnWriteArrayList<>();
    public final CopyOnWriteArrayList<ChestShops> openTransactions = new CopyOnWriteArrayList<>();
    public Object[] auctionListings, digitalListings, serverListings;
    public String[] customMessages;
    public List<String> market;
    public List<String> server;
    public File folder;
    public int LISTINGS_LIMIT, EXPIRATION;
    public boolean DIGITAL_ENABLED, AUCTIONS_ENABLED, PHYSICAL_ENABLED, SERVER_SHOP_ENABLED, TRADING_ENABLED, DEBUG;
    public String PREFIX;
    public final Commands commands = new Commands(this);
    EventManager eventManager;
    public static AllShop instance;
    private boolean stop = false;

    @Override
    public void onEnable() {
        instance = this;
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        int pluginId = 8125; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
        getCommand("allshop").setExecutor(commands);
        getCommand("shop").setExecutor(commands);
        getCommand("auction").setExecutor(commands);
        getCommand("market").setExecutor(commands);
        getCommand("trade").setExecutor(commands);
        saveDefaultConfig();
        folder = getDataFolder();
        data = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data.yml"));
        saveResource("messages.yml",false);
        saveResource("data.yml",false);
        config = getConfig();
        loadData();
        eventManager = new EventManager(this);
        inventoryUpdates();
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

    public void loadData(){
        reloadConfig();
        config = getConfig();
        data = YamlConfiguration.loadConfiguration(new File(folder, "data.yml"));
        messages = YamlConfiguration.loadConfiguration(new File(folder,"messages.yml"));
        customMessages = new String[33];
        for(int i = 0; i<messages.getConfigurationSection("messages").getKeys(false).toArray().length; i++){
            customMessages[i] = messages.getString("messages."+messages.getConfigurationSection("messages").getKeys(false).toArray()[i]);
        }
        DEBUG = config.getBoolean("debug");
        LISTINGS_LIMIT = config.getInt("shop-listings-limit");
        DIGITAL_ENABLED = config.getBoolean("player-shop-enabled");
        PHYSICAL_ENABLED = config.getBoolean("chest-shop-enabled");
        SERVER_SHOP_ENABLED = config.getBoolean("server-shop-enabled");
        TRADING_ENABLED = config.getBoolean("trading-enabled");
        EXPIRATION = config.getInt("days-before-removal");
        stop = !config.getBoolean("real-time-shop-updates");
        AUCTIONS_ENABLED = false;
        if(DIGITAL_ENABLED){
            digitalListings = data.getConfigurationSection("digital").getKeys(false).toArray();
            market = messages.getStringList("market-listing");
        }
        if(SERVER_SHOP_ENABLED){
            serverListings = data.getConfigurationSection("server").getKeys(false).toArray();
            server = messages.getStringList("server-listing");
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
        stop = true;
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

    public void inventoryUpdates(){
        new BukkitRunnable(){
            @Override
            public void run(){
                if(!stop) {
                    for(Shop shop : openShops){
                        shop.refresh();
                        shop.getPlayer().updateInventory();
                    }
                } else{
                    this.cancel();
                }
            }
        }.runTaskTimer(this,0, (config.getInt("seconds-before-next-update")*20));
    }




}
