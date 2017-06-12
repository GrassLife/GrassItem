# GrassItem

## example
```
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND || event.getItem() == null) return;

        GrassItem grassItem = new GrassItem(event.getItem());
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();

        switch (event.getAction()) {
            case RIGHT_CLICK_AIR:
                grassItem.getNBT(CookingTag.RESTORE_AMOUNT).ifPresent(restoreAmount -> player.sendMessage("restoreAmount: " + restoreAmount));
                break;
            case LEFT_CLICK_AIR:
                grassItem.setNBT(CookingTag.RESTORE_AMOUNT, 10);
                inventory.setItemInMainHand(grassItem.toItemStack());
                break;
        }
    }
```