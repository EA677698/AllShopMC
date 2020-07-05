# AllShopMC 0.7.0

### Features:
- [x] Server GUI Shop for admins
- [x] Player GUI Shop
- [ ] GUI Auction House - *In Progress*
- [ ] Physical Player Shop
- [ ] Physical Server Shop
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
  * remove [ID] [optional: return item] - *Removes a listing from the player shop and can return the item*
* /Trade - *Trading command*
  * {Player} - *Initiates a trade with the requested player*
  * accept - *Accepts a requested trade*
  * deny - *Denies a requested trade*
  
### Permissions:
* allshop.* - Permission node for everything
* allshop.admin - Permission node for administrative actions (Selling to the server shop, reloading)
* allshop.shop - permission node to use the server shop
* allshop.market - permission node to use player shop
* allshop.auction - permission node to use auction house
* allshop.trade - permission node for trading

### Known Bugs:

* Global:
  * Lore is overridden sometimes by product information
  
* Player Shop:
  * none
  
* Auction House:
  * none
  
* Server Shop:
  * none
  
### Bug Fixes:
 * none
  
### Dependencies:
* [Vault](https://www.spigotmc.org/resources/vault.34315/)
