# AllShopMC 0.3.0

### Features:
- [ ] Server GUI Shop for admins - *In Progress*
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

### Bugs:

* Global:
  * none
  
* Player Shop:
  * Incorrect Items removed when item purchased
  * Lore is overridden sometimes by product information
  * Extra line is added to Lore when purchased
  
* Auction House:
  * none
  
* Server Shop:
  * none
  
### Dependencies:
* [Vault](https://www.spigotmc.org/resources/vault.34315/)
