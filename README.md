# AllShopMC 0.3.1

### Features:
- [x] Server GUI Shop for admins
- [x] Player GUI Shop
- [ ] GUI Auction House - *In Progress*
- [ ] Physical Player Shop
- [ ] Physical Server Shop
- [ ] Plugin Moderation
- [ ] Search Feature
- [ ] Staff for Physical Shops
- [ ] Group Specific Shops
- [ ] MYSQL support
- [ ] Customizable messages
- [x] Customizable Prefix
- [x] Player trading

### Commands:
* /AllShop (aka /as) - *Main command*
  * reload - *reloads the plugins configuration*
* /Shop - *Opens the server shop*
  * sell {price} - *Sells anything in your hand*
* /Auction - *Opens the auction house*
  * bid {starting bid} - *Puts the item in your hand up for auction*
* /Market - *Opens the player market*
  * sell {price} - *Sells anything in your hand*
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

### Bugs:

* Global:
    * Incorrect Items removed when item purchased
  * Lore is overridden sometimes by product information
  * Extra line is added to Lore when purchased
  
* Player Shop:
  * none
  
* Auction House:
  * none
  
* Server Shop:
  * none
  
### Dependencies:
* [Vault](https://www.spigotmc.org/resources/vault.34315/)
