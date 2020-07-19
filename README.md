# AllShopMC 1.3.0

### Features:
- [x] Server GUI Shop for admins
- [x] Player GUI Shop
- [x] GUI Auction House
- [x] Physical Player Shop
- [x] Physical Server Shop
- [ ] Plugin Moderation Tools
- [ ] Search Feature
- [ ] Staff for Physical Shops
- [ ] Group Specific Shops
- [ ] MYSQL support
- [x] Customizable messages
- [x] Customizable Prefix
- [x] Player trading
- [ ] Per world Support - *Next on TODO*
- [x] 1.14 Compatibility
- [x] 1.15 Compatibility
- [x] 1.16 Compatibility

### Commands:
* /AllShop (aka /as) - *Main command*
  * reload - *reloads the plugins configuration*
* /Shop - *Opens the server shop*
  * sell {price} - *Sells anything in your hand*
  * remove [ID] - *Removes a listing from the server shop*
* /Auction - *Opens the auction house*
  * bid {starting bid} - *Puts the item in your hand up for auction*
  * remove [ID] [optional: return item boolean] - *Removes a listing from the player shop and can return the item*
* /Market - *Opens the player market*
  * sell {price} - *Sells anything in your hand*
  * remove [ID] [optional: return item boolean] - *Removes a listing from the player shop and can return the item*
* /Trade - *Trading command*
  * {Player} - *Initiates a trade with the requested player*
  * accept - *Accepts a requested trade*
  * deny - *Denies a requested trade*
  
### Permissions:
* allshop.* - permission node for everything
* allshop.admin - permission node for administrative actions (Selling to the server shop, reloading, removing chest shops)
* allshop.shop - permission node to use the server shop
* allshop.market - permission node to use player shop
* allshop.auction - permission node to use auction house
* allshop.trade - permission node for trading
* allshop.chest - permission node for anything relating to chest shops

### Known Bugs:

* Global:
  * none
  
* Player Shop:
  * none
  
* Auction House:
  * none
  
* Server Shop:
  * none
  
* Trading:
  * none
  
* Chest Shops:
  * none
  
### Bug Fixes:
  * Fixed bug that caused a null pointer exception when fetching time
  
### Dependencies:
* [Vault](https://www.spigotmc.org/resources/vault.34315/)
* Any economy plugin
