# AllShopMC 0.9.3

### Features:
- [x] Server GUI Shop for admins
- [x] Player GUI Shop
- [ ] GUI Auction House - *In Progress*
- [x] Physical Player Shop
- [x] Physical Server Shop - *In Progress*
- [ ] Plugin Moderation Tools - *In Progress*
- [ ] Search Feature
- [ ] Staff for Physical Shops
- [ ] Group Specific Shops
- [ ] MYSQL support
- [ ] Customizable messages
- [x] Customizable Prefix
- [x] Player trading
- [ ] Per world Support

### Commands:
* /AllShop (aka /as) - *Main command*
  * reload - *reloads the plugins configuration*
* /Shop - *Opens the server shop*
  * sell {price} - *Sells anything in your hand*
  * remove [ID] - *Removes a listing from the server shop*
* /Auction - *Opens the auction house*
  * bid {starting bid} - *Puts the item in your hand up for auction*
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
  * Lore is overridden sometimes by product information
  
* Player Shop:
  *none
  
* Auction House:
  * none
  
* Server Shop:
  * none
  
* Trading:
  *none
  
* Chest Shops:
  * none
  
### Bug Fixes:
  * Opening shops returns a null pointer when a new page is made with 1-4 items
  
### Dependencies:
* [Vault](https://www.spigotmc.org/resources/vault.34315/)
